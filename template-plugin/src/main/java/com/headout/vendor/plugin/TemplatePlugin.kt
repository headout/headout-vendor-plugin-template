package com.headout.vendor.plugins.template

import com.headout.vendor.api.IHeadoutInventoryApi
import com.headout.vendor.api.IVendorBooking
import com.headout.vendor.api.IVendorBookingPlugin
import com.headout.vendor.api.IVendorInventoryPlugin
import com.headout.vendor.api.Result
import com.headout.vendor.api.enums.InventoryAvailability
import com.headout.vendor.api.exceptions.InvalidVendorProductCodeException
import com.headout.vendor.api.exceptions.VendorBookingException
import com.headout.vendor.api.models.BookingResponse
import com.headout.vendor.api.models.CancellationRequest
import com.headout.vendor.api.models.CancellationResponse
import com.headout.vendor.api.models.InventoryInfo
import com.headout.vendor.api.models.InventoryRangeQuery
import com.headout.vendor.api.models.InventorySlotInfo
import com.headout.vendor.api.models.PaxPriceInfo
import com.headout.vendor.api.models.Ticket
import com.headout.vendor.api.models.TicketType
import com.headout.vendor.plugin.utils.RedisManager
import com.headout.vendor.plugin.utils.fetchAndUpdateSlotsPerQueryWrappingError
import com.headout.vendor.plugin.utils.wrapError
import com.headout.vendor.plugins.template.models.requests.CancelCompleteReservation
import com.headout.vendor.plugins.template.models.requests.CreateCompleteReservation
import com.headout.vendor.plugins.template.models.requests.CreateCompleteReservationTicket
import com.headout.vendor.plugins.template.models.requests.GetAvailability
import com.headout.vendor.plugins.template.models.requests.GetTicketTypes
import com.headout.vendor.plugins.template.models.responses.GetTicketTypesResponse
import com.headout.vendor.plugins.template.models.responses.TicketTypes
import org.joda.time.Days
import org.joda.time.Hours
import org.joda.time.LocalDate
import org.joda.time.LocalTime
import kotlin.random.Random

