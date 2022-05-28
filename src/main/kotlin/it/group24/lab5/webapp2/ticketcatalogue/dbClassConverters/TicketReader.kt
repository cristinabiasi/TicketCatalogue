package it.group24.lab5.webapp2.ticketcatalogue.dbClassConverters


import io.r2dbc.postgresql.api.PostgresqlRow
import io.r2dbc.spi.Row
import it.group24.lab5.webapp2.ticketcatalogue.domain.Ticket
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter
import org.springframework.stereotype.Component

@Component
@ReadingConverter
class TicketReader: Converter<PostgresqlRow, Ticket> {
    override fun convert(source: PostgresqlRow): Ticket? {
        return Ticket(
            source["id"] as Long,
            source["type"] as String,
            source["price"] as Double
        )
    }

}