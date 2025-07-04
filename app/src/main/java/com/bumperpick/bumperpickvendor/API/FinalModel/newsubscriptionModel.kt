package com.bumperpick.bumperpickvendor.API.FinalModel

import com.bumperpick.bumperpickvendor.Screens.Subscription.SubscriptionPlansResponse

data class newsubscriptionModel(
    val code: Int,
    val `data`: SubscriptionPlansResponse,
    val message: String
)