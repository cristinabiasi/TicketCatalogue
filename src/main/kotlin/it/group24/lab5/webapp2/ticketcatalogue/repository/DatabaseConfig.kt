package it.group24.lab5.webapp2.ticketcatalogue.repository

import io.r2dbc.spi.ConnectionFactories
import io.r2dbc.spi.ConnectionFactory
import io.r2dbc.spi.ConnectionFactoryOptions
import org.springframework.boot.r2dbc.ConnectionFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator

@Configuration
@EnableR2dbcRepositories
class DatabaseConfig: AbstractR2dbcConfiguration() {

    // Configurazione generica della connessione al db
    // Le connessioni sono chieste al Connection Pool
    @Bean
    override fun connectionFactory(): ConnectionFactory {
        val builder = ConnectionFactoryBuilder().build()
        //return ConnectionFactories.get("r2dbc:postgresql://localhost:5432/ticketCatalogue")
        /*return ConnectionFactories.get(
            ConnectionFactoryOptions.builder().apply {
                option(ConnectionFactoryOptions.DRIVER, "pool")
                option(ConnectionFactoryOptions.PROTOCOL, "postgresql")
                option(ConnectionFactoryOptions.HOST, "localhost")
                option(ConnectionFactoryOptions.PORT, 5432)
                option(ConnectionFactoryOptions.USER, "postgres")
                option(ConnectionFactoryOptions.PASSWORD, "mysecretpassword")
                option(ConnectionFactoryOptions.DATABASE, "ticketCatalogue")
            }.build()
        )*/
    }

    // Inizializzazione dello schema nel db
    @Bean
    fun initializer(connectionFactory: ConnectionFactory): ConnectionFactoryInitializer {
        val cfi = ConnectionFactoryInitializer()
        cfi.setConnectionFactory(connectionFactory)
        cfi.setDatabasePopulator(
            ResourceDatabasePopulator(
                ClassPathResource("schema.sql")
            )
        )

        return cfi
    }

}