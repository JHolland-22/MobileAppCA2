package org.wit.placemark.views.clothinglist


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.wit.placemark.models.ClothModel
import org.wit.placemark.databinding.CardClothBinding


interface ClothListener {
    fun onClothClick(cloth: ClothModel)
}

class ClothAdapter(private var cloths: List<ClothModel>,
                   private val listener: ClothListener) :
    RecyclerView.Adapter<ClothAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardClothBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val cloth = cloths[holder.adapterPosition]
        holder.bind(cloth, listener)
    }

    override fun getItemCount(): Int = cloths.size

    class MainHolder(private val binding : CardClothBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cloth: ClothModel, listener: ClothListener) {
            binding.clothTitle.text = cloth.title
            binding.description.text = cloth.description
            binding.root.setOnClickListener { listener.onClothClick(cloth) }
        }
    }
}

