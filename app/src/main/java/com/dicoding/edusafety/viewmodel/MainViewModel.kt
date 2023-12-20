package com.dicoding.edusafety.viewmodel

import android.text.Editable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.edusafety.data.api.response.CurrentUserResponse
import com.dicoding.edusafety.data.api.response.Data
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

    private val _currentUser = MutableLiveData<Data?>()
    val currentUser: MutableLiveData<Data?> = _currentUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getCurrentUser(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = currentUserRepository.currentUser(token)
                response.enqueue(object : Callback<CurrentUserResponse> {
                    override fun onResponse(
                        call: Call<CurrentUserResponse>,
                        response: Response<CurrentUserResponse>
                    ) {
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            val fullname = responseBody?.data?.fullname
                            _currentUser.value = responseBody?.data
                            Log.d("FULLNAME", "$fullname")
                            _isLoading.value = false
                        } else {
                            _isLoading.value = false
                            Log.e("ERROR ON FAILURE", "onFailure: ${response.message()}")
                        }

                    }

                    override fun onFailure(call: Call<CurrentUserResponse>, t: Throwable) {
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
                val response = currentUserRepository.currentUser(token)
                response.enqueue(object : Callback<CurrentUserResponse> {
                    override fun onResponse(
                        call: Call<CurrentUserResponse>,
                        response: Response<CurrentUserResponse>
                    ) {
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            val fullname = responseBody?.data?.fullname
                            _currentUser.value = responseBody?.data
                            Log.d("FULLNAME", "$fullname")
                            _isLoading.value = false
                        } else {
                            _isLoading.value = false
                            Log.e("ERROR ON FAILURE", "onFailure: ${response.message()}")
                        }

                    }

                    override fun onFailure(call: Call<CurrentUserResponse>, t: Throwable) {
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