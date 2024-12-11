package com.cs407.tapntab

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Navigate to LoginActivity when loginButton is clicked
        findViewById<View>(R.id.loginButtonFirstScreen).setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // Navigate to SignupActivity when signUpButton is clicked
        findViewById<View>(R.id.signUpButton).setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

//        if shared preferences has a user logged in, navigate to NavigationActivity
        validateRememberMe()
    }

    private fun validateRememberMe() {
        val sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE)
        val rememberMe = sharedPreferences.getBoolean("RememberMe", false)
        val rememberMeTimestamp = sharedPreferences.getLong("RememberMeTimestamp", 0)

        if (rememberMe) {
            val thirtyDaysInMillis = 30L * 24 * 60 * 60 * 1000 // 30 days in milliseconds
            val currentTime = System.currentTimeMillis()

            if (currentTime - rememberMeTimestamp <= thirtyDaysInMillis) {
                // "Remember Me" is valid; navigate to the first page activity TODO change
                val intent = Intent(this, NavigationActivity::class.java) // Adjust target as needed
                startActivity(intent)
                finish() // Prevent going back to LoginActivity
            } else {
                // "Remember Me" has expired; clear it
                val editor = sharedPreferences.edit()
                editor.remove("RememberMe")
                editor.remove("RememberMeTimestamp")
                editor.apply()
            }
        }
    }
}
