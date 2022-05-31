package it.group24.lab5.webapp2.ticketcatalogue.dtos

data class PaymentRequestDTO(
    val paymentInformationDTO: PaymentInformationDTO,
    val total: Double
)
