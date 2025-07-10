package com.bumperpick.bumperpickvendor.Repository

import com.bumperpick.bumperpickvendor.API.FinalModel.Data
import com.bumperpick.bumperpickvendor.API.FinalModel.newsubscriptionModel
import com.bumperpick.bumperpickvendor.API.FinalModel.select_subs_model
import com.bumperpick.bumperpickvendor.API.FinalModel.subscription_model
import com.bumperpick.bumperpickvendor.API.FinalModel.update_profile_model
import com.bumperpick.bumperpickvendor.API.FinalModel.vendor_details_model
import com.google.mlkit.vision.barcode.common.Barcode.Email
import java.io.File

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




}