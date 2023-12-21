package com.dicoding.edusafety.viewmodel

import android.text.Editable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.edusafety.data.api.response.GeneralResponse
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

    private val _validOtp = MutableLiveData<GeneralResponse?>()
    val valitOtp: MutableLiveData<GeneralResponse?> = _validOtp

    fun register(name: String, email: String, accessCode: String, phone: Editable?, password: String) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = userRegistLoginRepository.registerUser(name, email, accessCode, phone, password)
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
                withContext(Dispatchers.Main) {
                    _validRegist.value = false
                    Log.d("Error Register", e.toString())
                }
            }
        }
    }

    fun verifyOtp(email: String, otp: Int) {
        Log.d("CODE OTP VIEWMODEL","$otp + $email")
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = userRegistLoginRepository.verifyOtp(email,otp)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        _isLoading.value = false
                        _validOtp.value = response.body()
                    } else {
                        Log.d("Error OTP","CEK VIEWMODEL VERIFY OTP")
                        _isLoading.value = false
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _validRegist.value = false
                    Log.d("Error Register", e.toString())
                }
            }
        }
    }

    fun verifyOtpForgot(email: String, otp: Int) {
        Log.d("CODE OTP VIEWMODEL","$otp + $email")
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = userRegistLoginRepository.verifyOtpForgot(email,otp)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        _isLoading.value = false
                        _validOtp.value = response.body()
                    } else {
                        Log.d("Error OTP","CEK VIEWMODEL VERIFY OTP")
                        _isLoading.value = false
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _validRegist.value = false
                    Log.d("Error Register", e.toString())
                }
            }
        }
    }

    fun resetPassword(email: String, password: String) {
        Log.d("RESET PASSWORD VIEWMODEL","$email + $password")
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = userRegistLoginRepository.resetPassword(email,password)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        _isLoading.value = false
                        _validOtp.value = response.body()
                    } else {
                        Log.d("Error Reset","CEK VIEWMODEL RESET PASSWORD")
                        _isLoading.value = false
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _validRegist.value = false
                    Log.d("Error Register", e.toString())
                }
            }
        }
    }
    fun resendOtp(email: String) {
        Log.d("RESEND OTP VIEWMODEL FORGOT","$email")
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = userRegistLoginRepository.resendOtp(email)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        _isLoading.value = false
                        _validOtp.value = response.body()
                    } else {
                        Log.d("Error Get OTP","CEK VIEWMODEL RESEND OTP")
                        _isLoading.value = false
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _validRegist.value = false
                    Log.d("Error Register", e.toString())
                }
            }
        }
    }
    fun resetRegisterResponse() {
        _validRegist.value = null
    }
}
