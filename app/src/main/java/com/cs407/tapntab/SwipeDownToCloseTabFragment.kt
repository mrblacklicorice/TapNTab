package com.cs407.tapntab

import android.os.Bundle
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.cs407.tapntab.SwipeUpToOpenTabFragment.Companion

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
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, StartTabFragment())
            .addToBackStack(null)
            .commit()
    }


//    private fun navigateToFrontPage() {
//        requireActivity().supportFragmentManager.beginTransaction()
//            .replace(R.id.fragment_container, FrontpageFragment())
//            .addToBackStack(null)
//            .commit()
//    }

    private fun navigateToFrontPage() {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, StartTabFragment()) // change to front page
            .addToBackStack(null)
            .commit()
    }
}
