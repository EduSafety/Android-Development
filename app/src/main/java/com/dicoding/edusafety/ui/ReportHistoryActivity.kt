package com.dicoding.edusafety.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
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
        binding.backArrow.setOnClickListener {
            onBackPressed()
        }
        binding.fab.setOnClickListener {
            startActivity(Intent(this, ReportActivity::class.java))
        }
    }

    private fun setRecentAdapter() {
        binding.rvHistory.layoutManager = LinearLayoutManager(this)

        val itemList = listOf(
            MyItem(R.drawable.avatar, "Dave Leonard", "Verbal Abuse"),
            MyItem(R.drawable.avatar, "Dave Leonard", "Verbal Abuse"),
            MyItem(R.drawable.avatar, "Dave Leonard", "Verbal Abuse"),
            MyItem(R.drawable.avatar, "Dave Leonard", "Verbal Abuse"),
            MyItem(R.drawable.avatar, "Dave Leonard", "Verbal Abuse"),
            MyItem(R.drawable.avatar, "Dave Leonard", "Verbal Abuse"),
            MyItem(R.drawable.avatar, "Dave Leonard", "Verbal Abuse"),
            MyItem(R.drawable.avatar, "Dave Leonard", "Verbal Abuse"),

        )

        val adapter = MainRecentAdapter(itemList)
        binding.rvHistory.adapter = adapter
    }
}