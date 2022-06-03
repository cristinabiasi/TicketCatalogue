package it.group24.lab5.webapp2.ticketcatalogue.services

import it.group24.lab5.webapp2.ticketcatalogue.domain.Order
import it.group24.lab5.webapp2.ticketcatalogue.domain.Ticket
import it.group24.lab5.webapp2.ticketcatalogue.dtos.PaymentRequestDTO
import it.group24.lab5.webapp2.ticketcatalogue.dtos.TicketDTO
import it.group24.lab5.webapp2.ticketcatalogue.dtos.TicketPurchaseRequestDTO
import it.group24.lab5.webapp2.ticketcatalogue.dtos.toDTO
import it.group24.lab5.webapp2.ticketcatalogue.repository.OrderRepository
import it.group24.lab5.webapp2.ticketcatalogue.repository.TicketRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.http.MediaType;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

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
    private val kafkaListenerContainerFactory: ConcurrentKafkaListenerContainerFactory<String, Any>,
    @Value("\${kafka.topics.paymentReq}")
    private val topic: String
): TicketHandler{

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
                ticket_id = ticket.id,
                quantity = ticketPurchaseRequestDTO.quantity,
                user_id = userID
            )
        ).flatMap {
            val billingInformation = PaymentRequestDTO(
                paymentInformationDTO = ticketPurchaseRequestDTO.paymentInformationDTO,
                total = ticketPurchaseRequestDTO.quantity * ticket.price!!,
                orderId = it.id!!,
                userId = userID!!
            )

            // here I have to send the billing information to the
            // payment service endpoint

            kafkaTemplate.send(topic, billingInformation)

            ServerResponse.status(200).body(BodyInserters.fromValue(it.id!!))
        }
    }

    override fun getAllTickets(serverRequest: ServerRequest): Mono<ServerResponse> {
        return ServerResponse.status(200).body(ticketRepository.findAll(), Ticket::class.java)
    }

    override fun getTicketByID(serverRequest: ServerRequest): Mono<ServerResponse> {
        return serverRequest.bodyToMono(TicketPurchaseRequestDTO::class.java).flatMap{ ticketPurchaseRequestDTO ->
            getUserId(serverRequest).flatMap { userId ->
                ticketRepository.findById(ticketPurchaseRequestDTO.ticketID).flatMap { ticket ->
                    if (userId < 0)
                        ServerResponse.status(401).bodyValue("User is NOT Authenticated")
                    else {
                        if (ticket.age_restriction == null)
                            return@flatMap createOrder(ticket, ticketPurchaseRequestDTO, userId)

                        getUserAge(serverRequest).flatMap { dateOfBirth ->
                            if (!isAgeCompliant(dateOfBirth, ticket.age_restriction!!))
                                return@flatMap ServerResponse.status(400)
                                    .bodyValue("You are not allowed to buy this type of ticket!")

                            createOrder(ticket, ticketPurchaseRequestDTO, userId)
                        }
                    }
                }
            }
        }


        }
        override fun addTicket(serverRequest: ServerRequest): Mono<TicketDTO>? {
            if (!isAdmin(serverRequest))
                return null

            return serverRequest.bodyToMono(TicketDTO::class.java).flatMap {
                val newTicket = Ticket(null, it.type, it.price, it.age_restriction)

                return@flatMap ticketRepository.save(newTicket).map { savedTicket ->
                    savedTicket.toDTO()
                }
            }
        }

        private fun isAdmin(serverRequest: ServerRequest): Boolean{
            return WebClient
                .create("http://localhost:8082")
                .get()
                .uri("/my/isAdmin")
                .header(
                    "Authorization",
                    serverRequest.headers().header("Authorization").firstOrNull()
                )
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Boolean::class.java)
                .block()!!
    }
}


