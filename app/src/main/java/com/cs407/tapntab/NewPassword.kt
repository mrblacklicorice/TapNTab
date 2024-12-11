package com.cs407.tapntab

import android.content.Intent
import android.os.Bundle
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

class NewPassword : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_new_password)

        // Handle edge-to-edge UI
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Retrieve email passed from CheckEmail
        val email = intent.getStringExtra("email")

        // Initialize UI elements
        val newPasswordInput: EditText = findViewById(R.id.newPasswordInput)
        val reenterPasswordInput: EditText = findViewById(R.id.reenterPasswordInput)
        val updatePasswordButton: Button = findViewById(R.id.updatePasswordButton)

        updatePasswordButton.setOnClickListener {
            val newPassword = newPasswordInput.text.toString().trim()
            val reenterPassword = reenterPasswordInput.text.toString().trim()

            // Validate passwords
            if (newPassword.isEmpty() || reenterPassword.isEmpty()) {
                Toast.makeText(this, "Please fill in both fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (newPassword != reenterPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Update password in the database
            lifecycleScope.launch {
                val success = updatePasswordInDatabase(email, newPassword)
                if (success) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@NewPassword, "Password updated successfully!", Toast.LENGTH_SHORT).show()
                        // Navigate back to LoginActivity
                        val intent = Intent(this@NewPassword, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@NewPassword, "Failed to update password. Please try again.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    // Function to update password in the database
    private suspend fun updatePasswordInDatabase(email: String?, newPassword: String): Boolean = withContext(Dispatchers.IO) {
        if (email == null) return@withContext false

        val db = FirebaseFirestore.getInstance()
        val usersRef = db.collection("users")

        return@withContext try {
            val documents = usersRef.whereEqualTo("email", email).get().await()
            if (!documents.isEmpty) {
                for (document in documents) {
                    val hashedPassword = AccountUtil.hash(newPassword) // Assuming AccountUtil.hash is implemented for hashing passwords
                    usersRef.document(document.id).update("hashedPassword", hashedPassword).await()
                }
                true
            } else {
                false // Email not found
            }
        } catch (e: Exception) {
            false // Handle exceptions gracefully
        }
    }
}
