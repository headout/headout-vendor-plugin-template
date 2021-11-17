package com.headout.vendor.plugins.experticket.models.request

data class ReservationRequest(
    val apiKey: String,
    val accessDateTime: String,
    val products: List<ReservationProduct>
)

data class ReservationProduct(
    val productId: String,
    val tickets: List<ReservationTicket>? = null,
    val quantity: Int = 1
)

data class ReservationTicket(
    val ticketId: String,
    val sessionId: String? = null
)
