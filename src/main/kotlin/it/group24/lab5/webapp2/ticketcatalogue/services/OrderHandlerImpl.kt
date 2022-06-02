package it.group24.lab5.webapp2.ticketcatalogue.services

import it.group24.lab5.webapp2.ticketcatalogue.domain.Order
import it.group24.lab5.webapp2.ticketcatalogue.dtos.TicketRequestDTO
import it.group24.lab5.webapp2.ticketcatalogue.repository.OrderRepository
import it.group24.lab5.webapp2.ticketcatalogue.repository.TicketRepository
import org.springframework.stereotype.Component
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
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
            orderRepository.save(it)
            addPurchasedTicket(it)
        }
    }

    private fun addPurchasedTicket(order: Order) {
        WebClient
            .create("http://localhost:8082")
            .post()
            .uri("/api/tickets/${order.userId}")
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(TicketRequestDTO("buy_tickets", order.quantity, "abc"))
            .retrieve()
    }

    override fun getAllOrders(serverRequest: ServerRequest): Mono<ServerResponse> {
        TODO("Not yet implemented")
        /*template
        return WebClient
            .create("http://localhost:8082")
            .get()
            .uri("/isAuthenticated")
            .header(
                "Authorization",
                serverRequest.headers().header("Authorization").firstOrNull()
            )
            .retrieve()
            .bodyToMono(Long::class.java)
            .flatMap { userID ->
                if (userID > 0) {
                    //user is authenticated

                }
            }*/
    }

    override fun getOrderById(serverRequest: ServerRequest): Mono<ServerResponse> {
        TODO("Not yet implemented")
    }
}
