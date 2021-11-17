package com.headout.vendor.plugins.ho.api.models

data class TicketComplementSet(
        val endDatetime: String?,
        val eventId: String?,
        val startDatetime: String?,
        val status: String?,
        val venueCapacityOnlyAccessTime: String?,
        val venueId: VenueId?
)