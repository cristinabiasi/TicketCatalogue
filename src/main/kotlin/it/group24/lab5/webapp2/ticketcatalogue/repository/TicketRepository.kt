package it.group24.lab5.webapp2.ticketcatalogue.repository

import it.group24.lab5.webapp2.ticketcatalogue.domain.Ticket
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux


interface TicketRepository: ReactiveCrudRepository<Ticket, Long> {


}