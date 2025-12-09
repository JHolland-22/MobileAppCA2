package org.wit.placemark.models


interface TicketStore {
    fun findAll(): List<TicketModel>
    fun create(cloth: TicketModel)
    fun update(cloth: TicketModel)
}