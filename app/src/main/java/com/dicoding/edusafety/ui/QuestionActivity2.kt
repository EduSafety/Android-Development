package com.dicoding.edusafety.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.edusafety.R
import com.dicoding.edusafety.data.model.QuestionItem
import com.dicoding.edusafety.databinding.ActivityQuestion2Binding
import com.dicoding.mycustomview.QuestionAdapter


class QuestionActivity2 : AppCompatActivity() {
    private lateinit var binding: ActivityQuestion2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestion2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            onBackPressed()
        }


        binding.btnSubmit.setOnClickListener {
            if (isAllQuestionsAnswered()) {
                startActivity(Intent(this, DetailActivity::class.java))
            } else {
                Toast.makeText(this, "Mohon jawab semua pertanyaan", Toast.LENGTH_SHORT).show()
            }
        }

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val recyclerView = binding.recylerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        val items = createQuestionItems()
        val adapter = QuestionAdapter(items)
        recyclerView.adapter = adapter
    }

    private fun createQuestionItems(): List<QuestionItem> {
        // pertanyaan dan jawaban(autocomplete)
        return listOf(
            QuestionItem("Example question", resources.getStringArray(R.array.question_1)),
            QuestionItem("Example question", resources.getStringArray(R.array.question_2)),
            QuestionItem("Example question", resources.getStringArray(R.array.question_6)),
        )
    }

    private fun isAllQuestionsAnswered(): Boolean {
        val adapter = (binding.recylerView.adapter as? QuestionAdapter)
        adapter?.let {
            for (i in 0 until it.itemCount) {
                val viewHolder = binding.recylerView.findViewHolderForAdapterPosition(i) as? QuestionAdapter.ViewHolder
                val autoCompleteTextView = viewHolder?.autoCompleteTextViewJawaban

                // Check if AutoCompleteTextView is empty
                if (autoCompleteTextView?.text.isNullOrBlank()) {
                    return false
                }
            }
        }
        return true
    }


}
