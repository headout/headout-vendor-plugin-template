package com.headout.vendor.plugin.ho.api.models

import com.google.gson.annotations.SerializedName
import kotlin.collections.List

data class Venue(
    @SerializedName("venueId") val venueId: Long,
    @SerializedName("name") val name: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("canBeSoldOnceStarted") val canBeSoldOnceStarted: String?,
    @SerializedName("venueCapacityList") val venueCapacityList: List<VenueCapacity>
)
