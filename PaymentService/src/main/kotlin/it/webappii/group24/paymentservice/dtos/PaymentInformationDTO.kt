package it.webappii.group24.paymentservice.dtos

import java.util.*

data class PaymentInformationDTO(
    val creditCardNumber: String,
    val expirationDate: Date,
    val cvv: String,
    val cardHolder: String
)
