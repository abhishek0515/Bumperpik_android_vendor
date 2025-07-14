package com.bumperpick.bumperpickvendor.API.FinalModel

data class DashboardStats(
    val active_ads_count: Int,
    val active_campaigns_count: Int,
    val active_events_count: Int,
    val active_offers_count: Int,
    val availed_offers_count: Int,
    val expired_ads_count: Int,
    val expired_campaigns_count: Int,
    val expired_events_count: Int,
    val expired_offers_count: Int
)