package com.bumperpick.bumperpickvendor.Repository

import DataStoreManager
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import com.bumperpick.bumperpickvendor.API.FinalModel.DataX
import com.bumperpick.bumperpickvendor.API.FinalModel.Faqmodel
import com.bumperpick.bumperpickvendor.API.FinalModel.OfferUpdateModel
import com.bumperpick.bumperpickvendor.API.FinalModel.QrModel
import com.bumperpick.bumperpickvendor.API.FinalModel.Subcategory
import com.bumperpick.bumperpickvendor.API.FinalModel.error_model
import com.bumperpick.bumperpickvendor.API.FinalModel.getOfferDetailsModel
import com.bumperpick.bumperpickvendor.API.FinalModel.offerRedeemModel
import com.bumperpick.bumperpickvendor.API.Model.success_model
import com.bumperpick.bumperpickvendor.API.Provider.ApiResult
import com.bumperpick.bumperpickvendor.API.Provider.ApiService
import com.bumperpick.bumperpickvendor.API.Provider.prepareImageParts
import com.bumperpick.bumperpickvendor.API.Provider.safeApiCall
import com.bumperpick.bumperpickvendor.API.Provider.toMultipartPart
import com.bumperpick.bumperpickvendor.Screens.Component.formatDate
import com.google.gson.Gson
import kotlinx.coroutines.flow.firstOrNull
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream

