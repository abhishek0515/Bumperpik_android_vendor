package com.bumperpick.bumperpickvendor.Screens.Splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumperpick.bumperpickvendor.Navigation.Screen
import com.bumperpick.bumperpickvendor.Repository.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
 sealed class SplashState {
     object Loading : SplashState()
     data class Success(val screen: Screen) : SplashState()
     data class Error(val message: String) : SplashState()
 }

class SplashViewmodel (val authRepository: AuthRepository):ViewModel(){

    private val _loginCheckState = MutableStateFlow<SplashState>(SplashState.Loading)
    val loginCheckState: StateFlow<SplashState> = _loginCheckState
    init {
        checkAlreadyLoggedIn()
    }
    private fun checkAlreadyLoggedIn() {
        viewModelScope.launch {
            delay(5)
            when (val result = authRepository.checkAlreadyLogin()) {
                is Result.Success -> {
                    var gotoscreen:Screen=Screen.Login
                    if(result.data){
                            gotoscreen= Screen.HomePage
                        }
                    _loginCheckState.value = SplashState.Success(gotoscreen)
                }
                is Result.Error -> _loginCheckState.value = SplashState.Error(result.message)
                Result.Loading -> _loginCheckState.value = SplashState.Loading
            }

        }
    }

}