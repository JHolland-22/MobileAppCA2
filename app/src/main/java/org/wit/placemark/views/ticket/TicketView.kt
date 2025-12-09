package org.wit.ticket.views.ticket

import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTicketBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        presenter = TicketPresenter(this)

        binding.chooseImage.setOnClickListener {
            presenter.cacheTicket(binding.autoCompleteTxt.text.toString(), binding.description.text.toString())
            presenter.doSelectImage()
        }

        binding.btnAdd.setOnClickListener {
            if (binding.autoCompleteTxt.text.toString().isEmpty()) {
                Snackbar.make(binding.root, R.string.enter_ticket_title, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                presenter.doAddOrSave(binding.autoCompleteTxt.text.toString(), binding.description.text.toString())
            }
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
                presenter.doDelete()
            }
            R.id.item_cancel -> {
                presenter.doCancel()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun showTicket(ticket: TicketModel) {
        binding.autoCompleteTxt.setText(ticket.title)
        binding.description.setText(ticket.description)
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
