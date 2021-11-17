package com.headout.vendor.plugin.ho.api.models

import com.google.gson.annotations.SerializedName
import kotlin.collections.List

data class VenueCapacity(
    @SerializedName("venueCapacityId") val venueCapacityId: Long,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String?,
    @SerializedName("duration") val duration: String?,
    @SerializedName("privateEvent") val privateEvent: String?,
    @SerializedName("privateNextEvents") val privateNextEvents: String?,
    @SerializedName("venueId") val venueId: Long,
    @SerializedName("venueName") val venueName: String,
    @SerializedName("onlyAccessTime") val onlyAccessTime: String,
    @SerializedName("maxDisplayAvailability") val maxDisplayAvailability: Long?,
    @SerializedName("barCodeType") val barCodeType: String,
    @SerializedName("eventList") val eventList: List<Event>
)
