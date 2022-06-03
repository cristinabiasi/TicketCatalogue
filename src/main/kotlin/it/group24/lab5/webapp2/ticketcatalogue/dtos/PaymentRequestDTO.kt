package it.group24.lab5.webapp2.ticketcatalogue.dtos

import com.fasterxml.jackson.annotation.JsonProperty

data class PaymentRequestDTO(
    @JsonProperty("paymentInformationDTO")
    val paymentInformationDTO: PaymentInformationDTO,
    @JsonProperty("total")
    val total: Double,
    @JsonProperty("orderId")
    val orderId: Long
)
