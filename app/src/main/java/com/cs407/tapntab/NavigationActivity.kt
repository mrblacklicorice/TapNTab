package com.cs407.tapntab

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class NavigationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)  // Ensure this matches your layout file name

        val bottomNav = findViewById<BottomNavigationView>(R.id.navigation)
        bottomNav.setOnItemSelectedListener { item ->
            val selectedFragment: Fragment = when (item.itemId) {
//                R.id.navigation_home -> MenuFragment()
//                R.id.navigation_nfc -> NFCFragment()
                R.id.navigation_nfc -> StartTabFragment()
                R.id.navigation_coupon -> CouponFragment()
                R.id.navigation_profile -> ProfileFragment()
                else -> StartTabFragment()
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, selectedFragment)
                .commit()
            true
        }

        // Load the default home fragment initially
        bottomNav.selectedItemId = R.id.navigation_home
    }
}
