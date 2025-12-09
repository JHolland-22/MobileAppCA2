package org.wit.placemark.models

import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class ClothMemStore : TicketStore {

    val cloths = ArrayList<TicketModel>()

    override fun findAll(): List<TicketModel> {
        return cloths
    }

    override fun create(cloth: TicketModel) {
        cloth.id = getId()
        cloths.add(cloth)
        logAll()
    }

    override fun update(cloth: TicketModel) {
        var foundCloth: TicketModel? = cloths.find { p -> p.id == cloth.id }
        if (foundCloth != null) {
            foundCloth.title = cloth.title
            foundCloth.description = cloth.description
            logAll()
        }
    }

    private fun logAll() {
        cloths.forEach { i("$it") }
    }
}