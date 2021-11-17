package com.headout.vendor.plugins.experticket.models.response

data class ReservationResponse(
    val reservationId: String ?= null,
    val minutesToExpiry: Long ?= null,
    val accessDateTime: String ?= null,
    val success: Boolean,
    val errorMessage: String ?= null
)
