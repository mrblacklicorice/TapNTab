package com.cs407.tapntab

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.lifecycleScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import kotlinx.coroutines.tasks.await

class MenuActivity : AppCompatActivity() {
    private lateinit var categoriesRecyclerView: RecyclerView
    private lateinit var itemsRecyclerView: RecyclerView
    private lateinit var billRecyclerView: RecyclerView
    private lateinit var iconManager: IconManager
    private lateinit var category_view: TextView
    private lateinit var searchEdit: EditText
    private lateinit var totalTab: TextView

    private val menuCategories = mutableListOf<CategoryItem>()
    private val allItems = mutableListOf<MenuItem>()
    private val menuItems = mutableListOf<MenuItem>()
    private val billItems = mutableListOf<BillItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        category_view = findViewById(R.id.category_title)
        searchEdit = findViewById(R.id.search)
        totalTab = findViewById(R.id.total_amount)

        searchEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?){}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                showSearchResults(s.toString())
            }
        })

        // Set up the categories recycler view
        categoriesRecyclerView = findViewById(R.id.categories_recycler_view)
        categoriesRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        iconManager = IconManager(this)

        lifecycleScope.launch {
            fetchCategories()
            fetchItems()
        }
        categoriesRecyclerView.adapter = MenuCategoriesAdapter(menuCategories)

        // Set up the items recycler view
        itemsRecyclerView = findViewById(R.id.items_recycler_view)
        itemsRecyclerView.layoutManager = LinearLayoutManager(this)

        // Set up the bill recycler view
        billRecyclerView = findViewById(R.id.bill_recycler_view)
        billRecyclerView.layoutManager = LinearLayoutManager(this)

        billRecyclerView.adapter = BillItemAdapter(billItems)

        // anytime a bill item is added or removed, update the total
        billRecyclerView.adapter?.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                Log.d("Bill", "Data changed")
                updateTotal()
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                super.onItemRangeChanged(positionStart, itemCount)
                Log.d("Bill", "Item range changed")
                updateTotal()
            }
        })

        // Back button and bottom tabs
        val backButtonContainer: ConstraintLayout = findViewById(R.id.backButtonContainer)
        val expandedBottom: RelativeLayout = findViewById(R.id.bottom_tab_expanded)
        val bottomTab: RelativeLayout = findViewById(R.id.bottom_tab)
        val payTab: Button = findViewById(R.id.pay_button)

        backButtonContainer.setOnClickListener {
            expandedBottom.visibility = RelativeLayout.GONE
        }

        bottomTab.setOnClickListener {
            expandedBottom.visibility = RelativeLayout.VISIBLE
        }

        payTab.setOnClickListener {
            if (billItems.isEmpty()) {
                showToast("No items in bill")
            } else {
                val intent = Intent(this, NavigationActivity::class.java)

                val bill = mutableListOf<Map<String, *>>()

                billItems.forEach { item ->
                    bill.add(mapOf(
                        "name" to item.name,
                        "price" to item.price,
                        "qty" to item.qty
                    ))
                }

                intent.putExtra("bill", ArrayList(bill))
                intent.putExtra("goto", "startTab")

                billItems.clear()
                billRecyclerView.adapter?.notifyDataSetChanged()

                startActivity(intent)
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private suspend fun fetchCategories() {
        val db = FirebaseFirestore.getInstance()
        try {
            val querySnapshot = db.collection("stores")
                .whereEqualTo("name", "Brats Madison")
                .get().await()

            if (querySnapshot.documents.isNotEmpty()) {
                querySnapshot.documents.forEach { document ->
                    val categoriesList = document.get("categories") as List<Map<String, String>>?
                    categoriesList?.forEach { category ->
                        val iconName = category["image"] ?: "default_icon"
                        val name = category["name"] ?: "Unknown"
                        val iconResource = iconManager.getDrawableResourceId(iconName) ?: R.drawable.app_logo
                        menuCategories.add(CategoryItem(iconResource, name) { changeMenu(name) })
                    }
                }

                runOnUiThread {
                    categoriesRecyclerView.adapter?.notifyDataSetChanged()
                }
            } else {
                Log.d("Firestore", "No document found with name Brats Madison")
            }
        } catch (e: Exception) {
            Log.e("Firestore", "Error fetching categories", e)
        }
    }

    private suspend fun fetchItems() {
        val db = FirebaseFirestore.getInstance()
        try {
            val querySnapshot = db.collection("stores")
                .whereEqualTo("name", "Brats Madison")
                .get().await()

            if (querySnapshot.documents.isNotEmpty()) {
                querySnapshot.documents.forEach { document ->
                    val categoriesList = document.get("items") as List<Map<String, *>>?
                    categoriesList?.forEach { category ->
                        val iconName = (category["image"] ?: "default_icon").toString()
                        val iconResource = iconManager.getDrawableResourceId(iconName) ?: R.drawable.app_logo

                        val name = (category["name"] ?: "Unknown").toString()
                        val cost = (category["cost"] ?: "Unknown").toString().toDouble()
                        val description = (category["description"] ?: "Unknown").toString()
                        val categoryList = category["categories"] as List<String>?

                        allItems.add(MenuItem(iconResource, name, categoryList, cost, "$${cost.toInt()}+") { addItemsToBill(name) })
                    }
                }

                runOnUiThread {
                    changeMenu("Quick Adds")
                }
            } else {
                Log.d("Firestore", "No document found with name Brats Madison")
            }
        } catch (e: Exception) {
            Log.e("Firestore", "Error fetching categories", e)
        }
    }

    private fun changeMenu(category: String) {
        menuItems.clear()
        allItems.forEach { item ->
            if (item.categories?.contains(category) == true) {
                menuItems.add(item)
            }
        }

        itemsRecyclerView.adapter = MenuItemsAdapter(menuItems)

        itemsRecyclerView.adapter?.notifyDataSetChanged()
        category_view.text = category
    }

    private fun showSearchResults(searchQuery: String) {
        if(searchQuery.isEmpty()) {
            category_view.visibility = TextView.VISIBLE
            categoriesRecyclerView.visibility = RecyclerView.VISIBLE

            changeMenu("Quick Adds")
            return
        }

        menuItems.clear()
        allItems.forEach { item ->
            if (item.name.contains(searchQuery, ignoreCase = true)) {
                menuItems.add(item)
            }
        }

        itemsRecyclerView.adapter = MenuItemsAdapter(menuItems)
        category_view.visibility = TextView.GONE
        categoriesRecyclerView.visibility = RecyclerView.GONE

        itemsRecyclerView.adapter?.notifyDataSetChanged()
    }

    private fun addItemsToBill(itemName: String) {
        Log.d("Bill", "Adding $itemName to bill")
        var item = billItems.find { it.name == itemName }
        var itemIndex = billItems.indexOf(item)

        if(item == null) {
            item = BillItem(itemName, allItems.find { it.name == itemName }!!.cost, 1)
            billItems.add(item)
        } else {
            billItems[itemIndex].qty++
        }

        billRecyclerView.adapter?.notifyDataSetChanged()
    }

    private fun updateTotal() {
        var total = 0.0
        billItems.forEach { item ->
            total += item.price * item.qty
        }
        totalTab.text = String.format("$%.2f", total)
    }
}
