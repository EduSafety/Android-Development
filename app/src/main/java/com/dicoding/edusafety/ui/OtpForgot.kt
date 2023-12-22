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
import com.dicoding.edusafety.databinding.ActivityOtpForgotBinding
import com.dicoding.edusafety.viewmodel.LoginViewModelFactory
import com.dicoding.edusafety.viewmodel.RegisterViewModel

class OtpForgot : AppCompatActivity() {
    private lateinit var binding: ActivityOtpForgotBinding
    private var isCountdownRunning = false
    private var countdownTimer: CountDownTimer? = null

    private val viewModel by viewModels<RegisterViewModel> {
        LoginViewModelFactory.getInstance()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpForgotBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val email = intent.getStringExtra("email")
        with(binding) {
            setupTextWatcher(otp1, otp2, null)
            setupTextWatcher(otp2, otp3, otp1)
            setupTextWatcher(otp3, otp4, otp2)
            setupTextWatcher(otp4, null, otp3)

            verifyBtn.isEnabled = false // Initially, disable the Verify button
            resendOtp.setOnClickListener {
                if (email != null) {
                    viewModel.resendOtp(email)
                    disableVerifyButton()
                    startCountdown()
                    resendOtp.isClickable = false
                    Toast.makeText(this@OtpForgot,"Mengirim Otp", Toast.LENGTH_SHORT).show()
                }
            }
        }

        //verify
        binding.verifyBtn.setOnClickListener{
            val otp1Text = binding.otp1.text.toString()
            val otp2Text = binding.otp2.text.toString()
            val otp3Text = binding.otp3.text.toString()
            val otp4Text = binding.otp4.text.toString()

            val otp = (otp1Text+otp2Text+otp3Text+otp4Text).toInt()
            if (email != null) {
                viewModel.verifyOtpForgot(email,(otp1Text+otp2Text+otp3Text+otp4Text).toInt())
            }
            Log.d("CODE OTP ACTIVITY","$otp + $email")
            viewModel.valitOtp.observe(this, Observer { otpResp ->
                if(otpResp?.acknowledge == true){
                    val intent = Intent(this, CreateNewPassword::class.java)
                    intent.putExtra("email",email)
                    startActivity(intent)
                    finish()
                }else if(otpResp?.acknowledge == false){
                    showAlertDialog("Wrong OTP","Please Input Correct OTP","OK")
                }else{
                    Log.d("Server Error","$otpResp")
                }
            })
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