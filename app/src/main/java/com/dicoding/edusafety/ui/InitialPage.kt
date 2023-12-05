package com.dicoding.edusafety.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.edusafety.databinding.ActivityInitialPageBinding

class InitialPage : AppCompatActivity() {
    private lateinit var binding:ActivityInitialPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInitialPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginBtn.setOnClickListener{
            startActivity(Intent(this,LoginActivity::class.java))
        }
        binding.registerButton.setOnClickListener{
            startActivity(Intent(this,RegisterActivity::class.java))
        }
    }
}