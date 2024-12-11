package com.cs407.tapntab

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class EditProfile : AppCompatActivity() {

    private lateinit var profilePic: ImageView
    private var isImageChanged = false
    private val PICK_IMAGE_REQUEST = 1
    private val TAKE_PHOTO_REQUEST = 2

    // Dummy user data (replace with actual data source)
    private val existingUsers = mutableListOf(
        mapOf("username" to "user1", "email" to "user1@example.com", "phone" to "1234567890"),
        mapOf("username" to "user2", "email" to "user2@example.com", "phone" to "0987654321")
    )
    // Simulate the currently logged-in user
    private var currentUser = mutableMapOf(
        "username" to "current_user",
        "email" to "current_user@example.com",
        "phone" to "1112223333",
        "firstName" to "Current",
        "lastName" to "User"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_profile)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //
        findViewById<View>(R.id.backButtonContainer).setOnClickListener {
            val intent = Intent(this, ProfileFragment::class.java)
            startActivity(intent)
        }

        profilePic = findViewById(R.id.basicProfilePic) // Profile picture ImageView

        findViewById<ImageView>(R.id.editPic).setOnClickListener {
            showImagePickerOptions()
        }
        findViewById<Button>(R.id.updateButton).setOnClickListener {
            updateProfile()
        }
    }

    // Check if a field value is already in use by another user
    private fun isFieldInUse(field: String, value: String): Boolean {
        return existingUsers.any { user -> user[field] == value && currentUser[field] != value }
    }

    private fun showImagePickerOptions() {
        // Show options: Choose from Gallery or Take a Photo
        val options = arrayOf("Choose from Gallery", "Take a Photo", "Cancel")
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Change Profile Picture")
        builder.setItems(options) { dialog, which ->
            when (which) {
                0 -> openGallery() // Choose from Gallery
                1 -> openCamera() // Take a Photo
                2 -> dialog.dismiss() // Cancel
            }
        }
        builder.show()
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, TAKE_PHOTO_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PICK_IMAGE_REQUEST -> {
                    val selectedImageUri: Uri? = data?.data
                    selectedImageUri?.let {
                        profilePic.setImageURI(it)
                        saveImageUriToPreferences(it)
                        isImageChanged = true
                        Toast.makeText(this, "Profile picture updated!", Toast.LENGTH_SHORT).show()
                    }
                }
                TAKE_PHOTO_REQUEST -> {
                    val photo: Bitmap? = data?.extras?.get("data") as Bitmap?
                    photo?.let {
                        profilePic.setImageBitmap(it)
                        saveImageBitmapToPreferences(it)
                        isImageChanged = true
                        Toast.makeText(this, "Profile picture updated!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun saveImageUriToPreferences(uri: Uri) {
        val sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("ProfileImageUri", uri.toString())
        editor.apply()
    }

    private fun saveImageBitmapToPreferences(bitmap: Bitmap) {
        // Convert the bitmap to a string (base64 or another format) and save it
        val sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("ProfileImageBitmap", "BitmapPlaceholder") // Replace with actual conversion logic
        editor.apply()
    }
    private fun getImageUriFromPreferences(): String? {
        val sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE)
        return sharedPreferences.getString("ProfileImageUri", null)
    }


    private fun updateProfile() {
        val firstName = findViewById<EditText>(R.id.FirstName).text.toString().trim()
        val lastName = findViewById<EditText>(R.id.LastName).text.toString().trim()
        val username = findViewById<EditText>(R.id.username).text.toString().trim()
        val email = findViewById<EditText>(R.id.emailInput).text.toString().trim()
        val phone = findViewById<EditText>(R.id.editTextPhone).text.toString().trim()

        // Check if at least one field or image is updated
        if (firstName.isEmpty() && lastName.isEmpty() && username.isEmpty() &&
            email.isEmpty() && phone.isEmpty() && !isImageChanged
        ) {
            Toast.makeText(this, "Please fill at least one field to continue", Toast.LENGTH_SHORT).show()
            return
        }


        // Validate if username, email, or phone already exists
        if (username.isNotEmpty() && isFieldInUse("username", username)) {
            Toast.makeText(this, "Username already in use", Toast.LENGTH_SHORT).show()
            return
        }
        if (email.isNotEmpty() && isFieldInUse("email", email)) {
            Toast.makeText(this, "Email already in use", Toast.LENGTH_SHORT).show()
            return
        }
        if (phone.isNotEmpty() && isFieldInUse("phone", phone)) {
            Toast.makeText(this, "Phone number already in use", Toast.LENGTH_SHORT).show()
            return
        }

        // Save profile image if changed
        if (isImageChanged) {
            val profileImageUri = getImageUriFromPreferences()
            if (!profileImageUri.isNullOrEmpty()) {
                currentUser["profileImage"] = profileImageUri
            }
        }

        // Update `SharedPreferences` with new user details
        val sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        if (firstName.isNotEmpty()) editor.putString("firstName", firstName)
        if (lastName.isNotEmpty()) editor.putString("lastName", lastName)
        if (username.isNotEmpty()) editor.putString("username", username)
        if (email.isNotEmpty()) editor.putString("email", email)
        if (phone.isNotEmpty()) editor.putString("phone", phone)

        editor.apply()

        Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show()

        // Navigate back to ProfileActivity to refresh details
        finish()
    }

}
