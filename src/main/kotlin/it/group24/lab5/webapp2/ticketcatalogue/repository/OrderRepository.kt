package it.group24.lab5.webapp2.ticketcatalogue.repository

import it.group24.lab5.webapp2.ticketcatalogue.domain.Order
import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface OrderRepository: ReactiveCrudRepository<Order, Long> {


    @Query("select * from orders where user_id = ?1")
    fun findAllByUserId(user_id: Long): Flux<Order>

    @Query("select * from orders where order_id = ?1 and user_id = ?2")
    fun findOrderById(orderId: Long, userId: Long): Mono<Order>
}