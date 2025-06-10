package com.bumperpick.bumperpickvendor.Repository

import android.net.Uri
import com.bumperpick.bumperpickvendor.API.FinalModel.OfferUpdateModel
import com.bumperpick.bumperpickvendor.API.Model.success_model

interface offerRepository {
    suspend fun AddOffer(offerModel: OfferModel):Result<success_model>
    suspend fun GetOffers():Result<List<HomeOffer>>
    suspend fun getOfferDetails(id:String):Result<HomeOffer?>
    suspend fun updateOffer(offerModel: HomeOffer,deleted_image:List<String>,newLocalMedia:List<Uri>):Result<OfferUpdateModel>
    suspend fun DeleteOffer(id:String,delete:String):Result<success_model>

}