package com.headout.vendor.plugin.ho.api.models

import com.google.gson.annotations.SerializedName
import kotlin.collections.List

data class BuyerTypeList(@SerializedName("buyerTypeList") val buyerTypeList: List<BuyerType>?)
