package com.headout.vendor.plugins.experticket.models.response

data class AvailabilityResponse (
    val products: List<ProductAvailability>,
    val success: Boolean,
    val timestamp: String,
    val errorMessage: String ?= null
)

data class ProductAvailability (
    val productId: String,
    val date: String,
    val availableCapacity: Int
)
