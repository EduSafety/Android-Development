package com.dicoding.edusafety.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.dicoding.edusafety.R
import com.dicoding.edusafety.databinding.ActivityQuestion2Binding


class QuestionActivity2 : AppCompatActivity() {
    private lateinit var binding: ActivityQuestion2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestion2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        setupDropdowns()
        binding.backButton.setOnClickListener{
            onBackPressed()
            finish()
        }

        binding.btnNext.setOnClickListener{
            startActivity(Intent(this, ReportActivity::class.java))
        }
    }

    private fun setupDropdowns() {
        setupDropdown(binding.question1, R.array.question_1)
        setupDropdown(binding.question2, R.array.question_2)
        setupDropdown(binding.question3, R.array.often_question)
        setupDropdown(binding.question4, R.array.often_question)
        setupDropdown(binding.question5, R.array.often_question)
        setupDropdown(binding.question6, R.array.question_6)
        setupDropdown(binding.question7, R.array.often_question)
        setupDropdown(binding.question8, R.array.question_8)
        setupDropdown(binding.question9, R.array.often_question)
        setupDropdown(binding.question10, R.array.question_10)
        setupDropdown(binding.question11, R.array.often_question)
        setupDropdown(binding.question12, R.array.often_question)
    }

    private fun setupDropdown(autoCompleteTextView: AutoCompleteTextView, arrayId: Int) {
        val adapter = ArrayAdapter(this, R.layout.dropdown_item, resources.getStringArray(arrayId))
        autoCompleteTextView.setAdapter(adapter)
    }
}
