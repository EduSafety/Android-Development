package com.dicoding.edusafety.data.api.retrofit

import android.text.Editable
import com.dicoding.edusafety.data.api.response.LoginResponse
import com.dicoding.edusafety.data.api.response.RegisterResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

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
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("phone") phone: Editable,
        @Field("password") password: String
    ): Response<RegisterResponse>
}