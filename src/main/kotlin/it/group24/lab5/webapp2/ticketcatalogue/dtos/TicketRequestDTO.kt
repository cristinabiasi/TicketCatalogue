package it.group24.lab5.webapp2.ticketcatalogue.dtos

import java.util.*

data class TicketRequestDTO(
    var quantity: Int?,
    var type: String?,
    var validFrom: Date?,
    var issuedAt: Date?,
    var zones: String?
)