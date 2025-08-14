package com.bumperpick.bumperpick_Vendor.Repository

interface AuthRepository {
    suspend fun checkAlreadyLogin(): Result<Boolean>
    suspend fun sendOtp(mobileNumber: String): Result<String>
    suspend fun verifyOtp(mobileNumber: String,otp: String): Result<Pair<Boolean,Boolean>>
    suspend fun resendOtp(mobileNumber: String): Result<Boolean>
    suspend fun refresh_token(token: String): Result<String>

}
