package com.headout.vendor.plugin.ho.api.models

import com.google.gson.annotations.SerializedName

data class TicketComplement(
    @SerializedName("venueId") val venueId: Object?,
    @SerializedName("startDatetime") val startDatetime: String?,
    @SerializedName("endDatetime") val endDatetime: String?,
    @SerializedName("status") val status: String?,
    @SerializedName("venueCapacityOnlyAccessTime") val venueCapacityOnlyAccessTime: String?,
    @SerializedName("eventId") val eventId: String?
)
