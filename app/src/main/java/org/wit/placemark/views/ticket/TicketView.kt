package org.wit.ticket.views.ticket

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

class TicketView : AppCompatActivity() {

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
    val neutralButtonClick = { dialog: DialogInterface, which: Int ->
        Toast.makeText(applicationContext,
            "Maybe", Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTicketBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        presenter = TicketPresenter(this)

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
    }

    fun basicAlert(view: View) {

        val builder = AlertDialog.Builder(this)

        with(builder)
        {
            setTitle("Androidly Alert")
            setMessage("Are you sure ??????")
            setPositiveButton("OK", DialogInterface.OnClickListener(function = positiveButtonClick))
            setNegativeButton(android.R.string.no, negativeButtonClick)
            setNeutralButton("Maybe", neutralButtonClick)
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
}
