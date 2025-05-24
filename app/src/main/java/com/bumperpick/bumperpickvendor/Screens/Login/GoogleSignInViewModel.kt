package com.bumperpick.bumperpickvendor.Screens.Login

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumperpick.bumperpickvendor.Repository.GoogleSignInRepository
import com.bumperpick.bumperpickvendor.Repository.GoogleSignInState
import com.bumperpick.bumperpickvendor.Repository.GoogleUserData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class GoogleUiState(
    val isLoading: Boolean = false,
    val error: String = "",
    val userData: GoogleUserData? = null
)

class GoogleSignInViewModel(private val googleSignInRepository: GoogleSignInRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(GoogleUiState())
    val uiState: StateFlow<GoogleUiState> = _uiState.asStateFlow()

    val signInState: StateFlow<GoogleSignInState> = googleSignInRepository.signInState

    private val serverClientId = "157888938377-7of5gfcti98620ve4o930j2drar5mhe1.apps.googleusercontent.com"

    fun getSignInIntent(): Intent {
        return googleSignInRepository.getSignInIntent(serverClientId)
    }

    fun processSignInResult(data: Intent?) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = "") }
            val result = googleSignInRepository.processSignInResult(data)
            result.onSuccess { userData ->
                _uiState.update { it.copy(isLoading = false, userData = userData, error = "") }
            }.onFailure { exception ->
                _uiState.update { it.copy(isLoading = false, error = exception.message ?: "Google sign-in failed") }
            }
        }
    }

    fun signOut() {
        googleSignInRepository.signOut()
        _uiState.update { it.copy(userData = null, error = "", isLoading = false) }
    }

    fun clearError() {
        googleSignInRepository.clearError()
        _uiState.update { it.copy(error = "") }
    }
}