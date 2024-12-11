package com.cs407.tapntab

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PastTabsAdapter(private val pastTabs: MutableList<PastTab>) :
    RecyclerView.Adapter<PastTabsAdapter.PastTabViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PastTabViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_past_tab, parent, false)
        return PastTabViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PastTabViewHolder, position: Int) {
        val currentTab = pastTabs[position]
        holder.tabName.text = currentTab.tabName
        holder.tabTotal.text = String.format("$%.2f", currentTab.tabTotal)
        holder.timestamp.text = currentTab.timestamp.toDate().toString()
    }

    override fun getItemCount() = pastTabs.size

    class PastTabViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tabName: TextView = itemView.findViewById(R.id.barName)
        val tabTotal: TextView = itemView.findViewById(R.id.tabAmount)
        val timestamp: TextView = itemView.findViewById(R.id.dateText)
    }
}