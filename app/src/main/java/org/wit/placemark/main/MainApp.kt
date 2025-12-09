package org.wit.placemark.main

import android.app.Application
import org.wit.ticket.models.TicketStore
import org.wit.ticket.models.TicketMemStore
import org.wit.placemark.models.PlacemarkJSONStore
import org.wit.placemark.models.PlacemarkStore
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    lateinit var placemarks: PlacemarkStore
    lateinit var tickets: TicketStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        placemarks = PlacemarkJSONStore(applicationContext)
        tickets = TicketMemStore()
        i("App started")
    }
}
