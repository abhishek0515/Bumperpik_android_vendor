package com.bumperpick.bumperpickvendor.Screens.CreateOfferScreen

import DataStoreManager
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
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody
import com.google.gson.Gson
import android.content.Context
import com.bumperpick.bumperpickvendor.API.FinalModel.Data
import com.bumperpick.bumperpickvendor.Repository.Vendor_Details
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream

import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

data class EditofferUiState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false,
    val successMessage: String? = null
)








class EditOfferViewmodel(
    private val offerRepository: offerRepository, // You'll need to inject this
    private val context: Context,
    private val dataStoreManager: DataStoreManager// Add context for file operations
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

    // HTTP client and JSON parser
    private val client = OkHttpClient()
    private val gson = Gson()

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
                        _offerDetail.value = offerDetails.data ?: HomeOffer()
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
     * Update existing offer - Updated with API call
     */
    fun updateOffer(
        offerId: String,
        deletedUrlMedia: ArrayList<String>,
        newLocalMedia: ArrayList<Uri>,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                Log.d("deletedUrlMedia", deletedUrlMedia.toString())
                Log.d("newLocalMedia", newLocalMedia.toString())

                _uiState.value = _uiState.value.copy(isLoading = true, isError = false)

                // Validate required fields
                val currentOffer = _offerDetail.value
                if (!validateOfferData(currentOffer)) {
                    return@launch
                }

                // Call repository to update offer
                val result = offerRepository.updateOffer(
                    offerModel = currentOffer,
                    deleted_image = deletedUrlMedia,
                    newLocalMedia = newLocalMedia
                )

                when (result) {
                    is Result.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isSuccess = true,
                            successMessage = "Offer updated successfully",
                            isLoading = false
                        )
                        getOfferDetails(currentOffer.offerId?:"")
                        // Reset media lists after successful update
                        _newLocalMediaList.value = arrayListOf()
                        _deletedUrlMediaList.value = arrayListOf()

                        onSuccess()
                    }
                    is Result.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isError = true,
                            errorMessage = result.message ?: "Failed to update offer",
                            isLoading = false
                        )
                    }

                    Result.Loading -> _uiState.value=_uiState.value.copy(isLoading = true)
                }
            } catch (e: Exception) {
                Log.e("UpdateOffer", "Exception in updateOffer: ${e.message}", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isError = true,
                    errorMessage = e.message ?: "Failed to update offer"
                )
            }
        }
    }

    /**
     * API call to update offer (manual multipart construction)
     */
    private suspend fun updateOfferAPI(
        offerId: String,
        offer: HomeOffer,
        deletedUrlMedia: ArrayList<String>,
        newLocalMedia: ArrayList<Uri>
    ): kotlin.Result<UpdateOfferData> = withContext(Dispatchers.IO) {

        try {
            Log.d("offer_Id",offerId)
            val urlString = "${AppConstants.BASE_URL}${AppConstants.UPDATE_OFFER_API}/$offerId"

            // Get user token
            val token = dataStoreManager.getToken()!!.token
            val tokenType =dataStoreManager.getToken()!!.token_type

            // Generate boundary (same format as iOS)
            val boundary = UUID.randomUUID().toString()
            val vendorDetails=dataStoreManager.get_Vendor_Details()
            // Manually construct multipart body
            val requestBodyData = buildMultipartBody(
                boundary = boundary,
               vendorDetails= vendorDetails!!,
                offer = offer,
                token = token,
                deletedUrlMedia = deletedUrlMedia,
                newLocalMedia = newLocalMedia
            )

            val requestBody = requestBodyData.toRequestBody("multipart/form-data; boundary=$boundary".toMediaType())

            // Create request (don't set Content-Type manually when using custom boundary)
            val request = Request.Builder()
                .url(urlString)
                .post(requestBody)
                .addHeader("Authorization", "$tokenType $token")
                .build()

            Log.d("UpdateOffer", "Making request to: $urlString")
            Log.d("UpdateOffer", "Boundary: $boundary")
            Log.d("UpdateOffer", "Body size: ${requestBodyData.size} bytes")

            // Execute request
            val response = client.newCall(request).execute()

            Log.d("UpdateOffer", "Response code: ${response.code}")

            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                Log.d("UpdateOffer", "Response body: $responseBody")

                if (responseBody != null) {
                    val offerUpdateResponse = gson.fromJson(responseBody, OfferUpdateResponse::class.java)
                    Log.d("UpdateOffer", "✅ Offer updated: $offerUpdateResponse")
                    kotlin.Result.success(offerUpdateResponse.data)
                } else {
                    Log.e("UpdateOffer", "❌ No data returned by server")
                    kotlin.Result.failure(Exception("No data returned by server"))
                }
            } else {
                Log.e("UpdateOffer", "❌ Server returned status code: ${response.code}")
                val errorBody = response.body?.string()
                Log.e("UpdateOffer", "Error body: $errorBody")
                kotlin.Result.failure(Exception("Server error: ${response.code} - $errorBody"))
            }

        } catch (e: Exception) {
            Log.e("UpdateOffer", "❌ API error: ${e.message}", e)
            kotlin.Result.failure(e)
        }
    }

    /**
     * Manually build multipart body (similar to iOS approach)
     */
    private fun buildMultipartBody(
        boundary: String,
        vendorDetails: Data,
        offer: HomeOffer,
        token: String,
        deletedUrlMedia: ArrayList<String>,
        newLocalMedia: ArrayList<Uri>
    ): ByteArray {
        val output = ByteArrayOutputStream()
        val writer = output.bufferedWriter()


        try {
            // Add form fields
            val fields = mapOf<String,String>(
                "vendor_id" to (vendorDetails.vendor_id.toString()),
                "offer_template" to "2",
                "image_appearance" to "green",
                "heading" to (offer.offerTitle ?: ""),
                "discount" to (offer.discount ?: ""),
                "brand_name" to (offer.offerTitle ?: ""),
                "title" to (offer.offerTitle ?: ""),
                "description" to (offer.offerDescription ?: ""),
                "terms" to (offer.termsAndCondition ?: ""),
                "start_date" to (offer.startDate ?: ""),
                "end_date" to (offer.endDate ?: ""),
                "token" to token
            )

            // Add text fields
            fields.forEach { (key, value) ->
                if (value.isNotEmpty()) {
                    writer.write("--$boundary\r\n")
                    writer.write("Content-Disposition: form-data; name=\"$key\"\r\n\r\n")
                    writer.write("$value\r\n")
                }
            }
            writer.flush()

            // Add media files
            newLocalMedia.forEachIndexed { index, uri ->
                try {
                    val inputStream = context.contentResolver.openInputStream(uri)
                    if (inputStream != null) {
                        val mimeType = context.contentResolver.getType(uri) ?: "application/octet-stream"
                        val isImage = mimeType.startsWith("image/")
                        val isVideo = mimeType.startsWith("video/")

                        val fileName = when {
                            isImage -> "media$index.jpg"
                            isVideo -> "video$index.mp4"
                            else -> "media$index"
                        }

                        val contentType = when {
                            isImage -> "image/jpeg"
                            isVideo -> "video/mp4"
                            else -> "application/octet-stream"
                        }

                        writer.write("--$boundary\r\n")
                        writer.write("Content-Disposition: form-data; name=\"media[]\"; filename=\"$fileName\"\r\n")
                        writer.write("Content-Type: $contentType\r\n\r\n")
                        writer.flush()

                        // Copy file data
                        inputStream.copyTo(output)
                        inputStream.close()

                        writer.write("\r\n")
                        writer.flush()

                        Log.d("UpdateOffer", "Added media file: $fileName")
                    }
                } catch (e: Exception) {
                    Log.e("UpdateOffer", "Failed to process media at index $index: ${e.message}")
                }
            }

            // Add deleted media IDs
            deletedUrlMedia.forEach { id ->
                if (id.isNotEmpty()) {
                    writer.write("--$boundary\r\n")
                    writer.write("Content-Disposition: form-data; name=\"delete_media_ids[]\"\r\n\r\n")
                    writer.write("$id\r\n")
                }
            }

            // Add final boundary
            writer.write("--$boundary--\r\n")
            writer.flush()
            writer.close()

        } catch (e: Exception) {
            Log.e("UpdateOffer", "Error building multipart body: ${e.message}")
        }

        return output.toByteArray()
    }

    /**
     * Convert Uri to File
     */
    private fun uriToFile(uri: Uri, fileName: String): File? {
        return try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val tempFile = File(context.cacheDir, fileName)
            val outputStream = FileOutputStream(tempFile)

            inputStream?.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
            tempFile
        } catch (e: Exception) {
            Log.e("UpdateOffer", "Error converting URI to file: ${e.message}")
            null
        }
    }






    /**
     * Check if file is an image
     */
    private fun isImageFile(file: File): Boolean {
        val mimeType = context.contentResolver.getType(Uri.fromFile(file))
        return mimeType?.startsWith("image/") == true
    }

    /**
     * Check if file is a video
     */
    private fun isVideoFile(file: File): Boolean {
        val mimeType = context.contentResolver.getType(Uri.fromFile(file))
        return mimeType?.startsWith("video/") == true
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

        }
        return true
    }

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
        // Implementation for adding multiple media items
    }
}

// Supporting data classes
data class OfferUpdateResponse(
    val success: Boolean,
    val message: String,
    val data: UpdateOfferData
)

data class UpdateOfferData(
    val id: String,
    val title: String,
    val description: String
    // Add other fields as needed
)

// Constants
object AppConstants {
    const val BASE_URL = "http://13.50.109.14/"
    const val UPDATE_OFFER_API = "api/vendor/offers-update"
}

// User session
object UserSession {
    fun getToken(): String? {
        // Return your stored token
        return "your_token_here"
    }

    fun getTokenType(): String {
        return "Bearer"
    }
}