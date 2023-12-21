package com.dicoding.edusafety.ui

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dicoding.edusafety.R
import com.dicoding.edusafety.data.model.ReportData
import com.dicoding.edusafety.databinding.ActivityReportBinding
import com.dicoding.edusafety.helper.ReportDataHolder
import com.dicoding.edusafety.viewmodel.MainViewModel
import com.dicoding.edusafety.viewmodel.ReportViewModel
import com.dicoding.edusafety.viewmodel.ViewModelFactory
import com.dicoding.edusafety.viewmodel.ViewModelFactoryApi
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ReportActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReportBinding
    private val myCalendar = Calendar.getInstance()
    private var currentImageUri: Uri? = null


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


        // Ambil data yang dikirimkan dari adapter
        val selectedTitle = intent.getStringExtra("selectedCategory")

        // Ganti isi dari AutoCompleteTextView
        binding.autoComplete.setText(selectedTitle ?: "Pilih Kategori")

//        setupDropdown()
        setupDatePicker()
        setupBackButton()

        binding.imageAttachFile.setOnClickListener{
            startGallery()
        }


        val factoryPref: ViewModelFactoryApi = ViewModelFactoryApi.getInstance(this)
        val viewModel = ViewModelProvider(this, factoryPref)[ReportViewModel::class.java]

        val factoryDS: ViewModelFactory = ViewModelFactory.getInstance(this)
        val viewModelDS = ViewModelProvider(this, factoryDS)[MainViewModel::class.java]

        viewModelDS.getTokenUser().observe(this, Observer { token ->
            if (token != null) {
                viewModel.getCategory(token)
                viewModel.category.observe(this, Observer { record ->
                    Log.d("Observer Record","$record")
                    record?.let {
                        val categoryNameList = it.mapNotNull { recordItem -> recordItem?.name }
                        val adapter = ArrayAdapter(this, R.layout.dropdown_item, categoryNameList)
                        Log.d("LIST","$categoryNameList")
                        binding.autoComplete.setAdapter(adapter)
                    }
                })
            }
        })

        binding.btnNext.setOnClickListener {
            if (areAllFieldsFilled()) {
                // Success
                val category = binding.autoComplete.text.toString()
                val perpetratorName = binding.edtNamaPelaku.text.toString()
                val victimName = binding.edtNamaKorban.text.toString()
                val incidentDate = binding.tvDate.text.toString()

                val description = binding.edtDescription.text.toString()

                // Create ReportData instance
                val reportData = ReportData(category, perpetratorName, victimName, incidentDate, description, currentImageUri)

                // Store data in ReportDataHolder
                ReportDataHolder.reportData = reportData
                if(category == "Verbal Abuse"){
                    val idCategory = 1
                    val intent = Intent(this, QuestionActivity2::class.java)
                    intent.putExtra("idCategory","$idCategory")
                    startActivity(intent)
                } else if (category == "Physical Assault"){
                    val idCategory = 2
                    val intent = Intent(this, QuestionActivity2::class.java)
                    intent.putExtra("idCategory","$idCategory")
                    startActivity(intent)
                }
                else if (category == "Social"){
                    val idCategory = 3
                    val intent = Intent(this, QuestionActivity2::class.java)
                    intent.putExtra("idCategory","$idCategory")
                    startActivity(intent)
                }
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

//    private fun setupDropdown() {
////        val category = resources.getStringArray(R.array.report_category)
//
//        val factoryPref: ViewModelFactoryApi = ViewModelFactoryApi.getInstance(this)
//        val viewModel = ViewModelProvider(this, factoryPref)[ReportViewModel::class.java]
//
//        val factoryDS: ViewModelFactory = ViewModelFactory.getInstance(this)
//        val viewModelDS = ViewModelProvider(this, factoryDS)[MainViewModel::class.java]
//
//        viewModelDS.getTokenUser().observe(this, Observer { token ->
//            if (token != null) {
//                viewModel.getCategory(token)
//                viewModel.category.observe(this, Observer { record ->
//                    Log.d("Observer Record","$record")
//                    record?.let {
//                        val categoryNameList = it.mapNotNull { recordItem -> recordItem?.name }
//                        val idCategory = it.mapNotNull { recordItem -> recordItem?.categoryId }
//                        val adapter = ArrayAdapter(this, R.layout.dropdown_item, categoryNameList)
//                        Log.d("LIST","$categoryNameList")
//                        binding.autoComplete.setAdapter(adapter)
//                    }
//                })
//            }
//        })
//
//    }

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
        val myFormat = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
        binding.tvDate.setText(sdf.format(myCalendar.time))
        Log.d("SDF",sdf.format(myCalendar.time))
    }

    private fun setupBackButton() {
        binding.backButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finishAffinity()
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
