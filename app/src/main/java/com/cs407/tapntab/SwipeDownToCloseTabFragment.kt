package com.cs407.tapntab

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.cs407.tapntab.SwipeUpToOpenTabFragment.Companion
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class SwipeDownToCloseTabFragment : Fragment() {

    private lateinit var gestureDetector: GestureDetector

    companion object {
        private const val SWIPE_THRESHOLD = 100
        private const val VELOCITY_THRESHOLD = 100
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for the fragment
        return inflater.inflate(R.layout.fragment_swipe_down_to_close_tab, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize GestureDetector
        gestureDetector = GestureDetector(requireContext(), object : GestureDetector.SimpleOnGestureListener() {
            override fun onFling(
                e1: MotionEvent?,
                e2: MotionEvent,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                if (e1 != null && e2 != null) {
                    val diffY = e2.y - e1.y
                    val diffX = e2.x - e1.x

                    // Check for vertical swipe only
                    if (Math.abs(diffY) > Math.abs(diffX) && Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > VELOCITY_THRESHOLD) {
                        if (diffY > 0) {  // Swipe down detected
                            navigateToFrontPage()
                        }
                        return true
                    }
                }
                return false
            }
        })

        // Handle edge-to-edge insets
        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set touch listener on the root view to pass touch events to GestureDetector
        view.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            true
        }

        // Handle "Go Back to Tab" button click
        view.findViewById<Button>(R.id.goBackButton).setOnClickListener {
            navigateToStartTab()
        }
    }
    private fun navigateToStartTab() {
        val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.navigation)
        bottomNav.selectedItemId = R.id.navigation_nfc

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, StartTabFragment())
            .addToBackStack(null)
            .commit()
    }


    private fun navigateToFrontPage() {
        Toast.makeText(
            requireContext(),
            "Your tab was successfully closed. Talk to bartender to pay.",
            Toast.LENGTH_LONG
        ).show()

        val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.navigation)
        bottomNav.selectedItemId = R.id.navigation_home

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, FrontPageFragment1())
            .addToBackStack(null)
            .commit()

        val spentMoney = Intent(requireContext(), StartTabFragment::class.java).getDoubleExtra("tabTotal", 0.0)

        Log.d("SwipeDownToCloseTab", "Spent money: $spentMoney")

        lifecycleScope.launch {
            addTabToHistory()
        }
    }

    suspend private fun addTabToHistory() {
        val db = FirebaseFirestore.getInstance()
        val email = AccountUtil.getUserDetails(requireContext())["Email"]

        val historyItem = hashMapOf(
            "barName" to "Brats Madison",
            "total" to 0.0,
            "timestamp" to Timestamp.now()
        )

        val usersRef = db.collection("users")

        Log.d("Firestore", "Adding tab to email: $email")

        try {
            val documents = usersRef.whereEqualTo("email", email).get().await()

            if (!documents.isEmpty) {
                    for (document in documents) {
                        val items = document.get("openedTab") as? List<Map<String, Any>> ?: mutableListOf()

                        var total = 0.0
                        for (item in items) {
                            total += (item["price"] as Double) * (item["qty"] as Long)
                        }

                        historyItem["total"] = total

                        val tabs = document.get("history") ?: mutableListOf<Map<String, *>>()
                        val newTabs = tabs as MutableList<Map<String, *>>

                        newTabs.add(historyItem)
                        usersRef.document(document.id).update("history", newTabs).await()
                        usersRef.document(document.id).update("openedTab", mutableListOf<Map<String, *>>()).await()
                    }
                }
        } catch (e: Exception) {
            Log.e("Firestore", "Error adding tab to history", e)
        }
    }
}
