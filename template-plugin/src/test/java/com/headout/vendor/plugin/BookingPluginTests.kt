package com.headout.vendor.plugin

import com.headout.vendor.api.IVendorBooking
import com.headout.vendor.api.IVendorBookingPlugin
import com.headout.vendor.api.models.*
import kotlinx.coroutines.runBlocking
import org.joda.time.LocalDate
import org.joda.time.LocalTime
import org.junit.jupiter.api.Test
import tourlandish.common.enums.inventory.PaxType

interface BookingPluginTests<PC, VP : IVendorBookingPlugin<PC>> : VendorPluginTests<PC, VP> {

    val guestNumbersMap: Map<PaxType, Int>
    val vendorCustomFields: List<VendorCustomUserField>
        get() = emptyList()

    val defaultBookingTestDate: LocalDate
        get() = LocalDate.now().plusDays(10)

    val defaultBookingTestTime: LocalTime
        get() = LocalTime.parse("09:00")

    @Test
    fun testBooking() = runBlocking {
        val booking = getBookingForDateTime(defaultBookingTestDate, defaultBookingTestTime)
        val result = vendorPlugin.book(booking)
        println(result)
        assert(result.isSuccessful)
    }

    @Test
    fun testBookingAndCancellation() = runBlocking {
        val booking = getBookingForDateTime(defaultBookingTestDate, defaultBookingTestTime)
        val bookingResponse = vendorPlugin.book(booking)
        val cancellation = vendorPlugin.cancel(CancellationRequest(
                orderReferenceNumber = bookingResponse.body!!.orderId,
                productName = "Test Product",
                productCode = productCodes.first(),
                cancellationId = bookingResponse.body!!.cancellationId,
                cancellationReason = "Test Cancellation"
        ))
        println(cancellation)
        assert(cancellation.isSuccessful)
    }

    fun getPrimaryGuest(): VendorPrimaryGuest {
        return VendorPrimaryGuest.builder()
                .firstName("Bernard")
                .lastName("Lowe")
                .email("test@headout.com")
                .phoneNumber("+91 8888888888")
                .build()
    }

    fun getGuests(): List<VendorGuest> {
        var index = -1
        return guestNumbersMap.flatMap { (paxType, count) ->
            List(count) {
                index++
                VendorGuest.builder()
                        .firstName("Bernard" + if (index == 0) "" else "$index")
                        .lastName("Lowe" + if (index == 0) "" else "$index")
                        .personType(paxType)
                        .weight(60)
                        .email("test@headout.com")
                        .vendorCustomFields(vendorCustomFields)
                        .phoneNumber("+91-8888888888")
                        .build()
            }
        }
    }

    fun getBookingForDateTime(date: LocalDate, time: LocalTime): IVendorBooking<PC> {
        return VendorBooking.builder<PC>()
                .productCode(productCodes.first())
                .tourId(12345)
                .productName("Random Product Name")
                .inventoryDate(date)
                .inventoryTime(time)
                .guestNumbersMap(guestNumbersMap)
                .guests(getGuests())
                .primaryGuest(getPrimaryGuest())
                .bookingId(System.currentTimeMillis().toInt())
                .vendorId(12345)
                .build()
    }
}