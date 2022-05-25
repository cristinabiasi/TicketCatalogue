package it.group24.lab5.webapp2.ticketcatalogue.dtos

import it.group24.lab5.webapp2.ticketcatalogue.domain.Ticket

data class TicketDTO(
    var id: Long?,
    var type: String?,
    var price: Double?
)

fun Ticket.toDTO(): TicketDTO = TicketDTO(
    id,
    type,
    price,
)
