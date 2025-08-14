package com.bumperpick.bumperpick_Vendor.API.FinalModel

data class refresh_token_data(
    val code: Int,
    val `data`: DataXXXXXXXXXXXXXXXXXXXX,
    val is_approved: Int,
    val is_registered: Int,
    val message: String,
    val meta: Meta
)