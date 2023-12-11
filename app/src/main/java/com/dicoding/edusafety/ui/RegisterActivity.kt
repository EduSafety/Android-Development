package com.dicoding.edusafety.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.dicoding.edusafety.databinding.ActivityRegisterBinding
import com.dicoding.edusafety.viewmodel.LoginViewModelFactory
import com.dicoding.edusafety.viewmodel.RegisterViewModel

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    private val viewModel by viewModels<RegisterViewModel> {
        LoginViewModelFactory.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            startActivity(Intent(this, InitialPage::class.java))
        }

        val name = binding.edtFullname.text.toString()
        val email = binding.edtEmail.text.toString()
        val password = binding.edtPassword.text.toString()
        val confPassword = binding.edtConfirmPassword.text
        val phone = binding.edtPhone.text

        binding.btnRegister.setOnClickListener {
            if (true) {
                viewModel.register(name, email, phone, password)
                viewModel.isLoading.observe(this) {
                    showLoading(it)
                }
                viewModel.validRegist.observe(this, Observer {
                    if (it != null) {
                        validRegister(it)
                        viewModel.resetRegisterResponse()
                    }
                })
            } else {
                showAlertDialog("PERINGATAN", "Mohon lengkapi data terlebih dahulu", "OK")
            }

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
        val email = binding.edtEmail.text.toString()
        if (isValid) {
            Handler(Looper.getMainLooper()).post {
                showAlertDialogLogin(
                    "Yeay !",
                    "Akun dengan $email sudah dibuat. Yuk, buat cerita",
                    "LANJUT"
                )
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

    private fun showAlertDialogLogin(title: String, message: String, buttonPos: String) {
        AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton(buttonPos) { _, _ ->
                val login = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(login)
                finish()
            }
            create()
            show()
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
}