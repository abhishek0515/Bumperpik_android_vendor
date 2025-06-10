package com.bumperpick.bumperpickvendor.Repository

import DataStoreManager
import android.util.Log
import com.bumperpick.bumperpickvendor.API.Provider.ApiResult
import com.bumperpick.bumperpickvendor.API.Provider.ApiService
import com.bumperpick.bumperpickvendor.API.Provider.safeApiCall

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import java.util.UUID

class AuthRepositoryImpl(
    private val dataStoreManager: DataStoreManager,val apiService: ApiService
) : AuthRepository {

    override suspend fun checkAlreadyLogin(): Result<Boolean> {
        return try {
            val token=dataStoreManager.getToken()?.token
            Log.d("token splash",token.toString())
            Result.Success(!token.isNullOrEmpty())
        } catch (e: Exception) {
            Result.Error("Failed to check login status", e)
        }
    }



    override suspend fun sendOtp(mobileNumber: String): Result<String> {
        return try {
            val sendOtpResponse = safeApiCall {  apiService.vendor_send_otp(mobileNumber.replace(" ",""))}
            Log.d("Phone Number",mobileNumber)
            when(sendOtpResponse) {

                is ApiResult.Success -> {
                    if(sendOtpResponse.data.code==200) Result.Success(sendOtpResponse.data.message)
                    else Result.Error(sendOtpResponse.data.message)
                }

                is ApiResult.Error -> {
                    Result.Error(sendOtpResponse.message)
                }
            }

        } catch (e: Exception) {
            Result.Error("Failed to send OTP", e)
        }
    }

    override suspend fun verifyOtp(mobileNumber: String,otp: String): Result<Pair<Boolean,Boolean>> {
        return try {
            val verify_Otp= safeApiCall { apiService.vendor_verify_otp(mobileNumber.replace(" ",""),otp) }
            when(verify_Otp) {
                is ApiResult.Success->{
                    val pair=Pair(verify_Otp.data.is_registered==1,verify_Otp.data.code==200)
                    dataStoreManager.save_Vendor_Details(verify_Otp.data.data)
                    Log.d("VENDOR DETAILS",verify_Otp.data.data.toString())
                    if(verify_Otp.data.is_registered==1){
                        dataStoreManager.saveToken(verify_Otp.data.meta)


                    }
                    Result.Success(pair)

                }
                is ApiResult.Error-> Result.Error(verify_Otp.message)
            }
        } catch (e: Exception) {
            Result.Error("OTP verification failed", e)
        }
    }

    override suspend fun resendOtp(mobileNumber: String): Result<Boolean> {

        return try {
            val resendOtpResponse = safeApiCall { apiService.vendor_re_send_otp(mobileNumber.replace(" ","")) }
            when(resendOtpResponse) {
                is ApiResult.Success -> {
                    if(resendOtpResponse.data.code==200) Result.Success(true)
                    else Result.Error(resendOtpResponse.data.message)
                }
                is ApiResult.Error -> {
                    Result.Error(resendOtpResponse.message)
                }
            }
        } catch (e: Exception) {
            Result.Error("Failed to resend OTP", e)

        }
    }


}
