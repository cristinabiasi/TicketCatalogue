package it.group24.lab5.webapp2.ticketcatalogue.services

import it.group24.lab5.webapp2.ticketcatalogue.domain.Order
import it.group24.lab5.webapp2.ticketcatalogue.dtos.TicketRequestDTO
import it.group24.lab5.webapp2.ticketcatalogue.repository.OrderRepository
import it.group24.lab5.webapp2.ticketcatalogue.repository.TicketRepository
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.Query
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Component
class OrderHandlerImpl(
    private val ticketRepository: TicketRepository,
    private val orderRepository: OrderRepository,
    private val template: R2dbcEntityTemplate
): OrderHandler {

    override fun changeOrderStatus(id: Long) {
        orderRepository.findById(id).subscribe {
            it.status = "CONFIRMED"
            orderRepository.save(it).subscribe()
            addPurchasedTicket(it)

        }
    }

    private fun addPurchasedTicket(order: Order) {
        WebClient
            .create("http://localhost:8082")
            .post()
            .uri("/api/tickets/${order.user_id}")
            .accept(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(TicketRequestDTO("buy_tickets", order.quantity, "abc")))
            //.bodyValue(TicketRequestDTO("buy_tickets", order.quantity, "abc"))
            .retrieve()
            .bodyToFlux(Any::class.java)
            .subscribe()

    }

    override fun getAllOrders(serverRequest: ServerRequest): Mono<ServerResponse> {
        return WebClient.create("http://localhost:8082/my")
            .get()
            .uri("/isAuthenticated")
            .header(
                "Authorization",
                serverRequest.headers().header("Authorization").firstOrNull()
            )
            .retrieve()
            .bodyToMono(Long::class.java)
            .flatMap { userId ->
                if(userId > 0) {
                    val orders: Flux<Order> = template.select(Query.query(
                        Criteria.from(
                            Criteria.where("user_id").`is`(userId)
                        )), Order::class.java)
                        ServerResponse.ok().body(orders, Order::class.java)
                }
                else {
                     ServerResponse.status(HttpStatus.UNAUTHORIZED)
                        .body(BodyInserters.fromValue("Unauthorized user!"))
                }
            }
    }

    override fun getOrderById(order_id: Long, serverRequest: ServerRequest): Mono<ServerResponse> {
        return WebClient.create("http://localhost:8082/my")
            .get()
            .uri("/isAuthenticated")
            .header(
                "Authorization",
                serverRequest.headers().header("Authorization").firstOrNull()
            )
            .retrieve()
            .bodyToMono(Long::class.java)
            .flatMap { userId ->
                if(userId > 0) {
                    val orders: Flux<Order> = template.select(Query.query(
                        Criteria.from(
                            Criteria.where("user_id").`is`(userId)
                                .and("id").`is`(order_id)
                        )), Order::class.java)
                    ServerResponse.ok().body(orders, Order::class.java)

                }
                else {
                    ServerResponse.status(HttpStatus.UNAUTHORIZED)
                        .body(BodyInserters.fromValue("Unauthorized user!"))
                }
            }
    }

    override fun getOrdersByAllUsers(serverRequest: ServerRequest): Mono<ServerResponse> {
        return WebClient.create("http://localhost:8082/my")
            .get()
            .uri("/isAdmin")
            .header(
                "Authorization",
                serverRequest.headers().header("Authorization").firstOrNull()
            )
            .retrieve()
            .bodyToMono(Boolean::class.java)
            .flatMap { isAdmin ->
                if (isAdmin) {
                    // administrator
                    ServerResponse.status(200)
                        .body(
                            orderRepository.findAll(),
                            Order::class.java
                        )
                } else {
                    ServerResponse.status(HttpStatus.UNAUTHORIZED)
                        .body(BodyInserters.fromValue("Unauthorized user!"))
                }
            }
    }

    override fun getOrderByUserId(userId: Long, serverRequest: ServerRequest): Mono<ServerResponse> {
        return WebClient.create("http://localhost:8082/my")
            .get()
            .uri("/isAdmin")
            .header(
                "Authorization",
                serverRequest.headers().header("Authorization").firstOrNull()
            )
            .retrieve()
            .bodyToMono(Boolean::class.java)
            .flatMap { isAdmin ->
                if (isAdmin) {
                    // administrator
                    val orders: Flux<Order> = template.select(Query.query(
                        Criteria.from(
                            Criteria.where("user_id").`is`(userId)
                        )), Order::class.java)
                    ServerResponse.ok().body(orders, Order::class.java)
                } else {
                    ServerResponse.status(HttpStatus.UNAUTHORIZED)
                        .body(BodyInserters.fromValue("Unauthorized user!"))
                }
            }

    }
}