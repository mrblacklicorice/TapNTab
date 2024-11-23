package com.cs407.tapntab

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment

class ProfileFragment : Fragment() {
    private lateinit var profilePic: ImageView
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_profile, container, false)

        // Handle edge-to-edge insets
        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        profilePic = view.findViewById(R.id.profilePic)
        sharedPreferences = requireActivity().getSharedPreferences("UserProfile", AppCompatActivity.MODE_PRIVATE)
        loadProfileImage(view)

        // Navigation and button handling
        view.findViewById<View>(R.id.backButtonContainer).setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        view.findViewById<View>(R.id.editButton).setOnClickListener {
//            navigateToFragment(EditProfileFragment())
        }

        view.findViewById<View>(R.id.paymentRow).setOnClickListener {
//            navigateToFragment(EditPaymentMethodFragment()) // Assuming a separate fragment for payment methods
        }

        view.findViewById<View>(R.id.TabRow).setOnClickListener {
//            navigateToFragment(PastTabs) // Assuming a separate fragment for managing tabs
        }

        view.findViewById<View>(R.id.PasswordRow).setOnClickListener {
//            navigateToFragment(ChangePasswordFragment())
        }

        setupNotificationSwitch(view)
        setupOnlineSwitch(view)

        view.findViewById<View>(R.id.SignOut).setOnClickListener {
            Toast.makeText(context, "Signed out successfully", Toast.LENGTH_SHORT).show()
            val intent = Intent(activity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            activity?.finish()
        }

        return view
    }

    private fun navigateToFragment(fragment: Fragment) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun loadProfileImage(view: View?) {
        val imageUriString = sharedPreferences.getString("ProfileImageUri", null)
        if (!imageUriString.isNullOrEmpty()) {
            val imageUri = Uri.parse(imageUriString)
            profilePic.setImageURI(imageUri)
        } else {
            profilePic.setImageResource(R.drawable.profile_pic)
        }
    }

    private fun setupNotificationSwitch(view: View) {
        val switchNotif = view.findViewById<SwitchCompat>(R.id.switchNotif)
        switchNotif.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(context, "Notifications turned ON", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Notifications turned OFF", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupOnlineSwitch(view: View) {
        val switchStatus = view.findViewById<SwitchCompat>(R.id.switchStatus)
        val statusIcon = view.findViewById<ImageView>(R.id.OnlineIcon)

        switchStatus.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                statusIcon.setImageResource(R.drawable.online_picc)
                Toast.makeText(context, "Online status: ON", Toast.LENGTH_SHORT).show()
            } else {
                statusIcon.setImageResource(R.drawable.offline_pic)
                Toast.makeText(context, "Online status: OFF", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadProfileImage(view)
    }
}
