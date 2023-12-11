package com.dicoding.edusafety.ui

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import com.dicoding.edusafety.R
import com.dicoding.edusafety.databinding.ActivityReportBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ReportActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReportBinding
    private val myCalendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupDropdown()
        setupDatePicker()
        setupBackButton()

        binding.btnSubmit.setOnClickListener {
            if (areAllFieldsFilled()) {
                // Success
                Toast.makeText(this,"Berhasil", Toast.LENGTH_SHORT).show()
            } else {
                // Display error messages for empty fields
                checkAndSetErrorForEmptyField(binding.edtNamaKorban, binding.containerEdtNamaKorban, "Nama Korban is required")
                checkAndSetErrorForEmptyField(binding.edtNamaPelaku, binding.containerEdtNamaPelaku, "Nama Pelaku is required")
                checkAndSetErrorForEmptyField(binding.tvDate, binding.tvDateContainer, "Date is required")
                checkAndSetErrorForEmptyField(binding.edtDescription, binding.containerDescription, "Description is required")
            }
        }

        setupTextChangeListener(binding.edtNamaKorban, binding.containerEdtNamaKorban,"Nama Korban is required" )
        setupTextChangeListener(binding.edtNamaPelaku, binding.containerEdtNamaPelaku, "Nama Pelaku is required")
        setupTextChangeListener(binding.tvDate, binding.tvDateContainer, "Date is required")
        setupTextChangeListener(binding.edtDescription, binding.containerDescription, "Description is required")
    }

    private fun setupDropdown() {
        val category = resources.getStringArray(R.array.report_category)
        val adapter = ArrayAdapter(this, R.layout.dropdown_item, category)
        binding.autoComplete.setAdapter(adapter)
    }

    private fun setupDatePicker() {
        val datePicker = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateText()
        }

        binding.tvDate.setOnClickListener {
            DatePickerDialog(
                this,
                datePicker,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun updateDateText() {
        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        binding.tvDate.setText(sdf.format(myCalendar.time))
    }

    private fun setupBackButton() {
        binding.backArrow.setOnClickListener {
            onBackPressed()
        }
    }

    private fun areAllFieldsFilled(): Boolean {
        return  binding.autoComplete.text?.isNotBlank() == true &&
                binding.edtNamaPelaku.text?.isNotBlank() == true &&
                binding.edtNamaKorban.text?.isNotBlank() == true &&
                binding.tvDate.text?.isNotBlank() == true &&
                binding.edtDescription.text?.isNotBlank() == true
    }

    private fun checkAndSetErrorForEmptyField(
        editText: TextInputEditText,
        container: TextInputLayout,
        errorMessage: String
    ) {
        if (editText.text?.isBlank() == true) {
            container.helperText = errorMessage
        }
    }

    private fun setupTextChangeListener(
        editText: TextInputEditText,
        container: TextInputLayout,
        emptyMessage: String,
    ) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text = s?.toString()?.trim() ?: ""
                container.helperText = if (text.isEmpty()) emptyMessage else ""
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }
}
