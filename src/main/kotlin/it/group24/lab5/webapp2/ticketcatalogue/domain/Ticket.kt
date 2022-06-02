package it.group24.lab5.webapp2.ticketcatalogue.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table


@Table("ticketCatalogue")
class Ticket(
    @Id
    var id: Long?,
    var type: String?,
    var price: Double?,
    var age_restriction: Int?
)