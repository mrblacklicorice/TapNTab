package com.cs407.tapntab

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MenuCategoriesAdapter(private val categories: List<CategoryItem>) :
    RecyclerView.Adapter<MenuCategoriesAdapter.MenuViewHolder>() {

    class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById(R.id.icon_image)
        var textView: TextView = itemView.findViewById(R.id.item_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_item, parent, false)
        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val category = categories[position]
        holder.imageView.setImageResource(category.icon)
        holder.textView.text = category.title
        holder.itemView.setOnClickListener { category.onClick() }
    }

    override fun getItemCount(): Int = categories.size
}

