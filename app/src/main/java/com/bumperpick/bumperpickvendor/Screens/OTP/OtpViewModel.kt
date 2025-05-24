package com.bumperpick.bumperpickvendor.Screens.OTP

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumperpick.bumperpickvendor.Navigation.Screen
import com.bumperpick.bumperpickvendor.Repository.AuthRepository
import com.bumperpick.bumperpickvendor.Repository.Result
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class OtpUiState(
    val otp: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val message: String? = null,


)
class OtpViewModel(val authRepository: AuthRepository):ViewModel() {
    private val _uiState = MutableStateFlow(OtpUiState())
    val uiState: StateFlow<OtpUiState> = _uiState

    private val _gotoScreen=MutableStateFlow<Screen?>(null)
    val gotoScreen:StateFlow<Screen?> = _gotoScreen

    fun onOtpChanged(newOtp: String) {
        _uiState.update { it.copy(otp = newOtp, error = null, message = null) }
    }

    fun verifyOtp(mobile: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, message = null) }
            val otpVerify =
                authRepository.verifyOtp(mobileNumber = mobile, otp = _uiState.value.otp)
            when (otpVerify) {
                is Result.Error -> _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = otpVerify.message
                    )
                }

                Result.Loading -> _uiState.update { it.copy(isLoading = true) }
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            message =  if (otpVerify.data.second) "OTP Verified Successfully" else "Invalid OTP"
                        )
                    }

                    _gotoScreen.value = if(otpVerify.data.first) Screen.HomePage else Screen.AddVendorDetails
                }
            }
        }
    }



    fun resendOtp(mobile: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, message = null) }
            val resend_otp=authRepository.resendOtp(mobile)
            when(resend_otp){
                is Result.Error -> _uiState.update { it.copy(isLoading = false, error = resend_otp.message)}
                Result.Loading ->_uiState.update { it.copy(isLoading = true) }
                is Result.Success -> _uiState.update { it.copy(isLoading = false, error = "OTP resent to $mobile") }
            }

        }
    }

    fun clearMessage() {
        _uiState.update { it.copy(message = null) }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }



}