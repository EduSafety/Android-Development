package com.dicoding.edusafety.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.edusafety.R
import com.dicoding.edusafety.data.model.LeaderBoardItem

class LeaderboardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_leaderboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLeaderboardAdapter()
    }

    private fun setLeaderboardAdapter() {
        val recyclerView: RecyclerView = requireView().findViewById(R.id.rvLeaderBoard)
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())

        val itemList = listOf(
            LeaderBoardItem(R.drawable.indo_univ,"Universitas Indonesia", "Pengaduan : 2000", "Rank : 1"),
            LeaderBoardItem(R.drawable.indo_univ,"Universitas Indonesia", "Pengaduan : 2000", "Rank : 1"),
            LeaderBoardItem(R.drawable.indo_univ,"Universitas Indonesia", "Pengaduan : 2000", "Rank : 1"),
            LeaderBoardItem(R.drawable.indo_univ,"Universitas Indonesia", "Pengaduan : 2000", "Rank : 1"),
            LeaderBoardItem(R.drawable.indo_univ,"Universitas Indonesia", "Pengaduan : 2000", "Rank : 1"),
            LeaderBoardItem(R.drawable.indo_univ,"Universitas Indonesia", "Pengaduan : 2000", "Rank : 1"),
            LeaderBoardItem(R.drawable.indo_univ,"Universitas Indonesia", "Pengaduan : 2000", "Rank : 1"),
            LeaderBoardItem(R.drawable.indo_univ,"Universitas Indonesia", "Pengaduan : 2000", "Rank : 1"),
            LeaderBoardItem(R.drawable.indo_univ,"Universitas Indonesia", "Pengaduan : 2000", "Rank : 1"),
            LeaderBoardItem(R.drawable.indo_univ,"Universitas Indonesia", "Pengaduan : 2000", "Rank : 1"),
            LeaderBoardItem(R.drawable.indo_univ,"Universitas Indonesia", "Pengaduan : 2000", "Rank : 1"),
        )

        val adapter = LeaderboardAdapter(itemList)
        recyclerView.adapter = adapter
    }

}