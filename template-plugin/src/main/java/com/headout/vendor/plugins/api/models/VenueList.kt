package com.headout.vendor.plugins.ho.api.models

import com.google.gson.annotations.SerializedName
import kotlin.collections.List

data class VenueList(@SerializedName("events") val events: List<Venue>)
