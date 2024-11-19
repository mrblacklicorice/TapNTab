package com.cs407.tapntab

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BillItemAdapter(private val billItems: List<BillItem>) :
    RecyclerView.Adapter<BillItemAdapter.BillItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.bill_item, parent, false)
        return BillItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BillItemViewHolder, position: Int) {
        val currentItem = billItems[position]
        holder.itemTitle.text = currentItem.name
        holder.qty.text = currentItem.qty.toString()
        holder.cost.text = String.format("$%.2f", currentItem.price)
    }

    override fun getItemCount() = billItems.size

    class BillItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemTitle: TextView = itemView.findViewById(R.id.item_title)
        val qty: TextView = itemView.findViewById(R.id.qty)
        val cost: TextView = itemView.findViewById(R.id.cost)
    }
}