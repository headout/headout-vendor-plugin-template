package com.headout.vendor.plugin

import com.headout.vendor.plugin.utils.PriceSyncConfiguration
import com.headout.vendor.plugin.utils.PriceSyncField
import com.headout.vendor.plugin.utils.PriceSyncProductCode
import tourlandish.common.enums.inventory.PaxType
/*
* The product code object
* a product code can be considered as a configuration where the data to identify
* a product resides. This is configured by our internal tools and is populated and passed
* when a booking is made.
* */
data class TemplateCode(
    /**
     * The information about the actual place
     */
    val userId: Int,

    /**
     * Mapping between the ticketType names and our PaxType to be able to update prices
     */
    val personTypeMap: Map<PaxType, Int> = emptyMap(),

    override val priceSyncConfiguration: PriceSyncConfiguration = PriceSyncConfiguration(
        priceSyncFields = listOf(PriceSyncField.SELLING_PRICE)
    )
): PriceSyncProductCode
