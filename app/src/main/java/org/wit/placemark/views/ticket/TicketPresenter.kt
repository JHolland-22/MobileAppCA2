package org.wit.ticket.views.ticket

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import org.wit.placemark.main.MainApp
import org.wit.ticket.models.TicketModel
import timber.log.Timber
import java.util.Calendar

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
    fun doAddOrSave(title: String, description: String, statusDropdown: String, stageDropdown: String, teamA: String, teamB: String, pitchName: String) {
        ticket.title = title
        ticket.description = description
        ticket.ticketStatus = statusDropdown
        ticket.stage = stageDropdown
        ticket.teamA = teamA
        ticket.teamB = teamB
        ticket.pitchName = pitchName
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

    fun cacheTicket(title: String, description: String, statusDropdown: String, stageDropdown: String, teamA: String, teamB: String, pitchName: String) {
        ticket.title = title
        ticket.description = description
        ticket.ticketStatus = statusDropdown
        ticket.stage = stageDropdown
        ticket.teamA = teamA
        ticket.teamB = teamB
        ticket.pitchName = pitchName
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher = view.registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ) {
            try{
                view.contentResolver
                    .takePersistableUriPermission(it!!,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION )
                ticket.image = it // The returned Uri
                Timber.i("IMG :: ${ticket.image}")
                view.updateImage(ticket.image)
            }
            catch(e:Exception){
                e.printStackTrace()
            }
        }
    }


    fun setMatchDate(year: Int, month: Int, day: Int, hour: Int, minute: Int) {
        ticket.matchDate = Calendar.getInstance().apply {
            set(year, month, day, hour, minute)
        }.timeInMillis
    }

}
