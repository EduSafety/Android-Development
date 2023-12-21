package com.dicoding.edusafety.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.edusafety.data.pref.UserModel
import com.dicoding.edusafety.repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}