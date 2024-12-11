package com.cs407.tapntab

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BarAdapter(
    private val barList: List<Bar>,
    private val isHorizontal: Boolean = false
) : RecyclerView.Adapter<BarAdapter.BarViewHolder>() {

    inner class BarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val barImage: ImageView = if (isHorizontal) {
            itemView.findViewById(R.id.favoriteImage)
        } else {
            itemView.findViewById(R.id.popularImage)
        }
        val barName: TextView = if (isHorizontal) {
            TextView(itemView.context) // Horizontal doesn't display a name
        } else {
            itemView.findViewById(R.id.popularName)
        }
        val barVotes: TextView? = if (isHorizontal) null else itemView.findViewById(R.id.popularVotes)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BarViewHolder {
        val layoutId = if (isHorizontal) R.layout.item_favorite else R.layout.item_popular
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return BarViewHolder(view)
    }

    override fun onBindViewHolder(holder: BarViewHolder, position: Int) {
        val bar = barList[position]
        holder.barImage.setImageResource(bar.imageResId)
        if (!isHorizontal) {
            holder.barName.text = bar.name
            holder.barVotes?.text = "${bar.upvotes} votes"
        }
    }

    override fun getItemCount(): Int = barList.size
}
