package org.wit.ticket.models


import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TicketModel(var id: Long = 0,
                       var title: String? = "",
                       var description: String? = "",
                       var ticketStatus: String = "",
                       var stage: String = "",
                       var teamA: String = "",
                       var teamB: String = "",
                       var matchDate: Long = 0L,
                       var pitchName: String = "",
                       var image: Uri = Uri.EMPTY) : Parcelable