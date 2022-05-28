package it.group24.lab5.webapp2.ticketcatalogue.routers

import com.fasterxml.jackson.databind.ObjectMapper
import io.netty.handler.codec.http.HttpMethod.GET
import it.group24.lab5.webapp2.ticketcatalogue.services.TicketHandlerImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.web.reactive.config.WebFluxConfigurer
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.RequestPredicates.GET
import org.springframework.web.reactive.function.server.RequestPredicates.accept
import org.springframework.web.reactive.function.server.RouterFunctions.route
import sun.plugin2.message.JavaScriptMemberOpMessage.GET


@Configuration
class TicketRouter: WebFluxConfigurer {

    /*@Autowired
    private lateinit var objectMapper: ObjectMapper

    override fun configureHttpMessageCodecs(configurer: ServerCodecConfigurer) {
        configurer.defaultCodecs()
            .jackson2JsonEncoder(Jackson2JsonEncoder(objectMapper, MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN))
        configurer.defaultCodecs()
            .jackson2JsonDecoder(Jackson2JsonDecoder(objectMapper, MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN))
    }*/

    @Bean
    fun routes(ticketHandlerImpl: TicketHandlerImpl): RouterFunction<ServerResponse> {
        return RouterFunctions
            .route(GET("/tickets").and(accept(MediaType.APPLICATION_JSON)), ticketHandlerImpl::getAllTickets)
    }
}