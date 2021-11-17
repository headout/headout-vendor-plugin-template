package com.headout.vendor.plugins

import com.headout.vendor.api.*
import com.headout.vendor.api.models.VendorBooking
import com.headout.vendor.api.models.VendorGuest
import com.headout.vendor.api.models.VendorPrimaryGuest
import kotlinx.coroutines.runBlocking
import org.joda.time.LocalDate
import org.joda.time.LocalTime
import org.junit.jupiter.api.Test
import tourlandish.common.enums.inventory.PaxType

interface LiveAvailabilityTests<PC, VP : IVendorInventoryCheckPlugin<PC>> : VendorPluginTests<PC, VP>  {

    val guestNumbersMap: Map<PaxType, Int>

    val defaultBookingTestDate: LocalDate
        get() = LocalDate.now().plusDays(10)

    val defaultBookingTestTime: LocalTime
        get() = LocalTime.parse("09:00")


    fun getPrimaryGuest(): VendorPrimaryGuest {
        return VendorPrimaryGuest.builder()
            .firstName("Tanmay")
            .lastName("Mittal")
            .email("tanmay.mittal@headout.com")
            .phoneNumber("+91 8888888888")
            .build()
    }

    fun getGuests(): List<VendorGuest> {
        var index = -1
        return guestNumbersMap.flatMap { (paxType, count) ->
            List(count) {
                index++
                VendorGuest.builder()
                    .firstName("Tanmay" + if (index == 0) "" else "$index")
                    .lastName("Mittal" + if (index == 0) "" else "$index")
                    .personType(paxType)
                    .weight(60)
                    .email("test@headout.com")
                    .vendorCustomFields(emptyList())
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

    @Test
    fun testLiveAvailability() = runBlocking {
        val booking = getBookingForDateTime(defaultBookingTestDate, defaultBookingTestTime)
        val result = vendorPlugin.checkInventory(booking)
        println(result)
    }

}