package it.group24.lab5.webapp2.ticketcatalogue.dbClassConverters

import io.r2dbc.postgresql.api.PostgresqlRow
import it.group24.lab5.webapp2.ticketcatalogue.domain.Order
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter
import org.springframework.stereotype.Component

@Component
@ReadingConverter
class OrderReader: Converter<PostgresqlRow, Order> {
    override fun convert(source: PostgresqlRow): Order? {
        return Order(
            source["id"] as Long,
            source["status"] as String,
            source["ticket_id"] as Long,
            source["quantity"] as Int,
            source["user_id"] as Long
        )

    }
}