class TemplatePlugin private constructor(
    private val gtHelper: TemplatePluginHelper,
    private val headoutInventoryService: IHeadoutInventoryApi,
    private val redisManager: RedisManager
) : IVendorInventoryPlugin<TemplateCode>, IVendorBookingPlugin<TemplateCode> {
    private fun convertTicketTypesToPaxPrice(
        ticketTypes: List<TicketTypes>,
        productCode: TemplateCode
    ): List<PaxPriceInfo> {
        return productCode.personTypeMap.keys.map { paxType ->
            val ticketTypeID = productCode.personTypeMap[paxType]
                ?: throw InvalidVendorProductCodeException("Cannot decode ticket type id from Pax type $paxType")
            val ticketType = ticketTypes.find { it.ticketTypeId == ticketTypeID }
                ?: throw InvalidVendorProductCodeException("Cannot decode ticket type from ticket type name $ticketTypeID")
            PaxPriceInfo(
                paxType = paxType,
                priceInfo = productCode.getPriceUpdateDataWith(
                    ticketType.ticketTypePrice
                )
            )
        }
    }

    private suspend fun getCachedPriceList(userId: Int, date: String): GetTicketTypesResponse {
        val key = "template-$userId-$date"
        val secondsInTwelveHours = Hours.SIX.toStandardSeconds().seconds * 2
        return redisManager.getOrSetBacking(
            redisKey = key,
            classObject = GetTicketTypesResponse::class.java,
            expiryInSeconds = secondsInTwelveHours + Random.nextInt(secondsInTwelveHours)
        ) {
            mapOf(key to gtHelper.templateApi.getTicketTypes(GetTicketTypes(userId, "en", date)))
        }
    }

    override fun updateInventoryForRangeQueries(inventoryRangeQueries: List<InventoryRangeQuery<TemplateCode>>) {
        fetchAndUpdateSlotsPerQueryWrappingError(inventoryRangeQueries, headoutInventoryService) { query ->
            val userId = query.productCode.userId

            val availabilityRequest = GetAvailability(
                endDate = query.endDate,
                showTicketsAllocation = 1,
                startDate = query.startDate,
                userId = userId
            )

            val availabilityResponse = gtHelper.templateApi.getAvailability(availabilityRequest)
            val availabilityType = availabilityResponse.availabilityType

            val inventorySlotInfoList = if (availabilityType == "timeslots") {
                val availabilityData =
                    availabilityResponse.availability as Map<String, Map<String, Int>>
                availabilityData.keys.flatMap { date ->
                    val ticketTypes = getCachedPriceList(userId, date).ticketTypes
                    val inventoryPriceInfo = convertTicketTypesToPaxPrice(ticketTypes, query.productCode)
                    val timeslot = availabilityData[date]
                    timeslot?.keys?.map {
                        val remaining = timeslot.getOrDefault(it, 0)
                        InventorySlotInfo(
                            date = LocalDate(date),
                            startTime = LocalTime(it),
                            inventoryInfo = query.inventoryPaxTypes.map { paxType ->
                                InventoryInfo(
                                    paxType = paxType,
                                    remaining = remaining
                                )
                            },
                            inventoryPriceInfo = inventoryPriceInfo
                        )
                    }
                        ?: emptyList()
                }
            } else if (availabilityType == "date") {
                val availabilityData =
                    availabilityResponse.availability as List<String>
                availabilityData.map { date ->
                    val ticketTypes = getCachedPriceList(userId, date).ticketTypes
                    val inventoryPriceInfo = convertTicketTypesToPaxPrice(ticketTypes, query.productCode)
                    val startDate = LocalDate(date)
                    InventorySlotInfo(
                        date = startDate,
                        startTime = LocalTime.MIDNIGHT,
                        inventoryInfo = query.inventoryPaxTypes.map { paxType ->
                            InventoryInfo(
                                paxType = paxType,
                                remaining = 9999
                            )
                        },
                        inventoryPriceInfo = inventoryPriceInfo
                    )
                }
            } else {
                val days = Days.daysBetween(query.startDate, query.endDate).days + 1
                val availabilityDate =
                    availabilityResponse.availability as String
                (0 until days).map { day ->
                    val isAvailable = availabilityDate >= query.startDate.plusDays(day).toString()
                    val ticketTypes = getCachedPriceList(userId, query.startDate.plusDays(day).toString()).ticketTypes
                    val inventoryPriceInfo = convertTicketTypesToPaxPrice(ticketTypes, query.productCode)
                    val startDate = query.startDate.plusDays(day)
                    InventorySlotInfo(
                        date = startDate,
                        startTime = LocalTime.MIDNIGHT,
                        inventoryInfo = query.inventoryPaxTypes.map { paxType ->
                            InventoryInfo(
                                paxType = paxType,
                                remaining = 9999,
                                availability = if (isAvailable) {
                                    InventoryAvailability.UNLIMITED
                                } else {
                                    InventoryAvailability.CLOSED
                                }
                            )
                        },
                        inventoryPriceInfo = inventoryPriceInfo
                    )
                }
            }
            inventorySlotInfoList
        }
    }

    override fun getProductCodeClass(): Class<TemplateCode> = TemplateCode::class.java

    override suspend fun book(booking: IVendorBooking<TemplateCode>): Result<BookingResponse> = wrapError {
        val date = booking.inventoryDate
        val time = booking.inventoryTime
        val isoDate = date.toString()
        val isoTime = time.toString("HH:mm")

        val code = booking.productCode ?: throw VendorBookingException("No product code passed")

        val tickets = booking.guestNumbersMap.map { (pax, numOfPeople) ->
            val ticketTypeId = code.personTypeMap[pax]
                ?: throw VendorBookingException("No ticket type id found for pax $pax. Please check the product code configurations.")
            CreateCompleteReservationTicket(
                ticketTypeId, numOfPeople
            )
        }
        val createCompleteReservationRequest =
            CreateCompleteReservation(
                ticketDate = isoDate,
                ticketTime = isoTime,
                tickets = tickets,
                userId = code.userId
            )

        val reservation = gtHelper.templateApi.createCompleteReservation(createCompleteReservationRequest)

        BookingResponse.builder()
            .setTickets(
                reservation.ticketBarcodes.map {
                    Ticket(
                        type = booking.ticketType
                            ?: TicketType.BARCODE, ticketData = it.barcode
                    )
                }
            )
            .setOrderId(reservation.reservationId.toString())
            .setCancellationId(reservation.reservationId.toString())
            .build()
    }

    override suspend fun cancel(cancellationRequest: CancellationRequest<TemplateCode>): Result<CancellationResponse> =
        wrapError {
            val reservationId = cancellationRequest.cancellationId?.toInt()
                ?: throw VendorBookingException("Cancellation ID not provided")

            val cancelCompleteReservation = CancelCompleteReservation(
                reservationId = reservationId,
                userId = cancellationRequest.productCode.userId
            )

            gtHelper.templateApi.cancelCompleteReservation(cancelCompleteReservation)

            CancellationResponse(
                cancellationId = cancellationRequest.cancellationId,
                orderId = cancellationRequest.orderReferenceNumber
            )
        }

    companion object {
        fun create(
            credentials: TemplateCredentials,
            headoutInventoryService: IHeadoutInventoryApi,
            redisHost: String
        ): TemplatePlugin {
            val redisManger = RedisManager.getInstance(host = redisHost)
            return TemplatePlugin(TemplatePluginHelper(credentials), headoutInventoryService, redisManger)
        }
    }
}
