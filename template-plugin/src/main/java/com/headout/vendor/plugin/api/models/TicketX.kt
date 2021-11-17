package com.headout.vendor.plugin.ho.api.models

data class TicketX(
        val barcode: String,
        val buyerTypeId: String,
        val buyerTypeName: String?,
        val endDatetime: String,
        val eventId: String,
        val eventName: String?,
        val passBookurl: String?,
        val seatId: String?,
        val startDatetime: String,
        val status: String,
        val ticketComplementSet: List<TicketComplementSet>,
        val ticketExtraSet: List<TicketExtraSet>,
        val ticketId: String,
        val total: String,
        val used: String,
        val venueCapacityName: String
)