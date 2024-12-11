package com.cs407.tapntab

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CheckEmail : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_check_email)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize the text boxes
        val codeBoxes = listOf<EditText>(
            findViewById(R.id.codeDigit1),
            findViewById(R.id.codeDigit2),
            findViewById(R.id.codeDigit3),
            findViewById(R.id.codeDigit4),
            findViewById(R.id.codeDigit5),
            findViewById(R.id.codeDigit6)
        )

        setupCodeInput(codeBoxes)
    }

    private fun setupCodeInput(codeBoxes: List<EditText>) {
        for (i in codeBoxes.indices) {
            codeBoxes[i].addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (!s.isNullOrEmpty()) {
                        // Move to the next text box if available
                        if (i < codeBoxes.size - 1) {
                            codeBoxes[i + 1].requestFocus()
                        }
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                    // Ensure only one digit is allowed
                    if (s?.length ?: 0 > 1) {
                        codeBoxes[i].setText(s?.substring(0, 1))
                        codeBoxes[i].setSelection(1)
                    }
                }
            })

            // Handle backspacing
            codeBoxes[i].setOnKeyListener { _, keyCode, _ ->
                if (keyCode == android.view.KeyEvent.KEYCODE_DEL && codeBoxes[i].text.isEmpty()) {
                    if (i > 0) {
                        codeBoxes[i - 1].requestFocus()
                    }
                }
                false
            }
        }

        // Combine all digits into a single string when all are filled
        codeBoxes.last().addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (codeBoxes.all { it.text.toString().isNotEmpty() }) {
                    val enteredCode = codeBoxes.joinToString(separator = "") { it.text.toString() }
                    verifyCode(enteredCode)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun verifyCode(enteredCode: String) {
        val EXPECTED_CODE = "123456" // Example hardcoded value for verification

        if (enteredCode == EXPECTED_CODE) {
            Toast.makeText(this, "Code verified successfully!", Toast.LENGTH_SHORT).show()

            // Get the email passed from ChangePassword
            val email = intent.getStringExtra("email")

            // Navigate to NewPassword activity
            val intent = Intent(this, NewPassword::class.java)
            intent.putExtra("email", email) // Pass email to NewPassword
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Invalid code, please try again.", Toast.LENGTH_SHORT).show()
        }
    }
}
