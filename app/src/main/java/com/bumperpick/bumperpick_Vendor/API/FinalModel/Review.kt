package com.bumperpick.bumperpick_Vendor.API.FinalModel

data class Review(
    val customer_id: Int,
    val customer_name:String,
    val id: Int,
    val promotion_id: Int,
    val rating: Int,
    val review: Any
)