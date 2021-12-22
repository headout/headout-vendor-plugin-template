package com.headout.vendor.plugins.template.models.responses

data class GetAvailabilityResponse(
    val success: Boolean,
    val availabilityType: String,
    val availability: Any
)