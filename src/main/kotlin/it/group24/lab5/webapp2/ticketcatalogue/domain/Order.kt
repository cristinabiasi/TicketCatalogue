package it.group24.lab5.webapp2.ticketcatalogue.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("orders")
data class Order(
    @Id
    val id: Long? = null,
    var status: String?,
    val ticket_id: Long?,
    val quantity: Int?,
    val user_id: Long?
)