package org.wit.placemark.views.placemarklist

import android.content.DialogInterface
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import org.wit.placemark.R
import org.wit.placemark.databinding.ActivityPlacemarkListBinding
import org.wit.placemark.main.MainApp
import org.wit.placemark.models.PlacemarkModel
import org.wit.ticket.views.ticketlist.TicketAdapter

class PlacemarkListView : AppCompatActivity(), PlacemarkListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityPlacemarkListBinding
    lateinit var presenter: PlacemarkListPresenter
    private var position: Int = 0
    lateinit var adapter: PlacemarkAdapter

    val positiveDeleteAllClick = { _: DialogInterface, _: Int ->
        app.placemarks.deleteAll()
        adapter.notifyDataSetChanged()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlacemarkListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        presenter = PlacemarkListPresenter(this)
        app = application as MainApp
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        loadPlacemarks()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_placemark, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.item_add -> {
                presenter.doAddPlacemark()
                true
            }
            R.id.item_map -> {
                presenter.doShowPlacemarksMap()
                true
            }
            R.id.item_delete -> {
                deleteAllAlert(binding.root)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPlacemarkClick(placemark: PlacemarkModel, position: Int) {
        this.position = position
        presenter.doEditPlacemark(placemark, this.position)
    }

    private fun loadPlacemarks() {
        binding.recyclerView.adapter = PlacemarkAdapter(presenter.getPlacemarks(), this)
        onRefresh()
    }

    fun onRefresh() {
        binding.recyclerView.adapter?.
        notifyItemRangeChanged(0,presenter.getPlacemarks().size)
    }

    fun onDelete(position : Int) {
        binding.recyclerView.adapter?.notifyItemRemoved(position)
    }

    fun deleteAllAlert(view: View) {

        val builder = AlertDialog.Builder(this)

        with(builder) {
            setTitle("Delete all placemarks")
            setMessage("Are you sure you want to delete all placemarks??????")
            setPositiveButton("OK", DialogInterface.OnClickListener(function = positiveDeleteAllClick))
            setNegativeButton(android.R.string.no, null)
            show()
        }
    }
}