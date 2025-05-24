package com.bumperpick.bumperpickvendor.Repository

import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class GoogleSignInRepository(private val context: Context) {
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

    suspend fun processSignInResult(data: Intent?): kotlin.Result<GoogleUserData> {
        return try {
            _signInState.value = GoogleSignInState.Loading

            if (data == null) {
                throw Exception("Sign-in intent data is null")
            }

            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)

            val userData = GoogleUserData(
                userId = account.id ?: "",
                displayName = account.displayName,
                email = account.email, // Email is directly available
                profilePictureUrl = account.photoUrl?.toString(),
                idToken = account.idToken ?: ""
            )

            Log.d("GoogleSignIn", "Sign-in successful: email=${account.email}")

            _signInState.value = GoogleSignInState.Success(userData)
            kotlin.Result.success(userData)

        } catch (e: ApiException) {
            Log.e("GoogleSignIn", "Sign-in failed: ${e.message}, statusCode: ${e.statusCode}")
            _signInState.value = GoogleSignInState.Error("Sign-in failed: ${e.message}")
            kotlin.Result.failure(e)
        } catch (e: Exception) {
            Log.e("GoogleSignIn", "Unknown error: ${e.message}")
            _signInState.value = GoogleSignInState.Error("Unknown error: ${e.message}")
            kotlin.Result.failure(e)
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