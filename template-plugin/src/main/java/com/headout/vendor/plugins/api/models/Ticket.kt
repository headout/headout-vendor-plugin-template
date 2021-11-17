package com.headout.vendor.plugins.ho.api.models

import com.google.gson.annotations.SerializedName
import kotlin.collections.List

data class Ticket(
        @SerializedName("ticketId") val ticketId: String?,
        @SerializedName("total") val total: String?,
        @SerializedName("barcode") val barcode: String?,
        @SerializedName("status") val status: String?,
        @SerializedName("buyerTypeId") val buyerTypeId: String?,
        @SerializedName("buyerTypeName") val buyerTypeName: String?,
        @SerializedName("seatId") val seatId: String?,
        @SerializedName("eventId") val eventId: String?,
        @SerializedName("eventName") val eventName: String?,
        @SerializedName("venueCapacityName") val venueCapacityName: String?,
        @SerializedName("startDatetime") val startDatetime: String?,
        @SerializedName("endDatetime") val endDatetime: String?,
        @SerializedName("used") val used: String?,
        @SerializedName("passBookurl") val passBookurl: String?,
        @SerializedName("ticketComplementSet") val ticketComplementSet: List<TicketComplement>?,
        @SerializedName("ticketExtraSet") val ticketExtraSet: List<TicketExtra>?
)
