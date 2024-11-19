package com.cs407.tapntab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.activity_profile, container, false)

        // Handle edge-to-edge insets
        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize the ImageView and Switch
//        val statusIcon = view.findViewById<ImageView>(R.id.statusIcon)
//        val statusSwitch = view.findViewById<SwitchCompat>(R.id.statusSwitch)
//
//        // Set an OnCheckedChangeListener on the switch
//        statusSwitch.setOnCheckedChangeListener { _, isChecked ->
//            if (isChecked) {
//                // Switch is on, show online status icon
//                statusIcon.setImageResource(R.drawable.online_picc) // Online image with green dot
//            } else {
//                // Switch is off, show offline status icon
//                statusIcon.setImageResource(R.drawable.offline_pic) // Offline image with grey dot
//            }
//        }

        return view
    }
}
