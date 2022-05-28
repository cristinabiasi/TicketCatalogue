package it.group24.lab5.webapp2.ticketcatalogue.services

import it.group24.lab5.webapp2.ticketcatalogue.domain.Ticket
import it.group24.lab5.webapp2.ticketcatalogue.repository.TicketRepository
import org.springframework.stereotype.Service
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.function.server.body
import reactor.core.publisher.Flux

import reactor.core.publisher.Mono

@Component
class TicketHandlerImpl(
    private val ticketRepository: TicketRepository
): TicketHandler{

    override fun getAllTickets(serverRequest: ServerRequest): Mono<ServerResponse> {
        try{
            return ServerResponse.status(200).body(ticketRepository.findAll(), Ticket::class.java)
        }catch(e: Error){
            println(e)
        }
        return ServerResponse.status(200).body(Mono.empty())
    }
}