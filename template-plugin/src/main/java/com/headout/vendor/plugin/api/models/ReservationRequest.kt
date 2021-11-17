package com.headout.vendor.plugin.ho.api.models

import com.fasterxml.jackson.annotation.JsonFormat

data class ReservationRequest(
        @get:JsonFormat(shape = JsonFormat.Shape.STRING)
        val productId: Long,
        val buyerTypes: Map<String, String>,
        val events: Map<String, String>,
        val genericFields: Map<String, String>?,
        val distributorReference: String?
)
