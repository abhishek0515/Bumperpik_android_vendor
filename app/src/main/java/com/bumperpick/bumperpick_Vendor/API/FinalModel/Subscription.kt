package com.bumperpick.bumperpick_Vendor.API.FinalModel

data class Subscription(
    val features: List<FeatureX>,
    val id: Int,
    val name: String,
    val price: String,
    val time_period: String
)