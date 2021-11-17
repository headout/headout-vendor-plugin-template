package com.headout.vendor.plugins.ho.api.models

data class Purchase(
        val reservationList: List<ReservationX>,
        val total: String
)