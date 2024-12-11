package com.cs407.tapntab

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import com.cs407.tapntab.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class FragmentNFC : Fragment() {

    private lateinit var nfcReaderIcon: ImageView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_n_f_c, container, false)

        // Initialize NFC image view
        nfcReaderIcon = rootView.findViewById(R.id.nfcReaderIcon)

        lifecycleScope.launch {
            checkOpenedTab()
        }

        return rootView
    }

    private fun navigateToSwipeUpToOpenFragment() {
        // Navigate to SwipeUpToOpenFragment
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, SwipeUpToOpenTabFragment())
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .addToBackStack(null)
            .commit()
    }

    private suspend fun checkOpenedTab() {
        val db = FirebaseFirestore.getInstance()
        val email = AccountUtil.getUserDetails(requireContext())["Email"]

        val usersRef = db.collection("users")

        try {
            val documents = usersRef.whereEqualTo("email", email).get().await()

            if (!documents.isEmpty) {
                for (document in documents) {
                    val openedTab = document.get("openedTab") ?: listOf<Map<String, Any>>()

                    if (openedTab.equals(listOf<Map<String, Any>>())) {
                        // Set a click listener to navigate to SwipeUpToOpenFragment
                        nfcReaderIcon.setOnClickListener {
                            navigateToSwipeUpToOpenFragment()
                        }
                    } else{
                        // Navigate to FragmentNFC
                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, StartTabFragment())
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .addToBackStack(null)
                            .commit()
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("FragmentNFC", "Error checking opened tab: $e")
        }
    }
}
