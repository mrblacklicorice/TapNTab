package com.cs407.tapntab
import android.content.Context
import android.content.SharedPreferences
import java.security.MessageDigest

class AccountUtil {
    companion object {
        private const val PREF_NAME = "UserDetails"
        private const val KEY_USERNAME = "Username"
        private const val KEY_EMAIL = "Email"

        fun saveUserDetails(context: Context, username: String, email: String) {
            val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString(KEY_USERNAME, username)
            editor.putString(KEY_EMAIL, email)
            editor.apply() // Use apply() instead of commit() for asynchronous save
        }

        fun getUserDetails(context: Context): HashMap<String, String> {
            val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            val username = sharedPreferences.getString(KEY_USERNAME, null)
            val email = sharedPreferences.getString(KEY_EMAIL, null)
            return hashMapOf("Username" to (username ?: ""), "Email" to (email ?: ""))
        }

        fun hash(password: String): Any? {
            val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
            return bytes.joinToString("") { "%02x".format(it) }
        }
    }
}