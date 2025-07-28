package com.bumperpick.bumperpick_Vendor.API.FinalModel

data class NewData(
    val brand_name: String,
    val category: CategoryX,
    val created_at: String,
    val email: String,
    val establishment_address: String,
    val establishment_name: String,
    val gst_certificate_url: Any,
    val gst_number: String,
    val outlet_address: String,
    val phone_number: String,
    val subscription: SubscriptionXXX,
    val vendor_id: Int
)