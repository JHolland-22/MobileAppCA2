package org.wit.ticket.views.ticketlist


import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import org.wit.placemark.R
import org.wit.placemark.databinding.ActivityTicketListBinding
import org.wit.placemark.main.MainApp
import org.wit.placemark.models.PlacemarkModel
import org.wit.placemark.views.placemarklist.PlacemarkAdapter
import org.wit.ticket.models.TicketModel
import org.wit.ticket.views.ticket.TicketPresenter
import org.wit.ticket.views.ticket.TicketView

class TicketListActivity : AppCompatActivity(), TicketListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityTicketListBinding
    lateinit var adapter: TicketAdapter
    private lateinit var presenter: TicketPresenter
    private var position: Int = 0

    val positiveDeleteAllClick = { _: DialogInterface, _: Int ->
        app.tickets.deleteAll()
        adapter.notifyDataSetChanged()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTicketListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = TicketAdapter(app.tickets.findAll(), this)
        adapter = TicketAdapter(app.tickets.findAll(), this)
        binding.recyclerView.adapter = adapter


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_ticket, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.item_add -> {
                val launcherIntent = Intent(this, TicketView::class.java)
                getResult.launch(launcherIntent)
                true
            }
            R.id.item_delete -> {
                deleteAllAlert(binding.root)
                adapter.notifyDataSetChanged()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == RESULT_OK) {
                (binding.recyclerView.adapter)?.
                notifyItemRangeChanged(0,app.tickets.findAll().size)
            }
        }

    override fun onTicketClick(ticket: TicketModel) {
        val launcherIntent = Intent(this, TicketView::class.java)
        launcherIntent.putExtra("ticket_edit", ticket)
        getClickResult.launch(launcherIntent)
    }
    private val getClickResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == RESULT_OK) {
                (binding.recyclerView.adapter)?.
                notifyItemRangeChanged(0,app.tickets.findAll().size)
            }
        }

    fun deleteAllAlert(view: View) {

        val builder = AlertDialog.Builder(this)

        with(builder) {
            setTitle("Delete all tickets")
            setMessage("Are you sure you want to delete all tickets?")
            setPositiveButton("OK", DialogInterface.OnClickListener(function = positiveDeleteAllClick))
            setNegativeButton(android.R.string.no, null)
            show()
        }
    }
}