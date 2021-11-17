package com.headout.vendor.plugins.experticket.models.response

data class Catalog (
    val lastUpdatedDateTime: String,
    val providers: List<Provider>,
    val success: Boolean,
    val errorMessage: String ?= null
)

data class Provider (
    val providerId: String,
    val ticketEnclosures: List<TicketEnclosure>,
    val productBases: List<ProductBasis>
)

data class ProductBasis (
    val productBaseId: String,
    val products: List<Product>
)

data class Product (
    val productId: String,
    val daysWithLimitedCapacity: String,
    val priceMode: Int,
    val pricesAndDates: List<PricesAndDate>,
    val tickets: List<Ticket>
)

data class PricesAndDate (
    val price: Double,
    val dates: String
)

data class Ticket (
    val ticketId: String,
    val ticketEnclosureId: String
)


data class TicketEnclosure (
    val ticketEnclosureId: String,
    val sessions: Sessions ?= null
)

data class Sessions (
    val sessionContentProfileId: String,
    val sessionsGroupProfileId: String
)
