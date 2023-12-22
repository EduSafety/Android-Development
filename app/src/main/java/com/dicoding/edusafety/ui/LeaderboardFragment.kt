package com.dicoding.edusafety.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.edusafety.R
import com.dicoding.edusafety.databinding.FragmentLeaderboardBinding
import com.dicoding.edusafety.viewmodel.MainViewModel
import com.dicoding.edusafety.viewmodel.MainViewModelApi
import com.dicoding.edusafety.viewmodel.ViewModelFactory
import com.dicoding.edusafety.viewmodel.ViewModelFactoryApi

class LeaderboardFragment : Fragment() {
    private lateinit var binding: FragmentLeaderboardBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLeaderboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLeaderboardAdapter()
    }

    private fun setLeaderboardAdapter() {
        val recyclerView: RecyclerView = requireView().findViewById(R.id.rvLeaderBoard)
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())

        val factoryPref: ViewModelFactoryApi = ViewModelFactoryApi.getInstance(requireContext())
        val viewModel = ViewModelProvider(this, factoryPref)[MainViewModelApi::class.java]

        val factoryDS: ViewModelFactory = ViewModelFactory.getInstance(requireContext())
        val viewModelDS = ViewModelProvider(this, factoryDS)[MainViewModel::class.java]

        viewModel.isLoading.observe(requireActivity()) {
            showLoading(it)
        }

        viewModelDS.getTokenUser().observe(requireActivity(), Observer { token ->
            if (token != null){
                viewModel.getLeaderboard(token)
                viewModel.leaderBoard.observe(requireActivity(), Observer { data ->
                    if(data != null){
                        val adapter = LeaderboardAdapter(data)
                        recyclerView.adapter = adapter
                    }
                })
            }
        })
    }
    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}