package com.bumperpick.bumperpick_Vendor.Repository

import com.bumperpick.bumperpick_Vendor.API.FinalModel.Data
import com.bumperpick.bumperpick_Vendor.API.FinalModel.ads_subs_model
import com.bumperpick.bumperpick_Vendor.API.FinalModel.dasboard_modek
import com.bumperpick.bumperpick_Vendor.API.FinalModel.newsubscriptionModel
import com.bumperpick.bumperpick_Vendor.API.FinalModel.select_subs_model
import com.bumperpick.bumperpick_Vendor.API.FinalModel.update_profile_model
import com.bumperpick.bumperpick_Vendor.API.FinalModel.vendor_details_model

interface VendorRepository {
    suspend fun  FetchCategory():Result<List<Vendor_Category>>
    suspend fun  SavedDetail(details:Vendor_Details,number:String):Result<String>
    suspend fun  getSavedVendorDetail():Result<Data>
    suspend fun  fetchSubscription():Result<newsubscriptionModel>
    suspend fun selectSubsVendor(id:String,transactionId:String):Result<select_subs_model>
    suspend fun getProfile():Result<vendor_details_model>
    suspend fun updateProfile(
        vendorDetails: Vendor_Details
    ):Result<update_profile_model>
    suspend fun getAdsSubs(): Result<ads_subs_model>

    suspend fun getDashboard(): Result<dasboard_modek>



}