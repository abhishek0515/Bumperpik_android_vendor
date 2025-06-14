package com.bumperpick.bumperpickvendor.Screens.CreateOfferScreen

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumperpick.bumperpickvendor.Repository.HomeOffer
import com.bumperpick.bumperpickvendor.Repository.OfferModel
import com.bumperpick.bumperpickvendor.Repository.Result
import com.bumperpick.bumperpickvendor.Repository.VendorRepository
import com.bumperpick.bumperpickvendor.Repository.offerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class EditofferUiState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false,
    val successMessage: String? = null
)

class EditOfferViewmodel(
    private val offerRepository: offerRepository // You'll need to inject this
) : ViewModel() {

    // UI State
    private val _uiState = MutableStateFlow(EditofferUiState())
    val uiState: StateFlow<EditofferUiState> = _uiState.asStateFlow()

    // Offer Details
    private val _offerDetail = MutableStateFlow(HomeOffer())
    val offerDetail: StateFlow<HomeOffer> = _offerDetail.asStateFlow()

    // Media Management
    private val _newLocalMediaList = MutableStateFlow<ArrayList<Uri>>(arrayListOf())
    val newLocalMediaList: StateFlow<ArrayList<Uri>> = _newLocalMediaList.asStateFlow()

    private val _deletedUrlMediaList = MutableStateFlow<ArrayList<String>>(arrayListOf())
    val deletedUrlMediaList: StateFlow<ArrayList<String>> = _deletedUrlMediaList.asStateFlow()



    /**
     * Fetch offer details by ID
     */
    fun getOfferDetails(offerId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, isError = false)

            try {
                when (val offerDetails = offerRepository.getOfferDetails(offerId)) {
                    is Result.Error -> {
                        showError(offerDetails.message)
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isError = true,
                            errorMessage = offerDetails.message
                        )
                    }

                    is Result.Success -> {
                        _offerDetail.value = offerDetails.data?:HomeOffer()
                        _uiState.value = _uiState.value.copy(isLoading = false)
                    }

                    Result.Loading -> {
                        // Optional: can ignore or log, usually not emitted here
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isError = true,
                    errorMessage = e.message ?: "Failed to fetch offer details"
                )
            }
        }
    }

    /**
     * Update product name
     */
    fun updateProductname(name: String) {
        _offerDetail.value = _offerDetail.value.copy(offerTitle = name)
    }

    /**
     * Update product description
     */
    fun updateProductDescription(description: String) {
        _offerDetail.value = _offerDetail.value.copy(offerDescription = description)
    }

    /**
     * Update terms and conditions
     */
    fun updateTermsAndCondition(terms: String) {
        _offerDetail.value = _offerDetail.value.copy(termsAndCondition = terms)
    }

    /**
     * Update offer start date
     */
    fun updateStartDate(startDate: String) {
        _offerDetail.value = _offerDetail.value.copy(startDate = startDate)
    }

    /**
     * Update offer end date
     */
    fun updateEndDate(endDate: String) {
        _offerDetail.value = _offerDetail.value.copy(endDate = endDate)
    }

    /**
     * Update new local media list
     */
    fun updateNewLocalMediaList(mediaList: ArrayList<Uri>) {
        _newLocalMediaList.value = mediaList
    }

    /**
     * Add new media to local list
     */
    fun addNewLocalMedia(uri: Uri) {
        val currentList = _newLocalMediaList.value.toMutableList()
        currentList.add(uri)
        _newLocalMediaList.value = ArrayList(currentList)
    }

    /**
     * Remove media from local list
     */
    fun removeNewLocalMedia(uri: Uri) {
        val currentList = _newLocalMediaList.value.toMutableList()
        currentList.remove(uri)
        _newLocalMediaList.value = ArrayList(currentList)
    }

    /**
     * Update deleted URL media list
     */
    fun updateDeletedUrlMediaList(deletedList: ArrayList<String>) {
        _deletedUrlMediaList.value = deletedList
    }

    /**
     * Add URL to deleted list
     */
    fun addToDeletedUrlMedia(url: String) {
        val currentList = _deletedUrlMediaList.value.toMutableList()
        if (!currentList.contains(url)) {
            currentList.add(url)
            _deletedUrlMediaList.value = ArrayList(currentList)
        }
    }

    /**
     * Remove URL from deleted list
     */
    fun removeFromDeletedUrlMedia(url: String) {
        val currentList = _deletedUrlMediaList.value.toMutableList()
        currentList.remove(url)
        _deletedUrlMediaList.value = ArrayList(currentList)
    }

    /**
     * Update existing offer
     */
    fun updateOffer(
        offerId: String,
        deletedUrlMedia: ArrayList<String>,
        newLocalMedia: ArrayList<Uri>,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                Log.d("deletedUrlMedia",deletedUrlMedia.toString())
                Log.d("newLocalMedia",newLocalMedia.toString())
                _uiState.value = _uiState.value.copy(isLoading = true, isError = false)

                // Validate required fields
                val currentOffer = _offerDetail.value
                if (!validateOfferData(currentOffer)) {
                    return@launch
                }



                // Call repository to update offer
                val updatedOffer = offerRepository.updateOffer(currentOffer,deletedUrlMedia,newLocalMedia)
                _offerDetail.value = currentOffer
                when(updatedOffer){
                    is Result.Error ->  _uiState.value = _uiState.value.copy(isError = true, errorMessage = updatedOffer.message, isLoading = false)
                    Result.Loading -> _uiState.value = _uiState.value.copy(isLoading = true)
                    is Result.Success -> {
                        _uiState.value = _uiState.value.copy(isSuccess = true, successMessage = "Offer updated successfully", isLoading = false)
                        onSuccess()
                    }
                }



                // Reset media lists after successful update
                _newLocalMediaList.value = arrayListOf()
                _deletedUrlMediaList.value = arrayListOf()



            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isError = true,
                    errorMessage = e.message ?: "Failed to update offer"
                )
            }
        }
    }



    /**
     * Validate offer data before submission
     */
    private fun validateOfferData(offer: HomeOffer): Boolean {
        when {
            offer.offerTitle.isNullOrBlank() -> {
                showError("Product title is required")
                return false
            }
            offer.offerDescription.isNullOrBlank() -> {
                showError("Product description is required")
                return false
            }
            offer.termsAndCondition.isNullOrBlank() -> {
                showError("Terms and conditions are required")
                return false
            }
            offer.startDate.isNullOrBlank() -> {
                showError("Start date is required")
                return false
            }
            offer.endDate.isNullOrBlank() -> {
                showError("End date is required")
                return false
            }

        }
        return true
    }

    /**
     * Validate date range
     */


    /**
     * Show error message
     */
    fun showError(message: String) {
        _uiState.value = _uiState.value.copy(
            isError = true,
            errorMessage = message
        )
    }

    /**
     * Clear error state
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(
            isError = false,
            errorMessage = null
        )
    }

    /**
     * Clear success state
     */
    fun clearSuccess() {
        _uiState.value = _uiState.value.copy(
            isSuccess = false,
            successMessage = null
        )
    }


    /**
     * Get total media count (existing + new)
     */
    fun getTotalMediaCount(): Int {
        val existingCount = (_offerDetail.value.media?.size ?: 0) - _deletedUrlMediaList.value.size
        val newCount = _newLocalMediaList.value.size
        return maxOf(0, existingCount) + newCount
    }

    /**
     * Check if can add more media
     */
    fun canAddMoreMedia(maxCount: Int = 10): Boolean {
        return getTotalMediaCount() < maxCount
    }

    fun addMultipleLocalMedia(uris: List<Uri>) {

    }
}
