package com.bumperpick.bumperpick_Vendor.Repository

import DataStoreManager
import android.content.Context
import android.content.Intent
import android.util.Log
import com.bumperpick.bumperpick_Vendor.API.FinalModel.error_model
import com.bumperpick.bumperpick_Vendor.API.Provider.ApiResult
import com.bumperpick.bumperpick_Vendor.API.Provider.ApiService
import com.bumperpick.bumperpick_Vendor.API.Provider.safeApiCall
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GoogleSignInRepository(
    private val context: Context,
    private val apiService: ApiService,
    private val dataStoreManager: DataStoreManager
) {
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

            val login = safeApiCall(
                api = { apiService.auth_google(account.email ?: "") },
                errorBodyParser = { json ->
                    try {
                        Gson().fromJson(json, error_model::class.java)
                    } catch (e: Exception) {
                        error_model(message = "Unknown error format: $json")
                    }
                }
            )

            when (login) {
                is ApiResult.Error -> {
                    _signInState.value = GoogleSignInState.Error(login.error.message)
                }
                is ApiResult.Success -> {
                    if (login.data.code in 200..299) {
                        val meta = login.data.meta
                        val vendorData = login.data.data
                        FirebaseMessaging.getInstance().token.addOnCompleteListener {
                                task ->
                            if(task.isSuccessful){
                                CoroutineScope(Dispatchers.IO).launch {


                                    val send_fcm_token = safeApiCall(
                                        api = {
                                            apiService.send_token(
                                                token = meta.token,
                                                vendorId = vendorData.vendor_id.toString(),
                                                device_token = task.result
                                            )
                                        },
                                        errorBodyParser = { json ->
                                            try {
                                                Gson().fromJson(json, error_model::class.java)
                                            } catch (e: Exception) {
                                                error_model(message = "Unknown error format: $json")
                                            }
                                        }
                                    )

                                    when (send_fcm_token) {
                                        is ApiResult.Error -> {
                                            Log.d("Error_Fcm_token", send_fcm_token.error.message)
                                        }

                                        is ApiResult.Success -> {
                                            Log.d("Success_Fcm_token", send_fcm_token.data.message)
                                        }
                                    }
                                }
                            }
                        }


                        if (login.data.is_registered == 1) {
                            dataStoreManager.saveToken(meta)
                            dataStoreManager.save_Vendor_Details(vendorData)
                            _signInState.value = GoogleSignInState.Success(vendorData.email, true)
                        } else {
                            _signInState.value = GoogleSignInState.Success(account.email ?: "", false)
                        }
                    } else {
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

    fun signOut(serverClientId: String) {
        Log.d("GoogleSignIn", "Signing out")
        val googleSignInClient = getGoogleSignInClient(serverClientId)
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

// Sealed class for Google Sign-in states
