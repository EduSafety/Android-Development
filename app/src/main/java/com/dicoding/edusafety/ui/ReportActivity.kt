package com.dicoding.edusafety.ui

import android.Manifest
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.dicoding.edusafety.R
import com.dicoding.edusafety.data.model.ReportData
import com.dicoding.edusafety.databinding.ActivityReportBinding
import com.dicoding.edusafety.helper.ReportDataHolder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ReportActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReportBinding
    private val myCalendar = Calendar.getInstance()

    private var currentImageUri: Uri? = null
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.imageAttachFile.setImageURI(it)
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        // Ambil data yang dikirimkan dari adapter
        val selectedTitle = intent.getStringExtra("selectedCategory")

        // Ganti isi dari AutoCompleteTextView
        binding.autoComplete.setText(selectedTitle ?: "Social")

        setupDropdown()
        setupDatePicker()
        setupBackButton()

        binding.imageAttachFile.setOnClickListener{
            startGallery()
        }
        binding.btnNext.setOnClickListener {
            if (areAllFieldsFilled()) {
                // Success
                val category = binding.autoComplete.text.toString()
                val perpetratorName = binding.edtNamaPelaku.text.toString()
                val victimName = binding.edtNamaKorban.text.toString()
                val incidentDate = binding.tvDate.text.toString()
                val description = binding.edtDescription.text.toString()

                // Create ReportData instance
                val reportData = ReportData(category, perpetratorName, victimName, incidentDate, description)

                // Store data in ReportDataHolder
                ReportDataHolder.reportData = reportData

                startActivity(Intent(this, QuestionActivity2::class.java))
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
        binding.backButton.setOnClickListener {
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

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}
