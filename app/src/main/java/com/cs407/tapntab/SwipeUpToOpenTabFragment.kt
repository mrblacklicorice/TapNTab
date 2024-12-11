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

class SwipeUpToOpenTabFragment : Fragment() {

    private lateinit var gestureDetector: GestureDetector

    companion object {
        private const val SWIPE_THRESHOLD = 100
        private const val VELOCITY_THRESHOLD = 100
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_swipe_up_to_open_tab, container, false)
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

                    // Check for vertical swipe
                    if (Math.abs(diffY) > Math.abs(diffX) && Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > VELOCITY_THRESHOLD) {
                        if (diffY < 0) { // Swipe up detected
                            onSwipeUp()
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

        // Set touch listener for the root view
        view.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            true
        }
    }

    // Swipe up action
    private fun onSwipeUp() {
        // Handle the swipe-up action
        // Example: Navigate to another screen or perform a specific action
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, StartTabFragment()) // Replace with your next fragment
            .addToBackStack(null)
            .commit()
    }
}
