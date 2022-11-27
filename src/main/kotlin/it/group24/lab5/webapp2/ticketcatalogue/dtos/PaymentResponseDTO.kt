package it.group24.lab5.webapp2.ticketcatalogue.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class PaymentResponseDTO(
    @JsonProperty("orderID")
    val orderID: Long,
    @JsonProperty("type")
    val type: String,
    @JsonProperty("zones")
    val zones: String,
    @JsonProperty("validFrom")
    val validFrom: Date
)
