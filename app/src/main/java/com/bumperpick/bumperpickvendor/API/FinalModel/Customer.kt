package com.bumperpick.bumperpickvendor.API.FinalModel

data class Customer(
    val customer_id: Int,
    val customer_name: String?=null,
    val email: Any,
    val phone_number: String
)