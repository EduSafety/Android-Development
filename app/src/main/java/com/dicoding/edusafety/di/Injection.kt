package com.dicoding.edusafety.di

import android.content.Context
import com.dicoding.edusafety.data.api.retrofit.ApiConfig
import com.dicoding.edusafety.data.pref.UserPreference
import com.dicoding.edusafety.data.pref.dataStore
import com.dicoding.edusafety.repository.UserRegistLoginRepository
import com.dicoding.edusafety.repository.UserRepository

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }

    fun provideRegistLoginRepository(): UserRegistLoginRepository {
        val apiService = ApiConfig.getApiService()
        return UserRegistLoginRepository.getInstance(apiService)
    }
}