package com.bumperpick.bumperpickvendor.API.FinalModel

data class Author(
    val email: String,
    val id: Int,
    val image: String,
    val name: String?="",
    val type: String
)