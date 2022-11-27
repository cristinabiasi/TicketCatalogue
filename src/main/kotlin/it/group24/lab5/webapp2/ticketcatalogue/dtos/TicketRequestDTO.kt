package it.group24.lab5.webapp2.ticketcatalogue.dtos

import java.util.*

data class TicketRequestDTO(
    var cmd: String?,
    var quantity: Int?,
    var type: String?,
    var validFrom: Date? = Date(),
    var zones: String?
)