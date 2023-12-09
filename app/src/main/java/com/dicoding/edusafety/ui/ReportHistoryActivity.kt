package com.dicoding.edusafety.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.edusafety.R
import com.dicoding.edusafety.data.model.MyItem
import com.dicoding.edusafety.databinding.ActivityReportHistoryBinding

class ReportHistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReportHistoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setRecentAdapter()
        binding.backArrow.setOnClickListener{
            onBackPressed()
        }
    }

    private fun setRecentAdapter() {
        binding.rvHistory.layoutManager = LinearLayoutManager(this)

        val itemList = listOf(
            MyItem(R.drawable.foto_saya_dicoding, "Jawir", "Verbal Abuse"),
            MyItem(R.drawable.foto_saya_dicoding, "Mas Jawir", "Physical Assault"),
            MyItem(R.drawable.foto_saya_dicoding, "Dave Leonard", "Bullying"),
        )

        val adapter = MainRecentAdapter(itemList)
        binding.rvHistory.adapter = adapter
    }
}