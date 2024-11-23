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
import androidx.lifecycle.lifecycleScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.security.MessageDigest
import android.content.Context
import android.content.SharedPreferences


class SignupActivity : AppCompatActivity() {
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

            lifecycleScope.launch {
                val db = FirebaseFirestore.getInstance()
                val usersRef = db.collection("users")

                val querySnapshot = usersRef.whereEqualTo("username", username)
                    .get().await() + usersRef.whereEqualTo("email", email)
                    .get().await()

                if (querySnapshot.isNotEmpty()) {
                    Toast.makeText(this@SignupActivity, "Username or email already in use", Toast.LENGTH_SHORT).show()
                } else {
                    val hashedPassword = AccountUtil.hash(password)

                    val newUser = hashMapOf(
                        "username" to username,
                        "email" to email,
                        "hashedPassword" to hashedPassword,
                        "lastAccessed" to com.google.firebase.Timestamp.now()
                    )

                    usersRef.add(newUser).await()

                    Toast.makeText(this@SignupActivity, "Account created successfully!", Toast.LENGTH_SHORT).show()
                    AccountUtil.saveUserDetails(this@SignupActivity, username, email)

                    val intent = Intent(this@SignupActivity, NavigationActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }

    }
}