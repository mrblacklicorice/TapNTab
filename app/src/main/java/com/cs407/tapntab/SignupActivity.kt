package com.cs407.tapntab

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SignupActivity : AppCompatActivity() {

    // Replace with actual user data source TODO
    private val existingUsers = listOf(
        mapOf("username" to "user1", "email" to "user1@example.com"),
        mapOf("username" to "user2", "email" to "user2@example.com")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<View>(R.id.logInbuttonSignup).setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        findViewById<View>(R.id.backButtonContainer).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }


        // Handle signup logic
        findViewById<Button>(R.id.signUpButton).setOnClickListener {
            val username = findViewById<EditText>(R.id.username).text.toString().trim()
            val email = findViewById<EditText>(R.id.emailInput).text.toString().trim()
            val password = findViewById<EditText>(R.id.passwordInput).text.toString().trim()
            val confirmPassword = findViewById<EditText>(R.id.confirmPass).text.toString().trim()

            // Validate inputs
            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Check if username or email already exists (dummy data example)
            val isExistingUser = existingUsers.any { user ->
                user["username"] == username || user["email"] == email
            }

            if (isExistingUser) {
                Toast.makeText(this, "Username or email already in use", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // Successful signup
            Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show()
            // TODO: Replace with the correct Activity when it's available
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}