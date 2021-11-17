package com.headout.vendor.plugins.ho.api.models

import com.google.gson.annotations.SerializedName

data class BuyerType(
    @SerializedName("buyerTypeId") val buyerTypeId: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("enabled") val enabled: String?,
    @SerializedName("displayOrder") val displayOrder: String?,
    @SerializedName("price") val price: String?,
    @SerializedName("clientId") val clientId: String?
)
