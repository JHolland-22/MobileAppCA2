package org.wit.placemark.views.dashboard

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import org.wit.placemark.R
import org.wit.placemark.views.placemarklist.PlacemarkListView
import org.wit.ticket.views.ticketlist.TicketListActivity

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val placemarksButton: Button = findViewById(R.id.placemarks_loc)
        val ticketsButton: Button = findViewById(R.id.tickets_match)

        placemarksButton.setOnClickListener {
            val intent = Intent(this, PlacemarkListView::class.java)
            startActivity(intent)
        }

        ticketsButton.setOnClickListener {
            val intent = Intent(this, TicketListActivity::class.java)
            startActivity(intent)
        }
    }
}