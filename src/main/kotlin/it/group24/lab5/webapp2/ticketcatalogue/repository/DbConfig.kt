package it.group24.lab5.webapp2.ticketcatalogue.repository

import io.r2dbc.spi.ConnectionFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.r2dbc.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator


@Configuration
@EnableR2dbcRepositories
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
    fun databaseInitializer(connectionFactory: ConnectionFactory): ConnectionFactoryInitializer{
        val cfi = ConnectionFactoryInitializer()
        cfi.setConnectionFactory(connectionFactory)
        cfi.setDatabasePopulator(
            ResourceDatabasePopulator(
                ClassPathResource("schema.sql")
            )
        )
        return cfi
    }





    /*// Inizializzazione dello schema nel db
    @Bean
    fun initializer(connectionFactory: ConnectionFactory): ConnectionFactoryInitializer {
        //println(url)
        val cfi = ConnectionFactoryInitializer()
        cfi.setConnectionFactory(connectionFactory)
        cfi.setDatabasePopulator(
            ResourceDatabasePopulator(
                ClassPathResource("schema.sql")
            )
        )
        return cfi
    }*/

}













