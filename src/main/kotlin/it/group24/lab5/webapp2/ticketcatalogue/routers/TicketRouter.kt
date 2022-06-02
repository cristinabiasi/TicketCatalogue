package it.group24.lab5.webapp2.ticketcatalogue.routers

import com.fasterxml.jackson.databind.ObjectMapper
import io.netty.handler.codec.http.HttpMethod.GET
import io.netty.handler.codec.http.HttpMethod.POST
import it.group24.lab5.webapp2.ticketcatalogue.services.TicketHandlerImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.reactive.config.WebFluxConfigurer
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.RequestPredicates.*
import org.springframework.web.reactive.function.server.RouterFunctions.route


@Configuration
@Component
class TicketRouter(){

    /*@Autowired
    private lateinit var objectMapper: ObjectMapper

    override fun configureHttpMessageCodecs(configurer: ServerCodecConfigurer) {
        configurer.defaultCodecs()
            .jackson2JsonEncoder(Jackson2JsonEncoder(objectMapper, MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN))
        configurer.defaultCodecs()
            .jackson2JsonDecoder(Jackson2JsonDecoder(objectMapper, MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN))
    }*/

    @Bean
    fun mainRouter(ticketHandlerImpl: TicketHandlerImpl) = router {

        accept(MediaType.TEXT_HTML).nest {
            GET("/tickets", ticketHandlerImpl::getAllTickets)
        }

        accept(MediaType.APPLICATION_JSON).nest{
            POST("/shops/{ticket-id}") {
                ticketHandlerImpl.getTicketByID(it.pathVariable("ticket-id").toLong(), it)
            }
        }
    }

}















