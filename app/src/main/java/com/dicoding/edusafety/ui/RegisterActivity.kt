package com.dicoding.edusafety.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.dicoding.edusafety.databinding.ActivityRegisterBinding
import com.dicoding.edusafety.viewmodel.LoginViewModelFactory
import com.dicoding.edusafety.viewmodel.RegisterViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    private val viewModel by viewModels<RegisterViewModel> {
        LoginViewModelFactory.getInstance()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        // Initialize Firebase Auth
        auth = Firebase.auth

        binding.btnRegister.setOnClickListener {
            if (areAllFieldsFilled()) {
                val name = binding.edtFullname.text.toString()
                val accessCode = binding.edtUniversityCode.text.toString()
                val email = binding.edtEmail.text.toString()
                val password = binding.edtPassword.text.toString()
                val phone = binding.edtPhoneNumber.text
                // All fields are filled, navigate to LoginActivity
                Log.d("INPUT", "$name, $email, $accessCode,$phone, $password")
                viewModel.register(name, email, accessCode, phone, password)
                viewModel.isLoading.observe(this) {
                    showLoading(it)
                }
                viewModel.validRegist.observe(this, Observer {
                    if (it != null) {
                        validRegister(it)
                        val stringValue = binding.edtEmail.text.toString()
                        viewModel.resetRegisterResponse()
                    }
                })
            } else {
                // Display error messages for empty fields
                checkAndSetErrorForEmptyField(
                    binding.edtFullname,
                    binding.edtFullnameContainer,
                    "Fullname is required"
                )
                checkAndSetErrorForEmptyField(
                    binding.edtEmail,
                    binding.edtEmailContainer,
                    "Email is required"
                )
                checkAndSetErrorForEmptyField(
                    binding.edtPhoneNumber,
                    binding.edtPhoneNumberContainer,
                    "Phone number is required"
                )
                checkAndSetErrorForEmptyField(
                    binding.edtPassword,
                    binding.edtPasswordContainer,
                    "Password is required"
                )
                checkAndSetErrorForEmptyField(
                    binding.edtConfirmPassword,
                    binding.edtConfirmPasswordContainer,
                    "Confirm Password is required"
                )
                checkAndSetErrorForEmptyField(
                    binding.edtUniversityCode,
                    binding.edtUniversityCodeContainer,
                    "University Code is required"
                )
            }
        }

        setupTextChangeListener(
            binding.edtFullname,
            binding.edtFullnameContainer,
            "Fullname is required"
        )
        setupTextChangeListener(
            binding.edtFullname,
            binding.edtUniversityCodeContainer,
            "University Code is required"
        )
        setupTextChangeListener(
            binding.edtEmail,
            binding.edtEmailContainer,
            "Email is required"
        ) { text ->
            validEmail(text)
        }
        setupTextChangeListener(
            binding.edtPhoneNumber,
            binding.edtPhoneNumberContainer,
            "Phone number is required"
        ) { text ->
            validPhone(text)
        }
        setupTextChangeListener(
            binding.edtPassword,
            binding.edtPasswordContainer,
            "Password is required"
        ) { text ->
            validPassword(text)
        }
        setupTextChangeListener(
            binding.edtConfirmPassword,
            binding.edtConfirmPasswordContainer,
            "Confirm Password is required"
        ) { text ->
            validateConfirmPassword(text)
        }

    }


    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finishAffinity()
        }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        currentUser?.getIdToken(false)?.addOnSuccessListener { result ->
            val token = result.token
            Log.d("TOKEN GOOGLE", "$token")
        }
        updateUI(currentUser)
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
                container.helperText =
                    if (text.isEmpty()) emptyMessage else validationFunction?.invoke(text) ?: ""
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
        if (!passwordText.matches(".*[@#\$%^&+=!].*".toRegex())) {
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
                binding.edtConfirmPassword.text?.isNotBlank() == true &&
                binding.edtUniversityCode.text?.isNotBlank() == true
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

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun validRegister(isValid: Boolean) {
        val fullname = binding.edtFullname.text.toString()
        val email = binding.edtEmail.text.toString()
        val phone = binding.edtPhoneNumber.text.toString()
        val password = binding.edtPassword.text.toString()
        val codeUniv = binding.edtUniversityCode.text.toString()
        if (isValid) {
            Handler(Looper.getMainLooper()).post {
                val intent = Intent(this, Otp::class.java)
                intent.putExtra("fullname", fullname)
                intent.putExtra("email", email)
                intent.putExtra("phone", phone)
                intent.putExtra("password", password)
                intent.putExtra("codeUniv", codeUniv)
                startActivity(intent)
            }

        } else {
            Handler(Looper.getMainLooper()).post {
                showAlertDialog(
                    "Oh no!",
                    "Akun dengan $email sudah dipakai orang lain nih. Yuk, buat yang berbeda sedikit",
                    "BUAT ULANG"
                )
            }
            Log.d("AKUN GAGAL DIBUAT", "Registrasi Gagal")
        }
    }


    private fun showAlertDialog(title: String, message: String, buttonPos: String) {
        AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton(buttonPos) { _, _ ->
            }
            create()
            show()
        }
    }

    companion object {
        private const val TAG = "RegisterActivity"
    }
}
