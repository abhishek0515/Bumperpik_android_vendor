package com.bumperpick.bumperpickvendor.Repository

interface VendorRepository {
    suspend fun  FetchCategory():Result<List<Vendor_Category>>
    suspend fun  SavedDetail(details:Vendor_Details,number:String):Result<String>

}