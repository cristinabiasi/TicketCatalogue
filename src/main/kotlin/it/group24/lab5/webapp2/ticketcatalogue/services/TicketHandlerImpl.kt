package it.group24.lab5.webapp2.ticketcatalogue.services

import io.r2dbc.postgresql.codec.Json
import it.group24.lab5.webapp2.ticketcatalogue.domain.Order
import it.group24.lab5.webapp2.ticketcatalogue.domain.Ticket
import it.group24.lab5.webapp2.ticketcatalogue.dtos.PaymentRequestDTO
import it.group24.lab5.webapp2.ticketcatalogue.dtos.TicketPurchaseRequestDTO
import it.group24.lab5.webapp2.ticketcatalogue.repository.OrderRepository
import it.group24.lab5.webapp2.ticketcatalogue.repository.TicketRepository
import javafx.application.Application.launch
import org.json.JSONObject
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.stereotype.Service
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity.BodyBuilder
import org.springframework.http.ResponseEntity.ok
import org.springframework.messaging.handler.annotation.Header
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyExtractor
import org.springframework.web.reactive.function.BodyExtractors
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.function.server.body
import reactor.core.publisher.Flux

import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.text.DateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.abs

@Component
class TicketHandlerImpl(
    private val ticketRepository: TicketRepository,
    private val orderRepository: OrderRepository,
    private val template: R2dbcEntityTemplate
): TicketHandler{

    override fun getAllTickets(serverRequest: ServerRequest): Mono<ServerResponse> {
        return ServerResponse.status(200).body(ticketRepository.findAll(), Ticket::class.java)
    }

    override fun getTicketByID(ticketID: Long, serverRequest: ServerRequest): Mono<ServerResponse> {
        template
        return serverRequest.bodyToMono(TicketPurchaseRequestDTO::class.java).flatMap { ticketPurchaseRequestDTO ->
            ticketRepository.findById(ticketPurchaseRequestDTO.ticketID).flatMap { ticket ->
                WebClient
                    .create("http://localhost:8082")
                    .get()
                    .uri("/isAuthenticated")
                    .header("Authorization", serverRequest.headers().header("Authorization").firstOrNull())
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(Boolean::class.java)
                    .flatMap {
                        if (it) {
                            //user is authenticated
                            if (ticket.type.equals("weekend")){
                                //i have to check age restriction
                                WebClient
                                    .create("http://localhost:8082")
                                    .get()
                                    .uri("/my/dateOfBirth")
                                    .header(
                                        "Authorization",
                                        serverRequest.headers().header("Authorization").firstOrNull()
                                    )
                                    .accept(MediaType.APPLICATION_JSON)
                                    .retrieve()
                                    .bodyToMono(Date::class.java)
                                    .flatMap { dateOfBirth ->
                                        val diff = abs(Date().time - dateOfBirth.time)
                                        val age = (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)/365).toInt()
                                        if (age < 27){
                                            ServerResponse.status(403).body(Mono.just("You must be 27 years old to buy this type of ticket!"), String::class.java)
                                        }else{
                                            //I am at least 27, I can buy the ticket
                                            template.insert(Order(
                                                status = "PENDING",
                                                ticketId = ticket.id,
                                                quantity = ticketPurchaseRequestDTO.quantity,
                                                // I HAVE TO SAVE THE USERID CHECK LATER
                                            ))
                                            val billingInformation = PaymentRequestDTO(
                                                paymentInformationDTO = ticketPurchaseRequestDTO.paymentInformation,
                                                total = ticketPurchaseRequestDTO.quantity * ticket.price!!
                                            )
                                            // here I have to send the billing information to the
                                            // payment service endpoint

                                                //-------------//



                                                //-------------//

                                            ServerResponse.status(200).body(BodyInserters.empty<Any>())
                                        }
                                    }
                            }else{
                                //no age restriction for the other ticket type
                                ServerResponse.status(200).body(BodyInserters.empty<Any>())
                            }
                        } else {
                            //user is not authenticated
                            ServerResponse.status(401).body(Mono.just("User is NOT Authenticated"), String::class.java)
                        }
                    }
            }

        }
    }

}
