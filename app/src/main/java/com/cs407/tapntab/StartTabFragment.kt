package com.cs407.tapntab

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class StartTabFragment : Fragment() {

    companion object {
        private const val CHANNEL_ID = "start_tab_channel"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.activity_start_tab, container, false)

        // Create the notification channel
        createNotificationChannel(requireContext())


        // Check the notification switch setting and send a notification if enabled
        val sharedPreferences =
            requireContext().getSharedPreferences("UserSettings", Context.MODE_PRIVATE)
        val isNotifEnabled = sharedPreferences.getBoolean("NotificationsEnabled", true)

        if (isNotifEnabled) {
            sendNotification(requireContext())
        }

        // Handle edge-to-edge insets
        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set up button to navigate to MenuActivity
        view.findViewById<View>(R.id.menuButton).setOnClickListener {
            val intent = Intent(requireContext(), MenuActivity::class.java)
            startActivity(intent)
        }
        // Set up plusButton to navigate to the same MenuActivity
        view.findViewById<View>(R.id.plusButton).setOnClickListener {
            val intent = Intent(requireContext(), MenuActivity::class.java)
            startActivity(intent)
        }

        view.findViewById<View>(R.id.closeTabButton).setOnClickListener {
            val intent = Intent(requireContext(), SwipeDownToCloseTabFragment::class.java)
            startActivity(intent)
        }
        return view
    }

    fun createNotificationChannel(context: Context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val name = "Start Tab Notifications"
            val descriptionText = "Notifications for Start Tab reminders"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun sendNotification(context: Context) {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.app_logo)
            .setContentTitle("Don't miss out!")
            .setContentText("Don't forget to check the deals in your area!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            // Check if permission is granted
            if (ActivityCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                notify(1001, builder.build()) // Send the notification if granted
            } else {
                // Optionally request permission here
                requestPostNotificationPermission()
            }
        }
    }

    private fun requestPostNotificationPermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                1001
            )
        }
    }


}


