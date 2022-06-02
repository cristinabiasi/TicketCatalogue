package it.group24.lab5.webapp2.ticketcatalogue.dtos

import it.group24.lab5.webapp2.ticketcatalogue.domain.Order

data class OrderDTO(
    var id: Long?,
    var status: String?,
    var ticketId: Long?,
    var quantity: Int?,
    var userId: Long?
)

fun Order.toDTO(): OrderDTO = OrderDTO(
    id,
    status,
    ticketId,
    quantity,
    userId
)
