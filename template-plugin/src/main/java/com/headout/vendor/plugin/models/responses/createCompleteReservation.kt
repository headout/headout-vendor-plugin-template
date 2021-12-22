package com.headout.vendor.plugins.template.models.responses

data class TicketBarcodes(
    val barcode: String,
    val ticketTypeId: Int,
    val barcodeType: String
)

data class CreateCompleteReservationResponse(
    val success: Boolean,
    val reservationId: Int,
    val ticketBarcodes: List<TicketBarcodes>
)
