package com.headout.vendor.plugin

import com.headout.vendor.api.IHeadoutInventoryApi
import com.headout.vendor.api.IVendorInventoryPlugin
import com.headout.vendor.api.models.InventoryRangeQuery
import com.headout.vendor.api.models.MockHeadoutInventoryApi
import org.joda.time.LocalDate
import org.junit.jupiter.api.Test
import tourlandish.common.enums.inventory.PaxType
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

interface InventoryPluginTests<PC, VP : IVendorInventoryPlugin<PC>> : VendorPluginTests<PC, VP> {
    val headoutInventoryAPI: IHeadoutInventoryApi
        get() = MockHeadoutInventoryApi()

    val offsetDays: Int
        get() = 0

    val tourId: Int
        get() = 13876

    @Test
    fun testInventoryRangeFor10Days() {
        val queries = getQueriesForDays(20)
        this.vendorPlugin.updateInventoryForRangeQueries(queries)
    }

    @Test
    fun testInventoryRangeFor30Days() {
        val queries = getQueriesForDays(30)
        this.vendorPlugin.updateInventoryForRangeQueries(queries)
    }

    private fun getQueriesForDays(numberOfDays: Int): List<InventoryRangeQuery<PC>> {
        return productCodes.map {
            InventoryRangeQuery(
                    productCode = it,
                    vendorId = 1548,
                    tourId = tourId,
                    startDate = LocalDate.now().plusDays(offsetDays),
                    endDate = LocalDate.now().plusDays(offsetDays + numberOfDays),
                    inventoryPaxTypes = listOf(PaxType.GENERAL)
            )
        }
    }

    @Test
    fun testLocalTime() {
        val dateTimeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(java.util.Locale.ENGLISH)
        val a = LocalTime.parse("10:00", dateTimeFormatter)
        println(a)
    }
}