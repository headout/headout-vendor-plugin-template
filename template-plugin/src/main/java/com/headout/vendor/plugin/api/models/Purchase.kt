package com.headout.vendor.plugin.ho.api.models

data class Purchase(
        val reservationList: List<ReservationX>,
        val total: String
)