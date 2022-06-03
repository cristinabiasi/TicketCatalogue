package it.group24.lab5.webapp2.ticketcatalogue.dtos

data class TicketPurchaseRequestDTO(
    val ticketID: Long,
    val quantity: Int,
    val paymentInformationDTO: PaymentInformationDTO
)
