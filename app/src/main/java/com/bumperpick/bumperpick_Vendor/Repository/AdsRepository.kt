package com.bumperpick.bumperpick_Vendor.Repository

import com.bumperpick.bumperpick_Vendor.API.FinalModel.AdsDetailModel
import com.bumperpick.bumperpick_Vendor.API.FinalModel.ads_package_model
import com.bumperpick.bumperpick_Vendor.API.FinalModel.subs_ads_model
import com.bumperpick.bumperpick_Vendor.API.FinalModel.vendorAdsModel
import com.bumperpick.bumperpick_Vendor.API.Model.success_model
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface AdsRepository {

    suspend fun adsPackage(): Result<ads_package_model>
    suspend fun subscribePackage( adsSubscriptionId: String, paymentTransactionId: String): Result<subs_ads_model>
    suspend fun createAds(data: Map<String, RequestBody>, banner: MultipartBody.Part): Result<success_model>
    suspend fun vendorAds(): Result<vendorAdsModel>
    suspend fun vendorAdsDetail( id: String): Result<AdsDetailModel>
    suspend fun updateVendorAds( id: String, data: Map<String, RequestBody>, banner: MultipartBody.Part): Result<success_model>
    suspend fun deleteAds( id: String): Result<success_model>
}