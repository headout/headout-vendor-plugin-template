package com.headout.vendor.plugin.ho.api.models

data class Payment(
        val amount: String,
        val status: String,
        val reference: String,
        val creationDate: String
)

data class PaymentResponse(
        val payment: Payment
)