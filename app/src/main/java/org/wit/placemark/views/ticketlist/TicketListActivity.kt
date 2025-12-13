package org.wit.ticket.views.ticketlist


import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import org.wit.placemark.R
import org.wit.placemark.databinding.ActivityTicketListBinding
import org.wit.placemark.main.MainApp
import org.wit.ticket.models.TicketModel
import org.wit.ticket.views.ticket.TicketActivity

class TicketListActivity : AppCompatActivity(), TicketListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityTicketListBinding

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
                val launcherIntent = Intent(this, TicketActivity::class.java)
                getResult.launch(launcherIntent)
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
        val launcherIntent = Intent(this, TicketActivity::class.java)
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
}