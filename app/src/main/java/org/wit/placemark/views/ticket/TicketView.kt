package org.wit.ticket.views.ticket

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import org.wit.placemark.R
import org.wit.placemark.databinding.ActivityTicketBinding
import org.wit.ticket.models.TicketModel
import java.util.Calendar
import android.text.format.DateFormat
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.TimePicker


class TicketView : AppCompatActivity() , DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener{

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
    private lateinit var presenter: TicketPresenter
    var ticket = TicketModel()

    val positiveButtonClick = { dialog: DialogInterface, which: Int ->
        presenter.doDelete()
        Toast.makeText(applicationContext,
            android.R.string.yes, Toast.LENGTH_SHORT).show()
    }
    val negativeButtonClick = { dialog: DialogInterface, which: Int ->
        Toast.makeText(applicationContext,
            android.R.string.no, Toast.LENGTH_SHORT).show()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTicketBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
       // setSupportActionBar(binding.toolbarAdd)

        presenter = TicketPresenter(this)

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

        binding.chooseImage.setOnClickListener {
            presenter.cacheTicket(binding.typeDropdown.text.toString(), binding.description.text.toString(), binding.statusDropdown.text.toString(), binding.stageDropdown.text.toString(), binding.teamA.text.toString(), binding.teamB.text.toString(), binding.pitchName.text.toString())
            presenter.doSelectImage()
        }

        binding.btnAdd.setOnClickListener {
            if (binding.typeDropdown.text.toString().isEmpty()) {
                Snackbar.make(binding.root, R.string.enter_ticket_title, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                presenter.doAddOrSave(binding.typeDropdown.text.toString(), binding.description.text.toString(), binding.statusDropdown.text.toString(), binding.stageDropdown.text.toString(), binding.teamA.text.toString(), binding.teamB.text.toString(), binding.pitchName.text.toString())
            }
        }
        binding.dateButton.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()
            day = calendar.get(Calendar.DAY_OF_MONTH)
            month = calendar.get(Calendar.MONTH)
            year = calendar.get(Calendar.YEAR)

            val datePickerDialog =
                DatePickerDialog(this@TicketView, this@TicketView, year, month, day)

            datePickerDialog.show()
        }

    }

    fun basicAlert(view: View) {

        val builder = AlertDialog.Builder(this)

        with(builder)
        {
            setTitle("Do you really wanna delete this ticket???")
            setMessage("Are you sure ??????")
            setPositiveButton("OK", DialogInterface.OnClickListener(function = positiveButtonClick))
            setNegativeButton(android.R.string.no, negativeButtonClick)
            show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_ticket, menu)
        val deleteMenu: MenuItem = menu.findItem(R.id.item_delete)
        deleteMenu.isVisible = presenter.edit
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_delete -> {
                basicAlert(binding.root)
                true
            }
            R.id.item_cancel -> {
                presenter.doCancel()
                true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun showTicket(ticket: TicketModel) {
        binding.typeDropdown.setText(ticket.title, false)
        binding.description.setText(ticket.description)
        binding.statusDropdown.setText(ticket.ticketStatus, false)
        binding.stageDropdown.setText(ticket.stage ,false )
        binding.teamA.setText(ticket.teamA)
        binding.teamB.setText(ticket.teamB)
        binding.pitchName.setText(ticket.pitchName)
        binding.btnAdd.setText(R.string.save_ticket)
        Picasso.get()
            .load(ticket.image)
            .into(binding.ticketImage)
        if (ticket.image != Uri.EMPTY) {
            binding.chooseImage.setText(R.string.select_club_image)
        }
    }

    fun updateImage(image: Uri) {
        Picasso.get()
            .load(image)
            .into(binding.ticketImage)
        binding.chooseImage.setText(R.string.select_club_image)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        myDay = dayOfMonth
        myYear = year
        myMonth = month
        val calendar: Calendar = Calendar.getInstance()
        hour = calendar.get(Calendar.HOUR)
        minute = calendar.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(
            this@TicketView, this@TicketView, hour, minute,
            DateFormat.is24HourFormat(this)
        )
        timePickerDialog.show()
    }
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        myHour = hourOfDay
        myMinute = minute
        presenter.setMatchDate(myYear, myMonth, myDay, myHour, myMinute)
        binding.matchDateText.text =
            "Year" + myYear + "\n" +
                    "Month" + myMonth + "\n" +
                    "Day" + myDay + "\n" +
                    "Hour" + myHour + "\n" +
                    "Minute" + myMinute +"\n"
    }
}
