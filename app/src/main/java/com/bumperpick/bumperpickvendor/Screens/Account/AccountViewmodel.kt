package com.bumperpick.bumperpickvendor.Screens.Account

import DataStoreManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumperpick.bumperpickvendor.API.FinalModel.vendor_details_model
import com.bumperpick.bumperpickvendor.Repository.AuthRepository
import com.bumperpick.bumperpickvendor.Repository.Result
import com.bumperpick.bumperpickvendor.Repository.VendorRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.logger.MESSAGE

sealed class AccountUi_state{
    data class Error(val message: String):AccountUi_state()
    object Loading:AccountUi_state()
    object Empty:AccountUi_state()
    data class GetProfile(val vendorDetail:vendor_details_model):AccountUi_state()
}

class AccountViewmodel(
    private val dataStoreManager: DataStoreManager,
    private val VendorRepository: VendorRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<AccountUi_state>(AccountUi_state.Empty)
    val uiState:StateFlow<AccountUi_state> =_uiState

    private val _isLogout = MutableStateFlow(false)
    val isLogout: StateFlow<Boolean> = _isLogout

    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreManager.clearToken()
            _isLogout.value = true
        }
    }

    fun fetchProfile(){
        viewModelScope.launch {
            _uiState.value=AccountUi_state.Loading
            val data=VendorRepository.getProfile()

            when(data){
                is Result.Error -> _uiState.value=AccountUi_state.Error(data.message)
                Result.Loading -> _uiState.value=AccountUi_state.Loading
                is Result.Success-> _uiState.value=AccountUi_state.GetProfile(data.data)
            }
        }
    }

}
