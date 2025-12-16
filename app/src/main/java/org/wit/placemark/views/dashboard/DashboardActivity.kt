package org.wit.placemark.views.dashboard

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.switchmaterial.SwitchMaterial
import org.wit.placemark.R
import org.wit.placemark.views.placemarklist.PlacemarkListView
import org.wit.ticket.views.ticketlist.TicketListActivity

class DashboardActivity : AppCompatActivity() {
    private lateinit var switch: SwitchMaterial


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

        switch = findViewById(R.id.darkModeSwitch)

        // Saving state of our app using SharedPreferences
        val sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val isDarkModeOn = sharedPreferences.getBoolean("isDarkModeOn", false)

        // check for preferences
        if (isDarkModeOn) {

            // set to dark mode
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            switch.isChecked = true
        } else {
            // set to light mode
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            switch.isChecked = false
        }

        switch.setOnClickListener {

            // check for preferences
            if (isDarkModeOn) {

                // set to light mode
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

                // set value of isDarkModeOn to false
                editor.putBoolean("isDarkModeOn", false)
                editor.apply()
            } else {

                // set to dark mode
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

                // set value of isDarkModeOn to true
                editor.putBoolean("isDarkModeOn", true)
                editor.apply()
            }
        }
    }
}
