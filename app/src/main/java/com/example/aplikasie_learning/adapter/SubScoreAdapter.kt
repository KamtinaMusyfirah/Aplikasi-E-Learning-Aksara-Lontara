package com.example.aplikasie_learning.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aplikasie_learning.R
import com.example.aplikasie_learning.model.ScoreUser

class SubScoreAdapter(private val score : ArrayList<ScoreUser>) : RecyclerView.Adapter<SubScoreAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_score_user, parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val score = score[position]
        if (score != null){
            holder.bindItem(score)
        }
    }

    override fun getItemCount(): Int = score.size

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        private var username : TextView = itemView.findViewById(R.id.tv_username)
        private var scoreUser : TextView = itemView.findViewById(R.id.tv_score)

        fun bindItem(score: ScoreUser) {
            username.text = score.nama
            scoreUser.text = score.score.toString()
        }
    }
}