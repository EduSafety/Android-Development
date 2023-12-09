package com.dicoding.edusafety.ui

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.dicoding.edusafety.R
import com.dicoding.edusafety.databinding.ActivityReportBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ReportActivity : AppCompatActivity() {
    lateinit var binding: ActivityReportBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //make input dropdown
        val category = resources.getStringArray(R.array.report_category)
        val adapter = ArrayAdapter(this,R.layout.dropdown_item,category)
        binding.autoComplete.setAdapter(adapter)

        //make datapicker
        val myCalender = Calendar.getInstance()
        val datePicker = DatePickerDialog.OnDateSetListener{view, year, month, dayOfMonth ->
            myCalender.set(Calendar.YEAR, year)
            myCalender.set(Calendar.MONTH, month)
            myCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updatable(myCalender)
        }
        binding.tvDateContainer.setOnClickListener{
            DatePickerDialog(this,datePicker,myCalender.get(Calendar.YEAR), myCalender.get(Calendar.MONTH),
                myCalender.get(Calendar.DAY_OF_MONTH)).show()
        }

        //back button
        binding.backArrow.setOnClickListener{
            onBackPressed()
        }
    }

    private fun updatable(myCalender: Calendar ) {
        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        binding.tvDate.setText(sdf.format(myCalender.time))
    }

}