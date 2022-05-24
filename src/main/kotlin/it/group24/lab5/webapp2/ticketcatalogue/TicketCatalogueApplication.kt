package it.group24.lab5.webapp2.ticketcatalogue

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TicketCatalogueApplication

fun main(args: Array<String>) {
    runApplication<TicketCatalogueApplication>(*args)
}
