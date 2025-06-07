package com.bumperpick.bumperpickvendor.Repository

import DataStoreManager
import android.util.Log
import com.bumperpick.bumperpickvendor.API.FinalModel.Data
import com.bumperpick.bumperpickvendor.API.Provider.ApiResult
import com.bumperpick.bumperpickvendor.API.Provider.ApiService
import com.bumperpick.bumperpickvendor.API.Provider.safeApiCall
import com.bumperpick.bumperpickvendor.API.Provider.toMultipartPart
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody


class VendorRepositoryImpl(private val dataStoreManager: DataStoreManager,val apiService: ApiService):VendorRepository {


    override suspend fun FetchCategory(): Result<List<Vendor_Category>> {
     return try {
         val categories=safeApiCall { apiService.getCategory() }
         when(categories){
             is ApiResult.Success->{
                 if(categories.data.code==200) {
                     val data=categories.data.data
                     val categories=ArrayList<Vendor_Category>()
                     if(data.isNotEmpty()){
                         data.forEach {
                             categories.add(Vendor_Category(it.id.toString(),it.name))
                         }
                     }
                     Result.Success(categories)
                 }
                 else Result.Error(categories.data.message)
             }
             is ApiResult.Error-> Result.Error(categories.message)

         }
     }
     catch (e:Exception){
         Result.Error("Failed to fetch categories",e)
     }
    }

    override suspend fun SavedDetail(details: Vendor_Details,number:String): Result<String> {

        val map = mutableMapOf<String, RequestBody>()
        Log.d("NUMBER",number)

        map["establishment_name"] = details.Vendor_EstablishName.toRequestBody("text/plain".toMediaType())
        map["brand_name"] = details.Vendor_brand.toRequestBody("text/plain".toMediaType())
        map["email"] = details.Vendor_Email.toRequestBody("text/plain".toMediaType())
        map["phone_number"] = number.toRequestBody("text/plain".toMediaType())
        map["category_id"] = details.Vendor_Category.cat_id.toRequestBody("text/plain".toMediaType())
        map["establishment_address"] = details.Establisment_Adress.toRequestBody("text/plain".toMediaType())
        map["outlet_address"] = details.Outlet_Address.toRequestBody("text/plain".toMediaType())
        map["gst_number"] = details.GstNumber.toRequestBody("text/plain".toMediaType())


        val image=details.GstPicUrl!!.toMultipartPart()
        Log.d("MAP",map.toString())
        val submitDetail=safeApiCall { apiService.register_vendor(map,image) }
        Log.d("SUBMIT",submitDetail.toString())
        when(submitDetail){
            is ApiResult.Success-> {
                 dataStoreManager.save_Vendor_Details(submitDetail.data.data)
                dataStoreManager.saveToken(submitDetail.data.meta)
                return Result.Success(submitDetail.data.data.vendor_id.toString())
            }
            is ApiResult.Error-> return Result.Error(submitDetail.message)

        }


    }

    override suspend fun getSavedVendorDetail(): Result<Data> {
        val detail=dataStoreManager.get_Vendor_Details()
        return if(detail!=null) Result.Success(detail) else Result.Error("No Details Found")
    }
}