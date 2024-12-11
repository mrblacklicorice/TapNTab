package com.cs407.tapntab

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Adapter for the Bar RecyclerView
class BarAdapter(
    private val bars: List<BarForDisplay>,           // List of bars to display
    private val clickListener: (BarForDisplay) -> Unit // Lambda function for click handling
) : RecyclerView.Adapter<BarAdapter.BarViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BarViewHolder {
        // Inflate the layout for individual bar items
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_bar, parent, false)
        return BarViewHolder(view)
    }

    override fun onBindViewHolder(holder: BarViewHolder, position: Int) {
        val bar = bars[position]

        // Bind data to the ViewHolder
        holder.bind(bar, clickListener)
    }

    override fun getItemCount(): Int = bars.size

    // ViewHolder class for individual bar items
    class BarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val barImage: ImageView = itemView.findViewById(R.id.barImage)
        private val barName: TextView = itemView.findViewById(R.id.barName)
        private val barVotes: TextView = itemView.findViewById(R.id.barVotes)

        // Bind data to the views
        fun bind(bar: BarForDisplay, clickListener: (BarForDisplay) -> Unit) {
            barImage.setImageResource(bar.imageRes)
            barName.text = bar.name
            barVotes.text = "${bar.votes} votes"

            // Set click listener for the entire item view
            itemView.setOnClickListener { clickListener(bar) }
        }
    }
}
