package com.dicoding.mycustomview

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.edusafety.data.api.response.DataItemQuestion
import com.dicoding.edusafety.databinding.ItemQuestionBinding
import com.dicoding.edusafety.databinding.ItemQuestionTextBinding
import com.google.gson.JsonArray
import com.google.gson.JsonObject

class QuestionAdapter(private val questions: List<DataItemQuestion?>,  private val clearFocusListener: ClearFocusListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_DROPDOWN = 1
    private val VIEW_TYPE_TEXT = 2

    private val dropdownAnswers = mutableMapOf<Int, String>()
    private val editTextAnswers = mutableMapOf<Int, String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_DROPDOWN -> DropdownViewHolder(ItemQuestionBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            VIEW_TYPE_TEXT -> EditTextViewHolder(ItemQuestionTextBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val question = questions[position]
        when (holder) {
            is DropdownViewHolder -> {
                question?.let { holder.bindDropdown(it) }
                dropdownAnswers[position] = holder.getDropdownAnswer()
            }
            is EditTextViewHolder -> {
                question?.let { holder.bindEditText(it) }
                editTextAnswers[position] = holder.getEditTextAnswer()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val question = questions[position]
        return when (question?.type) {
            "dropdown" -> VIEW_TYPE_DROPDOWN
            "text" -> VIEW_TYPE_TEXT
            else -> throw IllegalArgumentException("Invalid question type")
        }
    }

    override fun getItemCount(): Int {
        return questions.size
    }

    interface ClearFocusListener {
        fun clearFocus()
    }
    inner class DropdownViewHolder(private val binding: ItemQuestionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindDropdown(question: DataItemQuestion) {
            binding.textViewPertanyaan.text = question.question
            val choicesWithId = question.choices?.map { choice ->
                "${choice?.id} - ${choice?.text}" to choice?.id.toString()
            }

            val adapter = choicesWithId?.let {
                ArrayAdapter(binding.root.context, android.R.layout.simple_dropdown_item_1line, it.map { it.first })
            }
            binding.autoCompleteTextViewJawaban.setAdapter(adapter)
        }
        init {
            binding.autoCompleteTextViewJawaban.setOnItemClickListener { _, _, _, _ ->
                clearFocusListener.clearFocus()
            }
        }
        fun getDropdownAnswer(): String {
            val answer = binding.autoCompleteTextViewJawaban.text.toString()
            return answer
        }
    }

    class EditTextViewHolder(private val binding: ItemQuestionTextBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindEditText(question: DataItemQuestion) {
            binding.textViewPertanyaan.text = question.question
        }

        fun getEditTextAnswer(): String {
            return binding.edtQuestionText.text.toString()
        }
    }

    fun getAllAnswersAsJsonArray(): JsonArray {
        val jsonArray = JsonArray()

        // Iterasi melalui jawaban dropdown
        for ((position, answer) in dropdownAnswers) {
            if (answer.isNotEmpty()) {
                val question = questions[position]
                val answerObject = JsonObject()

                // Menggunakan question_id dari response API
                val questionId = question?.questionId ?: 0
                answerObject.addProperty("question_id", questionId)

                // Menggunakan choice_id dari response API
                val selectedChoiceId = answer.split(" - ")[0].toIntOrNull() ?: 0
                answerObject.addProperty("answer", selectedChoiceId)
                jsonArray.add(answerObject)
            }
        }

        // Iterasi melalui jawaban edit text
        for ((position, answer) in editTextAnswers) {
            if (answer.isNotEmpty()) {
                val question = questions[position]
                val answerObject = JsonObject()

                // Menggunakan question_id dari response API
                val questionId = question?.questionId ?: 0
                answerObject.addProperty("question_id", questionId)

                answerObject.addProperty("answer", answer)
                jsonArray.add(answerObject)
            }
        }

        return jsonArray
    }
}
//class QuestionAdapter(private val items: List<QuestionItem>) : RecyclerView.Adapter<QuestionAdapter.ViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_question, parent, false)
//        return ViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val item = items[position]
//        holder.textViewPertanyaan.text = item.pertanyaan
//
//        val adapter = ArrayAdapter(holder.itemView.context, R.layout.dropdown_item, item.jawaban)
//        holder.autoCompleteTextViewJawaban.setAdapter(adapter)
//    }
//
//    override fun getItemCount(): Int {
//        return items.size
//    }
//
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val textViewPertanyaan: TextView = itemView.findViewById(R.id.textViewPertanyaan)
//        val autoCompleteTextViewJawaban: AutoCompleteTextView = itemView.findViewById(R.id.autoCompleteTextViewJawaban)
//    }
//}
