package it.group24.lab5.webapp2.ticketcatalogue.services

import it.group24.lab5.webapp2.ticketcatalogue.domain.Order
import it.group24.lab5.webapp2.ticketcatalogue.domain.Ticket
import it.group24.lab5.webapp2.ticketcatalogue.dtos.PaymentRequestDTO
import it.group24.lab5.webapp2.ticketcatalogue.dtos.TicketPurchaseRequestDTO
import it.group24.lab5.webapp2.ticketcatalogue.repository.OrderRepository
import it.group24.lab5.webapp2.ticketcatalogue.repository.TicketRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.http.MediaType
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse

import reactor.core.publisher.Mono
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.abs

@Component
class TicketHandlerImpl(
    private val ticketRepository: TicketRepository,
    private val orderRepository: OrderRepository,
    private val template: R2dbcEntityTemplate,
    private val kafkaTemplate: KafkaTemplate<String, Any>,
    @Value("\${kafka.topics.paymentReq}")
    private val topic: String
): TicketHandler{

    override fun getAllTickets(serverRequest: ServerRequest): Mono<ServerResponse> {
        return ServerResponse.status(200).body(ticketRepository.findAll(), Ticket::class.java)
    }

    private fun isAgeCompliant(dateOfBirth: Date, ageRestriction: Int): Boolean {
        val diff = abs(Date().time - dateOfBirth.time)
        val age = (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)/365).toInt()

        return age < ageRestriction
    }

    private fun getUserAge(serverRequest: ServerRequest): Mono<Date>{
        return WebClient
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
    }

    private fun getTicketFromCatalogue(serverRequest: ServerRequest): Mono<Ticket>{
        val ticketPurchaseRequestDTO = serverRequest.bodyToMono(TicketPurchaseRequestDTO::class.java)
        return ticketPurchaseRequestDTO.flatMap {
            ticketRepository.findById(it.ticketID)
        }
    }

    private fun getUserId(serverRequest: ServerRequest): Mono<Long>{
        return WebClient
            .create("http://localhost:8082")
            .get()
            .uri("/my/isAuthenticated")
            .header("Authorization", serverRequest.headers().header("Authorization").firstOrNull())
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(Long::class.java)
    }

    private fun createOrder(
        ticket: Ticket,
        ticketPurchaseRequestDTO: TicketPurchaseRequestDTO,
        userID: Long?
    ): Mono<ServerResponse> {
        return orderRepository.save(
            Order(
                status = "PENDING",
                ticketId = ticket.id,
                quantity = ticketPurchaseRequestDTO.quantity,
                userId = userID
            )
        ).flatMap {
            println(ticketPurchaseRequestDTO.paymentInformationDTO)
            val billingInformation = PaymentRequestDTO(
                paymentInformationDTO = ticketPurchaseRequestDTO.paymentInformationDTO,
                total = ticketPurchaseRequestDTO.quantity * ticket.price!!,
                orderId = it.id!!
            )

            // here I have to send the billing information to the
            // payment service endpoint

            kafkaTemplate.send(topic, billingInformation)

            ServerResponse.status(200).body(BodyInserters.fromValue(it.id!!))
        }
    }

    override fun getTicketByID(ticketID: Long, serverRequest: ServerRequest): Mono<ServerResponse> {
        template
        val ticketPurchaseRequestDTOMono = serverRequest.bodyToMono(TicketPurchaseRequestDTO::class.java)

        val userIdMono = getUserId(serverRequest)

        val ticketMono = getTicketFromCatalogue(serverRequest)

        // Serve per aspettare che finiscano tutti
        return Mono.zip(userIdMono, ticketMono, ticketPurchaseRequestDTOMono).flatMap {
            val userId = it.t1
            val ticket = it.t2
            val ticketPurchaseRequestDTO = it.t3

            if (userId < 0)
                return@flatMap ServerResponse.status(401).bodyValue("User is NOT Authenticated")

            if (ticket.age_restriction == null)
                return@flatMap createOrder(ticket, ticketPurchaseRequestDTO, userId)

            getUserAge(serverRequest).flatMap { dateOfBirth ->
                if(!isAgeCompliant(dateOfBirth, ticket.age_restriction!!))
                    return@flatMap ServerResponse.status(400).bodyValue("You are not allowed to buy this type of ticket!")

                createOrder(ticket, ticketPurchaseRequestDTO, userId)
            }
        }
    }

    /*fun getTicketByID2(ticketID: Long, serverRequest: ServerRequest): Mono<ServerResponse> {
        template
        return serverRequest.bodyToMono(TicketPurchaseRequestDTO::class.java)
            .flatMap { ticketPurchaseRequestDTO ->
            ticketRepository.findById(ticketPurchaseRequestDTO.ticketID).flatMap { ticket ->
                WebClient
                    .create("http://localhost:8082")
                    .get()
                    .uri("/my/isAuthenticated")
                    .header("Authorization", serverRequest.headers().header("Authorization").firstOrNull())
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(Long::class.java)
                    .flatMap {
                        userID ->
                        if (userID > 0) {
                            //user is authenticated
                            if (ticket.age_restriction != null){
                                //I have to check age restriction
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
                                        if (isAgeCompliant(dateOfBirth, ticket.age_restriction!!)){
                                            //da rivedere
                                            ServerResponse.status(400).body(Mono.just("You are not allowed to buy this type of ticket!"), String::class.java)
                                        }else{
                                            createOrder(ticket, ticketPurchaseRequestDTO, userID)
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
    }*/
}
