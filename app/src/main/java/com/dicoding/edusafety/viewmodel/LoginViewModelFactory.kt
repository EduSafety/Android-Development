package com.dicoding.edusafety.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.edusafety.di.Injection
import com.dicoding.edusafety.repository.UserRegistLoginRepository

class LoginViewModelFactory private constructor(private val userRegistLoginRepository: UserRegistLoginRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(userRegistLoginRepository) as T
        }
        if (modelClass.isAssignableFrom(LoginActivityViewModel::class.java)) {
            return LoginActivityViewModel(userRegistLoginRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var INSTANCE: LoginViewModelFactory? = null

        @JvmStatic
        fun getInstance(): LoginViewModelFactory {
            if (INSTANCE == null) {
                synchronized(LoginViewModelFactory::class.java) {
                    INSTANCE = LoginViewModelFactory(Injection.provideRegistLoginRepository())
                }
            }
            return INSTANCE as LoginViewModelFactory
        }
    }
}