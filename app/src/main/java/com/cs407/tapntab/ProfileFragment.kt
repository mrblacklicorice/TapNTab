package com.cs407.tapntab

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.switchmaterial.SwitchMaterial

class ProfileFragment : Fragment() {
    private lateinit var profilePic: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.activity_profile, container, false)
        profilePic = view.findViewById(R.id.profilePic)

        // Load user profile
        loadProfileData(view)
        loadProfileImage()

        // Handle edge-to-edge insets
        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Load user details
        val userDetails = AccountUtil.getUserDetails(requireContext())
        val usernameView = view.findViewById<TextView>(R.id.usernameText)
        val emailView = view.findViewById<TextView>(R.id.emailText)

        usernameView.text = userDetails["Username"]
        emailView.text = userDetails["Email"]

        // Initialize views
        profilePic = view.findViewById(R.id.profilePic)
        loadProfileImage()

        // Set up button click listeners
        setupClickListeners(view)
        setupNotificationSwitch(view)

        return view
    }

    private fun setupClickListeners(view: View) {
        view.findViewById<View>(R.id.backButtonContainer).setOnClickListener {
            navigateTo(NavigationActivity::class.java)
        }

        view.findViewById<View>(R.id.editButton).setOnClickListener {
            navigateTo(EditProfile::class.java)
        }

        view.findViewById<View>(R.id.paymentRow).setOnClickListener {
            navigateTo(NFCReaderActivity::class.java) // Placeholder
        }

        view.findViewById<View>(R.id.TabRow).setOnClickListener {
            navigateTo(PastTabs::class.java) // Placeholder
        }

        view.findViewById<View>(R.id.PasswordRow).setOnClickListener {
            navigateTo(ChangePassword::class.java)
        }


//        // Handle online status
//        view.findViewById<View>(R.id.OnlineRow).setOnClickListener {
//            val switchStatus = view.findViewById<SwitchMaterial>(R.id.switchStatus)
//            val statusIcon = view.findViewById<ImageView>(R.id.OnlineIcon)
//
//            switchStatus.isChecked = !switchStatus.isChecked
//
//            if (switchStatus.isChecked) {
//                statusIcon.setImageResource(R.drawable.online_picc)
//                showToast("Online status: ON")
//            } else {
//                statusIcon.setImageResource(R.drawable.offline_pic)
//                showToast("Online status: OFF")
//            }
//        }

        view.findViewById<View>(R.id.SignOut).setOnClickListener {
            showToast("Signed out successfully")
            navigateTo(MainActivity::class.java, clearTask = true)
        }
    }

    private fun toggleSwitch(view: View, switchId: Int, label: String) {
        val switch = view.findViewById<SwitchMaterial>(switchId)
        switch.isChecked = !switch.isChecked
        val state = if (switch.isChecked) "ON" else "OFF"
        showToast("$label turned $state")
    }

    private fun loadProfileImage() {
        val sharedPreferences = requireContext().getSharedPreferences("UserProfile", Context.MODE_PRIVATE)
        val imageUriString = sharedPreferences.getString("ProfileImageUri", null)

        if (!imageUriString.isNullOrEmpty()) {
            profilePic.setImageURI(android.net.Uri.parse(imageUriString))
        } else {
            profilePic.setImageResource(R.drawable.profile_pic)
        }
    }

    private fun navigateTo(destination: Class<*>, clearTask: Boolean = false) {
        val intent = Intent(requireContext(), destination)
        if (clearTask) {
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext().applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        loadProfileData(requireView())
        loadProfileImage()
    }

    private fun loadProfileData(view: View) {
        val sharedPreferences = requireContext().getSharedPreferences("UserProfile", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "N/A")
        val email = sharedPreferences.getString("email", "N/A")

        // Set the values in the profile fragment UI
        view.findViewById<TextView>(R.id.usernameText).text = username
        view.findViewById<TextView>(R.id.emailText).text = email
    }

    private fun setupNotificationSwitch(view: View) {
        val notificationsRow = view.findViewById<View>(R.id.NotificationsRow)
        val switchNotif = view.findViewById<SwitchMaterial>(R.id.switchNotif)
        val sharedPreferences = requireContext().getSharedPreferences("UserSettings", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Load saved state on startup
        val isNotifEnabled = sharedPreferences.getBoolean("NotificationsEnabled", true)
        switchNotif.isChecked = isNotifEnabled

        // Handle switch click
        notificationsRow.setOnClickListener {
            val newState = !switchNotif.isChecked
            switchNotif.isChecked = newState

            // Save the new state
            editor.putBoolean("NotificationsEnabled", newState)
            editor.apply()

            // Show appropriate toast message
            val message = if (newState) "Notifications enabled" else "Notifications disabled"
            showToast(message)
        }
    }

}
