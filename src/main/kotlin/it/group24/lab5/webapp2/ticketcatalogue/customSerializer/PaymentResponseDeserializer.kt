package it.group24.lab5.webapp2.ticketcatalogue.customSerializer

import com.fasterxml.jackson.databind.ObjectMapper
import it.group24.lab5.webapp2.ticketcatalogue.dtos.PaymentResponseDTO
import org.apache.kafka.common.errors.SerializationException
import org.apache.kafka.common.serialization.Deserializer
import org.slf4j.LoggerFactory
import java.nio.charset.StandardCharsets

class PaymentResponseDeserializer: Deserializer<PaymentResponseDTO> {

    private val objectMapper = ObjectMapper()
    private val log = LoggerFactory.getLogger(javaClass)

    override fun deserialize(topic: String?, data: ByteArray?): PaymentResponseDTO? {
        log.info("Deserializing...")
        return objectMapper.readValue(
            String(
                data ?: throw SerializationException("Error when deserializing byte[] to PaymentRequest"),
                StandardCharsets.UTF_8
            ), PaymentResponseDTO::class.java
        )
    }

    override fun close() {}
}