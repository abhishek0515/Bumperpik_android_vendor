package com.bumperpick.bumperpick_Vendor.Screens.NotificationScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumperpick.bumperpick_Vendor.API.FinalModel.Notification_model
import com.bumperpick.bumperpick_Vendor.Repository.offerRepository
import com.bumperpick.bumperpick_Vendor.Screens.QrScreen.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class NotificationViewmodel(val offerRepository: offerRepository): ViewModel() {
private val _notifiacation= MutableStateFlow<UiState<Notification_model>>(UiState.Empty)
    val notification: MutableStateFlow<UiState<Notification_model>> =_notifiacation

    fun fetchNotifcation(){
        viewModelScope.launch {
            val result=offerRepository.notification()
            _notifiacation.value=when(result){
                is com.bumperpick.bumperpick_Vendor.Repository.Result.Error -> UiState.Error(result.message)
                com.bumperpick.bumperpick_Vendor.Repository.Result.Loading -> UiState.Loading
                is com.bumperpick.bumperpick_Vendor.Repository.Result.Success -> UiState.Success(result.data)
            }
        }
    }

}
