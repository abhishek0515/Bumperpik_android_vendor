package com.bumperpick.bumperpickvendor.Repository

import android.net.Uri
import com.bumperpick.bumperpickvendor.API.FinalModel.OfferUpdateModel
import com.bumperpick.bumperpickvendor.API.FinalModel.QrModel
import com.bumperpick.bumperpickvendor.API.FinalModel.Subcategory
import com.bumperpick.bumperpickvendor.API.FinalModel.getOfferDetailsModel
import com.bumperpick.bumperpickvendor.API.FinalModel.offerRedeemModel
import com.bumperpick.bumperpickvendor.API.Model.success_model

interface offerRepository {
    suspend fun AddOffer(offerModel: OfferModel): Result<success_model>
    suspend fun GetOffers(): Result<List<HomeOffer>>
    suspend fun getOfferDetails(id: String): Result<HomeOffer?>
    suspend fun updateOffer(
        offerModel: HomeOffer,
        deleted_image: List<String>,
        newLocalMedia: List<Uri>
    ): Result<OfferUpdateModel>

    suspend fun DeleteOffer(id: String, delete: String): Result<success_model>

    suspend fun QrCodeData(customer_id: String, offer_id: String): Result<QrModel>

    suspend fun OfferRedeem(customer_id: String, offer_id: String): Result<offerRedeemModel>
    suspend fun getSubcategory(): Result<List<Subcategory>>

    suspend fun getOfferReview(id:String): Result<getOfferDetailsModel>
}