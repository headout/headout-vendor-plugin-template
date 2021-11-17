package com.headout.vendor.plugin.ho.api.models

import com.google.gson.annotations.SerializedName
import kotlin.collections.List

data class ProductFormSet(
    @SerializedName("productFormId") val productFormId: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("type") val type: String?,
    @SerializedName("formFieldsetSet") val formFieldsetSet: List<FormFieldSet>?
)
