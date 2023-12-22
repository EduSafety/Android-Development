package com.dicoding.edusafety.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.edusafety.R
import com.dicoding.edusafety.databinding.ActivityDetailBinding
import com.dicoding.edusafety.viewmodel.MainViewModel
import com.dicoding.edusafety.viewmodel.ReportViewModel
import com.dicoding.edusafety.viewmodel.ViewModelFactory
import com.dicoding.edusafety.viewmodel.ViewModelFactoryApi

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

        val factoryPref: ViewModelFactoryApi = ViewModelFactoryApi.getInstance(this)
        val viewModel = ViewModelProvider(this, factoryPref)[ReportViewModel::class.java]

        val factoryDS: ViewModelFactory = ViewModelFactory.getInstance(this)
        val viewModelDS = ViewModelProvider(this, factoryDS)[MainViewModel::class.java]
        val id = intent.getIntExtra("idCategory",0)
        viewModelDS.getTokenUser().observe(this, Observer { token ->
            if (token != null){
                if (id != null) {
                    viewModel.getHistoryComplaint(token, id)
                    viewModel.complaint.observe(this, Observer { data ->
                        if(data != null){
                            val label = data.violenceLabel
                            if(label == "Critical"){
                                binding.label.text = "PARAH"
                                binding.label.backgroundTintList = resources.getColorStateList(R.color.danger, theme)
                            }
                            else if(label == "Middle"){
                                binding.label.text = "SEDANG"
                                binding.label.backgroundTintList = resources.getColorStateList(R.color.middle, theme)
                            }
                            else if(label == "Low"){
                                binding.label.text = "RINGAN"
                                binding.label.backgroundTintList = resources.getColorStateList(R.color.low, theme)
                            }
                            binding.tvKategori.text = data.categoryName
                            binding.tvNamaKorban.text = data.victim
                            binding.namaPelaku.text = data.perpetrator
                            binding.tvTanggalKejadian.text = data.incidentDate.toString()
                            binding.tvKronologi.text = data.description
                            binding.tvUniversitas.text = data.institutionName
                            val originalUrl = data.file

                            val modifiedUrl = originalUrl?.let { transformUrl(it) }
                            Glide.with(this)
                                .load(modifiedUrl)
                                .error(R.drawable.baseline_image_24)
                                .into(binding.bukti)
                        }
                    })
                }
            }
        })
    }

    fun transformUrl(originalUrl: String): String {
        // Memecah URL menjadi bagian-bagian (protocol, domain, path)
        val urlParts = originalUrl.split("/").toMutableList()

        // Menambahkan "uploads" ke path
        urlParts.add(3, "uploads")

        // Menggabungkan kembali bagian-bagian URL
        val modifiedUrl = urlParts.joinToString("/")

        return modifiedUrl
    }
//    private fun updateUi() {
//        // Retrieve data from FormDataHolder
//        val formData = ReportDataHolder.reportData
//        binding.tvKategori.text = formData?.category
//        binding.tvNamaKorban.text = formData?.victimName
//        binding.namaPelaku.text = formData?.perpetratorName
//        binding.tvTanggalKejadian.text = formData?.incidentDate
//        binding.tvKronologi.text = formData?.description
//        binding.bukti.setImageURI(formData?.image)
//    }

    override fun onBackPressed() {
        super.onBackPressed()
        // Navigate to MainActivity
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }
}