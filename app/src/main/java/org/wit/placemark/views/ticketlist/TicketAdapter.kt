package org.wit.ticket.views.ticketlist


import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.wit.ticket.models.TicketModel
import org.wit.placemark.databinding.CardTicketBinding
import java.util.Date


interface TicketListener {
    fun onTicketClick(cloth: TicketModel)
}

class TicketAdapter(private var tickets: List<TicketModel>,
                   private val listener: TicketListener) :
    RecyclerView.Adapter<TicketAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardTicketBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val cloth = tickets[holder.adapterPosition]
        holder.bind(cloth, listener)
    }

    override fun getItemCount(): Int = tickets.size

    class MainHolder(private val binding : CardTicketBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(ticket: TicketModel, listener: TicketListener) {

            binding.ticketTitle.text = ticket.title
            binding.ticketDescription.text = ticket.description
            binding.ticketStatus.text = ticket.ticketStatus
            binding.ticketStage.text = ticket.stage

            binding.teams.text = "${ticket.teamA} vs ${ticket.teamB}"
            binding.pitchName.text = ticket.pitchName

            if (ticket.matchDate != 0L) {
                val formatter = java.text.SimpleDateFormat("dd MMM yyyy HH:mm" ,java.util.Locale.getDefault())
                binding.matchDate.text = formatter.format(java.util.Date(ticket.matchDate))
            } else {
                binding.matchDate.text = ""
            }

            if (ticket.image != Uri.EMPTY) {
                binding.ticketImage.setImageURI(ticket.image)
            } else {
                binding.ticketImage.setImageDrawable(null)
            }

            binding.root.setOnClickListener {
                listener.onTicketClick(ticket)
            }
        }


    }
}

