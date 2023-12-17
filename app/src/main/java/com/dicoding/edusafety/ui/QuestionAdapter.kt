package com.dicoding.mycustomview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.edusafety.R
import com.dicoding.edusafety.data.model.QuestionItem

class QuestionAdapter(private val items: List<QuestionItem>) : RecyclerView.Adapter<QuestionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_question, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.textViewPertanyaan.text = item.pertanyaan

        val adapter = ArrayAdapter(holder.itemView.context, R.layout.dropdown_item, item.jawaban)
        holder.autoCompleteTextViewJawaban.setAdapter(adapter)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewPertanyaan: TextView = itemView.findViewById(R.id.textViewPertanyaan)
        val autoCompleteTextViewJawaban: AutoCompleteTextView = itemView.findViewById(R.id.autoCompleteTextViewJawaban)
    }
}
