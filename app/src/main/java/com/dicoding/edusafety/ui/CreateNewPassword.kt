package com.dicoding.edusafety.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.dicoding.edusafety.databinding.ActivityCreateNewPasswordBinding
import com.dicoding.edusafety.viewmodel.LoginViewModelFactory
import com.dicoding.edusafety.viewmodel.RegisterViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class CreateNewPassword : AppCompatActivity() {
    private lateinit var binding: ActivityCreateNewPasswordBinding

    private val viewModel by viewModels<RegisterViewModel> {
        LoginViewModelFactory.getInstance()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateNewPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        // Menambahkan TextChangeListener untuk kedua password
        binding.edtPassword.addTextChangedListener(passwordTextWatcher)
        binding.edtConfirmPassword.addTextChangedListener(confirmPasswordTextWatcher)

        binding.resetPasswordBtn.setOnClickListener {
            val email = intent.getStringExtra("email")
            val password = binding.edtConfirmPassword.text.toString()
            if (email != null) {
                viewModel.resetPassword(email, password)
                viewModel.valitOtp.observe(this, Observer { data ->
                    if (data?.acknowledge == true){
                        startActivity(Intent(this, LoginActivity::class.java))
                        finishAffinity()
                    }else if (data?.acknowledge == false){
                        showAlertDialog("Gagal Reset Password","Periksa Koneksi Anda!","OK")
                    }
                })
            }
        }
        setUpBackButton()
    }

    private fun setUpBackButton() {
        binding.backButton.setOnClickListener{
            onBackPressed()
            finish()
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
                container.helperText =
                    if (text.isEmpty()) emptyMessage else validationFunction?.invoke(text) ?: ""
                checkPasswordValidity()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
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

    private fun validateConfirmPassword(confirmPasswordText: String): String? {
        val passwordText = binding.edtPassword.text?.toString() ?: ""
        return if (passwordText != confirmPasswordText) {
            "Password doesn't match"
        } else {
            null
        }
    }

    private val passwordTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            checkPasswordValidity()
        }

        override fun afterTextChanged(s: Editable?) {}
    }

    private val confirmPasswordTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            checkPasswordValidity()
        }

        override fun afterTextChanged(s: Editable?) {}
    }

    private fun checkPasswordValidity() {
        val passwordError = validPassword(binding.edtPassword.text?.toString()?.trim() ?: "")
        val confirmPasswordError =
            validateConfirmPassword(binding.edtConfirmPassword.text?.toString()?.trim() ?: "")

        // Aktifkan tombol reset jika kedua password valid dan sesuai
        binding.resetPasswordBtn.isEnabled = passwordError == null && confirmPasswordError == null
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
}
