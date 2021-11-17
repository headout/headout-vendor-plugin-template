package com.headout.vendor.plugins.ho.api.models

import com.google.gson.annotations.SerializedName
import kotlin.collections.List

data class ProductDetail(
        @SerializedName("maxDate") val maxDate: String?,
        @SerializedName("minTickets") val minTickets: String?,
        @SerializedName("maxtickets") val maxtickets: String?,
        @SerializedName("product") val product: Product?,
        @SerializedName("buyerTypeList") val buyerTypeList: List<BuyerType>?,
        @SerializedName("productServiceList") val productServiceList: List<ProductServiceList>?,
        @SerializedName("productFormSet") val productFormSet: List<ProductFormSet>?
)
