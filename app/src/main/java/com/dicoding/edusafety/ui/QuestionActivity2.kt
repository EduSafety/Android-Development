package com.dicoding.edusafety.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.edusafety.databinding.ActivityQuestion2Binding
import com.dicoding.edusafety.helper.ReportDataHolder
import com.dicoding.edusafety.utils.reduceFileImage
import com.dicoding.edusafety.utils.uriToFile
import com.dicoding.edusafety.viewmodel.MainViewModel
import com.dicoding.edusafety.viewmodel.ReportViewModel
import com.dicoding.edusafety.viewmodel.ViewModelFactory
import com.dicoding.edusafety.viewmodel.ViewModelFactoryApi
import com.dicoding.mycustomview.QuestionAdapter
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody


class QuestionActivity2 : AppCompatActivity(), QuestionAdapter.ClearFocusListener {
    private lateinit var binding: ActivityQuestion2Binding
    private lateinit var questionAdapter: QuestionAdapter
    override fun clearFocus() {
        currentFocus?.clearFocus()
        questionAdapter.notifyDataSetChanged()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestion2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        val idCategory = intent.getStringExtra("idCategory")
        Log.d("CATEGORI ID GET INTENT", "$idCategory")

        binding.btnSubmit.setOnClickListener {
            clearFocus()
            sendAnswersToServer()

            val factoryPref: ViewModelFactoryApi = ViewModelFactoryApi.getInstance(this)
            val viewModel = ViewModelProvider(this, factoryPref)[ReportViewModel::class.java]

            viewModel.general.observe(this, Observer { record ->
                Log.d("Observer Record Question", "$record")
                val id = record?.id
                if (id != null) {
                    val intent = Intent(this, DetailActivity::class.java)
                    intent.putExtra("idCategory", id)
                    Log.d("ID INTENT", "$id")
                    startActivity(intent)
                }else{
                    Log.d("ID INTENT", "$id")
                }
            })
        }


        val factoryPref: ViewModelFactoryApi = ViewModelFactoryApi.getInstance(this)
        val viewModel = ViewModelProvider(this, factoryPref)[ReportViewModel::class.java]

        val factoryDS: ViewModelFactory = ViewModelFactory.getInstance(this)
        val viewModelDS = ViewModelProvider(this, factoryDS)[MainViewModel::class.java]

        binding.recylerView.layoutManager = LinearLayoutManager(this)

        viewModelDS.getTokenUser().observe(this, Observer { token ->
            if (token != null) {
                Log.d("TOKEN QUESTION 2", "$token, $idCategory")
                if (idCategory != null) {
                    viewModel.getQuestion(token, idCategory.toInt())
                    viewModel.question.observe(this, Observer { record ->
                        Log.d("Observer Record Question", "$record")
                        record?.let {
                            questionAdapter = QuestionAdapter(it, this)
                            binding.recylerView.adapter = questionAdapter
                        }
                    })
                }
            }
        })
    }


    fun sendAnswersToServer() {
        val allAnswersJsonArray = questionAdapter.getAllAnswersAsJsonArray()
        val factoryPref: ViewModelFactoryApi = ViewModelFactoryApi.getInstance(this)
        val viewModel = ViewModelProvider(this, factoryPref)[ReportViewModel::class.java]

        val factoryDS: ViewModelFactory = ViewModelFactory.getInstance(this)
        val viewModelDS = ViewModelProvider(this, factoryDS)[MainViewModel::class.java]

        val idCategory = intent.getStringExtra("idCategory")

        val formData = ReportDataHolder.reportData
        val perpetrator = formData.perpetratorName
        val victim = formData.victimName
        val incidentDate = formData.incidentDate
        val description = formData.description
        var image = formData.image
        image?.let { uri ->
            val images = uriToFile(uri, this).reduceFileImage()
            val jsonString = allAnswersJsonArray.toString()
            val perpetratorRequestBody = perpetrator.toRequestBody("text/plain".toMediaTypeOrNull())
            val victimRequestBody = victim.toRequestBody("text/plain".toMediaTypeOrNull())
            val dateRequestBody = incidentDate.toRequestBody("text/plain".toMediaTypeOrNull())
            val descriptionRequestBody = description.toRequestBody("text/plain".toMediaTypeOrNull())
            val answerRequestBody =
                jsonString.toRequestBody("application/json".toMediaTypeOrNull())
            val filePart = MultipartBody.Part.createFormData(
                "file",
                images.name,
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), images)
            )
            viewModelDS.getTokenUser().observe(this, Observer { token ->
                if (token != null) {
                    Log.d("TOKEN SUBMIT QUESTION", "$token, $idCategory")
                    if (idCategory != null) {
                        viewModel.submitReport(
                            token,
                            idCategory.toInt(),
                            perpetratorRequestBody,
                            victimRequestBody,
                            dateRequestBody,
                            descriptionRequestBody,
                            filePart,
                            answerRequestBody
                        )
                        Log.d("DATE QUESTION","$dateRequestBody")
                    }
                }
            })
        }
    }
}
