package it.group24.lab5.webapp2.ticketcatalogue.dbClassConverters

import it.group24.lab5.webapp2.ticketcatalogue.domain.Ticket
import org.springframework.core.convert.converter.Converter
import org.springframework.data.r2dbc.mapping.OutboundRow
import org.springframework.r2dbc.core.Parameter
import org.springframework.data.convert.WritingConverter
import org.springframework.stereotype.Component


@Component
@WritingConverter
class TicketWriter: Converter<Ticket, OutboundRow> {
    override fun convert(source: Ticket): OutboundRow? {
        return OutboundRow().apply {
            if (source.id != null)
                put("id", Parameter.from(source.id))
            put("type", Parameter.from(source.type!!))
            put("price", Parameter.from(source.price!!))
            put("age_restriction", Parameter.from(source.age_restriction!!))
        }
    }
}