package com.bumperpick.bumperpickvendor.Repository

import DataStoreManager
import android.util.Log
import com.bumperpick.bumperpickvendor.API.FinalModel.Data
import com.bumperpick.bumperpickvendor.API.FinalModel.error_model
import com.bumperpick.bumperpickvendor.API.FinalModel.newsubscriptionModel
import com.bumperpick.bumperpickvendor.API.FinalModel.select_subs_model
import com.bumperpick.bumperpickvendor.API.FinalModel.update_profile_model
import com.bumperpick.bumperpickvendor.API.FinalModel.vendor_details_model
import com.bumperpick.bumperpickvendor.API.Provider.ApiResult
import com.bumperpick.bumperpickvendor.API.Provider.ApiService
import com.bumperpick.bumperpickvendor.API.Provider.safeApiCall
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class VendorRepositoryImpl(
    private val dataStoreManager: DataStoreManager,
    private val apiService: ApiService
) : VendorRepository {

    override suspend fun FetchCategory(): Result<List<Vendor_Category>> {
        return try {
            val categories = safeApiCall(
                api = { apiService.getCategory() },
                errorBodyParser = { json ->
                    try {
                        Gson().fromJson(json, error_model::class.java)
                    } catch (e: Exception) {
                        error_model(message = "Unknown error format: $json")
                    }
                }
            )

            when (categories) {
                is ApiResult.Success -> {
                    if (categories.data.code == 200) {
                        val data = categories.data.data
                        val categoriesList = ArrayList<Vendor_Category>()
                        if (data.isNotEmpty()) {
                            data.forEach {
                                categoriesList.add(Vendor_Category(it.id.toString(), it.name))
                            }
                        }
                        Result.Success(categoriesList)
                    } else {
                        Result.Error(categories.data.message)
                    }
                }
                is ApiResult.Error -> Result.Error(categories.error.message)
            }
        } catch (e: Exception) {
            Result.Error("Failed to fetch categories", e)
        }
    }
    fun prepareImagePart(file: File?, name: String): MultipartBody.Part? {

        if (file != null) {
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            return MultipartBody.Part.createFormData(name, file.name, requestFile)
        }
        else return null
    }
    fun createPartFromString(value: String): RequestBody {
        return value.toRequestBody("text/plain".toMediaTypeOrNull())
    }
    override suspend fun SavedDetail(details: Vendor_Details, number: String): Result<String>
    {
        return try {
            val map = mutableMapOf<String, RequestBody>()
            Log.d("NUMBER", number)

            val newDetails = details.copy(
                Vendor_Id = (1..1000).random().toString(),
                Vendor_Mobile = number

            )
            Log.d("DETAILS", newDetails.toString())

            val vendorId = dataStoreManager.get_Vendor_Details()?.vendor_id.toString()

            map["establishment_name"] = newDetails.Vendor_EstablishName.toRequestBody("text/plain".toMediaType())
            map["brand_name"] = newDetails.Vendor_brand.toRequestBody("text/plain".toMediaType())
            map["email"] = newDetails.Vendor_Email.toRequestBody("text/plain".toMediaType())
            map["phone_number"] = number.toRequestBody("text/plain".toMediaType())
            map["category_id"] = newDetails.Vendor_Category.cat_id.toRequestBody("text/plain".toMediaType())
            map["establishment_address"] = newDetails.Establisment_Adress.toRequestBody("text/plain".toMediaType())
            map["outlet_address"] = newDetails.Outlet_Address.toRequestBody("text/plain".toMediaType())
            map["gst_number"] = newDetails.GstNumber.toRequestBody("text/plain".toMediaType())
            map["vendor_id"] = vendorId.toRequestBody("text/plain".toMediaType())

            val image = prepareImagePart(name ="Gst_pic_url", file = details.GstPicUrl!!)
            Log.d("MAP", map.toString())

            val submitDetail = safeApiCall(
                api = {
                    if(image!=null) {
                        apiService.register_vendor(map, image)
                    }
                    apiService.register_vendorWOimage(map)
                      },
                errorBodyParser = { json ->
                    try {
                        Gson().fromJson(json, error_model::class.java)
                    } catch (e: Exception) {
                        error_model(message = "Unknown error format: $json")
                    }
                }
            )

            Log.d("SUBMIT", submitDetail.toString())

            when (submitDetail) {
                is ApiResult.Success -> {
                    dataStoreManager.save_Vendor_Details(submitDetail.data.data)
                    dataStoreManager.saveToken(submitDetail.data.meta)
                    Log.d("success","sccc")
                    Result.Success(submitDetail.data.data.vendor_id.toString())
                }
                is ApiResult.Error -> Result.Error(submitDetail.error.message)
            }
        } catch (e: Exception) {
            Result.Error("Failed to save vendor details: ${e}")
        }
    }

    override suspend fun getSavedVendorDetail(): Result<Data> {
        return try {
            val detail = dataStoreManager.get_Vendor_Details()
            if (detail != null) {
                Result.Success(detail)
            } else {
                Result.Error("No Details Found")
            }
        } catch (e: Exception) {
            Result.Error("Failed to get saved vendor details: ${e.message}")
        }
    }
    fun generateCustomTransactionId(length: Int = 16): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        return (1..length)
            .map { chars.random() }
            .joinToString("")
    }
    override suspend fun fetchSubscription(): Result<newsubscriptionModel> {
        return try {
            val subscription= safeApiCall(
                api = {apiService.Fetch_subs()},
                errorBodyParser = {
                    json->
                    try {
                        Gson().fromJson(json, error_model::class.java)
                    } catch (e: Exception) {
                        error_model(message = "Unknown error format: $json")
                    }
                }
            )
            when(subscription) {
                is ApiResult.Error -> Result.Error(subscription.error.message)
                is ApiResult.Success -> {
                    if (subscription.data.code == 200)
                        return Result.Success(subscription.data)
                    else {
                        return Result.Error(subscription.data.message)
                    }
                }
            }

        }
        catch (e:Exception){
            Result.Error("Failed to fetch subscriptions: ${e.message}")
        }
    }

    override suspend fun selectSubsVendor( id:String,transactionId:String): Result<select_subs_model> {
        return try {
            val token=dataStoreManager.getToken()!!.token
            val add_subs= safeApiCall(
                api = {apiService.Vendor_subs_choose(subscription_id = id,token=token,
                    transaction_id =transactionId,
                    status = 1)},
                errorBodyParser = {
                        json->
                    try {
                        Gson().fromJson(json, error_model::class.java)
                    } catch (e: Exception) {
                        error_model(message = "Unknown error format: $json")
                    }
                }
            )

            when(add_subs){
                is ApiResult.Error -> Result.Error(add_subs.error.message)
                is ApiResult.Success -> {
                    if (add_subs.data.code == 200)
                        return Result.Success(add_subs.data)
                    else {
                        return Result.Error(add_subs.data.message)
                    }
                }
            }


        }
        catch (e:Exception){
            Result.Error("Failed to fetch subscriptions: ${e.message}")
        }
    }

    override suspend fun getProfile(): Result<vendor_details_model> {
        return try {
            val token = dataStoreManager.getToken()!!.token
            val add_subs = safeApiCall(
                api = { apiService.FetchProfile( token = token) },
                errorBodyParser = { json ->
                    try {
                        Gson().fromJson(json, error_model::class.java)
                    } catch (e: Exception) {
                        error_model(message = "Unknown error format: $json")
                    }
                }
            )

            when (add_subs) {
                is ApiResult.Error -> Result.Error(add_subs.error.message)
                is ApiResult.Success -> {
                    if (add_subs.data.code == 200)
                        return Result.Success(add_subs.data)
                    else {
                        return Result.Error(add_subs.data.message)
                    }
                }
            }


        } catch (e: Exception) {
            Result.Error("Failed to fetch profile: ${e.message}")
        }
    }

    private fun String.toPart(): RequestBody =
        this.toRequestBody("text/plain".toMediaTypeOrNull())

    override suspend fun updateProfile( vendorDetails: Vendor_Details):Result<update_profile_model> {
      return try {
          Log.d("update_profile",vendorDetails.toString())
          val token=dataStoreManager.getToken()!!.token

          val image = prepareImagePart(name = "image", file = vendorDetails.userImage)

          val map = mutableMapOf<String, RequestBody>()
          map["token"]=token.toPart()
          map["name"]=vendorDetails.Vendor_EstablishName.toPart()
          map["phone_number"]=vendorDetails.Vendor_Mobile.toPart()
          map["email"]=vendorDetails.Vendor_Email.toPart()
          map["establishment_name"]=vendorDetails.Vendor_EstablishName.toPart()
          map["brand_name"]=vendorDetails.Vendor_brand.toPart()
          map["establishment_address"]=vendorDetails.Establisment_Adress.toPart()
          map["outlet_address"]=vendorDetails.Outlet_Address.toPart()
          map["gst_number"]=vendorDetails.GstNumber.toPart()
          map["start_time"]=vendorDetails.openingTime!!.toPart()
          map["close_time"]=vendorDetails.closingTime!!.toPart()


          val update= safeApiCall(
              api = {
                  if (image != null) {
                      apiService.UpdateProfile(map,image)
                  }
                  else{
                      apiService.UpdateProfileWOImage(map)
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

          when(update){
              is ApiResult.Error -> Result.Error(update.error.message)
              is ApiResult.Success -> {
                  if (update.data.code == 200)
                      return Result.Success(update.data)
                  else {
                      return Result.Error(update.data.message)
                  }
              }
          }



      }
      catch (e: Exception) {
          Result.Error("Failed to fetch profile: ${e.message}")
      }
    }
}