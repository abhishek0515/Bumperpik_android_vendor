package com.bumperpick.bumperpickvendor.API.FinalModel

data class QrModel(
    val code: Int,
    val customer: Customer,
    val message: String,
    val offer: Offer
)