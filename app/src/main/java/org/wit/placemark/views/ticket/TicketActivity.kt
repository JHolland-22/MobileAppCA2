package org.wit.placemark.views.ticket

import org.wit.placemark.R
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import org.wit.placemark.databinding.ActivityClothBinding
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import org.wit.placemark.helpers.showImagePicker
import org.wit.placemark.models.TicketModel
import org.wit.placemark.main.MainApp
import timber.log.Timber

class TicketActivity : AppCompatActivity() {

    private val clothingItems = arrayOf("jumpers", "crewnecks", "socks", "shorts")
    private lateinit var binding: ActivityClothBinding
    private var cloth: TicketModel = TicketModel()
    private lateinit var app: MainApp
    private lateinit var imageIntentLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClothBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        app = application as MainApp

        Timber.i("Clothes Activity started...")

        val autocomp = binding.autoCompleteTxt
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, clothingItems)
        autocomp.setAdapter(adapter)
        autocomp.setOnItemClickListener { parent, view, position, id ->
            Toast.makeText(this, "Clicked: ${clothingItems[position]}", Toast.LENGTH_SHORT).show()
        }

        var edit = false
        if (intent.hasExtra("cloth_edit")) {
            edit = true
            cloth = intent.extras?.getParcelable("cloth_edit")!!
            // binding.clothTitle.setText(cloth.title)
            binding.description.setText(cloth.description)
            binding.btnAdd.setText(R.string.save_cloth)
            Picasso.get()
                .load(cloth.image)
                .into(binding.clothImage)
        }

        binding.btnAdd.setOnClickListener {
            val selectedTitle = binding.autoCompleteTxt.text.toString()
            cloth.title = selectedTitle
            // cloth.title = binding.clothTitle.text.toString()
            cloth.description = binding.description.text.toString()
            if (cloth.title!!.isEmpty()) {
                Snackbar.make(it, "Please select a clothing item", Snackbar.LENGTH_LONG).show()
            } else {
                if (edit) {
                    app.cloths.update(cloth.copy())
                } else {
                    app.cloths.create(cloth.copy())
                }
            }
            setResult(RESULT_OK)
            Snackbar.make(it, "Cloth added successfully!", Snackbar.LENGTH_SHORT).show()
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
                        cloth.image = it
                        Picasso.get().load(it).into(binding.clothImage)
                    }
                }
                RESULT_CANCELED -> {}
                else -> {}
            }
        }
    }
}
