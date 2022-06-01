package it.webappii.group24.paymentservice.dtos

import com.fasterxml.jackson.annotation.JsonProperty

data class PaymentRequestDTO(
    @JsonProperty("paymentInformationDTO")
    val paymentInformationDTO: PaymentInformationDTO,
    @JsonProperty("total")
    val total: Double
)
