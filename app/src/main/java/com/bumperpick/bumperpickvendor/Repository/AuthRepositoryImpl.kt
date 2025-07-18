package com.bumperpick.bumperpickvendor.Repository

import DataStoreManager
import android.util.Log
import com.bumperpick.bumperpickvendor.API.FinalModel.VendorLoginModel
import com.bumperpick.bumperpickvendor.API.FinalModel.error_model
import com.bumperpick.bumperpickvendor.API.Model.success_model
import com.bumperpick.bumperpickvendor.API.Provider.ApiResult
import com.bumperpick.bumperpickvendor.API.Provider.ApiService
import com.bumperpick.bumperpickvendor.API.Provider.safeApiCall
import com.google.gson.Gson

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import java.util.UUID
class AuthRepositoryImpl(
    private val dataStoreManager: DataStoreManager,
    private val apiService: ApiService
) : AuthRepository {

    override suspend fun checkAlreadyLogin(): Result<Boolean> {
        return try {
            val token = dataStoreManager.getToken()?.token
            Log.d("SplashToken", token.orEmpty())
            Result.Success(!token.isNullOrEmpty())
        } catch (e: Exception) {
            Result.Error("Failed to check login status", e)
        }
    }

    override suspend fun sendOtp(mobileNumber: String): Result<String> {
        return try {
            val sanitizedNumber = mobileNumber.replace(" ", "")
            val response = safeApiCall(
                api = { apiService.vendor_send_otp(sanitizedNumber) },
                errorBodyParser = { json ->
                    try {
                        Gson().fromJson(json, error_model::class.java)
                    } catch (e: Exception) {
                        error_model(message = "Unknown error format: $json")
                    }
                }
            )

            Log.d("SendOTP", sanitizedNumber)

            when (response) {
                is ApiResult.Success -> {
                    if (response.data.code == 200) {
                        Result.Success(response.data.message)
                    } else {
                        Result.Error(response.data.message)
                    }
                }
                is ApiResult.Error -> Result.Error(response.error.message)
            }
        } catch (e: Exception) {
            Result.Error("Failed to send OTP", e)
        }
    }

    override suspend fun verifyOtp(mobileNumber: String, otp: String): Result<Pair<Boolean, Boolean>> {
        return try {
            val sanitizedNumber = mobileNumber.replace(" ", "")
            val result = safeApiCall(
                api = { apiService.vendor_verify_otp(sanitizedNumber, otp) },
                errorBodyParser = { json ->
                    try {
                        Gson().fromJson(json, error_model::class.java)
                    } catch (e: Exception) {
                        error_model(message = "Unknown error format: $json")
                    }
                }
            )

            when (result) {
                is ApiResult.Success -> {
                    val data = result.data
                    val isRegistered = data.is_registered == 1
                    val isSuccessful = data.code == 200

                    dataStoreManager.save_Vendor_Details(data.data)

                    if (isRegistered) {
                        dataStoreManager.saveToken(data.meta)
                    }

                    Result.Success(Pair(isRegistered, isSuccessful))
                }
                is ApiResult.Error -> Result.Error(result.error.message)
            }
        } catch (e: Exception) {
            Result.Error("OTP verification failed", e)
        }
    }

    override suspend fun resendOtp(mobileNumber: String): Result<Boolean> {
        return try {
            val sanitizedNumber = mobileNumber.replace(" ", "")
            val response = safeApiCall(
                api = { apiService.vendor_re_send_otp(sanitizedNumber) },
                errorBodyParser = { json ->
                    try {
                        Gson().fromJson(json, error_model::class.java)
                    } catch (e: Exception) {
                        error_model(message = "Unknown error format: $json")
                    }
                }
            )

            when (response) {
                is ApiResult.Success -> {
                    if (response.data.code == 200) Result.Success(true)
                    else Result.Error(response.data.message)
                }
                is ApiResult.Error -> Result.Error(response.error.message)
            }
        } catch (e: Exception) {
            Result.Error("Failed to resend OTP", e)
        }
    }
}