package it.group24.lab5.webapp2.ticketcatalogue.dtos

data class TicketPurchaseRequestDTO(
    val ticketID: Long,
    val quantity: Long,
    val paymentInformationDTO: PaymentInformationDTO
)
