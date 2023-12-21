package com.dicoding.edusafety.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.edusafety.databinding.ActivityInitialPageBinding
import com.dicoding.edusafety.viewmodel.MainViewModel
import com.dicoding.edusafety.viewmodel.ViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class InitialPage : AppCompatActivity() {
    private lateinit var binding: ActivityInitialPageBinding
    private lateinit var auth: FirebaseAuth

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInitialPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginBtn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        binding.registerButton.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        auth = Firebase.auth
        val currentUser = auth.currentUser
        showLoading(true)
        viewModel.getSession().observe(this) { user ->
            if (user.isLogin) {
                showLoading(true)
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                showLoading(false)
                if(currentUser != null) {
                    showLoading(true)
                    Log.d("AuthGoogle", "TRUE")
                    updateUI(currentUser)
                }
            }
        }
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            startActivity(Intent(this@InitialPage, MainActivity::class.java))
            finish()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}