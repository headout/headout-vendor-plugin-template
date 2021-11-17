package com.headout.vendor.plugins.experticket.models.request

data class TransactionRequest (
    val apiKey: String,
    val reservationId: String,
    val accessDateTime: String,
    val products: List<TransactionProduct>,
    val client: Client,
    val partnerSaleID: Int
)

data class Client (
    val fullName: String,
    val surname: String,
    val email: String,
    val phoneNumber: String
)

data class TransactionProduct (
    val productId: String
)
