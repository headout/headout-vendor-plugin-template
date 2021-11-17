package com.headout.vendor.plugin.ho.api.models

import com.google.gson.annotations.SerializedName
import kotlin.collections.List

data class FormFieldSet(
    @SerializedName("formFieldsetId") val formFieldsetId: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("displayOrder") val displayOrder: String?,
    @SerializedName("retrievable") val retrievable: String?,
    @SerializedName("genericFieldSet") val genericFieldSet: List<GenericField>?
)
