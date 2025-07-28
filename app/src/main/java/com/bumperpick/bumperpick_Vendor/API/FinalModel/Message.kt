package com.bumperpick.bumperpick_Vendor.API.FinalModel

data class Message(
    val author: Author,
    val body: String,

    val created_at: String,
    val id: Int
)