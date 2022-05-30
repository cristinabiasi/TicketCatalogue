package it.group24.lab5.webapp2.ticketcatalogue.dtos

import java.util.*

data class PaymentInformationDTO(
    val creditCardNumber: String,
    val expirationDate: Date,
    val cvv: String,
    val cardHolder: String
)
