package com.headout.vendor.plugins.ho.api.models

import com.google.gson.annotations.SerializedName

data class LoginResponse(@SerializedName("token") val token: String?)
