package com.example.aplikasie_learning.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.aplikasie_learning.databinding.ItemKuisBinding
import com.example.aplikasie_learning.model.Kuis

class KuisAdapter(private val kuisList : ArrayList<Kuis>): RecyclerView.Adapter<KuisAdapter.ViewHolder>(), Filterable {

    private var listener: ((Kuis, Int) -> Unit)? = null
    var kuis = mutableListOf<Kuis>()
        set(value){
            field = value
            kuisFilter = value as ArrayList<Kuis>
            notifyDataSetChanged()
        }

    private  var kuisFilter = kuisList

    private val filters = object : Filter(){
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            var filteredList = mutableListOf<Kuis>()
            val filterPattern = constraint.toString().trim().lowercase()

            if (filterPattern.isEmpty()){
                filteredList = kuisList
            }else{
                for (material in kuisList){
                    val title = material.titleKuis?.trim()?.lowercase()

                    if (title?.contains(filterPattern) == true){
                        filteredList.add(material)
                    }
                }
            }

            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            kuisFilter = results?.values as ArrayList<Kuis>
            notifyDataSetChanged()
        }
    }

    class ViewHolder (private val materialBinding: ItemKuisBinding)
        :RecyclerView.ViewHolder(materialBinding.root){

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
        val binding = ItemKuisBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val kuis = kuisFilter[position]

        if (kuis != null){
            holder.bindItem(kuis,listener)
        }

    }

    override fun getItemCount(): Int = kuisFilter.size

    override fun getFilter(): Filter = filters

    fun onClick(listener: ((Kuis, Int) -> Unit)){
        this.listener = listener
    }
}