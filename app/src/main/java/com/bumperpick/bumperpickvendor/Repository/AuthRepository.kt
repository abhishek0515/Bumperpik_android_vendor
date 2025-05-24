package com.bumperpick.bumperpickvendor.Repository

interface AuthRepository {
    suspend fun checkAlreadyLogin(): Result<Boolean>
    suspend fun sendOtp(mobileNumber: String): Result<String>
    suspend fun verifyOtp(mobileNumber: String,otp: String): Result<Pair<Boolean,Boolean>>
    suspend fun resendOtp(mobileNumber: String): Result<Boolean>

}
