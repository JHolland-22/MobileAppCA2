package org.wit.placemark.models


interface TicketStore {
    fun findAll(): List<TicketModel>
    fun create(ticket: TicketModel)
    fun update(ticket: TicketModel)
}