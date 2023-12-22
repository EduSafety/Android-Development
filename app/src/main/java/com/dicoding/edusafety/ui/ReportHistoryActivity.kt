package com.dicoding.edusafety.ui

import android.content.Intent
import android.os.Bundle
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.edusafety.R
import com.dicoding.edusafety.databinding.ActivityReportHistoryBinding
import com.dicoding.edusafety.viewmodel.MainViewModel
import com.dicoding.edusafety.viewmodel.MainViewModelApi
import com.dicoding.edusafety.viewmodel.ReportViewModel
import com.dicoding.edusafety.viewmodel.ViewModelFactory
import com.dicoding.edusafety.viewmodel.ViewModelFactoryApi

class ReportHistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReportHistoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setRecentAdapter()
        binding.backArrow.setOnClickListener {
            onBackPressed()
        }
        binding.fab.setOnClickListener {
            startActivity(Intent(this, ReportActivity::class.java))
        }
        binding.sortButton.setOnClickListener {
            // Show sorting options
            showSortOptions(it)
        }
    }

    // Function to show the sorting options
    fun showSortOptions(view: android.view.View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.menu_sorting, popupMenu.menu)

        binding.rvHistory.layoutManager = LinearLayoutManager(this)

        val factoryPref: ViewModelFactoryApi = ViewModelFactoryApi.getInstance(this)
        val viewModel = ViewModelProvider(this, factoryPref)[ReportViewModel::class.java]

        val factoryDS: ViewModelFactory = ViewModelFactory.getInstance(this)
        val viewModelDS = ViewModelProvider(this, factoryDS)[MainViewModel::class.java]

        // Set a click listener for each item in the menu
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.verbalSort -> {
                    viewModelDS.getTokenUser().observe(this, Observer { token ->
                        if (token != null){
                            viewModel.getComplaintCategory(token,1,30)
                            viewModel.data.observe(this, Observer { record ->
                                if(record != null){
                                    val adapter = MainRecentAdapter(record)
                                    binding.rvHistory.adapter = adapter
                                }
                            })
                        }
                    })
                    true
                }
                R.id.physicalSort -> {
                    viewModelDS.getTokenUser().observe(this, Observer { token ->
                        if (token != null){
                            viewModel.getComplaintCategory(token,2,30)
                            viewModel.data.observe(this, Observer { record ->
                                if(record != null){
                                    val adapter = MainRecentAdapter(record)
                                    binding.rvHistory.adapter = adapter
                                }
                            })
                        }
                    })
                    true
                }
                R.id.socailSort -> {
                    viewModelDS.getTokenUser().observe(this, Observer { token ->
                        if (token != null){
                            viewModel.getComplaintCategory(token,3,30)
                            viewModel.data.observe(this, Observer { record ->
                                if(record != null){
                                    val adapter = MainRecentAdapter(record)
                                    binding.rvHistory.adapter = adapter
                                }
                            })
                        }
                    })
                    true
                }
                else -> false
            }
        }

        // Show the popup menu
        popupMenu.show()
    }
    private fun setRecentAdapter() {
        binding.rvHistory.layoutManager = LinearLayoutManager(this)

        val factoryPref: ViewModelFactoryApi = ViewModelFactoryApi.getInstance(this)
        val viewModel = ViewModelProvider(this, factoryPref)[MainViewModelApi::class.java]

        val factoryDS: ViewModelFactory = ViewModelFactory.getInstance(this)
        val viewModelDS = ViewModelProvider(this, factoryDS)[MainViewModel::class.java]

        viewModelDS.getTokenUser().observe(this, Observer { token ->
            if (token != null){
                viewModel.getAllRecentReport(token)
                viewModel.recordReport.observe(this, Observer { record ->
                    if(record != null){
                        val adapter = MainRecentAdapter(record)
                        binding.rvHistory.adapter = adapter
                    }
                })
            }
        })
    }
}