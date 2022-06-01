package it.webappii.group24.paymentservice

import org.slf4j.LoggerFactory
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment


@SpringBootApplication
class PaymentServiceApplication(){
    private val logger = LoggerFactory.getLogger(javaClass)

    @KafkaListener(topics = ["\${kafka.topics.paymentReq}"], groupId = "paymentRequest")
    fun listenGroupFoo(consumerRecord: ConsumerRecord<Any, Any>, ack: Acknowledgment) {
        logger.info("Message received {}", consumerRecord)
        ack.acknowledge()
    }
}

fun main(args: Array<String>) {
    runApplication<PaymentServiceApplication>(*args)
}
