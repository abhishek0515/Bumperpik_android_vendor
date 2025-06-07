package com.bumperpick.bumperpickvendor.Repository

import com.bumperpick.bumperpickvendor.API.FinalModel.Data

interface VendorRepository {
    suspend fun  FetchCategory():Result<List<Vendor_Category>>
    suspend fun  SavedDetail(details:Vendor_Details,number:String):Result<String>
    suspend fun  getSavedVendorDetail():Result<Data>


}