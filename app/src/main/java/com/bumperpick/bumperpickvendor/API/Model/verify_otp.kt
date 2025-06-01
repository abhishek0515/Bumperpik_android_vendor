package com.bumperpick.bumperpickvendor.API.Model

data class verify_otp(
    val code: Int,
    val `data`: DataXXXXX,
    val message: String,
    val is_registered: Int,
    val is_approved:Int,
    val meta: MetaX
)