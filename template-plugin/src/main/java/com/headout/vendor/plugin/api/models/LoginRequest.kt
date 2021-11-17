package com.headout.vendor.plugin.ho.api.models

import com.google.gson.annotations.SerializedName

data class LoginRequest(@SerializedName("username") val username: String?, @SerializedName("password") val password: String?)