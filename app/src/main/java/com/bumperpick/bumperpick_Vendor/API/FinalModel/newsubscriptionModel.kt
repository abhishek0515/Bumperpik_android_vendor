package com.bumperpick.bumperpick_Vendor.API.FinalModel

import com.bumperpick.bumperpick_Vendor.Screens.Subscription.SubscriptionPlansResponse

data class newsubscriptionModel(
    val code: Int,
    val `data`: SubscriptionPlansResponse,
    val message: String
)