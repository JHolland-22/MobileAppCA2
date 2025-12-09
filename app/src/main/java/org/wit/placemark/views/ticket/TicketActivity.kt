package org.wit.ticket.views.ticket

import org.wit.placemark.R
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import org.wit.placemark.databinding.ActivityTicketBinding
import org.wit.placemark.helpers.showImagePicker
import org.wit.ticket.models.TicketModel
import org.wit.placemark.main.MainApp
import timber.log.Timber

class TicketActivity : AppCompatActivity() {

    private val ticketItems = arrayOf("championship ", "league")
    private lateinit var binding: ActivityTicketBinding
    private var ticket: TicketModel = TicketModel()
    private lateinit var app: MainApp
    private lateinit var imageIntentLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTicketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        app = application as MainApp

        Timber.i("Ticket Activity started...")

        val autocomp = binding.autoCompleteTxt
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, ticketItems)
        autocomp.setAdapter(adapter)
        autocomp.setOnItemClickListener { parent, view, position, id ->
            Toast.makeText(this, "Clicked: ${ticketItems[position]}", Toast.LENGTH_SHORT).show()
        }

        var edit = false
        if (intent.hasExtra("ticket_edit")) {
            edit = true
            ticket = intent.extras?.getParcelable("ticket_edit")!!
            // binding.ticketTitle.setText(ticket.title)
            binding.description.setText(ticket.description)
            binding.btnAdd.setText(R.string.save_ticket)
            Picasso.get()
                .load(ticket.image)
                .into(binding.ticketImage)
        }

        binding.btnAdd.setOnClickListener {
            val selectedTitle = binding.autoCompleteTxt.text.toString()
            ticket.title = selectedTitle
            // ticket.title = binding.ticketTitle.text.toString()
            ticket.description = binding.description.text.toString()
            if (ticket.title!!.isEmpty()) {
                Snackbar.make(it, "Please select a ticket type", Snackbar.LENGTH_LONG).show()
            } else {
                if (edit) {
                    app.tickets.update(ticket.copy())
                } else {
                    app.tickets.create(ticket.copy())
                }
            }
            setResult(RESULT_OK)
            Snackbar.make(it, "ticket added successfully!", Snackbar.LENGTH_SHORT).show()
            binding.description.text.clear()
            setResult(RESULT_OK)
            finish()
        }

        binding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher)
        }

        registerImagePickerCallback()
    }

    override fun onCreateOptionsMenu(menu: android.view.Menu): Boolean {
        menuInflater.inflate(R.menu.menu_ticket, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        if (item.itemId == R.id.item_cancel) finish()
        return super.onOptionsItemSelected(item)
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when(result.resultCode) {
                RESULT_OK -> {
                    result.data?.data?.let {
                        Timber.i("Got Result $it")
                        ticket.image = it
                        Picasso.get().load(it).into(binding.ticketImage)
                    }
                }
                RESULT_CANCELED -> {}
                else -> {}
            }
        }
    }
}
