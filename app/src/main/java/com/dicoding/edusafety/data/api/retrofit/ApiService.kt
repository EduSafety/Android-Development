package com.dicoding.edusafety.data.api.retrofit

import android.text.Editable
import com.dicoding.edusafety.data.api.response.CurrentUserResponse
import com.dicoding.edusafety.data.api.response.GeneralResponse
import com.dicoding.edusafety.data.api.response.HistoryReportResponse
import com.dicoding.edusafety.data.api.response.LoginResponse
import com.dicoding.edusafety.data.api.response.RegisterResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<LoginResponse>

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("fullname") name: String,
        @Field("email") email: String,
        @Field("access_code") accessCode: String,
        @Field("phone") phone: Editable?,
        @Field("password") password: String
    ): Response<RegisterResponse>

    @GET("user/current-user")
    fun getCurrentUser(): Call<CurrentUserResponse>

    @GET("complaint/history")
    fun getComplaintAllHistory(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Call<HistoryReportResponse>
    @FormUrlEncoded
    @PUT("user")
    suspend fun updateUser(
        @Field("fullname") name: String,
        @Field("access_code") accessCode: String,
        @Field("phone") phone: Editable?,
    ): Response<GeneralResponse>
}