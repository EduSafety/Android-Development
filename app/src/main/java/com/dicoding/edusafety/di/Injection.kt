package com.dicoding.edusafety.di

import android.content.Context
import com.dicoding.edusafety.data.api.retrofit.ApiConfig
import com.dicoding.edusafety.data.pref.UserPreference
import com.dicoding.edusafety.data.pref.dataStore
import com.dicoding.edusafety.repository.CurrentUserRepository
import com.dicoding.edusafety.repository.UserRegistLoginRepository
import com.dicoding.edusafety.repository.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }

    fun provideRegistLoginRepository(): UserRegistLoginRepository {
        val apiService = ApiConfig.getApiService()
        return UserRegistLoginRepository.getInstance(apiService)
    }

    fun provideCurrentUserRepository(context: Context): CurrentUserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiServiceToken(user.token)
        return CurrentUserRepository.getInstance(apiService, pref)
    }
}