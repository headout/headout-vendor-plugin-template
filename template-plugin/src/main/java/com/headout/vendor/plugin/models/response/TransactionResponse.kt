package com.headout.vendor.plugins.experticket.models.response

data class Transaction (
    val transactionId: String ?= null,
    val transactionDateTime: String ?= null,
    val products: List<TransactionProduct> = listOf(),
    val documents: List<Document> = listOf(),
    val success: Boolean,
    val errorMessage: String ?= null
)

data class Document (
    val salesDocumentUrl: String
)

data class TransactionProduct (
    val productId: String,
    val tickets: List<TransactionTickets>,
    val providerId: String
)

data class TransactionTickets (
    val accessCode: String
)
