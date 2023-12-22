package com.dicoding.edusafety.data.api.retrofit

import android.text.Editable
import com.dicoding.edusafety.data.api.response.CategoryResponse
import com.dicoding.edusafety.data.api.response.CurrentUserResponse
import com.dicoding.edusafety.data.api.response.GeneralResponse
import com.dicoding.edusafety.data.api.response.HistoryComplaintResponse
import com.dicoding.edusafety.data.api.response.HistoryReportResponse
import com.dicoding.edusafety.data.api.response.LeaderboardResponse
import com.dicoding.edusafety.data.api.response.LoginResponse
import com.dicoding.edusafety.data.api.response.QuestionChoiceResponse
import com.dicoding.edusafety.data.api.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
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

    @FormUrlEncoded
    @POST("register/otp-verification")
    suspend fun verifyOtp(
        @Field("email") email: String,
        @Field("otp") otp: Int,
    ): Response<GeneralResponse>

    @FormUrlEncoded
    @POST("user/reset-password/otp-verification")
    suspend fun verifyOtpForgot(
        @Field("email") email: String,
        @Field("otp") otp: Int,
    ): Response<GeneralResponse>

    @FormUrlEncoded
    @POST("user/reset-password/request")
    suspend fun resendOtp(
        @Field("email") email: String,
    ): Response<GeneralResponse>

    @FormUrlEncoded
    @POST("user/reset-password")
    suspend fun resetPassword(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<GeneralResponse>

    @GET("user/current-user")
    fun getCurrentUser(): Call<CurrentUserResponse>

    @GET("/complaint/{id}")
    fun getHistoryComplaint(
        @Path("id") id: Int
    ): Call<HistoryComplaintResponse>
    @GET("/category")
    fun getCategory(): Call<CategoryResponse>

    @GET("complaint/history")
    fun getComplaintCategory(
        @Query("category_id") category_id: Int,
        @Query("limit") limit: Int
    ): Call<HistoryReportResponse>

    @GET("complaint/history")
    fun getComplaintAllHistory(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Call<HistoryReportResponse>

    @GET("complaint/leaderboard")
    fun getLeaderboard(): Call<LeaderboardResponse>

    @GET("complaint/question")
    fun getQuestion(
        @Query("category_id") id: Int
    ): Call<QuestionChoiceResponse>

    @FormUrlEncoded
    @PUT("user")
    suspend fun updateUser(
        @Field("fullname") name: String,
        @Field("access_code") accessCode: String,
        @Field("phone") phone: Editable?,
    ): Response<GeneralResponse>


    @Multipart
    @POST("complaint")
    suspend fun submitComplaint(
        @Part("category_id") category_id: Int,
        @Part("perpetrator") perpetrator: RequestBody,
        @Part("victim") victim: RequestBody,
        @Part("date") date: RequestBody,
        @Part("description") description: RequestBody,
        @Part file: MultipartBody.Part,
        @Part("answer") answer: RequestBody
    ): Response<GeneralResponse>
}