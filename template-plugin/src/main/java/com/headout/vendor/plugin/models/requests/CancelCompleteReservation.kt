package com.headout.vendor.plugins.template.models.requests

data class CancelCompleteReservation(
    val reservationId: Int,
    val userId: Int
)
