package com.cs407.tapntab

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.firebase.firestore.FirebaseFirestore;
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.security.MessageDigest

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        // Ensure window insets are handled
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val emailInput: EditText = findViewById(R.id.emailInput)
        val passwordInput: EditText = findViewById(R.id.passwordInput)
        val loginButton: Button = findViewById(R.id.loginButton)

        loginButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            // Validate input fields
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in both fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Authenticate user
            lifecycleScope.launch {
                if (authenticateUser(email, password)) {
                    // If credentials are correct, navigate to the first page
                    val intent = Intent(this@LoginActivity, FrontPageActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // If credentials are incorrect, show a notification
                    runOnUiThread {
                        Toast.makeText(this@LoginActivity, "Incorrect email or password", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }


        // Back button logic to navigate to MainActivity
        findViewById<View>(R.id.backButtonContainer).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


        // Sign up button logic to navigate to SignupActivity
        findViewById<View>(R.id.signUpText).setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        // Validate Remember Me on app start
        validateRememberMe()
        // Handle Remember Me checkbox logic
        setupRememberMe()
    }

    private fun setupRememberMe() {
        val rememberMeCheckbox: CheckBox = findViewById(R.id.rememberMeCheckbox)

        rememberMeCheckbox.setOnCheckedChangeListener { _, isChecked ->
            val sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            if (isChecked) {
                // Save Remember Me status with current timestamp
                editor.putBoolean("RememberMe", true)
                editor.putLong("RememberMeTimestamp", System.currentTimeMillis())
            } else {
                // Clear Remember Me status
                editor.remove("RememberMe")
                editor.remove("RememberMeTimestamp")
            }
            editor.apply()
        }
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

    // Dummy authentication function
    suspend fun authenticateUser(email: String, password: String): Boolean = withContext(Dispatchers.IO) {
        val db = FirebaseFirestore.getInstance()
        val usersRef = db.collection("users")

        try {
            val documents = usersRef.whereEqualTo("email", email).get().await()
            if (documents.isEmpty) {
                return@withContext false
            } else {
                for (document in documents) {
                    val storedHashedPassword = document.getString("hashedPassword") ?: ""
                    val hashedInputPassword = AccountUtil.hash(password)

                    AccountUtil.saveUserDetails(this@LoginActivity, document.getString("username") ?: "", email)

                    if (storedHashedPassword == hashedInputPassword) {
                        // Save user details in SharedPreferences upon successful login
                        val sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("username", document.getString("username"))
                        editor.putString("email", email)
                        editor.apply()

                        // Update last accessed timestamp in Firebase
                        usersRef.document(document.id)
                            .update("lastAccessed", com.google.firebase.Timestamp.now()).await()

                        return@withContext true
                    }
                }
            }
        } catch (e: Exception) {
            Log.w("LoginActivity", "Error in authentication", e)
        }
        false
    }
}
