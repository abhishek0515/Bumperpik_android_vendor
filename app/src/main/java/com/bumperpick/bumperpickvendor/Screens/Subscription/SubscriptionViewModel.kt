package com.bumperpick.bumperpickvendor.Screens.Subscription

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumperpick.bumperpickvendor.API.FinalModel.newsubscriptionModel
import com.bumperpick.bumperpickvendor.API.FinalModel.select_subs_model
import com.bumperpick.bumperpickvendor.Repository.BillingCycle
import com.bumperpick.bumperpickvendor.Repository.Result
import com.bumperpick.bumperpickvendor.Repository.VendorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SubscriptionViewModel(private val vendorRepository: VendorRepository) : ViewModel() {

    // UI State sealed class for better state management
    sealed class UiState<out T> {
        object Loading : UiState<Nothing>()
        data class Success<T>(val data: T) : UiState<T>()
        data class Error(val message: String) : UiState<Nothing>()
        object Idle : UiState<Nothing>()
    }

    // Subscription state
    private val _subscriptionState = MutableStateFlow<UiState<newsubscriptionModel>>(UiState.Loading)
    val subscriptionState = _subscriptionState.asStateFlow()

    // Buy subscription state
    private val _buySubscriptionState = MutableStateFlow<UiState<select_subs_model>>(UiState.Idle)
    val buySubscriptionState = _buySubscriptionState.asStateFlow()

    // Selected subscription ID
    private val _selectedSubsId = MutableStateFlow("")
    val selectedSubsId = _selectedSubsId.asStateFlow()

    init {
        fetchSubscription()
    }

    fun fetchSubscription() {
        viewModelScope.launch {
            _subscriptionState.value = UiState.Loading
            try {
                val result = vendorRepository.fetchSubscription()
                _subscriptionState.value = when (result) {
                    is Result.Error -> UiState.Error(result.message)
                    Result.Loading -> UiState.Loading
                    is Result.Success -> UiState.Success(result.data)
                }
            } catch (e: Exception) {
                _subscriptionState.value = UiState.Error("Failed to fetch subscription: ${e.message}")
            }
        }
    }

    fun updateSelectedSubscription(subsId: String) {
        _selectedSubsId.value = subsId
        // Update the subscription state to reflect the selected subscription
        val currentSubscription = getCurrentSubscription()
        currentSubscription?.data?.let { plans ->
            val allPlans = plans.getAllPlans()
            val selectedPlan = allPlans.flatMap { it.second }.find { it.id.toString() == subsId }
            selectedPlan?.let {
                // Optionally, you can trigger additional logic here if needed
            }
        }
    }

    fun calculatePrice(basePrice: Int, cycle: BillingCycle): Int {
        return when (cycle) {
            BillingCycle.MONTHLY -> basePrice
            BillingCycle.QUARTERLY -> (basePrice * 3 * 0.85).toInt() // 15% discount
            BillingCycle.ANNUALLY -> (basePrice * 12 * 0.70).toInt() // 30% discount
        }
    }

    fun getCycleDuration(cycle: BillingCycle): String {
        return when (cycle) {
            BillingCycle.MONTHLY -> "1 month"
            BillingCycle.QUARTERLY -> "3 months"
            BillingCycle.ANNUALLY -> "12 months"
        }
    }

    fun getDiscountPercentage(cycle: BillingCycle): Int {
        return when (cycle) {
            BillingCycle.MONTHLY -> 0
            BillingCycle.QUARTERLY -> 15
            BillingCycle.ANNUALLY -> 30
        }
    }

    fun buySubscription(transactionId:String) {
        val currentSubsId = _selectedSubsId.value
        if (currentSubsId.isEmpty()) {
            _buySubscriptionState.value = UiState.Error("Please select a subscription")
            return
        }

        viewModelScope.launch {
            _buySubscriptionState.value = UiState.Loading
            try {
                val result = vendorRepository.selectSubsVendor(id = currentSubsId, transactionId = transactionId)
                _buySubscriptionState.value = when (result) {
                    is Result.Error -> UiState.Error(result.message)
                    Result.Loading -> UiState.Loading
                    is Result.Success -> UiState.Success(result.data)
                }
            } catch (e: Exception) {
                _buySubscriptionState.value = UiState.Error("Failed to buy subscription: ${e.message}")
            }
        }
    }

    fun clearBuySubscriptionState() {
        _buySubscriptionState.value = UiState.Idle
    }

    fun clearError() {
        when (val currentState = _subscriptionState.value) {
            is UiState.Error -> _subscriptionState.value = UiState.Loading
            else -> { /* No action needed */ }
        }

        when (val currentState = _buySubscriptionState.value) {
            is UiState.Error -> _buySubscriptionState.value = UiState.Idle
            else -> { /* No action needed */ }
        }
    }

    // Helper function to get current subscription data
    fun getCurrentSubscription(): newsubscriptionModel? {
        return when (val state = _subscriptionState.value) {
            is UiState.Success -> state.data
            else -> null
        }
    }

    // Helper function to check if loading
    fun isLoading(): Boolean {
        return _subscriptionState.value is UiState.Loading ||
                _buySubscriptionState.value is UiState.Loading
    }
}