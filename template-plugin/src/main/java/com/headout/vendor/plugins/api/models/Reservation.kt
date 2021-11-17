package com.headout.vendor.plugins.ho.api.models

import com.google.gson.annotations.SerializedName
import kotlin.collections.List

data class Reservation(
        @SerializedName("reservationId") val reservationId: String?,
        @SerializedName("total") val total: String?,
        @SerializedName("status") val status: String?,
        @SerializedName("eventStartDatetime") val eventStartDatetime: String,
        @SerializedName("currentTicketNumber") val currentTicketNumber: String,
        @SerializedName("productId") val productId: String,
        @SerializedName("productName") val productName: String?,
        @SerializedName("genericFieldAnswerSet") val genericFieldAnswerSet: List<GenericFieldAnswer>?,
        @SerializedName("ticketList") val ticketList: List<Ticket>,
        @SerializedName("url") val url: String?
)
