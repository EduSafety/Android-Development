package com.dicoding.edusafety.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.edusafety.R
import com.dicoding.edusafety.data.model.MyItem

class MainReportAdapter(private val itemList: List<MyItem>) :
    RecyclerView.Adapter<MainReportAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]

        holder.imageView.setImageResource(item.imageResource)
        holder.textTitle.text = item.title

        // Tambahkan event klik pada setiap item di RecyclerView
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ReportActivity::class.java)

            // Mengirim data ke ReportActivity
            intent.putExtra("selectedCategory", item.title)

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val textTitle: TextView = itemView.findViewById(R.id.textTitle)
    }
}
