package com.bumperpick.bumperpick_Vendor.Repository

import android.net.Uri
import com.bumperpick.bumperpick_Vendor.API.FinalModel.Faqmodel
import com.bumperpick.bumperpick_Vendor.API.FinalModel.Notification_model
import com.bumperpick.bumperpick_Vendor.API.FinalModel.OfferUpdateModel
import com.bumperpick.bumperpick_Vendor.API.FinalModel.QrModel
import com.bumperpick.bumperpick_Vendor.API.FinalModel.Subcategory
import com.bumperpick.bumperpick_Vendor.API.FinalModel.getOfferDetailsModel
import com.bumperpick.bumperpick_Vendor.API.FinalModel.offerRedeemModel
import com.bumperpick.bumperpick_Vendor.API.Model.success_model

interface offerRepository {
    suspend fun AddOffer(offerModel: OfferModel,templateData: Template_Data): Result<success_model>
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

    suspend fun FaqModel(): Result<Faqmodel>

    suspend fun send_reminder(offer_id: String): Result<success_model>

    suspend fun notification(): Result<Notification_model>
}