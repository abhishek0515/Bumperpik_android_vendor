package com.bumperpick.bumperpickvendor.API.FinalModel

data class Data(
    val brand_name: String,
    val category: Category,
    val created_at: String,
    val email: String,
    val establishment_address: String,
    val establishment_name: String,
    val gst_certificate_url: Any,
    val gst_number: Any,
    val outlet_address: String,
    val phone_number: String,
    val subscription: Any,
    val vendor_id: Int
)