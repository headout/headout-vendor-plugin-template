package com.headout.vendor.plugins.template

import com.headout.vendor.plugin.utils.PriceSyncConfiguration
import com.headout.vendor.plugin.utils.PriceSyncField
import com.headout.vendor.plugin.utils.PriceSyncProductCode
import tourlandish.common.enums.inventory.PaxType

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
