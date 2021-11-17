package com.headout.vendor.plugins.ho.api.models

import com.google.gson.annotations.SerializedName

data class TicketExtra(
    @SerializedName("ticketExtraId") val ticketExtraId: String?,
    @SerializedName("extraId") val extraId: String?,
    @SerializedName("ticketId") val ticketId: String?,
    @SerializedName("extraName") val extraName: String?,
    @SerializedName("startDatetime") val startDatetime: String?,
    @SerializedName("endDatetime") val endDatetime: String?,
    @SerializedName("status") val status: String?,
    @SerializedName("serviceId") val serviceId: String?,
    @SerializedName("serviceName") val serviceName: String?,
    @SerializedName("used") val used: String?,
    @SerializedName("ticketExtraGroupId") val ticketExtraGroupId: String?
)
