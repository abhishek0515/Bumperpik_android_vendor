package com.bumperpick.bumperpickvendor.API.Model

data class Data(
    val brand_name: String,
    val category: Category,
    val code: String,
    val created_at: String,
    val email: String,
    val establishment_address: String,
    val establishment_name: String,
    val gst_certificate_url: Any,
    val gst_number: Any,
    val id: Int,
    val message: String,
    val outlet_address: String,
    val phone_number: String
)