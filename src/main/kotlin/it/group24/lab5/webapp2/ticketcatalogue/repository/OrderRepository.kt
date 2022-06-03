package it.group24.lab5.webapp2.ticketcatalogue.repository

import it.group24.lab5.webapp2.ticketcatalogue.domain.Order
import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface OrderRepository: ReactiveCrudRepository<Order, Long> {

    @Modifying
    @Query("update orders set status = 'CONFIRMED' where id = ?1")
    fun updateStatusById(id: Long)
}