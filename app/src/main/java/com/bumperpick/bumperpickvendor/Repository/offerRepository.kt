package com.bumperpick.bumperpickvendor.Repository

import com.bumperpick.bumperpickvendor.API.FinalModel.OfferUpdateModel
import com.bumperpick.bumperpickvendor.API.Model.success_model

interface offerRepository {
    suspend fun AddOffer(offerModel: OfferModel):Result<success_model>
    suspend fun GetOffers():Result<List<HomeOffer>>
    suspend fun getOfferDetails(id:String):Result<OfferModel>
    suspend fun updateOffer(offerModel: OfferModel,deleted_image:List<String>):Result<OfferUpdateModel>
    suspend fun DeleteOffer(id:String,delete:String):Result<success_model>

}