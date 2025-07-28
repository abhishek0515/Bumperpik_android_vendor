package com.bumperpick.bumperpick_Vendor.API.FinalModel

data class DataXXXXXXXX(
    val address: String,
    val approval: Any,
    val banner_image_url: String?="",
    val description: String,
    val end_date: String,
    val expire: Boolean,
    val id: Int,
    val start_date: String,
    val status: String,
    val title: String,
    val number_of_participant: String,
    val vendor_id: Int,
    val registrations: List<Registration>
) {

}