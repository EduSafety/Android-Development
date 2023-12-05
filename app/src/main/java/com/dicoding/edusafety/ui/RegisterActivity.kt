package com.dicoding.edusafety.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.edusafety.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding:ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener{
            startActivity(Intent(this,InitialPage::class.java))
        }

        binding.btnRegister.setOnClickListener{
            startActivity(Intent(this,LoginActivity::class.java))
        }
    }
}