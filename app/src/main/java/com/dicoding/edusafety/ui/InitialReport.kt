package com.dicoding.edusafety.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.edusafety.R

class InitialReport : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial_report)

        findViewById<Button>(R.id.startQuestionBtn).setOnClickListener{
            startActivity(Intent(this, ReportActivity::class.java))
            finish()
        }

        findViewById<ImageButton>(R.id.backButton).setOnClickListener{
            onBackPressed()
            finish()
        }

    }
}