package it.group24.lab5.webapp2.ticketcatalogue.services

import it.group24.lab5.webapp2.ticketcatalogue.domain.Order
import it.group24.lab5.webapp2.ticketcatalogue.dtos.PaymentResponseDTO
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface OrderHandler {

    fun changeOrderStatus(paymentResponseDTO: PaymentResponseDTO)

    fun getAllOrders(serverRequest: ServerRequest): Mono<ServerResponse>

    fun getOrderById(order_id: Long, serverRequest: ServerRequest): Mono<ServerResponse>

    fun getOrdersByAllUsers(serverRequest: ServerRequest): Mono<ServerResponse>

    fun getOrderByUserId(userId: Long, serverRequest: ServerRequest): Mono<ServerResponse>
}