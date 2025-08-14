package com.bumperpick.bumperpick_Vendor.API.FinalModel

data class CategoryXX(
    val created_at: String,
    val id: Int,
    val image: String,
    val name: String,
    val parent_id: Any,
    val slug: String,
    val subcategory: List<SubcategoryX>,
    val updated_at: String
)