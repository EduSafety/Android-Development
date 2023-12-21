package com.dicoding.edusafety.repository

import android.text.Editable
import com.dicoding.edusafety.data.api.response.GeneralResponse
import com.dicoding.edusafety.data.api.response.LoginResponse
import com.dicoding.edusafety.data.api.response.RegisterResponse
import com.dicoding.edusafety.data.api.retrofit.ApiConfig
import com.dicoding.edusafety.data.api.retrofit.ApiService
import retrofit2.Response

class UserRegistLoginRepository private constructor(
    private val apiService: ApiService
) {

    suspend fun registerUser(
        name: String,
        email: String,
        accessCode: String,
        phone: Editable?,
        password: String
    ): Response<RegisterResponse> {
        return apiService.register(name, email, accessCode, phone, password)
    }

    suspend fun verifyOtp(
        email: String,
        otp: Int
    ): Response<GeneralResponse>{
        return apiService.verifyOtp(email,otp)
    }

    suspend fun verifyOtpForgot(
        email: String,
        otp: Int
    ): Response<GeneralResponse>{
        return apiService.verifyOtpForgot(email,otp)
    }
    suspend fun loginUser(email: String, password: String): Response<LoginResponse> {
        return apiService.login(email, password)
    }

    suspend fun resendOtp(email: String): Response<GeneralResponse>{
        val apiService = ApiConfig.getApiService()
        return apiService.resendOtp(email)
    }

    suspend fun resetPassword(email: String, password: String): Response<GeneralResponse>{
        val apiService = ApiConfig.getApiService()
        return apiService.resetPassword(email, password)
    }

    companion object {
        @Volatile
        private var instance: UserRegistLoginRepository? = null

        fun getInstance(apiService: ApiService): UserRegistLoginRepository {
            return instance ?: synchronized(this) {
                instance ?: UserRegistLoginRepository(apiService).also { instance = it }
            }
        }
    }
}