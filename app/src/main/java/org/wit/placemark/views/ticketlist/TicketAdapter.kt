package org.wit.placemark.views.ticketlist


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.wit.placemark.models.TicketModel
import org.wit.placemark.databinding.CardTicketBinding


interface TicketListener {
    fun onTicketClick(cloth: TicketModel)
}

class ClothAdapter(private var tickets: List<TicketModel>,
                   private val listener: TicketListener) :
    RecyclerView.Adapter<ClothAdapter.MainHolder>() {

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
            binding.description.text = ticket.description
            binding.root.setOnClickListener { listener.onTicketClick(ticket) }
        }
    }
}

