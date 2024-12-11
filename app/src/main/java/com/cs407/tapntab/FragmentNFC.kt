package com.cs407.tapntab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.cs407.tapntab.R

class FragmentNFC : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_n_f_c, container, false)

        // Initialize NFC image view
        val nfcReaderIcon: ImageView = rootView.findViewById(R.id.nfcReaderIcon)

        // Set a click listener to navigate to SwipeUpToOpenFragment
        nfcReaderIcon.setOnClickListener {
            navigateToSwipeUpToOpenFragment()
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
}