class OfferRepositoryImpl(
    private val apiService: ApiService,
    private val dataStoreManager: DataStoreManager,
    private val context: Context
) : offerRepository {

    override suspend fun AddOffer(offerModel: OfferModel): Result<success_model> {
        return try {
            val vendorDetails = dataStoreManager.get_Vendor_Details()
            val token = dataStoreManager.getToken()?.token
            val map = mutableMapOf<String, RequestBody>()

            if (vendorDetails != null) {
                Log.d("Offer_vendorId", vendorDetails.vendor_id.toString())
            }
            map["is_unlimited"]=(if(offerModel?.toogleStockLast==true)"1" else "0").toRequestBody("text/plain".toMediaType())
            map["vendor_id"] = vendorDetails?.vendor_id.toString().toRequestBody("text/plain".toMediaType())
            map["offer_template"] = offerModel.gradientType?.name.toString().toRequestBody("text/plain".toMediaType())
            map["heading"] = offerModel.heading?.text.toString().toRequestBody("text/plain".toMediaType())
            map["sub_heading"] = offerModel.subHeading?.text.toString().toRequestBody("text/plain".toMediaType())
            map["discount"] = offerModel.discount.text.toString().toRequestBody("text/plain".toMediaType())
            map["brand_name"] = offerModel.brandName?.text.toString().toRequestBody("text/plain".toMediaType())
            map["title"] = offerModel.productTittle.toString().toRequestBody("text/plain".toMediaType())
            map["description"] = offerModel.productDiscription.toString().toRequestBody("text/plain".toMediaType())
            map["terms"] = offerModel.termsAndCondition.toString().toRequestBody("text/plain".toMediaType())
            map["start_date"] = offerModel.offerStartDate.toString().toRequestBody("text/plain".toMediaType())
            if(!offerModel.offerEndDate.isNullOrEmpty()) map["end_date"] = offerModel.offerEndDate.toRequestBody("text/plain".toMediaType())
            map["sub_category_id"] = offerModel.subcat_id.toString().toRequestBody("text/plain".toMediaType())
            map["token"] = token.toString().toRequestBody("text/plain".toMediaType())

            val banner = uriToFile(context, offerModel.BannerImage!!)?.toMultipartPart(partName = "brand_logo")
            val mediaList = offerModel.medialList.map { uri: Uri ->
                uriToFile(context, uri)
            }
            val mediaListMulti = prepareImageParts(mediaList)

            mediaList.forEach {
                Log.d("URI", it.path.toString())
            }
            Log.d("Offer add map", map.toString())
            if (banner != null) {
                Log.d("Offer banner", offerModel.BannerImage!!.toString())
            }
            Log.d("Offer mediaList", mediaList.toString())

            val addOffer = safeApiCall(
                api = { apiService.addOffers(map, banner, mediaListMulti) },
                errorBodyParser = { json ->
                    try {
                        Gson().fromJson(json, error_model::class.java)
                    } catch (e: Exception) {
                        error_model(message = "Unknown error format: $json")
                    }
                }
            )

            when (addOffer) {
                is ApiResult.Error -> Result.Error(addOffer.error.message)
                is ApiResult.Success -> Result.Success(addOffer.data)
            }
        } catch (e: Exception) {
            Result.Error("Failed to add offer: ${e.message}")
        }
    }

     fun uriToFile(context: Context, uri: Uri): File {
        val inputStream = context.contentResolver.openInputStream(uri)
        val tempFile = File.createTempFile("upload_", ".png", context.cacheDir)
        if (inputStream != null) {
            tempFile.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        return tempFile
    }



    override suspend fun GetOffers(): Result<List<HomeOffer>> {
        return try {
            val token = dataStoreManager.getToken()?.token
            val result = safeApiCall(
                api = { apiService.homeOffers(token ?: "") },
                errorBodyParser = { json ->
                    try {
                        Gson().fromJson(json, error_model::class.java)
                    } catch (e: Exception) {
                        error_model(message = "Unknown error format: $json")
                    }
                }
            )

            when (result) {
                is ApiResult.Error -> Result.Error(result.error.message)
                is ApiResult.Success -> {
                    val res = result.data.data
                    val offerList: ArrayList<HomeOffer> = ArrayList()

                    res.forEach {
                        Log.d("Offer", it.toString())
                        offerList.add(
                            HomeOffer(
                                offerId = it.id.toString(),
                                Type = MarketingOption.OFFERS,
                                offerValid = if (!it.expire) OfferValidation.Valid else OfferValidation.Expired,
                                Media_list = it.media.map { mediaItem ->
                                    mediaItem.url
                                },
                                media = it.media,
                                brand_logo_url = it.brand_logo_url,
                                approval = it.approval,
                                discount = it.discount,
                                startDate = it.start_date,
                                endDate = it.end_date,
                                active = if (it.status == "inactive") "in-active" else "active",
                                offerTitle = it.title,
                                offerTag = it.quantity.toString() + " left",
                                offerDescription = it.description,
                                termsAndCondition = it.terms,
                            )
                        )
                    }
                    Result.Success(offerList)
                }
            }
        } catch (e: Exception) {
            Result.Error("Failed to get offers: ${e.message}")
        }
    }

    override suspend fun getOfferDetails(id: String): Result<HomeOffer?> {
        return when (val results = GetOffers()) {
            is Result.Error -> Result.Error(message = results.message)
            Result.Loading -> Result.Loading
            is Result.Success -> {
                val homeOffer = results.data.find {
                    it.offerId.equals(id)
                }

                val changedoffer=homeOffer?.copy(startDate = formatDate(homeOffer?.startDate), endDate = formatDate(homeOffer?.endDate))
                Log.d("offer of $id",changedoffer.toString())
                Result.Success(changedoffer)
            }
        }
    }
    fun String.toPart(): RequestBody {
        return this.toRequestBody("text/plain".toMediaTypeOrNull())
    }

    fun prepareImagePart(file: File,name:String): MultipartBody.Part {
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(name, file.name, requestFile)
    }
    fun getFileFromUri(context: Context, uri: Uri): File? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val file = File(context.cacheDir, "temp_${System.currentTimeMillis()}")

            inputStream?.copyTo(file.outputStream())
            inputStream?.close()

            file
        } catch (e: Exception) {
            null
        }
    }

    fun String.toRequestBody(): RequestBody =
        this.toRequestBody("text/plain".toMediaTypeOrNull())
    override suspend fun updateOffer(
        offerModel: HomeOffer,
        deletedImage: List<String>,
        newLocalMedia: List<Uri>
    ): Result<OfferUpdateModel> {
        return try {
            val vendorDetails = dataStoreManager.get_Vendor_Details()
            val token="Bearer ${dataStoreManager.getToken()!!.token}"
            Log.d("token",token.toString())

            // Prepare media list only if not empty
            val medialist = if (newLocalMedia.isNotEmpty()) {
                newLocalMedia.mapNotNull { uri ->
                    val file = getFileFromUri(context, uri)
                    file?.let { prepareImagePart(it, it.name) }
                }
            } else {
                emptyList()
            }

            // Prepare string values
            val vendorId = vendorDetails?.vendor_id.toString()
            val offerTemplate = offerModel.Type?.name.toString()
            val imageAppearance = "green"
            val heading = offerModel.offerTitle.toString()
            val discount = offerModel.discount
            val brandName = offerModel.offerTitle.toString()
            val title = offerModel.offerTitle.toString()
            val description = offerModel.offerDescription.toString()
            val terms = offerModel.termsAndCondition.toString()
            val startDate = offerModel.startDate.toString()
            val endDate = offerModel.endDate.toString()
            val map = mutableMapOf<String, RequestBody>()
            map["vendor_id"]=vendorId.toRequestBody()
            map["offer_template"]=offerTemplate.toRequestBody()
             map["title"]=title.toRequestBody()
            map["description"]=description.toRequestBody()
            map["terms"]=terms.toRequestBody()
            Log.d("start_date",startDate)
           map["start_date"]=startDate.toRequestBody()
           map["end_date"]=endDate.toRequestBody()
            deletedImage.forEachIndexed { index, id ->
                map["delete_media_ids[$index]"] = RequestBody.create("text/plain".toMediaTypeOrNull(), id)
            }

            val mediaList = newLocalMedia.map { uri: Uri ->
                uriToFile(context, uri)
            }
            val mediaListMulti = prepareImageParts(mediaList)


//




            Log.d("UpdateOffer", "Media list size: ${medialist.size}")
            Log.d("UpdateOffer", "Deleted images: ${deletedImage}")
            Log.d("UpdateOffer", "brand name: ${brandName}")

            val editOffer = safeApiCall(
                api = {
if(mediaListMulti.isEmpty()) {
    apiService.updateOfferTextOnly(
        id = offerModel.offerId ?: "",
        token = token,
        map
    )
}else{
    apiService.updateOfferWithMedia(id=offerModel.offerId?:"", token = token,data=map, mediaFiles = mediaListMulti)
}

                },
                errorBodyParser = { json ->
                    try {
                        Gson().fromJson(json, error_model::class.java)
                    } catch (e: Exception) {
                        error_model(message = "Unknown error format: $json")
                    }
                }
            )

            when (editOffer) {
                is ApiResult.Error -> Result.Error(editOffer.error.message)
                is ApiResult.Success -> Result.Success(editOffer.data)
            }
        } catch (e: Exception) {
            Log.e("UpdateOffer", "Exception in updateOffer: ${e.message}", e)
            Result.Error(e.message ?: "Unknown error occurred")
        }
    }

    override suspend fun DeleteOffer(id: String, delete: String): Result<success_model> {
        return try {
            val token = dataStoreManager.getToken()?.token
            val result = safeApiCall(
                api = { apiService.offer_destroy(id = id, token = token ?: "", delete = delete) },
                errorBodyParser = { json ->
                    try {
                        Gson().fromJson(json, error_model::class.java)
                    } catch (e: Exception) {
                        error_model(message = "Unknown error format: $json")
                    }
                }
            )

            when (result) {
                is ApiResult.Error -> Result.Error(result.error.message)
                is ApiResult.Success -> Result.Success(result.data)
            }
        } catch (e: Exception) {
            Result.Error("Failed to delete offer: ${e.message}")
        }
    }

    override suspend fun QrCodeData(customerId: String, offerId: String): Result<QrModel> {
        return try {
            val token = dataStoreManager.getToken()?.token
            val result = safeApiCall(
                api = { apiService.getQrOfferDetail(offer_id = offerId, token = token ?: "", customer_id = customerId) },
                errorBodyParser = { json ->
                    try {
                        Gson().fromJson(json, error_model::class.java)
                    } catch (e: Exception) {
                        error_model(message = "Unknown error format: $json")
                    }
                }
            )

            when (result) {
                is ApiResult.Error -> Result.Error(result.error.message)
                is ApiResult.Success -> Result.Success(result.data)
            }
        } catch (e: Exception) {
            Result.Error("Failed to get QR code data: ${e.message}")
        }
    }

    override suspend fun OfferRedeem(customerId: String, offerId: String): Result<offerRedeemModel> {
        return try {
            val token = dataStoreManager.getToken()?.token
            val result = safeApiCall(
                api = { apiService.offer_redeem(offer_id = offerId, token = token ?: "", customer_id = customerId) },
                errorBodyParser = { json ->
                    try {
                        Gson().fromJson(json, error_model::class.java)
                    } catch (e: Exception) {
                        error_model(message = "Unknown error format: $json")
                    }
                }
            )

            when (result) {
                is ApiResult.Error -> Result.Error(result.error.message)
                is ApiResult.Success -> Result.Success(result.data)
            }
        } catch (e: Exception) {
            Result.Error("Failed to redeem offer: ${e.message}")
        }
    }

    override suspend fun getSubcategory(): Result<List<Subcategory>> {
        val data=dataStoreManager.get_Vendor_Details()!!.category.subcategory
        if(data.isEmpty())
            return Result.Error("No SubCategory Found")
        else{
            return Result.Success(data)
        }

    }

    override suspend fun getOfferReview(id: String): Result<getOfferDetailsModel> {
        return try {
            val data = dataStoreManager.getToken()?.token ?: ""
            val result = safeApiCall(
                api = { apiService.getRatings(id, data) },
                errorBodyParser = { json ->
                    try {
                        Gson().fromJson(json, error_model::class.java)
                    } catch (e: Exception) {
                        error_model(message = "Unknown error format: $json")
                    }
                }
            )
            when (result) {
                is ApiResult.Error -> Result.Error(result.error.message)
                is ApiResult.Success -> Result.Success(result.data)
            }


        } catch (e: Exception) {
            Result.Error("Failed to get offer review offer: ${e.message}")
        }
    }

    override suspend fun FaqModel(): Result<Faqmodel> {
        val token=dataStoreManager.getToken()?.token?:""
        val response = safeApiCall(
            api = { apiService.faqs(token) },
            errorBodyParser = { json ->
                try {
                    Gson().fromJson(json, error_model::class.java)
                } catch (e: Exception) {
                    error_model(message = "Unknown error format: $json")
                }
            }
        )
        return when(response){
            is ApiResult.Error -> {
                Result.Error(response.error.message)
            }
            is ApiResult.Success ->Result.Success(response.data)
        }
    }

}