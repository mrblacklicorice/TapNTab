package com.cs407.tapntab

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MenuItemsAdapter(private val items: List<MenuItem>) :
    RecyclerView.Adapter<MenuItemsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var icon: ImageView = view.findViewById(R.id.icon_image)
        var title: TextView = view.findViewById(R.id.item_title)
        var cost: TextView = view.findViewById(R.id.cost_text)

        fun bind(item: MenuItem) {
            icon.setImageResource(item.icon)
            title.text = item.name
            cost.text = item.cost
            itemView.setOnClickListener { item.onClick() }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.card_long_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}

