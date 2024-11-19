package com.cs407.tapntab

import android.os.Bundle
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MenuActivity : AppCompatActivity() {
    private lateinit var categoriesRecyclerView: RecyclerView
    private lateinit var itemsRecyclerView: RecyclerView
    private lateinit var billRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        // Set up the categories recycler view
        categoriesRecyclerView = findViewById(R.id.categories_recycler_view)
        categoriesRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val menuCategories = listOf(
            CategoryItem(R.drawable.beer, "Beers"),
            CategoryItem(R.drawable.mixers, "Mixers"),
            CategoryItem(R.drawable.shots, "Shots")
        )
        categoriesRecyclerView.adapter = MenuCategoriesAdapter(menuCategories)

        // Set up the items recycler view
        itemsRecyclerView = findViewById(R.id.items_recycler_view)
        itemsRecyclerView.layoutManager = LinearLayoutManager(this)

        val itemList = listOf(
            MenuItem(R.drawable.vodkacoke, "Vodka + Mixer", "$6+") { showToast("Item 1 clicked") },
            MenuItem(R.drawable.tequilasoda, "Tequila Soda Lime", "$6+") { showToast("Item 2 clicked") },
            MenuItem(R.drawable.corona, "Corona 12oz", "$6+") { showToast("Item 3 clicked") }
        )
        itemsRecyclerView.adapter = MenuItemsAdapter(itemList)

        // Set up the bill recycler view
        billRecyclerView = findViewById(R.id.bill_recycler_view)
        billRecyclerView.layoutManager = LinearLayoutManager(this)
        billRecyclerView.adapter = BillItemAdapter(
            listOf(
                BillItem("Vodka + Coke", 6.00, 1),
                BillItem("Miller Lite (12 oz)", 6.50, 2),
                BillItem("Cranberry Juice", 6.78, 1)
            )
        )

        // Back button and bottom tabs
        val backButtonContainer: ConstraintLayout = findViewById(R.id.backButtonContainer)
        val expandedBottom: RelativeLayout = findViewById(R.id.bottom_tab_expanded)
        val bottomTab: RelativeLayout = findViewById(R.id.bottom_tab)

        backButtonContainer.setOnClickListener {
            expandedBottom.visibility = RelativeLayout.GONE
        }

        bottomTab.setOnClickListener {
            expandedBottom.visibility = RelativeLayout.VISIBLE
        }


    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
