package com.dicoding.edusafety.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.edusafety.R
import com.dicoding.edusafety.data.model.MyItem
import com.dicoding.edusafety.databinding.FragmentHomeBinding
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
            updateUI(currentUser)
        }
    }

    private fun setReportAdapter() {
        val recyclerView: RecyclerView = requireView().findViewById(R.id.rc_category)
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
            MyItem(R.drawable.foto_saya_dicoding, "Dave Leonard", "Verbal Abuse"),
            MyItem(R.drawable.foto_saya_dicoding, "Dave Leonard", "Physical Assault"),
            MyItem(R.drawable.foto_saya_dicoding, "Dave Leonard", "Social"),
        )

        val adapter = MainRecentAdapter(itemList)
        recyclerView.adapter = adapter
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            val uid = currentUser.uid
            val photoUrl = currentUser.photoUrl
            val displayName = currentUser.displayName

            binding.nameProfile.text = displayName
            Glide.with(this)
                .load(photoUrl)
                .into(binding.profilePhoto)
        }
    }

}