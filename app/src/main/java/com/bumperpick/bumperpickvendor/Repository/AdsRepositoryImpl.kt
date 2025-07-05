package com.bumperpick.bumperpickvendor.Repository

import DataStoreManager
import android.util.Log
import com.bumperpick.bumperpickvendor.API.FinalModel.AdsDetailModel
import com.bumperpick.bumperpickvendor.API.FinalModel.ads_package_model
import com.bumperpick.bumperpickvendor.API.FinalModel.subs_ads_model
import com.bumperpick.bumperpickvendor.API.FinalModel.vendorAdsModel
import com.bumperpick.bumperpickvendor.API.Model.success_model
import com.bumperpick.bumperpickvendor.API.Provider.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class AdsRepositoryImpl(private val dataStoreManager: DataStoreManager,
                        private val api: ApiService
):AdsRepository {
    override suspend fun adsPackage(): Result<ads_package_model> {
        return try {
            val response = api.adsPackages()
            if (response.isSuccessful && response.body() != null) {
                Result.Success(response.body()!!)
            } else {
                Result.Error("Failed to fetch ad packages: ${response.message()}", null)
            }
        } catch (e: Exception) {
            Result.Error("Error fetching ad packages", e)
        }
    }

    override suspend fun subscribePackage(
        adsSubscriptionId: String,
        paymentTransactionId: String
    ): Result<subs_ads_model> {
        return try {
            val token=dataStoreManager.getToken()!!.token
            val response = api.subscribePackage(token, adsSubscriptionId, paymentTransactionId)
            if (response.isSuccessful && response.body() != null) {
                Result.Success(response.body()!!)
            } else {
                Result.Error("Failed to subscribe to package: ${response.message()}", null)
            }
        } catch (e: Exception) {
            Result.Error("Error subscribing to package", e)
        }
    }

    override suspend fun createAds(
        data: Map<String, RequestBody>,
        banner: MultipartBody.Part
    ): Result<success_model> {
        return try {
            val map:MutableMap<String,RequestBody> =HashMap()
            map.putAll(data)
            val token =dataStoreManager.getToken()!!.token
            map["token"]=token.toRequestBody()
            val response = api.createads(map, banner)

            if (response.isSuccessful && response.body() != null) {
                Result.Success(response.body()!!)
            } else {
                Result.Error("Failed to create ad: ${response.message()}", null)
            }
        } catch (e: Exception) {
            Log.d("createAds ERROR",e.toString())
            Result.Error("Error creating ad", e)
        }
    }

    override suspend fun vendorAds(): Result<vendorAdsModel> {
        return try {
            val token=dataStoreManager.getToken()!!.token
            val response = api.vendor_ads(token)
            if (response.isSuccessful && response.body() != null) {
                Result.Success(response.body()!!)
            } else {
                Result.Error("Failed to fetch vendor ads: ${response.message()}", null)
            }
        } catch (e: Exception) {
            Result.Error("Error fetching vendor ads", e)
        }
    }

    override suspend fun vendorAdsDetail( id: String): Result<AdsDetailModel> {
        return try {
            val token=dataStoreManager.getToken()!!.token
            val response = api.vendor_ads_dfetail(id=id, token=token)
            if (response.isSuccessful && response.body() != null) {
                Result.Success(response.body()!!)
            } else {
                Result.Error("Failed to fetch vendor ad details: ${response.message()}", null)
            }
        } catch (e: Exception) {
            Log.d("EXCEPTION vendorAdsDetail",e.toString())
            Result.Error("Error fetching vendor ad details", e)
        }
    }

    override suspend fun updateVendorAds(

        id: String,
        data: Map<String, RequestBody>,
        banner: MultipartBody.Part
    ): Result<success_model> {
        return try {
            val token=dataStoreManager.getToken()!!.token

            val response = api.vendors_ads_update(id, token, data, banner)
            if (response.isSuccessful && response.body() != null) {
                Result.Success(response.body()!!)
            } else {
                Result.Error("Failed to update vendor ad: ${response.message()}", null)
            }
        } catch (e: Exception) {
            Log.d("ERROR updateVendorAds",e.toString())
            Result.Error("Error updating vendor ad", e)
        }
    }

    override suspend fun deleteAds( id: String): Result<success_model> {
        return try {
            val token=dataStoreManager.getToken()!!.token
            val response = api.ads_dewstroy(id,token)
            if (response.isSuccessful && response.body() != null) {
                Result.Success(response.body()!!)
            } else {
                Result.Error("Failed to delete ad: ${response.message()}", null)
            }
        } catch (e: Exception) {
            Log.d("Delete",e.toString())
            Result.Error("Error deleting ad", e)
        }
    }

}