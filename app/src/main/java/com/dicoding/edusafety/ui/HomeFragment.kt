package com.dicoding.edusafety.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.edusafety.R
import com.dicoding.edusafety.data.model.MyItem
import com.dicoding.edusafety.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding : FragmentHomeBinding
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

        binding.recentReportContainer.setOnClickListener{
            startActivity(Intent(requireContext(),ReportHistoryActivity::class.java))
        }
    }

    private fun setReportAdapter() {
        val recyclerView: RecyclerView = requireView().findViewById(R.id.rc_category)
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())

        val itemList = listOf(
            MyItem(R.drawable.phsyical_assault, "Bullying", "1h 30 Minutes"),
            MyItem(R.drawable.bullying, "Physical Assault", "1h 30 Minutes"),
            MyItem(R.drawable.verbal_abuse, "Verbal Abuse", "30 Minutes"),
        )

        val adapter = MainReportAdapter(itemList)
        recyclerView.adapter = adapter
    }

    private fun setRecentAdapter() {
        val recyclerView: RecyclerView = requireView().findViewById(R.id.rc_recent_report)
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())

        val itemList = listOf(
            MyItem(R.drawable.foto_saya_dicoding, "Jawir", "Verbal Abuse"),
            MyItem(R.drawable.foto_saya_dicoding, "Mas Jawir", "Physical Assault"),
            MyItem(R.drawable.foto_saya_dicoding, "Dave Leonard", "Bullying"),
        )

        val adapter = MainRecentAdapter(itemList)
        recyclerView.adapter = adapter
    }


}