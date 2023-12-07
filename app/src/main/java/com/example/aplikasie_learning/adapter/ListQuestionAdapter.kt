package com.example.aplikasie_learning.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aplikasie_learning.R
import com.example.aplikasie_learning.databinding.ItemQuestionBinding
import com.example.aplikasie_learning.model.Kuis
import com.example.aplikasie_learning.model.QuestionAdmin
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ListQuestionAdapter(private val questionList : ArrayList<QuestionAdmin>) : RecyclerView.Adapter<ListQuestionAdapter.ViewHolder>() {
    private var onClickDelete : ((QuestionAdmin, Int) -> Unit)? = null

    var question = mutableListOf<QuestionAdmin>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    fun setOnClickDelete(callback : (QuestionAdmin, Int) -> Unit){
        this.onClickDelete = callback
    }

    class ViewHolder(itemView : View):RecyclerView.ViewHolder(itemView){
        private var actionDelete : ImageView? = null
        private var question : TextView? = null
        private var onClickDelete : ((QuestionAdmin, Int) -> Unit)? = null

        fun bindItem(questionAdmin: QuestionAdmin) {
            question = itemView.findViewById(R.id.tv_question)
            actionDelete = itemView.findViewById(R.id.btn_delete)

            question?.text = questionAdmin.question

            actionDelete?.setOnClickListener {
                onClickDelete?.invoke(questionAdmin, adapterPosition)
            }
        }

        fun setOnClickDelete(callback : (QuestionAdmin, Int) -> Unit){
            this.onClickDelete = callback
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutInflater.from(parent.context).inflate(R.layout.item_question, parent,false)
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val questionAdmin = questionList[position]
        if (questionAdmin != null) {
            holder.bindItem(questionAdmin)
        }
        holder.setOnClickDelete { idQuestion, adapterPosition ->
            onClickDelete?.invoke(idQuestion, adapterPosition)
        }

    }

    override fun getItemCount(): Int = questionList.size
}