package com.bumperpick.bumperpickvendor.Repository

import DataStoreManager
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import com.bumperpick.bumperpickvendor.API.FinalModel.DataX
import com.bumperpick.bumperpickvendor.API.FinalModel.OfferUpdateModel
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
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream

val demoOffers = listOf(
    HomeOffer(
        offerId = "OFF001",
        Type = MarketingOption.OFFERS,
        offerValid = OfferValidation.Valid,
        Media_list = listOf(
            "https://images.unsplash.com/photo-1558769132-cb1aea458c5e?ixlib=rb-4.0.3&auto=format&fit=crop&w=1350&q=80",
            "https://images.unsplash.com/photo-1560243563-0627e57aaed1?ixlib=rb-4.0.3&auto=format&fit=crop&w=1350&q=80"
        ),
        active = "active",
        discount = "20% OFF",
        startDate = "2025-05-01",
        endDate = "2025-06-15",
        offerTitle = "Summer Sale Extravaganza",
        offerTag = "Summer Deal",
        offerDescription = "Get 20% off on all summer clothing collections. Shop now for the latest trends in beachwear, shorts, and sunglasses!",
        termsAndCondition = "Valid on purchases over $50. Cannot be combined with other offers. Excludes clearance items. Offer valid online and in-store until June 15, 2025."
    ),
    HomeOffer(
        offerId = "OFF002",
        Type = MarketingOption.OFFERS,
        offerValid = OfferValidation.Expired,
        Media_list = listOf(
            "https://images.unsplash.com/photo-1558769132-cb1aea458c5e?ixlib=rb-4.0.3&auto=format&fit=crop&w=1350&q=80",
        ),
        active = "active",
        discount = "30% OFF",
        startDate = "2024-12-01",
        endDate = "2024-12-31",
        offerTitle = "Winter Clearance Sale",
        offerTag = "Winter Deal",
        offerDescription = "Save 30% on winter jackets, boots, and accessories. Limited stock available, shop before it’s gone!",
        termsAndCondition = "Offer valid on select winter items only. No rainchecks. Expired on December 31, 2024."
    ),
    HomeOffer(
        offerId = "OFF003",
        Type = MarketingOption.OFFERS,
        active = "active",
        offerValid = OfferValidation.Valid,
        Media_list = listOf(
            "https://images.unsplash.com/photo-1516321318423-f06f85e504b3?ixlib=rb-4.0.3&auto=format&fit=crop&w=1350&q=80",
            "https://images.unsplash.com/photo-1504279577553-1c4197a20491?ixlib=rb-4.0.3&auto=format&fit=crop&w=1350&q=80"
        ),
        discount = "$100 OFF",
        startDate = "2025-05-20",
        endDate = "2025-07-01",
        offerTitle = "Tech Gadget Bonanza",
        offerTag = "Electronics Deal",
        offerDescription = "Save $100 on select electronics, including smartwatches, earbuds, and tablets. Upgrade your tech today!",
        termsAndCondition = "Minimum purchase of $500 required. Offer valid on select brands only. Cannot be combined with other promotions. Valid until July 1, 2025."
    ),
    HomeOffer(
        offerId = "OFF004",
        active = "in-active",
        Type = MarketingOption.OFFERS,
        offerValid = OfferValidation.Valid,
        Media_list = listOf(
            "https://images.unsplash.com/photo-1512568400610-62da28bc8a13?ixlib=rb-4.0.3&auto=format&fit=crop&w=1350&q=80"
        ),
        discount = "Buy 1 Get 1 Free",
        startDate = "2025-05-10",
        endDate = "2025-06-10",
        offerTitle = "BOGO Footwear Fiesta",
        offerTag = "Footwear Offer",
        offerDescription = "Buy one pair of shoes and get another pair free. Mix and match your favorite styles!",
        termsAndCondition = "Free item must be of equal or lesser value. Excludes premium brands. Offer valid online only until June 10, 2025."
    ),
    HomeOffer(
        offerId = "OFF005",
        active = "active",
        Type = MarketingOption.OFFERS,
        offerValid = OfferValidation.Expired,
        Media_list = listOf(
            "https://images.unsplash.com/photo-1543168256-418811576f69?ixlib=rb-4.0.3&auto=format&fit=crop&w=1350&q=80"
        ),
        discount = "15% OFF",
        startDate = "2024-11-15",
        endDate = "2024-12-25",
        offerTitle = "Holiday Gift Bundle",
        offerTag = "Holiday Deal",
        offerDescription = "Enjoy 15% off on gift bundles, including skincare, fragrances, and accessories. Perfect for holiday gifting!",
        termsAndCondition = "Valid on gift bundles only. Cannot be combined with other discounts. Expired on December 25, 2024."
    )
)

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
                    offerList.add(
                        HomeOffer(
                            offerId = it.id.toString(),
                            Type = MarketingOption.OFFERS,
                            offerValid = if(!it.expire) OfferValidation.Valid else OfferValidation.Expired,
                            Media_list = it.media.map { mediaItem ->

                                mediaItem.url
                            }.plus(it.brand_logo_url),
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

    override suspend fun getOfferDetails(id: String): Result<OfferModel> {
        Log.d("getOfferDetails_id",id)
        val token=dataStoreManager.getToken()?.token
        val result= safeApiCall { apiService.homeOffers(token?:"") }

        when(result) {
            is ApiResult.Error -> {
                return Result.Error(result.message)
            }

            is ApiResult.Success -> {

                val offerList: ArrayList<OfferModel> = ArrayList()
                val list:List<DataX> =ArrayList()
                list.forEach {
                    offerList.add(
                        OfferModel(
                            UrlMedialList = it.media,
                            UrlBannerIMage =  it.brand_logo_url,
                            productTittle = it.title,
                            productDiscription = it.description,
                            termsAndCondition = it.terms,
                            offerStartDate = it.start_date,
                            offerEndDate = it.end_date)
                    )

                }
                val offer=offerList.find {
                    id.equals(it)
                }
                return Result.Success(offer!!)


            }
        }

    }
    fun createDeleteRequestBody(value: String): RequestBody =
        value.toRequestBody("text/plain".toMediaTypeOrNull())
    override suspend fun updateOffer(
        offerModel: OfferModel,
        deleted_image: List<String>
    ): Result<OfferUpdateModel> {
        Log.d("updated_id",deleted_image.toString())
       try {
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
           val deeted_mediaIds=deleted_image.map{
               id->MultipartBody.Part.createFormData("delete_media_ids[]",id)
           }
           val editOffer= safeApiCall { apiService.updateOffer(data = map, media = medialist_multi, deletedmedia =deeted_mediaIds ) }
           when(editOffer){

               is ApiResult.Error ->  return Result.Error(editOffer.message)
               is ApiResult.Success ->  return Result.Success(editOffer.data)
           }


       }
       catch (e:Exception){
           return Result.Error(e.message.toString())
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


}