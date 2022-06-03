package it.group24.lab5.webapp2.ticketcatalogue.dbClassConverters

import it.group24.lab5.webapp2.ticketcatalogue.domain.Order
import org.springframework.core.convert.converter.Converter
import org.springframework.data.r2dbc.mapping.OutboundRow
import org.springframework.r2dbc.core.Parameter
import org.springframework.data.convert.WritingConverter
import org.springframework.stereotype.Component


@Component
@WritingConverter
class OrderWriter: Converter<Order, OutboundRow> {
    override fun convert(source: Order): OutboundRow? {
        return OutboundRow().apply {
            if (source.id != null)
                put("id", Parameter.from(source.id))
            put("status", Parameter.from(source.status!!))
            put("ticket_id", Parameter.from(source.ticket_id!!))
            put("quantity", Parameter.from(source.quantity!!))
            put("user_id", Parameter.from(source.user_id!!))
        }
    }
}