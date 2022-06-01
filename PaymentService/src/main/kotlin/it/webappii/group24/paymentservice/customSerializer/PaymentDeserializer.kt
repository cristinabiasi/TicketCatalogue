package it.webappii.group24.paymentservice.customSerializer

import com.fasterxml.jackson.databind.ObjectMapper
import it.webappii.group24.paymentservice.dtos.PaymentRequestDTO
import org.apache.kafka.common.errors.SerializationException
import org.apache.kafka.common.serialization.Deserializer
import org.slf4j.LoggerFactory
import java.nio.charset.StandardCharsets.UTF_8

class PaymentDeserializer: Deserializer<PaymentRequestDTO> {

    private val objectMapper = ObjectMapper()
    private val log = LoggerFactory.getLogger(javaClass)

    override fun deserialize(topic: String?, data: ByteArray?): PaymentRequestDTO? {
        log.info("Deserializing...")
        return objectMapper.readValue(
            String(
                data ?: throw SerializationException("Error when deserializing byte[] to PaymentRequest"), UTF_8
            ), PaymentRequestDTO::class.java
        )
    }

        override fun close() {}
}
