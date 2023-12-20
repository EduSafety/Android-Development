package com.dicoding.edusafety.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.edusafety.R
import com.dicoding.edusafety.data.model.MyItem
import com.dicoding.edusafety.databinding.FragmentHomeBinding
import com.dicoding.edusafety.viewmodel.MainViewModel
import com.dicoding.edusafety.viewmodel.MainViewModelApi
import com.dicoding.edusafety.viewmodel.ViewModelFactory
import com.dicoding.edusafety.viewmodel.ViewModelFactoryApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setReportAdapter()
        setRecentAdapter()

        binding.recentReportContainer.setOnClickListener {
            startActivity(Intent(requireContext(), ReportHistoryActivity::class.java))
        }

        auth = Firebase.auth
        val currentUser = auth.currentUser
        if(currentUser != null){
            val factoryPref: ViewModelFactoryApi = ViewModelFactoryApi.getInstance(requireContext())
            val viewModel = ViewModelProvider(this, factoryPref)[MainViewModelApi::class.java]

            val factoryDS: ViewModelFactory = ViewModelFactory.getInstance(requireContext())
            val viewModelDS = ViewModelProvider(this, factoryDS)[MainViewModel::class.java]


            viewModelDS.getTokenUser().observe(requireActivity(), Observer { token ->
                if (token != null){
                    viewModel.getCurrentUser(token)
                    viewModel.currentUser.observe(requireActivity(), Observer { user ->
                        if(user != null){
                            val displayName = user.fullname
                            Log.d("DISPLAY NAME", "$displayName")
                            binding.nameProfile.text = displayName
                            binding.profilePhoto.setImageResource(R.drawable.photoprofile)
                        }
                    })
                }
            })
            updateUI(currentUser)
        }
    }

    private fun setReportAdapter() {
        val recyclerView: RecyclerView = requireView().findViewById(R.id.rv_category)
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())

        val itemList = listOf(
            MyItem(R.drawable.phsyical_assault, "Verbal Abuse", "1h 30 Minutes"),
            MyItem(R.drawable.bullying, "Physical Assault", "1h 30 Minutes"),
            MyItem(R.drawable.verbal_abuse, "Social", "30 Minutes"),
        )

        val adapter = MainReportAdapter(itemList)
        recyclerView.adapter = adapter
    }

    private fun setRecentAdapter() {
        val recyclerView: RecyclerView = requireView().findViewById(R.id.rc_recent_report)
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())

        val itemList = listOf(
            MyItem(R.drawable.avatar, "Dave Leonard", "Verbal Abuse"),
            MyItem(R.drawable.avatar, "Dave Leonard", "Physical Assault"),
            MyItem(R.drawable.avatar, "Dave Leonard", "Social"),
        )

        val adapter = MainRecentAdapter(itemList)
        recyclerView.adapter = adapter
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {

        }
    }
}