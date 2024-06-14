package com.example.mycompanion

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SportAdapter(private val sports: List<Sport>, private val onClick: (Sport) -> Unit) :
    RecyclerView.Adapter<SportAdapter.SportViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SportViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sport, parent, false)
        return SportViewHolder(view)
    }

    override fun onBindViewHolder(holder: SportViewHolder, position: Int) {
        holder.bind(sports[position])
    }

    override fun getItemCount(): Int = sports.size

    inner class SportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val emojiTextView: TextView = itemView.findViewById(R.id.emojiTextView)
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)

        fun bind(sport: Sport) {
            emojiTextView.text = sport.emoji
            nameTextView.text = sport.name
            itemView.tag = sport.tag
            itemView.setOnClickListener { onClick(sport) }
        }
    }
}
