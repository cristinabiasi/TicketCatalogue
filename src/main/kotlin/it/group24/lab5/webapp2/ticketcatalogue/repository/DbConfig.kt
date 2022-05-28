package it.group24.lab5.webapp2.ticketcatalogue.repository

import io.r2dbc.spi.ConnectionFactory
import it.group24.lab5.webapp2.ticketcatalogue.dbClassConverters.TicketReader
import it.group24.lab5.webapp2.ticketcatalogue.dbClassConverters.TicketWriter
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.r2dbc.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.r2dbc.connection.R2dbcTransactionManager
import org.springframework.r2dbc.connection.init.CompositeDatabasePopulator
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator
import org.springframework.transaction.ReactiveTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement


@Configuration
@EnableR2dbcRepositories
@EnableTransactionManagement
class DbConfig(): AbstractR2dbcConfiguration(){

    @Value("\${spring.r2dbc.url}")
    val url: String? = null

    @Bean
    override fun connectionFactory(): ConnectionFactory {
        return ConnectionFactoryBuilder.withUrl(url)
            .port(5432)
            .username("postgres")
            .password("postgres")
            .build()
    }

    @Bean
    fun template(connectionFactory: ConnectionFactory): R2dbcEntityTemplate {
        return R2dbcEntityTemplate(connectionFactory)
    }

    @Bean
    fun transactionManager(connectionFactory: ConnectionFactory): ReactiveTransactionManager {
        return R2dbcTransactionManager(connectionFactory)
    }

    @Bean
    fun databaseInitializer(connectionFactory: ConnectionFactory): ConnectionFactoryInitializer{
        val cfi = ConnectionFactoryInitializer()
        cfi.setConnectionFactory(connectionFactory)
        val populator = CompositeDatabasePopulator()
        populator.addPopulators(ResourceDatabasePopulator(ClassPathResource("schema.sql")))
        populator.addPopulators(ResourceDatabasePopulator(ClassPathResource("data.sql")))
        cfi.setDatabasePopulator(populator)
        return cfi
    }

    override fun getCustomConverters(): MutableList<Any> {
        return mutableListOf(TicketReader(), TicketWriter())
    }

}













