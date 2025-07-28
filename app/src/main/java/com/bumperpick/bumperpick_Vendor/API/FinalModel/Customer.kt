package com.bumperpick.bumperpick_Vendor.API.FinalModel

data class Customer(
    val customer_id: Int,
    val name: String?="",
    val email: Any,
    val phone_number: String
)