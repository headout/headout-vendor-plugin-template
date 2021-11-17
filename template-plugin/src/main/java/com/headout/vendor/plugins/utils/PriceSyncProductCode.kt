package com.headout.vendor.plugins.utils

import com.headout.vendor.api.models.PriceInfo
import java.math.BigDecimal

enum class PriceSyncField {
    NET_PRICE, SELLING_PRICE, RETAIL_PRICE
}

data class PriceSyncConfiguration(
        val priceSyncFields: List<PriceSyncField> = emptyList(),
        val priceSyncMultipliers: Map<PriceSyncField, BigDecimal> = emptyMap()
)

interface PriceSyncProductCode {
    val priceSyncConfiguration: PriceSyncConfiguration

    fun getPriceUpdateDataWith(pulledPrice: BigDecimal): PriceInfo {

        fun priceFor(priceSyncField: PriceSyncField) = if (priceSyncField in priceSyncConfiguration.priceSyncFields)
            pulledPrice * priceSyncConfiguration.priceSyncMultipliers.getOrDefault(priceSyncField, BigDecimal.ONE) else null

        return PriceInfo(
                netPrice = priceFor(PriceSyncField.NET_PRICE),
                sellingPrice = priceFor(PriceSyncField.SELLING_PRICE),
                retailPrice = priceFor(PriceSyncField.RETAIL_PRICE)
        )
    }
}