package com.dicoding.edusafety.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.edusafety.R
import com.dicoding.edusafety.data.api.response.DataLeaderboard
import java.util.Collections

class LeaderboardAdapter(private val itemList: List<DataLeaderboard?>?) :
    RecyclerView.Adapter<LeaderboardAdapter.ViewHolder>() {

    init {
        // Urutkan itemList secara descending berdasarkan totalComplaints
        itemList?.let {
            Collections.sort(it, compareByDescending { it?.totalComplaints })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_leaderboard, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList?.get(position)
        if (item != null){
            //        holder.imageView.setImageResource(item.imageResource)
            holder.univName.text = item.name
            holder.reportNumber.text = "Total Report : "+item.totalComplaints.toString()
            holder.rankNumber.text = (1 + position).toString()
        }
    }

    override fun getItemCount(): Int {
        return itemList?.size ?: 0
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val univName: TextView = itemView.findViewById(R.id.univName)
        val reportNumber: TextView = itemView.findViewById(R.id.reportNumber)
        val rankNumber: TextView = itemView.findViewById(R.id.rankNumber)
    }
}