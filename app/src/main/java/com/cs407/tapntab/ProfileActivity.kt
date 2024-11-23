package com.cs407.tapntab

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ProfileActivity : AppCompatActivity() {
    private lateinit var profilePic: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val userDetails = AccountUtil.getUserDetails(this)

        val usernameView = findViewById<TextView>(R.id.usernameText)
        val emailView = findViewById<TextView>(R.id.emailText)

        usernameView.text = userDetails["Username"]
        emailView.text = userDetails["Email"]


        profilePic = findViewById(R.id.profilePic)
        loadProfileImage()

        // Back button logic to navigate to first screen TODO change
        findViewById<View>(R.id.backButtonContainer).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Edit profile button logic to navigate to EditProfile
        findViewById<View>(R.id.editButton).setOnClickListener {
            val intent = Intent(this, EditProfile::class.java)
            startActivity(intent)
        }

        // TODO change to correct activity
        findViewById<View>(R.id.paymentRow).setOnClickListener {
            val intent = Intent(this, EditProfile::class.java)
            startActivity(intent)
        }

        // TODO change to correct activity
        findViewById<View>(R.id.TabRow).setOnClickListener {
            val intent = Intent(this, EditProfile::class.java)
            startActivity(intent)
        }

        //
        findViewById<View>(R.id.PasswordRow).setOnClickListener {
            val intent = Intent(this, ChangePassword::class.java)
            startActivity(intent)
        }

        findViewById<View>(R.id.NotificationsRow).setOnClickListener {
            val switchNotif = findViewById<com.google.android.material.switchmaterial.SwitchMaterial>(R.id.switchNotif)
            switchNotif.isChecked = !switchNotif.isChecked // Toggle the switch state

            // Handle notification settings based on the switch state
            if (switchNotif.isChecked) {
                Toast.makeText(this, "Notifications turned ON", Toast.LENGTH_SHORT).show()
                // Add logic to enable notifications
            } else {
                Toast.makeText(this, "Notifications turned OFF", Toast.LENGTH_SHORT).show()
                // Add logic to disable notifications
            }
        }

        findViewById<View>(R.id.OnlineRow).setOnClickListener {
            val switchStatus = findViewById<com.google.android.material.switchmaterial.SwitchMaterial>(R.id.switchStatus)
            val statusIcon = findViewById<ImageView>(R.id.OnlineIcon)

            switchStatus.isChecked = !switchStatus.isChecked // Toggle the switch state

            if (switchStatus.isChecked) {
                // Online: Change icon to online status
                statusIcon.setImageResource(R.drawable.online_picc) // Online image with green dot
                Toast.makeText(this, "Online status: ON", Toast.LENGTH_SHORT).show()
                // Add logic to mark user online
            } else {
                // Offline: Change icon to offline status
                statusIcon.setImageResource(R.drawable.offline_pic) // Offline image with grey dot
                Toast.makeText(this, "Online status: OFF", Toast.LENGTH_SHORT).show()
                // Add logic to mark user offline
            }
        }

        findViewById<View>(R.id.SignOut).setOnClickListener {
            // Add logic to clear session or user state
            Toast.makeText(this, "Signed out successfully", Toast.LENGTH_SHORT).show()

            // Navigate to MainActivity
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Clear backstack
            startActivity(intent)
            finish()
        }

    }
    private fun loadProfileImage() {
        val sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE)
        val imageUriString = sharedPreferences.getString("ProfileImageUri", null)

        if (!imageUriString.isNullOrEmpty()) {
            // Load the saved custom profile picture
            val imageUri = Uri.parse(imageUriString)
            profilePic.setImageURI(imageUri)
        } else {
            // Fall back to the default profile picture
            profilePic.setImageResource(R.drawable.profile_pic)
        }
    }

    // Dynamically reload the profile image when returning to this screen
    override fun onResume() {
        super.onResume()
        loadProfileImage()
    }
}