package com.bumperpick.bumperpickvendor.API.FinalModel

data class Message(
    val author: Author,
    val body: String,

    val created_at: String,
    val id: Int
)