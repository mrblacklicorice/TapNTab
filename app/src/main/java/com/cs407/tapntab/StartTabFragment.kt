package com.cs407.tapntab

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.lifecycleScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class StartTabFragment : Fragment() {
    private lateinit var billedItemsRecyclerView: RecyclerView
    private lateinit var billedItemsAdapter: TotalBillItemAdapter
    private lateinit var billedItems: MutableList<TotalBillItem>

    private lateinit var topBar: View
    private lateinit var bottomBar: View
    private lateinit var plusButton: ImageButton
    private lateinit var instructionText: TextView
    private lateinit var totalLabel: TextView

    companion object {
        private const val CHANNEL_ID = "start_tab_channel"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_start_tab, container, false)

        billedItemsRecyclerView = view.findViewById(R.id.bill_recycler_view)
        topBar = view.findViewById(R.id.top_bar)
        bottomBar = view.findViewById(R.id.bottom_bar)
        plusButton = view.findViewById(R.id.plusButton)
        instructionText = view.findViewById(R.id.instructionText)
        totalLabel = view.findViewById(R.id.total_label)

        billedItems = mutableListOf()
        billedItemsAdapter = TotalBillItemAdapter(billedItems)
        billedItemsRecyclerView.adapter = billedItemsAdapter

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

        // Handle Close Tab Button Click
        view.findViewById<Button>(R.id.closeTabButton).setOnClickListener {
            val swipeDownFragment = SwipeDownToCloseTabFragment()

            // Calculate the total
            var total = 0.0
            billedItems.forEach { item ->
                total += item.price * item.qty
            }

            // Create a Bundle to pass the total
            val bundle = Bundle()
            bundle.putDouble("tabTotal", total)
            swipeDownFragment.arguments = bundle

            totalLabel.visibility = View.GONE

            // Replace fragment in the container
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, swipeDownFragment)
                .addToBackStack(null)  // Optional: Allows back navigation
                .commit()

            // Make FragmentContainerView visible
            view.findViewById<View>(R.id.fragmentContainerView).visibility = View.VISIBLE
        }

        return view
    }

    override fun onResume() {
        super.onResume()

        // Get the bills
        lifecycleScope.launch {
            getBills()
            updateBills()
        }
    }

    private suspend fun getBills() {
        val db = FirebaseFirestore.getInstance()
        val email = AccountUtil.getUserDetails(requireContext())["Email"]

        val usersRef = db.collection("users")

        try {
            val documents = usersRef.whereEqualTo("email", email).get().await()

            if (!documents.isEmpty) {
                for (document in documents) {
                    val items = document.get("openedTab") ?: mutableListOf<Map<String, Any>>()
                    var total = 0.0

                    Log.d("StartTabFragment", "Items: $items")

                    billedItems.clear()

                    for (item in items as List<Map<String, Any>>) {
                        val itemName = item["name"] as String
                        val itemPrice = item["price"] as Double
                        val itemQuantity = item["qty"] as Long

                        billedItems.add(TotalBillItem(itemName, itemPrice, itemQuantity))
                        total += itemPrice * itemQuantity
                    }

                    totalLabel.text = "Total: $${String.format("%.2f", total)}"
                }
            }
        } catch (e: Exception) {
            Log.e("StartTabFragment", "Error getting past tabs", e)
        }
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

    private fun updateBills() {
        Log.d("StartTabFragment", "Previous bills: $billedItems")

        billedItemsAdapter.notifyDataSetChanged()

        billedItemsRecyclerView.visibility = if (billedItems.isEmpty()) View.GONE else View.VISIBLE
        topBar.visibility = if (billedItems.isEmpty()) View.GONE else View.VISIBLE
        bottomBar.visibility = if (billedItems.isEmpty()) View.GONE else View.VISIBLE
        totalLabel.visibility = if (billedItems.isEmpty()) View.GONE else View.VISIBLE

        instructionText.visibility = if (billedItems.isEmpty()) View.VISIBLE else View.GONE
        plusButton.visibility = if (billedItems.isEmpty()) View.VISIBLE else View.GONE
    }
}



