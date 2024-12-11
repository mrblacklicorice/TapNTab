package com.cs407.tapntab

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

class StartTabFragment : Fragment() {
    private lateinit var billedItemsRecyclerView: RecyclerView
    private lateinit var billedItemsAdapter: TotalBillItemAdapter
    private lateinit var billedItems: MutableList<TotalBillItem>

    private lateinit var topBar: View
    private lateinit var bottomBar: View
    private lateinit var plusButton: ImageButton
    private lateinit var instructionText: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_start_tab, container, false)

        billedItemsRecyclerView = view.findViewById(R.id.bill_recycler_view)
        topBar = view.findViewById(R.id.top_bar)
        bottomBar = view.findViewById(R.id.bottom_bar)
        plusButton = view.findViewById(R.id.plusButton)
        instructionText = view.findViewById(R.id.instructionText)

        billedItems = mutableListOf()
        billedItemsAdapter = TotalBillItemAdapter(billedItems)
        billedItemsRecyclerView.adapter = billedItemsAdapter

        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        view.findViewById<View>(R.id.menuButton).setOnClickListener {
            val intent = Intent(requireContext(), MenuActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    override fun onResume() {
        super.onResume()

        val bill = arguments?.getSerializable("bill") as? ArrayList<Map<String, *>>
        Log.d("StartTabFragment", "Bill: $bill")

        val totalBillItems = bill?.map { item ->
            TotalBillItem(
                name = item["name"] as String,
                price = item["price"] as Double,
                qty = item["qty"] as Int
            )
        } ?: emptyList()

        updateBills(totalBillItems)
    }

    private fun updateBills(bills: List<TotalBillItem>) {
        Log.d("StartTabFragment", "Previous bills: $billedItems")
        Log.d("StartTabFragment", "Updating bills: $bills")

        billedItems.addAll(bills)

        billedItemsAdapter.notifyDataSetChanged()

        billedItemsRecyclerView.visibility = if (billedItems.isEmpty()) View.GONE else View.VISIBLE
        topBar.visibility = if (billedItems.isEmpty()) View.GONE else View.VISIBLE
        bottomBar.visibility = if (billedItems.isEmpty()) View.GONE else View.VISIBLE

        instructionText.visibility = if (billedItems.isEmpty()) View.VISIBLE else View.GONE
        plusButton.visibility = if (billedItems.isEmpty()) View.VISIBLE else View.GONE
    }
}