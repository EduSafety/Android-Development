package com.dicoding.edusafety.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.edusafety.data.api.response.CategoryResponse
import com.dicoding.edusafety.data.api.response.DataComplaint
import com.dicoding.edusafety.data.api.response.DataItemQuestion
import com.dicoding.edusafety.data.api.response.DataReport
import com.dicoding.edusafety.data.api.response.GeneralData
import com.dicoding.edusafety.data.api.response.HistoryComplaintResponse
import com.dicoding.edusafety.data.api.response.HistoryReportResponse
import com.dicoding.edusafety.data.api.response.QuestionChoiceResponse
import com.dicoding.edusafety.data.api.response.RecordItemCategory
import com.dicoding.edusafety.data.api.response.RecordReportItem
import com.dicoding.edusafety.repository.CurrentUserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReportViewModel(private val currentUserRepository: CurrentUserRepository) : ViewModel() {

    private val _category = MutableLiveData<List<RecordItemCategory?>?>()
    val category: MutableLiveData<List<RecordItemCategory?>?> = _category

    private val _question = MutableLiveData<List<DataItemQuestion?>?>()
    val question: MutableLiveData<List<DataItemQuestion?>?> = _question

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _data1 = MutableLiveData<DataReport?>()
    val data1: MutableLiveData<DataReport?> = _data1

    private val _data2 = MutableLiveData<DataReport?>()
    val data2: MutableLiveData<DataReport?> = _data2

    private val _data3 = MutableLiveData<DataReport?>()
    val data3: MutableLiveData<DataReport?> = _data3

    private val _data = MutableLiveData<List<RecordReportItem?>?>()
    val data: MutableLiveData<List<RecordReportItem?>?> = _data

    private val _complaint = MutableLiveData<DataComplaint?>()
    val complaint: MutableLiveData<DataComplaint?> = _complaint

    private val _general = MutableLiveData<GeneralData?>()
    val general: MutableLiveData<GeneralData?> = _general
    fun getCategory(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = currentUserRepository.getCategory(token)
                response.enqueue(object : Callback<CategoryResponse> {
                    override fun onResponse(
                        call: Call<CategoryResponse>,
                        response: Response<CategoryResponse>
                    ) {
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            val record = responseBody?.data?.record
                            _category.value = record
                            Log.d("Category", "$record")
                            _isLoading.value = false
                        } else {
                            _isLoading.value = false
                            Log.e("ERROR ON FAILURE", "onFailure: ${response.message()}")
                        }

                    }

                    override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                        _isLoading.value = false
                        Log.e("ERROR ON FAILURE", "onFailure: ${t.message}")
                    }
                })
            } catch (e: Exception) {
                Log.d("ERROR GET CATEGORY", "$e")
                Log.d("Token Catch GET CATEGORY", token)
            }
        }
    }

    fun getHistoryComplaint(token: String, id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = currentUserRepository.getHistoryComplaint(token, id)
                response.enqueue(object : Callback<HistoryComplaintResponse> {
                    override fun onResponse(
                        call: Call<HistoryComplaintResponse>,
                        response: Response<HistoryComplaintResponse>
                    ) {
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            val record = responseBody?.data
                            _complaint.value = record
                            Log.d("Complaint", "$record")
                            _isLoading.value = false
                        } else {
                            _isLoading.value = false
                            Log.e("ERROR ON FAILURE", "onFailure: ${response.message()}")
                        }

                    }

                    override fun onFailure(call: Call<HistoryComplaintResponse>, t: Throwable) {
                        _isLoading.value = false
                        Log.e("ERROR ON FAILURE", "onFailure: ${t.message}")
                    }
                })
            } catch (e: Exception) {
                Log.d("ERROR GET Question", "$e")
                Log.d("Token Catch GET Question", token)
            }
        }
    }

    fun getComplaintCategory(token: String, category_id: Int, limit: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = currentUserRepository.getComplaintAllHistory(token, category_id, limit)
                response.enqueue(object : Callback<HistoryReportResponse> {
                    override fun onResponse(
                        call: Call<HistoryReportResponse>,
                        response: Response<HistoryReportResponse>
                    ) {
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            val record = responseBody?.data?.record
                            _data.value = record
                            _isLoading.value = false
                        } else {
                            _isLoading.value = false
                            Log.e("ERROR ON FAILURE", "onFailure: ${response.message()}")
                        }

                    }

                    override fun onFailure(call: Call<HistoryReportResponse>, t: Throwable) {
                        _isLoading.value = false
                        Log.e("ERROR ON FAILURE", "onFailure: ${t.message}")
                    }
                })
            } catch (e: Exception) {
                Log.d("ERROR GET Question", "$e")
                Log.d("Token Catch GET Question", token)
            }
        }
    }

    fun getComplaintCategory1(token: String, category_id: Int, limit: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = currentUserRepository.getComplaintAllHistory(token, category_id, limit)
                response.enqueue(object : Callback<HistoryReportResponse> {
                    override fun onResponse(
                        call: Call<HistoryReportResponse>,
                        response: Response<HistoryReportResponse>
                    ) {
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            val record = responseBody?.data
                            _data1.value = record
                            _isLoading.value = false
                        } else {
                            _isLoading.value = false
                            Log.e("ERROR ON FAILURE", "onFailure: ${response.message()}")
                        }

                    }

                    override fun onFailure(call: Call<HistoryReportResponse>, t: Throwable) {
                        _isLoading.value = false
                        Log.e("ERROR ON FAILURE", "onFailure: ${t.message}")
                    }
                })
            } catch (e: Exception) {
                Log.d("ERROR GET Question", "$e")
                Log.d("Token Catch GET Question", token)
            }
        }
    }
    fun getComplaintCategory2(token: String, category_id: Int, limit: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = currentUserRepository.getComplaintAllHistory(token, category_id, limit)
                response.enqueue(object : Callback<HistoryReportResponse> {
                    override fun onResponse(
                        call: Call<HistoryReportResponse>,
                        response: Response<HistoryReportResponse>
                    ) {
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            val record = responseBody?.data
                            _data2.value = record
                            _isLoading.value = false
                        } else {
                            _isLoading.value = false
                            Log.e("ERROR ON FAILURE", "onFailure: ${response.message()}")
                        }

                    }

                    override fun onFailure(call: Call<HistoryReportResponse>, t: Throwable) {
                        _isLoading.value = false
                        Log.e("ERROR ON FAILURE", "onFailure: ${t.message}")
                    }
                })
            } catch (e: Exception) {
                Log.d("ERROR GET Question", "$e")
                Log.d("Token Catch GET Question", token)
            }
        }
    }

    fun getComplaintCategory3(token: String, category_id: Int, limit: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = currentUserRepository.getComplaintAllHistory(token, category_id, limit)
                response.enqueue(object : Callback<HistoryReportResponse> {
                    override fun onResponse(
                        call: Call<HistoryReportResponse>,
                        response: Response<HistoryReportResponse>
                    ) {
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            val record = responseBody?.data
                            _data3.value = record
                            _isLoading.value = false
                        } else {
                            _isLoading.value = false
                            Log.e("ERROR ON FAILURE", "onFailure: ${response.message()}")
                        }

                    }

                    override fun onFailure(call: Call<HistoryReportResponse>, t: Throwable) {
                        _isLoading.value = false
                        Log.e("ERROR ON FAILURE", "onFailure: ${t.message}")
                    }
                })
            } catch (e: Exception) {
                Log.d("ERROR GET Question", "$e")
                Log.d("Token Catch GET Question", token)
            }
        }
    }

    fun getQuestion(token: String, id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = currentUserRepository.getQuestion(token, id)
                response.enqueue(object : Callback<QuestionChoiceResponse> {
                    override fun onResponse(
                        call: Call<QuestionChoiceResponse>,
                        response: Response<QuestionChoiceResponse>
                    ) {
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            val record = responseBody?.data
                            _question.value = record
                            Log.d("Question", "$record")
                            _isLoading.value = false
                        } else {
                            _isLoading.value = false
                            Log.e("ERROR ON FAILURE", "onFailure: ${response.message()}")
                        }

                    }

                    override fun onFailure(call: Call<QuestionChoiceResponse>, t: Throwable) {
                        _isLoading.value = false
                        Log.e("ERROR ON FAILURE", "onFailure: ${t.message}")
                    }
                })
            } catch (e: Exception) {
                Log.d("ERROR GET Question", "$e")
                Log.d("Token Catch GET Question", token)
            }
        }
    }

    fun submitReport(
        token: String,
        categoryId: Int,
        perpetrator: RequestBody,
        victim: RequestBody,
        incident_date: RequestBody,
        description: RequestBody,
        file: MultipartBody.Part,
        answer: RequestBody
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = currentUserRepository.submitComplaint(
                    token,
                    categoryId,
                    perpetrator,
                    victim,
                    incident_date,
                    description,
                    file,
                    answer
                )
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        val record = responseBody?.data
                        _general.value = record
                        Log.d("Question", "$record")
                        _isLoading.value = false
                    } else {
                        _isLoading.value = false
                        Log.e("ERROR ON FAILURE", "onFailure: ${response.message()}")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.d("ERROR Viewmodel", "$e")
                    Log.d("Token Catch GET Question", token)
                }
            }
        }
    }
}