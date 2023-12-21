package com.dicoding.edusafety.repository

import android.text.Editable
import com.dicoding.edusafety.data.api.response.CategoryResponse
import com.dicoding.edusafety.data.api.response.CurrentUserResponse
import com.dicoding.edusafety.data.api.response.GeneralResponse
import com.dicoding.edusafety.data.api.response.HistoryComplaintResponse
import com.dicoding.edusafety.data.api.response.HistoryReportResponse
import com.dicoding.edusafety.data.api.response.LeaderboardResponse
import com.dicoding.edusafety.data.api.response.QuestionChoiceResponse
import com.dicoding.edusafety.data.api.retrofit.ApiConfig
import com.dicoding.edusafety.data.api.retrofit.ApiService
import com.dicoding.edusafety.data.pref.UserPreference
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response

class CurrentUserRepository private constructor(
    private val apiService: ApiService, private val pref: UserPreference
) {

    fun currentUser(token: String): Call<CurrentUserResponse> {
        val apiService = ApiConfig.getApiServiceToken(token)
        return apiService.getCurrentUser()
    }

    fun getReportHistory(token: String): Call<HistoryReportResponse>{
        val apiService = ApiConfig.getApiServiceToken(token)
        return apiService.getComplaintAllHistory(1,3)
    }

    fun getAllReportHistory(token: String): Call<HistoryReportResponse>{
        val apiService = ApiConfig.getApiServiceToken(token)
        return apiService.getComplaintAllHistory(1,15)
    }

    fun getLeaderboard(token: String): Call<LeaderboardResponse>{
        val apiService = ApiConfig.getApiServiceToken(token)
        return apiService.getLeaderboard()
    }

    fun getCategory(token: String): Call<CategoryResponse>{
        val apiService = ApiConfig.getApiServiceToken(token)
        return apiService.getCategory()
    }

    fun getComplaintAllHistory(token: String, category_id: Int, limit: Int): Call<HistoryReportResponse>{
        val apiService = ApiConfig.getApiServiceToken(token)
        return apiService.getComplaintCategory(category_id,limit)
    }

    fun getQuestion(token: String, id: Int): Call<QuestionChoiceResponse>{
        val apiService = ApiConfig.getApiServiceToken(token)
        return apiService.getQuestion(id)
    }

    fun getHistoryComplaint(token: String, id: Int): Call<HistoryComplaintResponse>{
        val apiService = ApiConfig.getApiServiceToken(token)
        return apiService.getHistoryComplaint(id)
    }
    suspend fun editProfileUser(token: String, fullname: String, accessCode: String, phone: Editable): Response<GeneralResponse>{
        val apiService = ApiConfig.getApiServiceToken(token)
        return apiService.updateUser(fullname,accessCode,phone)
    }

    suspend fun submitComplaint(token: String, categoryId: Int, perpetrator: RequestBody, victim: RequestBody, incident_date: RequestBody, description: RequestBody, file: MultipartBody.Part, answer: RequestBody) : Response<GeneralResponse> {
        val apiService = ApiConfig.getApiServiceToken(token)
        return apiService.submitComplaint(categoryId, perpetrator, victim, incident_date, description, file, answer)
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