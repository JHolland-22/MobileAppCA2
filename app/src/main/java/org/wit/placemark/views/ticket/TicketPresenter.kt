package org.wit.ticket.views.ticket

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import org.wit.placemark.main.MainApp
import org.wit.ticket.models.TicketModel
import timber.log.Timber

class TicketPresenter(private val view: TicketView) {

    var ticket = TicketModel()
    var app: MainApp = view.application as MainApp
    private lateinit var imageIntentLauncher: ActivityResultLauncher<PickVisualMediaRequest>
    var edit = false

    init {
        if (view.intent.hasExtra("ticket_edit")) {
            edit = true
            ticket = view.intent.extras?.getParcelable("ticket_edit")!!
            view.showTicket(ticket)
        }
        registerImagePickerCallback()
    }

    fun doAddOrSave(title: String, description: String) {
        ticket.title = title
        ticket.description = description

        if (edit) {
            app.tickets.update(ticket)
        } else {
            app.tickets.create(ticket)
        }

        view.setResult(Activity.RESULT_OK)
        view.finish()
    }

    fun doCancel() {
        view.finish()
    }

    fun doDelete() {
        view.setResult(99)
        app.tickets.delete(ticket)
        view.finish()
    }

    fun doSelectImage() {
        val request = PickVisualMediaRequest.Builder()
            .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly)
            .build()
        imageIntentLauncher.launch(request)
    }

    fun cacheTicket(title: String, description: String) {
        ticket.title = title
        ticket.description = description
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher = view.registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ) { uri ->
            try {
                if (uri != null) {
                    view.contentResolver.takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                    ticket.image = uri
                    Timber.i("IMG :: ${ticket.image}")
                    view.updateImage(ticket.image)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
