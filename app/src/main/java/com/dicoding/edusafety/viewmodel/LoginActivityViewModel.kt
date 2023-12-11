package com.dicoding.edusafety.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.edusafety.repository.UserRegistLoginRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivityViewModel(private val userRegistLoginRepository: UserRegistLoginRepository) :
    ViewModel() {

    private val _validLogin = MutableLiveData<Boolean?>()
    val validLogin: MutableLiveData<Boolean?> = _validLogin

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _token = MutableLiveData<String?>()
    val token: LiveData<String?> = _token

    fun login(email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = userRegistLoginRepository.loginUser(email, password)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        _isLoading.value = false
                        val loginResult = response.body()
                        val token = loginResult?.token
                        if (!token.isNullOrBlank()) {
                            _token.value = token
                            Log.d("TOKEN ViewModel", _token.value.toString())
                        }
                        _validLogin.value = true
                    } else {
                        _validLogin.value = false
                    }
                }
            } catch (e: Exception) {
                _validLogin.value = false
                Log.d("ERROR VIEWMODEL API", e.toString())
            }
        }
    }

    fun resetRegisterResponse() {
        _validLogin.value = null
    }
}