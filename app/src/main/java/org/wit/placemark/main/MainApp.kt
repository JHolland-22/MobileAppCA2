package org.wit.placemark.main

import android.app.Application
import org.wit.placemark.models.ClothMemStore
import org.wit.placemark.models.PlacemarkJSONStore
import org.wit.placemark.models.PlacemarkMemStore
import org.wit.placemark.models.PlacemarkStore
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    lateinit var placemarks: PlacemarkStore
    val cloths = ClothMemStore()


    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        placemarks = PlacemarkJSONStore(applicationContext)
        i("App started")
    }
}

