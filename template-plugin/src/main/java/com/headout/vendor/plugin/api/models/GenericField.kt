package com.headout.vendor.plugin.ho.api.models

import com.google.gson.annotations.SerializedName

data class GenericField(
    @SerializedName("genericFieldId") val genericFieldId: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("fieldType") val fieldType: String?,
    @SerializedName("fieldOptions") val fieldOptions: String?,
    @SerializedName("required") val required: String?
)
