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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ChangePassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_change_password)

        // Handle edge-to-edge UI
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val emailInput: EditText = findViewById(R.id.emailInput)
        val sendEmailButton: Button = findViewById(R.id.sendEmailButton)

        sendEmailButton.setOnClickListener {
            val email = emailInput.text.toString().trim()

            // Validate email input
            if (email.isEmpty()) {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Check email in the database
            lifecycleScope.launch {
                val emailExists = checkEmailInDatabase(email)
                if (emailExists) {
                    // Navigate to CheckEmail activity if the email exists
                    val intent = Intent(this@ChangePassword, CheckEmail::class.java)
                    intent.putExtra("email", email) // Pass email to next activity
                    startActivity(intent)
                } else {
                    // Show notification if the email does not exist
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@ChangePassword,
                            "No account linked to this email, try again.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    // Function to check if email exists in the Firestore database
    private suspend fun checkEmailInDatabase(email: String): Boolean = withContext(Dispatchers.IO) {
        val db = FirebaseFirestore.getInstance()
        val usersRef = db.collection("users")

        return@withContext try {
            val documents = usersRef.whereEqualTo("email", email).get().await()
            !documents.isEmpty // Returns true if email exists
        } catch (e: Exception) {
            false // Returns false if there is an error
        }
    }
}
