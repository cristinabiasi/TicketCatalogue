package it.group24.lab5.webapp2.ticketcatalogue.services

import io.r2dbc.postgresql.codec.Json
import it.group24.lab5.webapp2.ticketcatalogue.domain.Ticket
import it.group24.lab5.webapp2.ticketcatalogue.dtos.TicketPurchaseRequestDTO
import it.group24.lab5.webapp2.ticketcatalogue.repository.TicketRepository
import org.json.JSONObject
import org.springframework.stereotype.Service
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity.ok
import org.springframework.messaging.handler.annotation.Header
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyExtractor
import org.springframework.web.reactive.function.BodyExtractors
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.function.server.body
import reactor.core.publisher.Flux

import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Component
class TicketHandlerImpl(
    private val ticketRepository: TicketRepository
): TicketHandler{

    override fun getAllTickets(serverRequest: ServerRequest): Mono<ServerResponse> {
        return ServerResponse.status(200).body(ticketRepository.findAll(), Ticket::class.java)
    }

    override fun getTicketByID(ticketID: Long, serverRequest: ServerRequest): Mono<ServerResponse> {

        return serverRequest.bodyToMono(TicketPurchaseRequestDTO::class.java).flatMap {

            WebClient
                .create("http://localhost:8082")
                .get()
                .uri("/isAuthenticated")
                .header("Authorization", serverRequest.headers().header("Authorization").firstOrNull())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Boolean::class.java)
                .flatMap {
                    if (it){
                        //user is authenticated
                        println("L'utente è autenticato")
                        ServerResponse.status(200).body(BodyInserters.empty<Any>())
                    } else {
                        //user is not authenticated
                        println("L'utente non è autenticato")
                        ServerResponse.status(401).body(BodyInserters.empty<Any>())
                    }
                }
        }
    }

}
