package com.dicoding.edusafety.repository

import android.text.Editable
import com.dicoding.edusafety.data.api.response.CurrentUserResponse
import com.dicoding.edusafety.data.api.response.GeneralResponse
import com.dicoding.edusafety.data.api.response.HistoryReportResponse
import com.dicoding.edusafety.data.api.retrofit.ApiConfig
import com.dicoding.edusafety.data.api.retrofit.ApiService
import com.dicoding.edusafety.data.pref.UserPreference
import retrofit2.Call
import retrofit2.Response

class CurrentUserRepository private constructor(
    private val apiService: ApiService, private val pref: UserPreference
) {

    fun currentUser(token: String): Call<CurrentUserResponse> {
        val apiService = ApiConfig.getApiServiceToken(token)
        return apiService.getCurrentUser()
    }

//    fun getReportHistory(token: String): Call<HistoryReportResponse>{
//        val apiService = ApiConfig.getApiServiceToken(token)
//        return apiService.getComplaintHistory()
//    }
    suspend fun editProfileUser(token: String, fullname: String, accessCode: String, phone: Editable): Response<GeneralResponse>{
        val apiService = ApiConfig.getApiServiceToken(token)
        return apiService.updateUser(fullname,accessCode,phone)
    }
    companion object {
        @Volatile
        private var instance: CurrentUserRepository? = null

        fun getInstance(apiService: ApiService, pref: UserPreference): CurrentUserRepository {
            return instance ?: synchronized(this) {
                instance ?: CurrentUserRepository(apiService, pref).also { instance = it }
            }
        }
    }
}