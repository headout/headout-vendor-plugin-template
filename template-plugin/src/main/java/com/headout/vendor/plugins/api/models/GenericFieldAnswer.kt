package com.headout.vendor.plugins.ho.api.models

import com.google.gson.annotations.SerializedName

data class GenericFieldAnswer(
    @SerializedName("genericFieldAnswerId") val genericFieldAnswerId: String?,
    @SerializedName("answer") val answer: String?,
    @SerializedName("answerValue") val answerValue: String?,
    @SerializedName("genericFieldId") val genericFieldId: String?,
    @SerializedName("reservationId") val reservationId: String?,
    @SerializedName("quantity") val quantity: String?,
    @SerializedName("ticketId") val ticketId: String?,
    @SerializedName("purchaseId") val purchaseId: String?
)
