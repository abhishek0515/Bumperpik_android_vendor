package com.bumperpick.bumperpick_Vendor.API.FinalModel

data class QrModel(
    val code: Int,
    val customer: Customer,
    val message: String,
    val offer: Offer
)