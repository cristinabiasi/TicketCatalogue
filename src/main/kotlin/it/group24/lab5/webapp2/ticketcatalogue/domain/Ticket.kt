package it.group24.lab5.webapp2.ticketcatalogue.domain

import org.springframework.data.annotation.Id

class Ticket(
    @Id
    val id: Long?,
    val type: String?,
    val price: Double?
)