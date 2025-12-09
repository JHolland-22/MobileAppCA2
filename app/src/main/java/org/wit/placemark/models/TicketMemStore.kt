package org.wit.placemark.models

import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class TicketMemStore : TicketStore {

    val tickets = ArrayList<TicketModel>()

    override fun findAll(): List<TicketModel> {
        return tickets
    }

    override fun create(ticket: TicketModel) {
        ticket.id = getId()
        tickets.add(ticket)
        logAll()
    }

    override fun update(ticket: TicketModel) {
        var foundTicket: TicketModel? = tickets.find { p -> p.id == ticket.id }
        if (foundTicket != null) {
            foundTicket.title = ticket.title
            foundTicket.description = ticket.description
            logAll()
        }
    }

    private fun logAll() {
        tickets.forEach { i("$it") }
    }
}