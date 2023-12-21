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
import com.dicoding.edusafety.data.model.MyCategory
import com.dicoding.edusafety.databinding.FragmentHomeBinding
import com.dicoding.edusafety.viewmodel.MainViewModel
import com.dicoding.edusafety.viewmodel.MainViewModelApi
import com.dicoding.edusafety.viewmodel.ReportViewModel
import com.dicoding.edusafety.viewmodel.ViewModelFactory
import com.dicoding.edusafety.viewmodel.ViewModelFactoryApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
        if (currentUser != null) {
            val factoryPref: ViewModelFactoryApi = ViewModelFactoryApi.getInstance(requireContext())
            val viewModel = ViewModelProvider(this, factoryPref)[MainViewModelApi::class.java]
            val viewModelData = ViewModelProvider(this, factoryPref)[ReportViewModel::class.java]

            val factoryDS: ViewModelFactory = ViewModelFactory.getInstance(requireContext())
            val viewModelDS = ViewModelProvider(this, factoryDS)[MainViewModel::class.java]


            viewModelDS.getTokenUser().observe(requireActivity(), Observer { token ->
                if (token != null) {
                    viewModel.getCurrentUser(token)
                    viewModelData.getComplaintCategory1(token,1,30)
                    viewModelData.data1.observe(requireActivity(), Observer { data ->
                        if(data != null){
                            binding.verbalNumber.text = data.total.toString()
                        }
                    })
                    viewModelData.getComplaintCategory2(token,2,30)
                    viewModelData.data2.observe(requireActivity(), Observer { data ->
                        if(data != null){
                            binding.socialNumber.text = data.total.toString()
                        }
                    })
                    viewModelData.getComplaintCategory3(token,3,30)
                    viewModelData.data3.observe(requireActivity(), Observer { data ->
                        if(data != null){
                            binding.physicalNumber.text = data.total.toString()
                        }
                    })
                    viewModel.currentUser.observe(requireActivity(), Observer { user ->
                        if (user != null) {
                            val displayName = user.fullname
                            Log.d("DISPLAY NAME", "$displayName")
                            binding.nameProfile.text = displayName
                            binding.profilePhoto.setImageResource(R.drawable.photoprofile)
                        }
                    })

                }
                GlobalScope.launch(Dispatchers.Main) {
                    delay(1000) // Delay 1 detik (1000 milidetik)
                    val verbal = binding.verbalNumber.text.toString()
                    val social = binding.socialNumber.text.toString()
                    val physic = binding.physicalNumber.text.toString()

                    val verbalNumber = verbal.toIntOrNull() ?: 0
                    val socialNumber = social.toIntOrNull() ?: 0
                    val physicalNumber = physic.toIntOrNull() ?: 0
                    Log.d("TOTAL","$verbalNumber,$socialNumber,$physicalNumber")
                    val totalReport = verbalNumber + socialNumber + physicalNumber
                    binding.reportsTotalNumber.text = totalReport.toString()
                }

            })
            updateUI(currentUser)
        }
    }

    private fun setReportAdapter() {
        val recyclerView: RecyclerView = requireView().findViewById(R.id.rv_category)
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())

        val itemList = listOf(
            MyCategory(R.drawable.phsyical_assault, "Verbal Abuse", "1h 30 Minutes"),
            MyCategory(R.drawable.bullying, "Physical Assault", "1h 30 Minutes"),
            MyCategory(R.drawable.verbal_abuse, "Social", "30 Minutes"),
        )

        val adapter = MainReportAdapter(itemList)
        recyclerView.adapter = adapter
    }

    private fun setRecentAdapter() {
        val recyclerView: RecyclerView = requireView().findViewById(R.id.rc_recent_report)
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())

        val factoryPref: ViewModelFactoryApi = ViewModelFactoryApi.getInstance(requireContext())
        val viewModel = ViewModelProvider(this, factoryPref)[MainViewModelApi::class.java]

        val factoryDS: ViewModelFactory = ViewModelFactory.getInstance(requireContext())
        val viewModelDS = ViewModelProvider(this, factoryDS)[MainViewModel::class.java]

        viewModelDS.getTokenUser().observe(requireActivity(), Observer { token ->
            if (token != null) {
                viewModel.getRecentReport(token)
                viewModel.recordReport.observe(requireActivity(), Observer { record ->
                    if (record != null) {
                        val adapter = MainRecentAdapter(record)
                        recyclerView.adapter = adapter
                    }
                })
            }
        })

    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {

        }
    }
}