package com.bumperpick.bumperpickvendor.Screens.QrScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumperpick.bumperpickvendor.API.FinalModel.QrModel
import com.bumperpick.bumperpickvendor.Repository.AuthRepository
import com.bumperpick.bumperpickvendor.Repository.Result
import com.bumperpick.bumperpickvendor.Repository.offerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<out T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
    object Empty : UiState<Nothing>()
}
class QrScreenViewmodel(val offer_Repository: offerRepository,val authRepository: AuthRepository):ViewModel() {
    private val _uiState= MutableStateFlow<UiState<QrModel>>(UiState.Empty)
    val uiState: StateFlow<UiState<QrModel>> = _uiState

   private val _is_otpsend=MutableStateFlow<Boolean>(false)
    val is_otpsend:StateFlow<Boolean> = _is_otpsend
    private val _verify_otpsend=MutableStateFlow<Boolean>(false)
    val verify_otpsend:StateFlow<Boolean> = _verify_otpsend



    fun sendotp(customerPhone:String){
        viewModelScope.launch {
          val result=  authRepository.sendOtp(customerPhone)
            when(result){
                is Result.Error -> _uiState.value=UiState.Error(result.message)
                Result.Loading -> _uiState.value=UiState.Loading
                is Result.Success -> {
                    _is_otpsend.value=true
                }
            }
        }
    }

    fun verifyOtp(customerPhone:String,otp:String){
        viewModelScope.launch {
            val result=authRepository.verifyOtp(customerPhone,otp)
            when(result){
                is Result.Error -> _uiState.value=UiState.Error(result.message)
                Result.Loading -> _uiState.value=UiState.Loading
                is Result.Success -> {
                    _verify_otpsend.value=true
                }

            }            }
    }



    fun fetchOfferDetail( customerId:String, offerId:String){
        viewModelScope.launch {
            _uiState.value=UiState.Loading
            val result=offer_Repository.QrCodeData(customer_id = customerId, offer_id = offerId)

            Log.d("DATA","$customerId $offerId")
            when(result){
                is Result.Error -> {
                    Log.d("message",result.message)
                    _uiState.value=UiState.Error(result.message)}
                Result.Loading -> _uiState.value=UiState.Loading
                is Result.Success -> _uiState.value=UiState.Success(result.data)
            }

        }

    }

    private val __offer_redeemed=MutableStateFlow<Boolean>(false)
    val _offer_redeemed:StateFlow<Boolean> = __offer_redeemed


    fun redeem_offer(customerId:String,offerId:String){
        viewModelScope.launch {
            _uiState.value=UiState.Loading
            val result=offer_Repository.OfferRedeem(customerId,offerId)
            when(result){
                is Result.Error -> {
                    Log.d("message",result.message)
                    _uiState.value=UiState.Error(result.message)}

                Result.Loading -> _uiState.value=UiState.Loading
                is Result.Success -> {
                    val result=result.data
                    if(result.code>=200 && result.code<300){
                        __offer_redeemed.value=true
                    }
                    else{
                        _uiState.value=UiState.Error(result.message)
                    }
                }
            }
        }
    }







}