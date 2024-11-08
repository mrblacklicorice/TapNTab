package com.cs407.tapntab

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // Initialize the ImageView and Switch
//    val statusIcon = findViewById<ImageView>(R.id.statusIcon)
//    val statusSwitch = findViewById<SwitchCompat>(R.id.statusSwitch)

// Set an OnCheckedChangeListener on the switch
//    statusSwitch.setOnCheckedChangeListener { _, isChecked ->
 //       if (isChecked) {
            // Switch is on, show online status icon
            //statusIcon.setImageResource(R.drawable.online_picc) // Online image with green dot
  //      } else {
//            // Switch is off, show offline status icon
//            statusIcon.setImageResource(R.drawable.offline_pic) // Offline image with grey dot
//        }
//    }
//
}