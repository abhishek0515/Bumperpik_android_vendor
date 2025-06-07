package com.bumperpick.bumperpickvendor.Repository

import DataStoreManager
import android.content.Context
import android.content.Intent
import android.util.Log
import com.bumperpick.bumperpickvendor.API.FinalModel.Data
import com.bumperpick.bumperpickvendor.API.Provider.ApiResult
import com.bumperpick.bumperpickvendor.API.Provider.ApiService
import com.bumperpick.bumperpickvendor.API.Provider.safeApiCall
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.log


class GoogleSignInRepository(private val context: Context,val apiService: ApiService,val DataStoreManager:DataStoreManager) {
    private val _signInState = MutableStateFlow<GoogleSignInState>(GoogleSignInState.Idle)
    val signInState: StateFlow<GoogleSignInState> = _signInState.asStateFlow()

    private fun getGoogleSignInClient(serverClientId: String): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(serverClientId)
            .requestEmail() // Explicitly request email
            .requestProfile()
            .build()
        return GoogleSignIn.getClient(context, gso)
    }

    fun getSignInIntent(serverClientId: String): Intent {
        return getGoogleSignInClient(serverClientId).signInIntent
    }

    suspend fun processSignInResult(data: Intent?) {

         try {
            _signInState.value = GoogleSignInState.Loading

            if (data == null) {
                throw Exception("Sign-in intent data is null")
            }

            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)



            Log.d("GoogleSignIn", "Sign-in successful: email=${account.email}")
            val login= safeApiCall { apiService.auth_google(account.email?:"") }

            when(login){
                is ApiResult.Error -> {
                  _signInState.value=GoogleSignInState.Error(login.message)
                }
                is ApiResult.Success -> {
                    if(login.data.code in 200..299){
                        val meta=login.data.meta
                        val vendor_data=login.data.data

                        if(login.data.is_registered==1){
                            DataStoreManager.saveToken(meta)
                            DataStoreManager.save_Vendor_Details(vendor_data)
                            _signInState.value = GoogleSignInState.Success(vendor_data.email,true)
                        }
                        else{
                            _signInState.value = GoogleSignInState.Success(account.email?:"",false)

                        }

                    }
                    else{
                        _signInState.value = GoogleSignInState.Error(login.data.message)

                    }

                }
            }




        } catch (e: ApiException) {
            Log.e("GoogleSignIn", "Sign-in failed: ${e.message}, statusCode: ${e.statusCode}")
            _signInState.value = GoogleSignInState.Error("Sign-in failed: ${e.message}")

        } catch (e: Exception) {
            Log.e("GoogleSignIn", "Unknown error: ${e.message}")
            _signInState.value = GoogleSignInState.Error("Unknown error: ${e.message}")

        }
    }

    fun signOut() {
        Log.d("GoogleSignIn", "Signing out")
        val googleSignInClient = getGoogleSignInClient("")
        googleSignInClient.signOut()
        _signInState.value = GoogleSignInState.Idle
    }

    fun clearError() {
        if (_signInState.value is GoogleSignInState.Error) {
            Log.d("GoogleSignIn", "Clearing error state")
            _signInState.value = GoogleSignInState.Idle
        }
    }
}