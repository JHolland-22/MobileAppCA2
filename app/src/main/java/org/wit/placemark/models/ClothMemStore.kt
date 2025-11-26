package org.wit.placemark.models

import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class ClothMemStore : ClothStore {

    val cloths = ArrayList<ClothModel>()

    override fun findAll(): List<ClothModel> {
        return cloths
    }

    override fun create(cloth: ClothModel) {
        cloth.id = getId()
        cloths.add(cloth)
        logAll()
    }

    override fun update(cloth: ClothModel) {
        var foundCloth: ClothModel? = cloths.find { p -> p.id == cloth.id }
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