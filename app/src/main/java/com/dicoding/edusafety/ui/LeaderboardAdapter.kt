package com.dicoding.edusafety.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.edusafety.R
import com.dicoding.edusafety.data.model.LeaderBoardItem

class LeaderboardAdapter(private val itemList: List<LeaderBoardItem>) :
    RecyclerView.Adapter<LeaderboardAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_leaderboard, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]

        holder.imageView.setImageResource(item.imageResource)
        holder.univName.text = item.univName
        holder.reportNumber.text = item.reportNumber
        holder.rankNumber.text = item.rankNumber
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val univName: TextView = itemView.findViewById(R.id.univName)
        val reportNumber: TextView = itemView.findViewById(R.id.reportNumber)
        val rankNumber: TextView = itemView.findViewById(R.id.rankNumber)
    }
}