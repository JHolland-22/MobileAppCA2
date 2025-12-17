package org.wit.ticket.models


interface TicketStore {
    fun findAll(): List<TicketModel>
    fun create(ticket: TicketModel)
    fun update(ticket: TicketModel)
    fun delete(ticket: TicketModel)
    fun findById(id: Long): TicketModel?
    fun deleteAll()

}