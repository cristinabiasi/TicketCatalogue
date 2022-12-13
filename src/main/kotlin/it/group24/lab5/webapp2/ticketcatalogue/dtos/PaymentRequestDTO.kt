package it.group24.lab5.webapp2.ticketcatalogue.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class PaymentRequestDTO(
    @JsonProperty("paymentInformationDTO")
    val paymentInformationDTO: PaymentInformationDTO,
    @JsonProperty("total")
    val total: Double,
    @JsonProperty("orderId")
    val orderId: Long,
    @JsonProperty("userID")
    val userId: Long,
    @JsonProperty("type")
    val type: String,
    @JsonProperty("validFrom")
    val validFrom: Date,
    @JsonProperty("zones")
    val zones: String,
    @JsonProperty("authToken")
    val authToken: String
)
