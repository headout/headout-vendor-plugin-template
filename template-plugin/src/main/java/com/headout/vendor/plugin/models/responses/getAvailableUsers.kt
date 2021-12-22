package com.headout.vendor.plugins.template.models.responses

data class UserAddress(
    val streetName: String,
    val houseNumber: Int,
    val postalCode: String,
    val cityName: String,
    val country: String
)

data class UserCoordinates(
    val latitude: Double,
    val longitude: Double
)

data class UserImages(
    val logo: String
)

data class Users(
    val userId: Int,
    val userName: String,
    val userAddress: UserAddress,
    val userCoordinates: UserCoordinates,
    val userPhoneNumber: String,
    val userWebsite: String,
    val userImages: UserImages
)
data class GetAvailableUsersResponse(
    val success: Boolean,
    val users: List<Users>
)
