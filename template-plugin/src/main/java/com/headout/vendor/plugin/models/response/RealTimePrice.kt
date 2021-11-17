package com.headout.vendor.plugins.experticket.models.response

import java.math.BigDecimal

data class RealTimePrice (
    val productsRealTimePrices: List<ProductsRealTimePrice> = listOf(),
    val success: Boolean,
    val errorMessage: String ?= null
)

data class ProductsRealTimePrice (
    val productId: String,
    val date: String,
    val retailPrice: Long,
    val price: BigDecimal,
    val priceMode: Long,
    val success: Boolean
)
