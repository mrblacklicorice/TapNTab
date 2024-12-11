package com.cs407.tapntab

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class PastTabs : AppCompatActivity() {
    private var pastTabs: MutableList<PastTab> = mutableListOf()

    private lateinit var pastTabsAdapter: PastTabsAdapter
    private lateinit var pastTabsRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_past_tabs)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        pastTabsRecyclerView = findViewById(R.id.pastTabsRecyclerView)
        pastTabsRecyclerView.layoutManager = LinearLayoutManager(this) // Set the layout manager
        pastTabsAdapter = PastTabsAdapter(pastTabs)
        pastTabsRecyclerView.adapter = pastTabsAdapter


        lifecycleScope.launch {
            getPastTabs()
        }
    }

    suspend fun getPastTabs() {
        val db = FirebaseFirestore.getInstance()
        val email = AccountUtil.getUserDetails(this)["Email"]

        val usersRef = db.collection("users")

        try {
            val documents = usersRef.whereEqualTo("email", email).get().await()

            if (!documents.isEmpty) {
                for (document in documents) {
                    val tabs = document.get("history")?: mutableListOf<Map<String, Any>>()
                    var pastTabsList = mutableListOf<PastTab>()

                    for (tab in tabs as List<Map<String, Any>>) {
                        val barName = tab["barName"] as String
                        val tabTotal = tab["total"] as Double
                        val tabDate = tab["timestamp"] as Timestamp

                        pastTabsList.add(PastTab(barName, tabTotal, tabDate))
                    }

                    pastTabs.clear()
                    pastTabs.addAll(pastTabsList.asReversed())
                }
            }
            Log.d("Firestore", pastTabs.toString())

            withContext(Dispatchers.Main) {
                pastTabsRecyclerView.adapter?.notifyDataSetChanged()
            }
        } catch (e: Exception) {
            Log.e("Firestore", "Error fetching past tabs", e)
        }
    }
}