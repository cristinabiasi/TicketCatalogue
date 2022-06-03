package it.group24.lab5.webapp2.ticketcatalogue.customSerializer

import com.fasterxml.jackson.databind.ObjectMapper
import it.group24.lab5.webapp2.ticketcatalogue.dtos.PaymentRequestDTO
import org.apache.kafka.common.errors.SerializationException
import org.apache.kafka.common.serialization.Serializer
import org.slf4j.LoggerFactory

class PaymentSerializer: Serializer<PaymentRequestDTO> {
    private val objectMapper = ObjectMapper()
    private val log = LoggerFactory.getLogger(javaClass)

    override fun serialize(topic: String?, data: PaymentRequestDTO?): ByteArray? {
        log.info("Serializing...")
        return objectMapper.writeValueAsBytes(
            data ?: throw SerializationException("Error when serializing PaymentRequest  to ByteArray[]")
        )
    }

    override fun close() {}
}