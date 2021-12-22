package com.headout.vendor.plugins.template.models.requests

data class GetTicketTypes(
    val userId: Int,
    val language: String,
    val ticketDate: String
)
