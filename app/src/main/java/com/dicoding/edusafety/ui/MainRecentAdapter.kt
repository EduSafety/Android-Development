package com.dicoding.edusafety.ui

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.edusafety.R
import com.dicoding.edusafety.data.api.response.RecordReportItem

class MainRecentAdapter(private val itemList: List<RecordReportItem?>?) :
    RecyclerView.Adapter<MainRecentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_recent, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList?.get(position)
        if (item != null){
            Glide.with(holder.itemView.context)
                .load(R.drawable.baseline_report_24)
                .into(holder.imageView)
            holder.textTitle.text = "Pelaku "+item.victim
            holder.textDescription.text = item.description
        }

        // Tambahkan event klik pada setiap item di RecyclerView
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val id = item?.complaintId
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("idCategory",id)
            Log.d("ID ADAPTER","$id")
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return this.itemList?.size ?: 0
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val textTitle: TextView = itemView.findViewById(R.id.textTitle)
        val textDescription: TextView = itemView.findViewById(R.id.textDescription)
    }
}