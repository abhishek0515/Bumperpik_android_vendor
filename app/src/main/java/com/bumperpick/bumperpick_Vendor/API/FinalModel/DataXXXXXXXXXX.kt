package com.bumperpick.bumperpick_Vendor.API.FinalModel

data class DataXXXXXXXXXX(
    val address: String? = "",
    val approval: String? = "", // kept nullable unless you know the type
    val banner_image_url: String? = "",
    val description: String? = "",
    val end_date: String? = "",
    val end_time: String? = "",
    val expire: Boolean = false,
    val facebook_link: String? = "",
    val id: Int = 0,
    val start_date: String? = "",
    val start_time: String? = "",
    val status: String? = "",
    val title: String? = "",
    val vendor_id: Int? = 0,
    val youtube_link: String?= "",
    val instagram_link:String?= ""
)
