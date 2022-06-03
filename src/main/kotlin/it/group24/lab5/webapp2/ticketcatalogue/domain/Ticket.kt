package it.group24.lab5.webapp2.ticketcatalogue.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table


@Table("ticketCatalogue")
data class Ticket(
    @Id
    val id: Long? = null,
    val type: String?,
    val price: Double?,
    val age_restriction: Int? = null
)