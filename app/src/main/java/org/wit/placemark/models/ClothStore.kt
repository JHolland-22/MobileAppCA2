package org.wit.placemark.models


interface ClothStore {
    fun findAll(): List<ClothModel>
    fun create(cloth: ClothModel)
    fun update(cloth: ClothModel)
}