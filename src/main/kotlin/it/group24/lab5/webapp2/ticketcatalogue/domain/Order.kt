package it.group24.lab5.webapp2.ticketcatalogue.domain

import org.springframework.data.annotation.Id

class Order(
    @Id
    var id: Long? = null,
    var status: String?,
    var ticketId: Long?,
    var quantity: Int?,
    var userId: Long?
)
