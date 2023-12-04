package com.dicoding.edusafety.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.edusafety.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener{
            startActivity(Intent(this,InitialPage::class.java))
        }

        binding.loginBtn.setOnClickListener{
            MainActivity.isLogin = true
            startActivity(Intent(this,MainActivity::class.java))
        }

        binding.registerButton.setOnClickListener{
            startActivity(Intent(this,RegisterActivity::class.java))
        }
    }
}