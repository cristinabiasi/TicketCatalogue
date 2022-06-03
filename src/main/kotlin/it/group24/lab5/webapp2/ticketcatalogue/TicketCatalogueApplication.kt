package it.group24.lab5.webapp2.ticketcatalogue

import it.group24.lab5.webapp2.ticketcatalogue.KafkaConfig.KafkaConfig
import it.group24.lab5.webapp2.ticketcatalogue.services.OrderHandlerImpl
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.Acknowledgment
import java.util.*

@SpringBootApplication
class TicketCatalogueApplication(
    private val kafkaTemplate: KafkaTemplate<String, Any>,
    @Value("\${kafka.topics.paymentAns}")
    private val topic2: String,
    val orderHandler: OrderHandlerImpl
){
    private val logger = LoggerFactory.getLogger(javaClass)


    @KafkaListener(topics = ["\${kafka.topics.paymentAns}"], groupId = "paymentAnswer")
    fun listenPaymentAnswer(consumerRecord: ConsumerRecord<Any, String>, ack: Acknowledgment) {
        val request = consumerRecord.value().toLong()
        logger.info("Message received {}", request)
        if(request >= 0){
            orderHandler.changeOrderStatus(request)
        }

        ack.acknowledge()
    }
}

fun main(args: Array<String>) {
    runApplication<TicketCatalogueApplication>(*args)
}
