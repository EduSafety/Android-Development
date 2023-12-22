package com.dicoding.edusafety.viewmodel

import android.text.Editable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.edusafety.data.api.response.CurrentUserResponseNew
import com.dicoding.edusafety.data.api.response.DataLeaderboard
import com.dicoding.edusafety.data.api.response.DataNew
import com.dicoding.edusafety.data.api.response.HistoryReportResponse
import com.dicoding.edusafety.data.api.response.LeaderboardResponse
import com.dicoding.edusafety.data.api.response.RecordReportItem
import com.dicoding.edusafety.data.pref.UserModel
import com.dicoding.edusafety.repository.CurrentUserRepository
import com.dicoding.edusafety.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val repository: UserRepository) : ViewModel() {
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun getTokenUser(): LiveData<String?> {
        return repository.getTokenUser().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

}

class MainViewModelApi(private val currentUserRepository: CurrentUserRepository) : ViewModel() {

    private val _currentUser = MutableLiveData<DataNew?>()
    val currentUser: MutableLiveData<DataNew?> = _currentUser

    private val _leaderBoard = MutableLiveData<List<DataLeaderboard?>?>()
    val leaderBoard: MutableLiveData<List<DataLeaderboard?>?> = _leaderBoard

    private val _recordReport = MutableLiveData<List<RecordReportItem?>?>()
    val recordReport: MutableLiveData<List<RecordReportItem?>?> = _recordReport

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getCurrentUser(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = currentUserRepository.currentUser(token)
                response.enqueue(object : Callback<CurrentUserResponseNew> {
                    override fun onResponse(
                        call: Call<CurrentUserResponseNew>,
                        response: Response<CurrentUserResponseNew>
                    ) {
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            _currentUser.value = responseBody?.data
                            _isLoading.value = false
                        } else {
                            _isLoading.value = false
                            Log.e("ERROR ON FAILURE", "onFailure: ${response.message()}")
                        }

                    }

                    override fun onFailure(call: Call<CurrentUserResponseNew>, t: Throwable) {
                        _isLoading.value = false
                        Log.e("ERROR ON FAILURE", "onFailure: ${t.message}")
                    }
                })
            } catch (e: Exception) {
                Log.d("ERROR GET CURRENT USER", "$e")
                Log.d("Token Catch Current User", token)
            }
        }
    }

    fun getLeaderboard(token: String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = currentUserRepository.getLeaderboard(token)
                response.enqueue(object : Callback<LeaderboardResponse> {
                    override fun onResponse(
                        call: Call<LeaderboardResponse>,
                        response: Response<LeaderboardResponse>
                    ) {
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            val data = responseBody?.data
                            _leaderBoard.value = data
                            Log.d("DATA LEADERBOARD", "$data")
                            _isLoading.value = false
                        } else {
                            _isLoading.value = false
                            Log.e("ERROR ON FAILURE", "onFailure: ${response.message()}")
                        }

                    }

                    override fun onFailure(call: Call<LeaderboardResponse>, t: Throwable) {
                        _isLoading.value = false
                        Log.e("ERROR ON FAILURE", "onFailure: ${t.message}")
                    }
                })
            } catch (e: Exception) {
                Log.d("ERROR GET CURRENT USER", "$e")
                Log.d("Token Catch Current User", token)
            }
        }
    }

    fun getAllRecentReport(token: String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = currentUserRepository.getAllReportHistory(token)
                response.enqueue(object : Callback<HistoryReportResponse> {
                    override fun onResponse(
                        call: Call<HistoryReportResponse>,
                        response: Response<HistoryReportResponse>
                    ) {
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            val record = responseBody?.data?.record
                            _recordReport.value = record
                            Log.d("RECORD", "$record")
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
                Log.d("ERROR GET CURRENT USER", "$e")
                Log.d("Token Catch Current User", token)
            }
        }
    }

    fun getRecentReport(token: String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = currentUserRepository.getReportHistory(token)
                response.enqueue(object : Callback<HistoryReportResponse> {
                    override fun onResponse(
                        call: Call<HistoryReportResponse>,
                        response: Response<HistoryReportResponse>
                    ) {
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            val record = responseBody?.data?.record
                            _recordReport.value = record
                            Log.d("RECORD", "$record")
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
                Log.d("ERROR GET CURRENT USER", "$e")
                Log.d("Token Catch Current User", token)
            }
        }
    }

    fun updateCurrentUser(token: String, fullname: String, accessCode: String, phone: Editable){
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = currentUserRepository.editProfileUser(token, fullname, accessCode, phone)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        _isLoading.value = false
                        val updateResult = response.body()?.message
                        if(updateResult == "success"){
                            Log.d("SUKSES UPDATE", "$updateResult")
                        } else {
                            Log.d("GAGAL UPDATE", "$updateResult")
                        }
                    } else {
                        Log.d("GAGAL UPDATE", "TIDAK ADA KONEKSI INTERNET")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.d("ERROR VIEWMODEL API", e.toString())
                }
            }
        }
    }
}
