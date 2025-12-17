package org.wit.ticket.models

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
            foundTicket.ticketStatus = ticket.ticketStatus
            foundTicket.stage = ticket.stage
            foundTicket.teamA = ticket.teamA
            foundTicket.teamB = ticket.teamB
            foundTicket.pitchName = ticket.pitchName
            foundTicket.matchDate = ticket.matchDate
            foundTicket.image = ticket.image
            logAll()
        }
    }

    override fun delete(ticket: TicketModel) {
        tickets.remove(ticket)
        logAll()
    }

    override fun findById(id: Long): TicketModel? {
        return tickets.find { it.id == id }
    }

    private fun logAll() {
        tickets.forEach { i("$it") }
    }
    override fun deleteAll() {
        tickets.clear()
    }

}