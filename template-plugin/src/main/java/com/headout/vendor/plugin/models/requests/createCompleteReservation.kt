package com.headout.vendor.plugins.template.models.requests

data class CreateCompleteReservationTicket(
    val ticketTypeId: Int,
    val ticketNumber: Int
)

data class CreateCompleteReservation(
    val ticketDate: String,
    val ticketTime: String,
    val tickets: List<CreateCompleteReservationTicket>,
    val userId: Int
)
