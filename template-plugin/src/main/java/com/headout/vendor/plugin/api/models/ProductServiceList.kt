package com.headout.vendor.plugin.ho.api.models

import com.google.gson.annotations.SerializedName
import kotlin.collections.List

data class ProductServiceList(
    @SerializedName("serviceId") val serviceId: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("displayOrder") val displayOrder: String?,
    @SerializedName("price") val price: String?,
    @SerializedName("required") val required: String?,
    @SerializedName("selectionType") val selectionType: String?,
    @SerializedName("buyerTypesForService") val buyerTypesForService: List<BuyerType>?
)
