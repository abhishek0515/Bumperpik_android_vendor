package com.bumperpick.bumperpick_Vendor.Screens.Account

import DataStoreManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumperpick.bumperpick_Vendor.API.FinalModel.vendor_details_model
import com.bumperpick.bumperpick_Vendor.API.Model.success_model
import com.bumperpick.bumperpick_Vendor.Repository.GoogleSignInRepository
import com.bumperpick.bumperpick_Vendor.Repository.HomeOffer
import com.bumperpick.bumperpick_Vendor.Repository.OfferRepositoryImpl
import com.bumperpick.bumperpick_Vendor.Repository.Result
import com.bumperpick.bumperpick_Vendor.Repository.VendorRepository
import com.bumperpick.bumperpick_Vendor.Repository.offerRepository
import com.bumperpick.bumperpick_Vendor.Screens.QrScreen.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AccountUi_state{
    data class Error(val message: String):AccountUi_state()
    object Loading:AccountUi_state()
    object Empty:AccountUi_state()
    data class GetProfile(val vendorDetail:vendor_details_model):AccountUi_state()
}

class AccountViewmodel(
    private val dataStoreManager: DataStoreManager,
    private val VendorRepository: VendorRepository,
    private val OfferRepository: offerRepository,
    private val GoogleSignInRepository: GoogleSignInRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<AccountUi_state>(AccountUi_state.Empty)
    val uiState:StateFlow<AccountUi_state> =_uiState


    private val _offer_fetch_uiState= MutableStateFlow<UiState<List<HomeOffer>>>(UiState.Empty)
    val offer_fetch_uistate: StateFlow<UiState<List<HomeOffer>>> =_offer_fetch_uiState


    private val _send_remineder_uiState= MutableStateFlow<UiState<success_model>>(UiState.Empty)
    val send_remineder_uistate: StateFlow<UiState<success_model>> =_send_remineder_uiState

    fun sendReminder(offer_id:String) {
        viewModelScope.launch {
            _send_remineder_uiState.value = UiState.Loading
            val result = OfferRepository.send_reminder(offer_id)

            _send_remineder_uiState.value = when (result) {
                is Result.Error -> UiState.Error(result.message)
                Result.Loading -> UiState.Loading
                is Result.Success -> UiState.Success(result.data)
            }

        }
    }

    fun fetchOffer(){
        viewModelScope.launch {
            _offer_fetch_uiState.value= UiState.Loading

            val result=OfferRepository.GetOffers()
          _offer_fetch_uiState.value=  when(result){
                is Result.Error -> UiState.Error(result.message)
                Result.Loading -> UiState.Loading
                is Result.Success -> UiState.Success(result.data)
            }
        }
    }

    private val _isLogout = MutableStateFlow(false)
    val isLogout: StateFlow<Boolean> = _isLogout

    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreManager.clearToken()
            GoogleSignInRepository.signOut()
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

    fun setReminderEmpty() {
   viewModelScope.launch {
       _send_remineder_uiState.value=UiState.Empty
   }
    }

}
