package com.example.aplikasie_learning.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.aplikasie_learning.adapter.MaterialsAdapter.*
import com.example.aplikasie_learning.databinding.ItemMaterialBinding
import com.example.aplikasie_learning.model.Materials

class MaterialsAdapter : RecyclerView.Adapter<ViewHolder>(), Filterable{

    private var listener: ((Materials, Int) -> Unit)? = null
    var materials = mutableListOf<Materials>()
        set(value){
            field = value
            materialsFilter = value
            notifyDataSetChanged()
        }

    private  var materialsFilter = mutableListOf<Materials>()

    private val filters = object : Filter(){
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            var filteredList = mutableListOf<Materials>()
            val filterPattern = constraint.toString().trim().lowercase()

            if (filterPattern.isEmpty()){
                filteredList = materials
            }else{
                for (material in materials){
                    val title = material.titleMaterial?.trim()?.lowercase()

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
            materialsFilter = results?.values as MutableList<Materials>
            notifyDataSetChanged()
        }
    }

    class ViewHolder(private val materialBinding: ItemMaterialBinding)
        :RecyclerView.ViewHolder(materialBinding.root){
            fun bindItem(material: Materials, listener: ((Materials, Int) -> Unit)?){

                materialBinding.tvTitleMaterial.text = material.titleMaterial
                materialBinding.partMaterial.text = material.partMaterial


                listener?.let {
                    itemView.setOnClickListener{
                        it(material, adapterPosition)
                    }
                }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMaterialBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(materialsFilter[position],listener)
    }

    override fun getItemCount(): Int = materialsFilter.size

    override fun getFilter(): Filter = filters

    fun onClick(listener: ((Materials, Int) -> Unit)){
        this.listener = listener
    }
}