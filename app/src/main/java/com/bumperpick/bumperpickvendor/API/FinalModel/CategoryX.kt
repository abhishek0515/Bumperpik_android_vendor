package com.bumperpick.bumperpickvendor.API.FinalModel

data class CategoryX(
    val created_at: String,
    val id: Int,
    val image: String,
    val name: String,
    val parent_id: Any,
    val slug: String,
    val subcategory: List<Subcategory>,
    val updated_at: String
)