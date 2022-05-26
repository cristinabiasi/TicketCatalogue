package it.group24.lab5.webapp2.ticketcatalogue.repository

import it.group24.lab5.webapp2.ticketcatalogue.domain.Ticket
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface TicketRepository: CoroutineCrudRepository<Ticket, Long> {

}