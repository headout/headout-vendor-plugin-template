package com.headout.vendor.plugin.ho.api.models

data class ReservationX(
        val currentTicketNumber: String?,
        val eventStartDatetime: String?,
        val genericFieldAnswerSet: List<GenericFieldAnswerSet?>?,
        val productId: String?,
        val productName: String?,
        val reservationId: String?,
        val status: String?,
        val ticketList: List<TicketX>,
        val total: String?,
        val url: String?
)