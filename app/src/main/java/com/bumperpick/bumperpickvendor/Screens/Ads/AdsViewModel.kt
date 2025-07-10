package com.bumperpick.bumperpickvendor.Screens.Ads

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil3.Uri
import com.bumperpick.bumperpickvendor.API.FinalModel.AdsDetailModel
import com.bumperpick.bumperpickvendor.API.FinalModel.ads_package_model
import com.bumperpick.bumperpickvendor.API.FinalModel.subs_ads_model
import com.bumperpick.bumperpickvendor.API.FinalModel.vendorAdsModel
import com.bumperpick.bumperpickvendor.API.Model.success_model
import com.bumperpick.bumperpickvendor.API.Provider.toMultipartPart
import com.bumperpick.bumperpickvendor.Repository.AdsRepository
import com.bumperpick.bumperpickvendor.Repository.Result
import com.bumperpick.bumperpickvendor.Repository.uriToFile
import com.bumperpick.bumperpickvendor.Screens.QrScreen.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AdsViewModel(val adsRepository: AdsRepository) : ViewModel() {

    // State for ads package
    private val _adsSubsUiState = MutableStateFlow<UiState<ads_package_model>>(UiState.Empty)
    val adsSubsUiState = _adsSubsUiState.asStateFlow()

    // State for subscription
    private val _subscribePackageUiState = MutableStateFlow<UiState<subs_ads_model>>(UiState.Empty)
    val subscribePackageUiState = _subscribePackageUiState.asStateFlow()

    // State for create ads
    private val _createAdsUiState = MutableStateFlow<UiState<success_model>>(UiState.Empty)
    val createAdsUiState = _createAdsUiState.asStateFlow()

    // State for vendor ads
    private val _vendorAdsUiState = MutableStateFlow<UiState<vendorAdsModel>>(UiState.Empty)
    val vendorAdsUiState = _vendorAdsUiState.asStateFlow()

    // State for vendor ads detail
    private val _vendorAdsDetailUiState = MutableStateFlow<UiState<AdsDetailModel>>(UiState.Empty)
    val vendorAdsDetailUiState = _vendorAdsDetailUiState.asStateFlow()

    // State for update vendor ads
    private val _updateVendorAdsUiState = MutableStateFlow<UiState<success_model>>(UiState.Empty)
    val updateVendorAdsUiState = _updateVendorAdsUiState.asStateFlow()

    // State for delete ads
    private val _deleteAdsUiState = MutableStateFlow<UiState<success_model>>(UiState.Empty)
    val deleteAdsUiState = _deleteAdsUiState.asStateFlow()

    // Fields to track ad updates
    private var updatedBannerUri: android.net.Uri? = null
    private var updatedStartDate: String = ""
    private var updatedEndDate: String = ""

    fun getAdsSub() {
        viewModelScope.launch {
            _adsSubsUiState.value = UiState.Loading
            try {
                val result = adsRepository.adsPackage()
                _adsSubsUiState.value = when (result) {
                    is Result.Error -> UiState.Error(result.message)
                    Result.Loading -> UiState.Loading
                    is Result.Success -> UiState.Success(result.data)
                }
            } catch (e: Exception) {
                _adsSubsUiState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun subscribePackage( adsSubscriptionId: String, paymentTransactionId: String) {
        viewModelScope.launch {
            _subscribePackageUiState.value = UiState.Loading
            try {
                val result = adsRepository.subscribePackage( adsSubscriptionId, paymentTransactionId)
                _subscribePackageUiState.value = when (result) {
                    is Result.Error -> UiState.Error(result.message)
                    Result.Loading -> UiState.Loading
                    is Result.Success -> UiState.Success(result.data)
                }
            } catch (e: Exception) {
                _subscribePackageUiState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun createAds(data: Map<String, RequestBody>, banner: MultipartBody.Part) {
        viewModelScope.launch {
            _createAdsUiState.value = UiState.Loading
            try {
                val result = adsRepository.createAds(data, banner)
                _createAdsUiState.value = when (result) {
                    is Result.Error -> UiState.Error(result.message)
                    Result.Loading -> UiState.Loading
                    is Result.Success -> UiState.Success(result.data)
                }
            } catch (e: Exception) {
                _createAdsUiState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun getVendorAds() {
        viewModelScope.launch {
            _vendorAdsUiState.value = UiState.Loading
            try {
                val result = adsRepository.vendorAds()
                _vendorAdsUiState.value = when (result) {
                    is Result.Error -> UiState.Error(result.message)
                    Result.Loading -> UiState.Loading
                    is Result.Success -> UiState.Success(result.data)
                }
            } catch (e: Exception) {
                _vendorAdsUiState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun getVendorAdsDetail( id: String) {
        Log.d("getVendorAdsDetail",id.toString())
        viewModelScope.launch {
            _vendorAdsDetailUiState.value = UiState.Loading
            try {
                val result = adsRepository.vendorAdsDetail( id)
                _vendorAdsDetailUiState.value = when (result) {
                    is Result.Error -> UiState.Error(result.message)
                    Result.Loading -> UiState.Loading
                    is Result.Success -> {
                        // Initialize update fields with existing data
                        val data = result.data.data
                        updatedStartDate = data.start_date
                        updatedEndDate = data.end_date
                        UiState.Success(result.data)
                    }
                }
            } catch (e: Exception) {
                _vendorAdsDetailUiState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun updateVendorAds( id: String, data: Map<String, RequestBody>, banner: MultipartBody.Part) {
        viewModelScope.launch {
            _updateVendorAdsUiState.value = UiState.Loading
            try {
                val result = adsRepository.updateVendorAds( id, data, banner)
                _updateVendorAdsUiState.value = when (result) {
                    is Result.Error -> UiState.Error(result.message)
                    Result.Loading -> UiState.Loading
                    is Result.Success -> UiState.Success(result.data)
                }
            } catch (e: Exception) {
                _updateVendorAdsUiState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun deleteAds( id: String) {
        viewModelScope.launch {
            _deleteAdsUiState.value = UiState.Loading
            try {
                val result = adsRepository.deleteAds(id)
                _deleteAdsUiState.value = when (result) {
                    is Result.Error -> UiState.Error(result.message)
                    Result.Loading -> UiState.Loading
                    is Result.Success ->{
                        getVendorAds()
                        if(result.data.code>=200 && result.data.code<=300){
                            UiState.Success(result.data)

                        }
                        else{
                            UiState.Error(result.data.message)
                        }

                    }
                }
            } catch (e: Exception) {
                _deleteAdsUiState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun updateBannerImage(uri: android.net.Uri) {
        updatedBannerUri = uri
    }

    fun updateStartDate(date: String) {
        updatedStartDate = date
    }

    fun updateEndDate(date: String) {
        updatedEndDate = date
    }

    fun validateAdDetails(isUpdate:Boolean): Boolean {
        return  if(isUpdate){
            if(updatedStartDate.isEmpty()|| updatedEndDate.isEmpty()) false
            else true
        }
        else
            if(updatedStartDate.isEmpty()|| updatedEndDate.isEmpty()||updatedBannerUri==null) false
            else true

    }
    fun createPartFromString(value: String): RequestBody =
        value.toRequestBody("text/plain".toMediaTypeOrNull())
    fun updateAd(id: Int,context: Context) {
        viewModelScope.launch {
            try {
                // Prepare the data map
                val dataMap = mutableMapOf<String, RequestBody>()

                // Add dates to the request
                dataMap["start_date"] =createPartFromString( updatedStartDate)
                dataMap["end_date"] =createPartFromString(  updatedEndDate)

                // Prepare banner image
                val bannerPart =if(updatedBannerUri!=null){
                    uriToFile(context,updatedBannerUri!!).toMultipartPart("banner")}
                else {
                    // Create empty multipart if no new image
                    MultipartBody.Part.createFormData("banner", "")
                }

                // Call the update API
                updateVendorAds(id.toString(), dataMap, bannerPart)

            } catch (e: Exception) {
                _updateVendorAdsUiState.value = UiState.Error(e.message ?: "Update failed")
            }
        }
    }

    fun resetUpdateState() {
        _updateVendorAdsUiState.value = UiState.Empty
    }

    // Getters for current update values
    fun getCurrentStartDate(): String = updatedStartDate
    fun getCurrentEndDate(): String = updatedEndDate
    fun getCurrentBannerUri(): android.net.Uri? = updatedBannerUri
    fun createAd(context:Context) {
        viewModelScope.launch {
            try {
                // Prepare the data map
                val dataMap = mutableMapOf<String, RequestBody>()

                // Add dates to the request
                dataMap["start_date"] = updatedStartDate.toRequestBody("text/plain".toMediaType())
                dataMap["end_date"] = updatedEndDate.toRequestBody("text/plain".toMediaType())

                // Prepare banner image
                val bannerPart =if(updatedBannerUri!=null){
                    uriToFile(context,updatedBannerUri!!).toMultipartPart("banner")}
                 else {
                    // Create empty multipart if no new image
                    MultipartBody.Part.createFormData("banner", "")
                }

                // Call the update API
                createAds( dataMap, bannerPart)

            } catch (e: Exception) {
                _updateVendorAdsUiState.value = UiState.Error(e.message ?: "Update failed")
            }
        }
    }
}