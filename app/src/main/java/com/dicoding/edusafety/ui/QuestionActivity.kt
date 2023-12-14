package com.dicoding.edusafety.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.edusafety.R

class QuestionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)

        findViewById<Button>(R.id.startQuestionBtn).setOnClickListener{
            startActivity(Intent(this, QuestionActivity2::class.java))
            finish()
        }
    }
}