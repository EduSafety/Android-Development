package com.dicoding.edusafety.viewmodel

import android.text.Editable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.edusafety.data.api.response.RegisterResponse
import com.dicoding.edusafety.repository.UserRegistLoginRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterViewModel(private val userRegistLoginRepository: UserRegistLoginRepository) :
    ViewModel() {
    private val _registerResponse = MutableLiveData<RegisterResponse>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _validRegist = MutableLiveData<Boolean?>()
    val validRegist: MutableLiveData<Boolean?> = _validRegist

    fun register(name: String, email: String, phone: Editable?, password: String) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = userRegistLoginRepository.registerUser(name, email, phone, password)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        _isLoading.value = false
                        _validRegist.value = true
                        _registerResponse.value = response.body()
                    } else {
                        _validRegist.value = false
                    }
                }
            } catch (e: Exception) {
                Log.d("Error Register", e.toString())
            }
        }
    }

    fun resetRegisterResponse() {
        _validRegist.value = null
    }
}
