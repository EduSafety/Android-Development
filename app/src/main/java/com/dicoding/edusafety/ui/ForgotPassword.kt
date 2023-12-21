package com.dicoding.edusafety.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.edusafety.databinding.ActivityForgotPasswordBinding
import com.dicoding.edusafety.viewmodel.LoginViewModelFactory
import com.dicoding.edusafety.viewmodel.RegisterViewModel

class ForgotPassword : AppCompatActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding

    private val viewModel by viewModels<RegisterViewModel> {
        LoginViewModelFactory.getInstance()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        emailFocusListener()
        setUpBackButton()
        binding.sendCodeBtn.setOnClickListener{
            val email = binding.edtEmail.text.toString()
            viewModel.resendOtp(email)
            val intent = Intent(this@ForgotPassword, OtpForgot::class.java)
            intent.putExtra("email",binding.edtEmail.text.toString())
            startActivity(intent)
        }
    }

    private fun setUpBackButton() {
        binding.backButton.setOnClickListener{
            onBackPressed()
            finish()
        }
    }

    private fun emailFocusListener() {
        binding.edtEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val errorMessage = validEmail()
                binding.edtEmailContainer.helperText = errorMessage
                binding.sendCodeBtn.isEnabled = errorMessage == null
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun validEmail(): String? {
        val emailText = binding.edtEmail.text.toString()
        return if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            "Invalid Email Address"
        } else {
            null
        }
    }
}
