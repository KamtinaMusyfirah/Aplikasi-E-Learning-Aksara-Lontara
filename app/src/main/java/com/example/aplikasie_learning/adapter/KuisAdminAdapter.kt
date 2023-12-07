package com.example.aplikasie_learning.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aplikasie_learning.R
import com.example.aplikasie_learning.model.Kuis

class KuisAdminAdapter(private val kuisList : ArrayList<Kuis>) : RecyclerView.Adapter<KuisAdminAdapter.ViewHolder>(){

    private var onClickDelete : ((Kuis, Int) -> Unit)? = null
    private var listener: ((Kuis, Int) -> Unit)? = null

    var kuis = mutableListOf<Kuis>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    fun setOnClickDelete(callback : (Kuis, Int) -> Unit){
        this.onClickDelete = callback
    }

    class ViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView){
        private var actionDelete : ImageView? = null
        private var titleKuis : TextView? = null
        private var onClickDelete : ((Kuis, Int) -> Unit)? = null

        fun bindItem(kuis: Kuis, listener: ((Kuis, Int) -> Unit)?) {
            titleKuis = itemView.findViewById(R.id.tv_title_kuis)
            actionDelete = itemView.findViewById(R.id.btn_delete)

            titleKuis?.text = kuis.titleKuis

            listener?.let {
                itemView.setOnClickListener{
                    it(kuis, adapterPosition)
                }
            }

            actionDelete?.setOnClickListener {
                onClickDelete?.invoke(kuis, adapterPosition)
            }

        }

        fun setOnClickDelete(callback : (Kuis, Int) -> Unit){
            this.onClickDelete = callback
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutInflater.from(parent.context).inflate(R.layout.item_kuis_admin, parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val kuisList = kuisList[position]
        if (kuisList != null){
            holder.bindItem(kuisList, listener)
        }
        holder.setOnClickDelete { idKuis, adapterPosition ->
            onClickDelete?.invoke(idKuis, adapterPosition)
        }

    }

    override fun getItemCount(): Int = kuisList.size

    fun onClick(listener : ((Kuis, Int) -> Unit)){
        this.listener = listener
    }
}