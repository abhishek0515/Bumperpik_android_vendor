package com.bumperpick.bumperpickvendor.API.FinalModel

data class VendorLoginModel(
    val code: Int,
    val `data`: Data,
    val is_approved: Int,
    val is_registered: Int,
    val message: String,
    val meta: Meta
)