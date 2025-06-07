package com.bumperpick.bumperpickvendor.Screens.Account

import DataStoreManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumperpick.bumperpickvendor.Repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AccountViewmodel(val dataStoreManager: DataStoreManager,val authRepository: AuthRepository) :ViewModel(){
private val _isLogout =MutableStateFlow(false)
val logout=_isLogout.asStateFlow()
    fun logout(){
        viewModelScope.launch {
            dataStoreManager.clearToken()
            _isLogout.value=true
        }

    }
}