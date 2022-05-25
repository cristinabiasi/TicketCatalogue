package it.group24.lab5.webapp2.ticketcatalogue.repository

import it.group24.lab5.webapp2.ticketcatalogue.domain.Order
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface OrderRepository: CoroutineCrudRepository<Order, Long> {
}