package com.cs407.tapntab

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TotalBillItemAdapter(private val billItems: MutableList<TotalBillItem>) :
    RecyclerView.Adapter<TotalBillItemAdapter.TotalBillItemHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TotalBillItemHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.billed_item, parent, false)
        return TotalBillItemHolder(itemView)
    }

    @SuppressLint("DefaultLocale")
    override fun onBindViewHolder(holder: TotalBillItemHolder, position: Int) {
        val currentItem = billItems[position]
        holder.itemTitle.text = String.format("%d x %s", currentItem.qty, currentItem.name)
        holder.cost.text = String.format("$%.2f", currentItem.price * currentItem.qty)
    }

    override fun getItemCount() = billItems.size

    class TotalBillItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemTitle: TextView = itemView.findViewById(R.id.item_title)
        val cost: TextView = itemView.findViewById(R.id.cost)
    }
}