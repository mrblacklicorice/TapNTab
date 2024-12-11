package com.cs407.tapntab

import android.os.Bundle
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment

class SwipeDownToCloseTabFragment : Fragment() {

    private lateinit var gestureDetector: GestureDetector

    // Constants for swipe detection
    private val SWIPE_THRESHOLD = 100
    private val VELOCITY_THRESHOLD = 100

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
                    // Detect swipe down: e2.y > e1.y
                    if (e2.y - e1.y > SWIPE_THRESHOLD && Math.abs(velocityY) > VELOCITY_THRESHOLD) {
                        closeTab()
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
    }

    private fun closeTab() {
        // Close the fragment or notify the parent activity
        parentFragmentManager.popBackStack()
    }
}
