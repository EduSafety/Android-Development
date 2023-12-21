package com.dicoding.edusafety.ui

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.dicoding.edusafety.R
import com.dicoding.edusafety.databinding.ActivityOtpBinding
import com.dicoding.edusafety.viewmodel.LoginViewModelFactory
import com.dicoding.edusafety.viewmodel.RegisterViewModel

class Otp : AppCompatActivity() {

    private lateinit var binding: ActivityOtpBinding
    private var isCountdownRunning = false
    private var countdownTimer: CountDownTimer? = null

    private val viewModel by viewModels<RegisterViewModel> {
        LoginViewModelFactory.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            setupTextWatcher(otp1, otp2, null)
            setupTextWatcher(otp2, otp3, otp1)
            setupTextWatcher(otp3, otp4, otp2)
            setupTextWatcher(otp4, null, otp3)

            verifyBtn.isEnabled = false // Initially, disable the Verify button
            val fullname = intent.getStringExtra("fullname")
            val email = intent.getStringExtra("email")
            val phone = intent.getStringExtra("phone")
            val password = intent.getStringExtra("password")
            val codeUniv = intent.getStringExtra("codeUniv")
            val phoneConverted: Editable = Editable.Factory.getInstance().newEditable(phone)
            //resend otp
            resendOtp.setOnClickListener {
                if (email != null && fullname != null && phoneConverted != null && password != null && codeUniv != null) {
                    viewModel.register(fullname,email,codeUniv,phoneConverted,password)
                    disableVerifyButton()
                    startCountdown()
                    resendOtp.isClickable = false
                    Toast.makeText(this@Otp,"Mengirim Otp",Toast.LENGTH_SHORT).show()
                }
            }

            //verify
            verifyBtn.setOnClickListener{
                val otp1Text = binding.otp1.text.toString()
                val otp2Text = binding.otp2.text.toString()
                val otp3Text = binding.otp3.text.toString()
                val otp4Text = binding.otp4.text.toString()

                val otp = (otp1Text+otp2Text+otp3Text+otp4Text).toInt()
                if (email != null) {
                    viewModel.verifyOtp(email,(otp1Text+otp2Text+otp3Text+otp4Text).toInt())
                }
                Log.d("CODE OTP ACTIVITY","$otp + $email")
                viewModel.valitOtp.observe(this@Otp, Observer { otpResp ->
                    if(otpResp != null){
                        startActivity(Intent(this@Otp, RegisterSuccessActivity::class.java))
                        finish()
                    }else{
                        showAlertDialog("Wrong OTP","Please Input Correct OTP","OK")
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

    private fun setupTextWatcher(currentEditText: EditText, nextEditText: EditText?, previousEditText: EditText?) {
        currentEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    currentEditText.setBackgroundResource(R.drawable.rounded_input)
                    previousEditText?.requestFocus()
                } else {
                    currentEditText.setBackgroundResource(R.drawable.rounded_input_primary)
                    nextEditText?.requestFocus()
                }
                checkOtpFields()
            }
        })
    }

    private fun checkOtpFields() {
        val otp1Text = binding.otp1.text.toString()
        val otp2Text = binding.otp2.text.toString()
        val otp3Text = binding.otp3.text.toString()
        val otp4Text = binding.otp4.text.toString()

        val isOtpFilled = otp1Text.isNotEmpty() && otp2Text.isNotEmpty() && otp3Text.isNotEmpty() && otp4Text.isNotEmpty()

        binding.verifyBtn.isEnabled = isOtpFilled
    }

    private fun disableVerifyButton() {
        binding.verifyBtn.isEnabled = false
    }

    private fun startCountdown() {
        if (!isCountdownRunning) {
            binding.countdownTextView.visibility = View.VISIBLE

            countdownTimer = object : CountDownTimer(30000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val secondsRemaining = millisUntilFinished / 1000
                    binding.countdownTextView.text = "Waktu tersisa ${secondsRemaining} s"
                }

                override fun onFinish() {
                    binding.countdownTextView.visibility = View.INVISIBLE
                    isCountdownRunning = false
                    binding.resendOtp.isClickable = true
                }
            }.start()

            isCountdownRunning = true
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

    override fun onDestroy() {
        super.onDestroy()
        countdownTimer?.cancel()
    }
}