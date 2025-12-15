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
import java.util.Calendar
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import android.widget.TimePicker
import android.text.format.DateFormat
import android.view.MenuItem
import org.wit.placemark.views.placemark.PlacemarkPresenter


class TicketActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {


    var day = 0
    var month: Int = 0
    var year: Int = 0
    var hour: Int = 0
    var minute: Int = 0
    var myDay = 0
    var myMonth: Int = 0
    var myYear: Int = 0
    var myHour: Int = 0
    var myMinute: Int = 0


    private val ticketItems = arrayOf("championship ", "league")
    private val availabilityItems = arrayOf("Available", "Sold Out")
    private val stageItems = arrayOf("Round 1", "Round 2", "Quarter-final", "Semi-final", "Final")


    private lateinit var binding: ActivityTicketBinding
    private var ticket: TicketModel = TicketModel()
    private lateinit var app: MainApp
    private lateinit var imageIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var presenter: TicketPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTicketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarAdd.title = title
        app = application as MainApp

        Timber.i("Ticket Activity started...")

        val autocomp = binding.typeDropdown
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, ticketItems)
        autocomp.setAdapter(adapter)
        val statusAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, availabilityItems)
        binding.statusDropdown.setAdapter(statusAdapter)
        val stageAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, stageItems)
        binding.stageDropdown.setAdapter(stageAdapter)
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

        binding.dateButton.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()
            day = calendar.get(Calendar.DAY_OF_MONTH)
            month = calendar.get(Calendar.MONTH)
            year = calendar.get(Calendar.YEAR)

            val datePickerDialog =
                DatePickerDialog(this@TicketActivity, this@TicketActivity, year, month, day)

            datePickerDialog.show()
        }


        binding.btnAdd.setOnClickListener {
            val selectedTitle = binding.typeDropdown.text.toString()
            ticket.title = selectedTitle
            // ticket.title = binding.ticketTitle.text.toString()
            ticket.ticketStatus = binding.statusDropdown.text.toString()
            ticket.stage = binding.stageDropdown.text.toString()
            ticket.teamA = binding.teamA.text.toString()
            ticket.teamB = binding.teamB.text.toString()
            ticket.pitchName = binding.pitchName.text.toString()
            ticket.description = binding.description.text.toString()
            ticket.matchDate = Calendar.getInstance().apply {
                set(myYear, myMonth, myDay, myHour, myMinute)
            }.timeInMillis
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_delete -> {
                presenter.doDelete()
                true
            }

            R.id.item_cancel -> {
                presenter.doCancel()
                true
            }

            android.R.id.home -> {
                finish()
                true
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
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


    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        myDay = dayOfMonth
        myYear = year
        myMonth = month
        val calendar: Calendar = Calendar.getInstance()
        hour = calendar.get(Calendar.HOUR)
        minute = calendar.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(this@TicketActivity, this@TicketActivity, hour, minute,
            DateFormat.is24HourFormat(this))
        timePickerDialog.show()
    }
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        myHour = hourOfDay
        myMinute = minute
        binding.matchDateText.text =
                            "Year" + myYear + "\n" +
                            "Month" + myMonth + "\n" +
                            "Day" + myDay + "\n" +
                            "Hour" + myHour + "\n" +
                            "Minute" + myMinute +"\n"

    }
    }
//https://www.tutorialspoint.com/how-to-use-date-time-picker-dialog-in-kotlin-android
