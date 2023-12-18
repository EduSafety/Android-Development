package com.dicoding.edusafety.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.dicoding.edusafety.R
import com.dicoding.edusafety.databinding.ActivityDetailBinding
import com.dicoding.edusafety.helper.ReportDataHolder

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finishAffinity()
        }

        updateUi()

    }

    private fun updateUi() {
        // Retrieve data from FormDataHolder
        val formData = ReportDataHolder.reportData
        binding.tvKategori.text = formData?.category
        binding.tvNamaKorban.text = formData?.victimName
        binding.namaPelaku.text = formData?.perpetratorName
        binding.tvTanggalKejadian.text = formData?.incidentDate
        binding.tvKronologi.text = formData?.description
    }

    override fun onBackPressed() {
        super.onBackPressed()
        // Navigate to MainActivity
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }
}