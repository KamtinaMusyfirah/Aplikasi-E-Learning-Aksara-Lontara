package com.example.aplikasie_learning.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aplikasie_learning.databinding.ItemKuisBinding
import com.example.aplikasie_learning.databinding.ItemScoreAdminBinding
import com.example.aplikasie_learning.model.Kuis

class ScoreAdminAdapter(private val kuisList : ArrayList<Kuis>): RecyclerView.Adapter<ScoreAdminAdapter.ViewHolder>() {
    private var listener: ((Kuis, Int) -> Unit)? = null

    class ViewHolder (private val materialBinding: ItemScoreAdminBinding)
        : RecyclerView.ViewHolder(materialBinding.root){

        fun bindItem(material: Kuis, listener: ((Kuis, Int) -> Unit)?){

            materialBinding.tvTitleKuis.text = material.titleKuis

            listener?.let {
                itemView.setOnClickListener{
                    it(material, adapterPosition)
                }
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemScoreAdminBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val kuis = kuisList[position]

        if (kuis != null){
            holder.bindItem(kuis,listener)
        }

    }

    override fun getItemCount(): Int = kuisList.size

    fun onClick(listener: ((Kuis, Int) -> Unit)){
        this.listener = listener
    }
}