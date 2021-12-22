package com.headout.vendor.plugins.template.models.requests

import org.joda.time.LocalDate

data class GetAvailability(
    val endDate: LocalDate,
    val showTicketsAllocation: Int,
    val startDate: LocalDate,
    val userId: Int,
)

data class GetAvailabilityToSign(
    val endDate: String,
    val showTicketsAllocation: Int,
    val startDate: String,
    val userId: Int,
) {
    constructor(getAvailability: GetAvailability) : this(
        // ISO format
        getAvailability.endDate.toString(),
        getAvailability.showTicketsAllocation,
        // ISO format
        getAvailability.startDate.toString(),
        getAvailability.userId,
    )
}
