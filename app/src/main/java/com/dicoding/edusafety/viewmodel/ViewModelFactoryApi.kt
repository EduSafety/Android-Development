package com.dicoding.edusafety.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.edusafety.di.Injection
import com.dicoding.edusafety.repository.CurrentUserRepository

class ViewModelFactoryApi private constructor(private val currentUserRepository: CurrentUserRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModelApi::class.java)) {
            return MainViewModelApi(currentUserRepository) as T
        }
        if (modelClass.isAssignableFrom(ReportViewModel::class.java)) {
            return ReportViewModel(currentUserRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactoryApi? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactoryApi {
            if (INSTANCE == null) {
                synchronized(ViewModelFactoryApi::class.java) {
                    INSTANCE = ViewModelFactoryApi(Injection.provideCurrentUserRepository(context))
                }
            }
            return INSTANCE as ViewModelFactoryApi
        }
    }
    }
