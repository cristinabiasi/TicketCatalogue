package it.group24.lab5.webapp2.ticketcatalogue.domain

import org.springframework.data.annotation.Id

class Order(
    @Id
    val id: Long?,
    val status: String?,
    val ticketId: Long?,
    val quantity: Int?,
    val userId: Long?
)