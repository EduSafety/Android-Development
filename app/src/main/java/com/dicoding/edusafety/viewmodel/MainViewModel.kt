package com.dicoding.edusafety.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.edusafety.data.pref.UserModel
import com.dicoding.edusafety.repository.UserRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository) : ViewModel() {
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }


    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

}