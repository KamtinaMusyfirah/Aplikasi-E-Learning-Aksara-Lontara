package com.example.aplikasie_learning.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aplikasie_learning.databinding.ItemAksaraBinding
import com.example.aplikasie_learning.model.AksaraLontara

class AksaraLontaraAdapter: RecyclerView.Adapter<AksaraLontaraAdapter.ViewHolder>(){

    private var listener : ((AksaraLontara, Int) -> Unit)? = null
    var aksaraLontara = mutableListOf<AksaraLontara>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder(private val aksaraLontaraItemBinding: ItemAksaraBinding)
        : RecyclerView.ViewHolder(aksaraLontaraItemBinding.root) {

        fun bindItem(aksaraLontara: AksaraLontara, listener : ((AksaraLontara, Int) -> Unit)?){
            aksaraLontaraItemBinding.tvAksaraLontara.text = aksaraLontara.aksaraLontara

            listener?.let {
                itemView.setOnClickListener{
                    it(aksaraLontara, adapterPosition)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAksaraBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(aksaraLontara[position], listener)
    }

    override fun getItemCount(): Int = aksaraLontara.size
}