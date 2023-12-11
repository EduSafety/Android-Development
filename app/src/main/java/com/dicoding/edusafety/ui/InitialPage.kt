package com.dicoding.edusafety.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
        validate()
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            startActivity(Intent(this@InitialPage, MainActivity::class.java))
            finish()
        }
    }

    private fun validate(){
        val currentUser = auth.currentUser
        if (currentUser != null) {
            Log.d("AuthGoogle", "TRUE")
            updateUI(currentUser)
        } else {
            Log.d("AuthGoogle", "FALSE")
            viewModel.getSession().observe(this) { user ->
                if (user.isLogin) {
                    startActivity(Intent(this@InitialPage, MainActivity::class.java))
                    finish()
                }
            }
        }
    }
//    override fun onStart() {
//        super.onStart()
//        // Check if user is signed in (non-null) and update UI accordingly.
//        val currentUser = auth.currentUser
//        if (currentUser != null) {
//            Log.d("AuthGoogle", "TRUE")
//            updateUI(currentUser)
//        } else {
//            Log.d("AuthGoogle", "FALSE")
//            viewModel.getSession().observe(this) { user ->
//                if (user.isLogin) {
//                    startActivity(Intent(this@InitialPage, MainActivity::class.java))
//                    finish()
//                }
//            }
//        }
//    }
}