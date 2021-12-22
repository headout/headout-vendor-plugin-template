package com.headout.vendor.plugins.template.models.responses

import java.math.BigDecimal

data class CancelationPolicy(
    val cancelCompleteReservationMinutesBeforeTicketDate: Int,
    val cancelCompleteReservationMinutesAfterReservationDate: Int
)

data class TicketTypes(
    val ticketTypeId: Int,
    val ticketTypeName: String,
    val ticketTypePrice: BigDecimal,
    val cancelationPolicy: CancelationPolicy
)

data class GetTicketTypesResponse(
    val success: Boolean,
    val ticketTypes: List<TicketTypes>
)
