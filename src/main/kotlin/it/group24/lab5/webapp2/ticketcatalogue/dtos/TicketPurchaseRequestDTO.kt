package it.group24.lab5.webapp2.ticketcatalogue.dtos

import java.util.Date

data class TicketPurchaseRequestDTO(
    val ticketID: Long,
    val quantity: Int,
    val paymentInformationDTO: PaymentInformationDTO,
    val validFrom: Date,
    val zones: String
)
