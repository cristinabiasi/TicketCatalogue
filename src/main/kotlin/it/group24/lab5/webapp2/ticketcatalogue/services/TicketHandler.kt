package it.group24.lab5.webapp2.ticketcatalogue.services

import it.group24.lab5.webapp2.ticketcatalogue.dtos.TicketDTO
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

interface TicketHandler {

    fun getAllTickets(serverRequest: ServerRequest): Mono<ServerResponse>

    fun getTicketByID(serverRequest: ServerRequest): Mono<ServerResponse>

    fun addTicket(serverRequest: ServerRequest): Mono<ServerResponse>
}