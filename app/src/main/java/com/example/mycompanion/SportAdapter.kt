package com.example.mycompanion

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SportAdapter(
    private val sports: List<Sport>,
    private val clickListener: (Sport) -> Unit
) : RecyclerView.Adapter<SportAdapter.SportViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SportViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sport, parent, false)
        return SportViewHolder(view)
    }

    override fun onBindViewHolder(holder: SportViewHolder, position: Int) {
        holder.bind(sports[position], clickListener)
    }

    override fun getItemCount(): Int = sports.size

    class SportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val sportName: TextView = itemView.findViewById(R.id.sport_name)
        private val sportEmoji: TextView = itemView.findViewById(R.id.sport_emoji)

        fun bind(sport: Sport, clickListener: (Sport) -> Unit) {
            sportName.text = sport.name
            sportEmoji.text = sport.emoji
            itemView.setOnClickListener { clickListener(sport) }
        }
    }
}
