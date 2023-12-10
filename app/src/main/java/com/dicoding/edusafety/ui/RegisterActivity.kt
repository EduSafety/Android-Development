package com.dicoding.edusafety.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.edusafety.databinding.ActivityRegisterBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        binding.btnRegister.setOnClickListener {
            if (areAllFieldsFilled()) {
                // All fields are filled, navigate to MainActivity
                startActivity(Intent(this, MainActivity::class.java))
                finishAffinity()
            } else {
                // Display error messages for empty fields
                checkAndSetErrorForEmptyField(binding.edtFullname, binding.edtFullnameContainer, "Fullname is required")
                checkAndSetErrorForEmptyField(binding.edtEmail, binding.edtEmailContainer, "Email is required")
                checkAndSetErrorForEmptyField(binding.edtPhoneNumber, binding.edtPhoneNumberContainer, "Phone number is required")
                checkAndSetErrorForEmptyField(binding.edtPassword, binding.edtPasswordContainer, "Password is required")
                checkAndSetErrorForEmptyField(binding.edtConfirmPassword, binding.edtConfirmPasswordContainer, "Confirm Password is required")
            }
        }

        setupTextChangeListener(binding.edtFullname, binding.edtFullnameContainer, "Fullname is required")
        setupTextChangeListener(binding.edtEmail, binding.edtEmailContainer, "Email is required") { text ->
            validEmail(text)
        }
        setupTextChangeListener(binding.edtPhoneNumber, binding.edtPhoneNumberContainer, "Phone number is required") { text ->
            validPhone(text)
        }
        setupTextChangeListener(binding.edtPassword, binding.edtPasswordContainer, "Password is required") { text ->
            validPassword(text)
        }
        setupTextChangeListener(binding.edtConfirmPassword, binding.edtConfirmPasswordContainer, "Confirm Password is required") { text ->
            validateConfirmPassword(text)
        }

    }

    private fun setupTextChangeListener(
        editText: TextInputEditText,
        container: TextInputLayout,
        emptyMessage: String,
        validationFunction: ((String) -> String?)? = null
    ) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text = s?.toString()?.trim() ?: ""
                container.helperText = if (text.isEmpty()) emptyMessage else validationFunction?.invoke(text) ?: ""
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun validEmail(emailText: String): String? {
        return if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            "Invalid Email Address"
        } else {
            null
        }
    }

    private fun validPassword(passwordText: String): String? {
        if (passwordText.length < 8) {
            return "Minimum 8 Character Password"
        }
        if (!passwordText.matches(".*[A-Z].*".toRegex())) {
            return "Must Contain 1 Upper-case Character"
        }
        if (!passwordText.matches(".*[a-z].*".toRegex())) {
            return "Must Contain 1 Lower-case Character"
        }
        if (!passwordText.matches(".*[@#\$%^&+=].*".toRegex())) {
            return "Must Contain 1 Special Character (@#\$%^&+=)"
        }
        return null
    }

    private fun validPhone(phoneText: String): String? {
        if (!phoneText.matches(".*[0-9].*".toRegex())) {
            return "Must be all Digits"
        }
        if (phoneText.length <= 11) {
            return "Must be 11 Digits"
        }
        return null
    }

    private fun validateConfirmPassword(confirmPasswordText: String): String? {
        val passwordText = binding.edtPassword.text?.toString() ?: ""
        return if (passwordText != confirmPasswordText) {
            "Password doesn't match"
        } else {
            null
        }
    }

    private fun areAllFieldsFilled(): Boolean {
        return binding.edtFullname.text?.isNotBlank() == true &&
                binding.edtEmail.text?.isNotBlank() == true &&
                binding.edtPhoneNumber.text?.isNotBlank() == true &&
                binding.edtPassword.text?.isNotBlank() == true &&
                binding.edtConfirmPassword.text?.isNotBlank() == true
    }

    private fun checkAndSetErrorForEmptyField(
        editText: TextInputEditText,
        container: TextInputLayout,
        errorMessage: String
    ) {
        if (editText.text?.isBlank() == true) {
            container.helperText = errorMessage
        }
    }
}
