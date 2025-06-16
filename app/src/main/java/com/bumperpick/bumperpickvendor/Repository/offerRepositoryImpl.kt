package com.bumperpick.bumperpickvendor.Repository

import DataStoreManager
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import com.bumperpick.bumperpickvendor.API.FinalModel.DataX
import com.bumperpick.bumperpickvendor.API.FinalModel.OfferUpdateModel
import com.bumperpick.bumperpickvendor.API.FinalModel.QrModel
import com.bumperpick.bumperpickvendor.API.FinalModel.offerRedeemModel
import com.bumperpick.bumperpickvendor.API.Model.success_model
import com.bumperpick.bumperpickvendor.API.Provider.ApiResult
import com.bumperpick.bumperpickvendor.API.Provider.ApiService
import com.bumperpick.bumperpickvendor.API.Provider.prepareImageParts
import com.bumperpick.bumperpickvendor.API.Provider.safeApiCall
import com.bumperpick.bumperpickvendor.API.Provider.toMultipartPart
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream



class offerRepositoryImpl(val apiService: ApiService,val dataStoreManager: DataStoreManager,val context:Context):offerRepository {

    override suspend fun AddOffer(offerModel: OfferModel): Result<success_model> {
        val result=Result.Loading
        val vendorDetails=dataStoreManager.get_Vendor_Details()
        val token=dataStoreManager.getToken()?.token
        val map = mutableMapOf<String, RequestBody>()
        if (vendorDetails != null) {
            Log.d("Offer_vendorId",vendorDetails.vendor_id.toString())
        }
        map["vendor_id"] = vendorDetails?.vendor_id.toString().toRequestBody("text/plain".toMediaType())
        map["offer_template"] = offerModel.gradientType?.name.toString().toRequestBody("text/plain".toMediaType())
        map["heading"]=offerModel.heading?.text.toString().toRequestBody("text/plain".toMediaType())
        map["discount"]= offerModel.discount.text.toString().toRequestBody("text/plain".toMediaType())
        map["brand_name"]=offerModel.brandName?.text.toString().toRequestBody("text/plain".toMediaType())
        map["title"]=offerModel.productTittle.toString().toRequestBody("text/plain".toMediaType())
        map["description"]=offerModel.productDiscription.toString().toRequestBody("text/plain".toMediaType())
        map["terms"]=offerModel.termsAndCondition.toString().toRequestBody("text/plain".toMediaType())
        map["start_date"]=offerModel.offerStartDate.toString().toRequestBody("text/plain".toMediaType())
        map["end_date"]=offerModel.offerEndDate.toString().toRequestBody("text/plain".toMediaType())
        map["token"]=token.toString().toRequestBody("text/plain".toMediaType())
        val banner= uriToFile(context,offerModel.BannerImage!!)?.toMultipartPart(partName = "brand_logo")
        val medialist=offerModel.medialList.map { uri: Uri ->
            uriToFile(context,uri) }
        val medialist_multi=prepareImageParts(medialist)

        medialist.forEach{
            Log.d("URI", it.path.toString())
        }
        Log.d("Offer add map ",map.toString())
        if (banner != null) {
            Log.d("Offer banner ",offerModel.BannerImage!!.toString())
        }
        Log.d("Offer medialist ",medialist.toString())
        val addoffer= safeApiCall { apiService.addOffers(
            data = map,
            banner,
            medialist_multi) }
        when(addoffer){

            is ApiResult.Error ->  return Result.Error(addoffer.message)
            is ApiResult.Success ->  return Result.Success(addoffer.data)
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
    fun getFileName(context: Context, uri: Uri): String? {
        var result: String? = null
        if (uri.scheme == "content") {
            context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
                }
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result?.lastIndexOf('/')
            if (cut != null && cut != -1) {
                result = result?.substring(cut + 1)
            }
        }
        return result
    }



    override suspend fun GetOffers(): Result<List<HomeOffer>> {
        val token=dataStoreManager.getToken()?.token
        val result= safeApiCall { apiService.homeOffers(token?:"") }
        when(result){
            is ApiResult.Error -> {return Result.Error(result.message)}
            is ApiResult.Success -> {
                val res=result.data.data
                val offerList:ArrayList<HomeOffer> =ArrayList()

                res.forEach {
                    Log.d("Offer",it.toString())
                    offerList.add(
                        HomeOffer(
                            offerId = it.id.toString(),
                            Type = MarketingOption.OFFERS,
                            offerValid = if(!it.expire) OfferValidation.Valid else OfferValidation.Expired,
                            Media_list = it.media.map { mediaItem ->
                                mediaItem.url
                            },
                            media = it.media,
                            brand_logo_url = it.brand_logo_url,
                            approval = it.approval,

                            discount = it.discount,
                            startDate = it.start_date,
                            endDate = it.end_date,
                            active = if(it.status=="inactive") "in-active" else "active",
                            offerTitle = it.title,
                            offerTag = it.quantity.toString() +" left",
                            offerDescription = it.description,
                            termsAndCondition = it.terms,)
                    )

                }

                return Result.Success(offerList)}
        }
    }

    override suspend fun getOfferDetails(id: String): Result<HomeOffer?> {
       val results=GetOffers()

        when(results){
            is Result.Error -> return Result.Error(message = results.message)
            Result.Loading ->return  Result.Loading
            is Result.Success ->{
                val homeoffer=results.data.find {
                    it.offerId.equals(id)
                }
                return Result.Success(homeoffer
                )

            }
        }

    }




/*    override suspend fun updateOffer(
        offerModel: HomeOffer,
        deleted_image: List<String>,
        newLocalMedia: List<Uri>
    ): Result<OfferUpdateModel> {
        Log.d("updated_id",deleted_image.toString())
       try {
           val result=Result.Loading
           val vendorDetails=dataStoreManager.get_Vendor_Details()
           val token=dataStoreManager.getToken()?.token
           val map = mutableMapOf<String, RequestBody>()


           vendorDetails?.vendor_id?.let {
               map["vendor_id"] = it.toString().toRequestBody("text/plain".toMediaType())
           }
           offerModel.Type?.name?.let {
               map["offer_template"] = it.toRequestBody("text/plain".toMediaType())
           }
           map["image_appearance"] = "blue".toRequestBody("text/plain".toMediaType())
           map["brand_name"] = (offerModel.brand_logo_url ?: "").toRequestBody("text/plain".toMediaType())
           map["heading"] = (offerModel.offerTitle ?: "").toRequestBody("text/plain".toMediaType())
           map["discount"] = "13".toRequestBody("text/plain".toMediaType())
           map["title"] = (offerModel.offerTitle ?: "").toRequestBody("text/plain".toMediaType())
           map["description"] = (offerModel.offerDescription ?: "").toRequestBody("text/plain".toMediaType())
           map["terms"] = (offerModel.termsAndCondition ?: "").toRequestBody("text/plain".toMediaType())
           map["start_date"] = (offerModel.startDate ?: "").toRequestBody("text/plain".toMediaType())
           map["end_date"] = (offerModel.endDate ?: "").toRequestBody("text/plain".toMediaType())
           map["token"] = token.toString().toRequestBody("text/plain".toMediaType())
           Log.d("Offer update map ",map.toString())
           val medialist=newLocalMedia.map { uri: Uri ->
               uriToFile(context,uri) }
           medialist.forEach{
               Log.d("URI", it.path.toString())
           }
           val medialist_multi=prepareImageParts(medialist)
           val deeted_mediaIds=deleted_image.map{
               id->MultipartBody.Part.createFormData("delete_media_ids[]",id)
           }
           Log.d("deleted media",deeted_mediaIds.size.toString())
           val editOffer= safeApiCall { apiService.updateOffer(data = map,
               //media = medialist_multi,
             //  deletedmedia =deeted_mediaIds,
               id = offerModel.offerId) }
           when(editOffer){

               is ApiResult.Error ->  return Result.Error(editOffer.message)
               is ApiResult.Success ->  return Result.Success(editOffer.data)
           }


       }
       catch (e:Exception){
           return Result.Error(e.message.toString())
       }

    }*/
/*override suspend fun updateOffer(
    offerModel: HomeOffer,
    deleted_image: List<String>,
    newLocalMedia: List<Uri>
): Result<OfferUpdateModel> {
    try {
        val vendorDetails = dataStoreManager.get_Vendor_Details()
        val token = dataStoreManager.getToken()?.token
        val map = mutableMapOf<String, RequestBody>()

        // Build your PartMap exactly as shown in Postman
        vendorDetails?.vendor_id?.let {
            map["vendor_id"] = it.toString().toRequestBody("text/plain".toMediaType())
        }
        offerModel.Type?.name?.let {
            map["offer_template"] = it.toRequestBody("text/plain".toMediaType())
        }
        map["image_appearance"] = "green".toRequestBody("text/plain".toMediaType()) // Changed to green as per your data
        map["heading"] = (offerModel.offerTitle ?: "").toRequestBody("text/plain".toMediaType())
        map["discount"] = "20%".toRequestBody("text/plain".toMediaType()) // Changed to match your data
        map["brand_name"] = (offerModel.brand_logo_url ?: "").toRequestBody("text/plain".toMediaType())
        map["title"] = (offerModel.offerTitle ?: "").toRequestBody("text/plain".toMediaType())
        map["description"] = (offerModel.offerDescription ?: "").toRequestBody("text/plain".toMediaType())
        map["terms"] = (offerModel.termsAndCondition ?: "").toRequestBody("text/plain".toMediaType())
        map["start_date"] = (offerModel.startDate ?: "").toRequestBody("text/plain".toMediaType())
        map["end_date"] = (offerModel.endDate ?: "").toRequestBody("text/plain".toMediaType())
        map["token"] = token.toString().toRequestBody("text/plain".toMediaType())

        Log.d("Offer update map", map.toString())

        val editOffer = safeApiCall {
            apiService.updateOffer(
                data = map,
                id = offerModel.offerId
            )
        }

        return when(editOffer) {
            is ApiResult.Error -> Result.Error(editOffer.message)
            is ApiResult.Success -> Result.Success(editOffer.data)
        }

    } catch (e: Exception) {
        return Result.Error(e.message.toString())
    }
}*/

    // Updated repository function
    override suspend fun updateOffer(
        offerModel: HomeOffer,
        deleted_image: List<String>,
        newLocalMedia: List<Uri>
    ): Result<OfferUpdateModel> {
        try {
            val vendorDetails = dataStoreManager.get_Vendor_Details()
            val token = dataStoreManager.getToken()?.token

            // Prepare individual RequestBody parts
            val vendorId = (vendorDetails?.vendor_id?.toString() ?: "")
                .toRequestBody("text/plain".toMediaTypeOrNull())

            val offerTemplate = (offerModel.Type?.name ?: "")
                .toRequestBody("text/plain".toMediaTypeOrNull())

            val imageAppearance = "green"
                .toRequestBody("text/plain".toMediaTypeOrNull())

            val heading = (offerModel.offerTitle ?: "")
                .toRequestBody("text/plain".toMediaTypeOrNull())

            val discount = "20%"
                .toRequestBody("text/plain".toMediaTypeOrNull())

            val brandName = (offerModel.brand_logo_url ?: "")
                .toRequestBody("text/plain".toMediaTypeOrNull())

            val title = (offerModel.offerTitle ?: "")
                .toRequestBody("text/plain".toMediaTypeOrNull())

            val description = (offerModel.offerDescription ?: "")
                .toRequestBody("text/plain".toMediaTypeOrNull())

            val terms = (offerModel.termsAndCondition ?: "")
                .toRequestBody("text/plain".toMediaTypeOrNull())

            val startDate = (offerModel.startDate ?: "")
                .toRequestBody("text/plain".toMediaTypeOrNull())

            val endDate = (offerModel.endDate ?: "")
                .toRequestBody("text/plain".toMediaTypeOrNull())

            val tokenBody = (token ?: "")
                .toRequestBody("text/plain".toMediaTypeOrNull())

            // Handle deleted images as comma-separated string or JSON array
            val deleteMediaIds = if (deleted_image.isNotEmpty()) {
                deleted_image.joinToString(",").toRequestBody("text/plain".toMediaTypeOrNull())
            } else {
                "".toRequestBody("text/plain".toMediaTypeOrNull())
            }

            // Prepare media files
            val mediaParts = mutableListOf<MultipartBody.Part>()
            newLocalMedia.forEachIndexed { index, uri ->
                try {
                    val file = uriToFile(context, uri)
                    if (file != null && file.exists()) {
                        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                        val part = MultipartBody.Part.createFormData(
                            "media[$index]",
                            file.name,
                            requestFile
                        )
                        mediaParts.add(part)
                    }
                } catch (e: Exception) {
                    Log.e("UpdateOffer", "Error processing media file at index $index: ${e.message}")
                }
            }

            Log.d("UpdateOffer", "Vendor ID: ${vendorDetails?.vendor_id}")
            Log.d("UpdateOffer", "Offer Template: ${offerModel.Type?.name}")
            Log.d("UpdateOffer", "Title: ${offerModel.offerTitle}")
            Log.d("UpdateOffer", "Deleted Images: $deleted_image")
            Log.d("UpdateOffer", "New Media Count: ${mediaParts.size}")

            val editOffer = safeApiCall {
                apiService.updateOffer2(
                    id = offerModel.offerId,
                    vendorId = vendorId,
                    offerTemplate = offerTemplate,
                    imageAppearance = imageAppearance,
                    heading = heading,
                    discount = discount,
                    brandName = brandName,
                    title = title,
                    description = description,
                    terms = terms,
                    startDate = startDate,
                    endDate = endDate,
                    token = tokenBody,
                  //  deleteMediaIds = deleteMediaIds,
                   // media = mediaParts
                )
            }

            return when (editOffer) {
                is ApiResult.Error -> Result.Error(editOffer.message)
                is ApiResult.Success -> Result.Success(editOffer.data)
            }

        } catch (e: Exception) {
            Log.e("UpdateOffer", "Exception in updateOffer: ${e.message}", e)
            return Result.Error(e.message ?: "Unknown error occurred")
        }
    }

    override suspend fun DeleteOffer(id: String, delete: String): Result<success_model> {
        try {
            val token=dataStoreManager.getToken()?.token
            val result= safeApiCall { apiService.offer_destroy(id=id, token = token?:"", delete = delete)  }

            when(result){
                is ApiResult.Error -> return Result.Error(result.message)
                is ApiResult.Success ->return Result.Success(result.data)
            }
        }
        catch (e:Exception){
            return Result.Error(e.message.toString())
        }
    }

    override suspend fun QrCodeData(customer_id: String, offer_id: String): Result<QrModel> {
        try {
            val token=dataStoreManager.getToken()?.token
            val result= safeApiCall { apiService.getQrOfferDetail(offer_id = offer_id, token = token?:"", customer_id = customer_id)  }

            when(result){
                is ApiResult.Error -> return Result.Error(result.message)
                is ApiResult.Success ->return Result.Success(result.data)
            }
        }
        catch (e:Exception){
            return Result.Error(e.message.toString())
        }
    }

    override suspend fun OfferRedeem(
        customer_id: String,
        offer_id: String
    ): Result<offerRedeemModel> {
        try {
            val token=dataStoreManager.getToken()?.token
            val result= safeApiCall { apiService.offer_redeem(offer_id = offer_id, token = token?:"", customer_id = customer_id )}
            when(result){
                is ApiResult.Error -> return Result.Error(result.message)
                is ApiResult.Success ->return Result.Success(result.data)
            }

        }
        catch (e:Exception){
            return Result.Error(e.message.toString())
        }
    }


}