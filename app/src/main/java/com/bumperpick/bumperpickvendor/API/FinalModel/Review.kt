package com.bumperpick.bumperpickvendor.API.FinalModel

data class Review(
    val customer_id: Int,
    val id: Int,
    val promotion_id: Int,
    val rating: Int,
    val review: Any
)