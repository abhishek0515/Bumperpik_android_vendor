package com.bumperpick.bumperpick_Vendor.Screens.Login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumperpick.bumperpick_Vendor.Repository.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewmodel(val authRepository: AuthRepository,val googleSignInRepository: GoogleSignInRepository):ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()
    init {
        _uiState.value=LoginUiState()
    }
    // Validation
    private val phoneRegex = Regex("^\\+91 [0-9]{10}$")

    /**
     * Updates phone number with proper formatting
     * @param input Raw input from the user
     */
    fun updatePhoneNumber(input: String) {
        val formattedNumber = (input)
        if(input.length<=14) {
            _uiState.update { currentState ->

                currentState.copy(
                    phoneNumber = formattedNumber,
                    isPhoneValid = validatePhoneNumber(formattedNumber)
                )
            }
        }
    }

    /**
     * Properly formats a phone number to ensure it follows the +91 format
     * @param input Raw phone number input
     * @return Formatted phone number
     */
    private fun formatPhoneInput(input: String): String {
        val prefix = "+91 "

        // If input doesn't start with prefix, ensure we add it
        val withPrefix = if (!input.startsWith(prefix)) {
            prefix + input.filter { it.isDigit() }
        } else {
            // Otherwise just keep the prefix and any digits that follow
            prefix + input.removePrefix(prefix).filter { it.isDigit() }
        }

        // Limit to 10 digits after the prefix
        return prefix + withPrefix.removePrefix(prefix).take(10)
    }

    /**
     * Validates phone number format
     * @param phoneNumber Number to validate
     * @return true if valid, false otherwise
     */
    private fun validatePhoneNumber(phoneNumber: String): Boolean {
        return phoneNumber.matches(phoneRegex)
    }

    /**
     * Toggles acceptance of terms and conditions
     */
    fun toggleTermsAccepted() {
        _uiState.update { currentState ->
            currentState.copy(termsAccepted = !currentState.termsAccepted)
        }
    }

    /**
     * Initiates OTP sending process with validation
     */
    fun sendOtp() {
        val currentState = _uiState.value

        if (!currentState.isPhoneValid) {
            _uiState.update { it.copy(error = "Please enter a valid 10-digit phone number") }
            return
        }

        if (!currentState.termsAccepted) {
            _uiState.update { it.copy(error = "Please accept the terms and conditions") }
            return
        }

        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, error = "") }

                val result = authRepository.sendOtp(currentState.phoneNumber)

                when (result) {
                    is Result.Success -> {
                        _uiState.update { it.copy(
                            isOtpSent = true,
                            message = result.data,
                            isLoading = false
                        )}
                    }
                    is Result.Error -> {
                        _uiState.update { it.copy(
                            error = result.message,
                            isLoading = false
                        )}
                    }

                    Result.Loading -> {
                        _uiState.update { it.copy(isLoading = true, error = "", isOtpSent = false) }

                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    error = e.message ?: "An unexpected error occurred",
                    isLoading = false
                )}
            }
        }
    }


    /**
     * Clears any displayed error message
     */
    fun clearError() {
        _uiState.update { it.copy(error = "") }
    }

    fun updateState() {
        _uiState.value=LoginUiState()
    }

    /**
     * Data class representing the UI state for login screen
     */
    data class LoginUiState(
        val message:String="",
        val phoneNumber: String = "",
        val isPhoneValid: Boolean = false,
        val termsAccepted: Boolean = false,
        val isOtpSent: Boolean = false,
        val isLoading: Boolean = false,
        val error: String = ""
    )
}