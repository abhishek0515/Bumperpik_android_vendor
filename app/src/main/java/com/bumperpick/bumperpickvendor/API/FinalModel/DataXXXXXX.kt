package com.bumperpick.bumperpickvendor.API.FinalModel

import com.google.gson.annotations.SerializedName

data class DataXXXXXX(
    val brand_name: String,
    val category: String,
    val email: String,
    val establishment_address: String,
    val establishment_name: String,
    val gst_number: String?,
    val id: Int,
    val start_time:String?,
    val close_time:String?,
    val image_url: String,
    val name: String,
    val outlet_address: String,
    val phone_number: String,
    @SerializedName("subscription")
    val subscription: DataXXXX?=null,
    @SerializedName("adsSubscription")
    val adsSubscription:AdsSubscription?=null
)