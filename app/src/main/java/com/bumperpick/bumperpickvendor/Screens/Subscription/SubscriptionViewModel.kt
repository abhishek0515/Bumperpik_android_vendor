package com.bumperpick.bumperpickvendor.Screens.Subscription

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.bumperpick.bumperpickvendor.Repository.BillingCycle
import com.bumperpick.bumperpickvendor.Repository.Feature
import com.bumperpick.bumperpickvendor.Repository.FeatureType
import com.bumperpick.bumperpickvendor.Repository.Plan
import com.bumperpick.bumperpickvendor.Repository.VendorRepository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SubscriptionViewModel(val vendorRepository: VendorRepository) : ViewModel() {
    private val _plans = MutableStateFlow(
        listOf(
            Plan(
                name = "Platinum",
                basePrice = 575,
                gradientColors = listOf(Color(0xFFD9D8D6), Color(0xFFE5E4E2),Color(0xFFF1F0ED),Color(0xFFEDEBE5)),
                features = listOf(
                    Feature("Logo on home screen", FeatureType.INCLUDED),
                    Feature("Offer booking facility for customers with payment", FeatureType.INCLUDED),
                    Feature( "Reminder to customers", FeatureType.INCLUDED),
                    Feature("Monthly viewers/redemption data with analysis", FeatureType.INCLUDED),
                    Feature("Instant offer with flash message", FeatureType.UNLIMITED),
                    Feature("Offer upload per quarter", FeatureType.UNLIMITED),
                    Feature("Scratch and win contest", FeatureType.UNLIMITED),
                    Feature("Lucky draw", FeatureType.UNLIMITED),
                    Feature("Contests", FeatureType.LIMITED, "5"),
                    Feature("Upload/LIVE telecast", FeatureType.LIMITED, "5")
                )
            ),
            Plan(
                name = "Golden",
                basePrice = 375,
                gradientColors = listOf(Color(0xFFF9F295), Color(0xFFE0AA3E),Color(0xFFFAF398), Color(0xFFB88A44)),
                features = listOf(
                    Feature("Logo on home screen", FeatureType.INCLUDED),
                    Feature("Offer booking facility for customers with payment", FeatureType.INCLUDED),
                    Feature("Reminder to customers", FeatureType.INCLUDED),
                    Feature("Monthly viewers/redemption data with analysis", FeatureType.INCLUDED),
                    Feature("Instant offer with flash message", FeatureType.UNLIMITED),
                    Feature("Offer upload per quarter", FeatureType.UNLIMITED),
                    Feature("Scratch and win contest", FeatureType.LIMITED, "10"),
                    Feature("Lucky draw", FeatureType.LIMITED, "10"),
                    Feature("Contests", FeatureType.LIMITED, "3"),
                    Feature("Upload/LIVE telecast", FeatureType.LIMITED, "3")
                )
            ),
            Plan(
                name = "Silver",
                basePrice = 175,
                gradientColors = listOf(Color(0xFFD4D5D7), Color(0xFFBCBCBC),Color(0xFFE7E7E7),Color(0xFF777779)),
                features = listOf(
                    Feature("Logo on home screen", FeatureType.INCLUDED),
                    Feature("Offer booking facility for customers with payment", FeatureType.INCLUDED),
                    Feature("Reminder to customers", FeatureType.INCLUDED),
                    Feature("Monthly viewers/redemption data with analysis", FeatureType.INCLUDED),
                    Feature("Instant offer with flash message", FeatureType.LIMITED, "50"),
                    Feature("Offer upload per quarter", FeatureType.LIMITED, "20"),
                    Feature("Scratch and win contest", FeatureType.LIMITED, "5"),
                    Feature("Lucky draw", FeatureType.LIMITED, "5"),
                    Feature("Contests", FeatureType.LIMITED, "2"),
                    Feature("Upload/LIVE telecast", FeatureType.LIMITED, "2")
                )
            )
        )
    )
    val plans = _plans.asStateFlow()

    private val _selectedBillingCycle = MutableStateFlow(BillingCycle.MONTHLY)
    val selectedBillingCycle = _selectedBillingCycle.asStateFlow()

    fun updateBillingCycle(cycle: BillingCycle) {
        _selectedBillingCycle.value = cycle
    }

    fun calculatePrice(basePrice: Int, cycle: BillingCycle): Int {
        return when (cycle) {
            BillingCycle.MONTHLY -> basePrice
            BillingCycle.QUARTERLY -> (basePrice * 3 * 0.85).toInt() // 15% discount
            BillingCycle.ANNUALLY -> (basePrice * 12 * 0.7).toInt() // 30% discount
        }
    }

    fun getCycleDuration(cycle: BillingCycle): String {
        return when (cycle) {
            BillingCycle.MONTHLY -> "1 month"
            BillingCycle.QUARTERLY -> "3 months"
            BillingCycle.ANNUALLY -> "12 months"
        }
    }
}
