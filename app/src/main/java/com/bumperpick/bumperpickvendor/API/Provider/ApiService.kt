package com.bumperpick.bumperpickvendor.API.Provider

import com.bumperpick.bumperpickvendor.API.FinalModel.EventModel
import com.bumperpick.bumperpickvendor.API.FinalModel.EventRegisterModel
import com.bumperpick.bumperpickvendor.API.FinalModel.HomeOfferModel
import com.bumperpick.bumperpickvendor.API.FinalModel.NewEventDetailModel
import com.bumperpick.bumperpickvendor.API.FinalModel.NewEventmodel
import com.bumperpick.bumperpickvendor.API.FinalModel.OfferUpdateModel
import com.bumperpick.bumperpickvendor.API.FinalModel.QrModel
import com.bumperpick.bumperpickvendor.API.FinalModel.VendorLoginModel
import com.bumperpick.bumperpickvendor.API.FinalModel.newsubscriptionModel
import com.bumperpick.bumperpickvendor.API.FinalModel.offerRedeemModel
import com.bumperpick.bumperpickvendor.API.FinalModel.select_subs_model
import com.bumperpick.bumperpickvendor.API.FinalModel.subscription_model
import com.bumperpick.bumperpickvendor.API.FinalModel.update_profile_model
import com.bumperpick.bumperpickvendor.API.FinalModel.vendor_details_model
import com.bumperpick.bumperpickvendor.API.Model.Category_Model
import com.bumperpick.bumperpickvendor.API.Model.success_model
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("api/vendor/send-otp")
    suspend  fun vendor_send_otp(@Field("phone_number") mobile_number: String): Response<success_model>
    @FormUrlEncoded
    @POST("api/vendor/resend-otp")
    suspend fun vendor_re_send_otp(@Field("phone_number") mobile_number: String): Response<success_model>
    @FormUrlEncoded
    @POST("api/vendor/verify-otp")
    suspend fun vendor_verify_otp(@Field("phone_number") mobile_number: String,@Field("otp") otp: String): Response<VendorLoginModel>
    @Multipart
   @POST("api/vendor/register")
   suspend fun register_vendor(
        @PartMap data: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part gst_certificate: MultipartBody.Part
   ):Response<VendorLoginModel>
   @GET("api/categories")
   suspend fun getCategory():Response<Category_Model>
   @FormUrlEncoded
   @POST("api/vendor/auth-google")
   suspend fun auth_google(@Field("email") email:String):Response<VendorLoginModel>



    // For updates with media (multipart)

    @POST("api/vendor/offers-update/{id}")
    suspend fun updateOfferWithMedia(
        @Path("id") id: String,
        @Body requestBody: RequestBody
    ): Response<OfferUpdateModel>
    // For text-only updates (form-encoded)
    @FormUrlEncoded
    @POST("api/vendor/offers-update/{id}")
    suspend fun updateOfferTextOnly(
        @Path("id") id: String?,
        @Field("vendor_id", encoded = false) vendorId: String,
        @Field("offer_template", encoded = false) offerTemplate: String,
        @Field("image_appearance", encoded = false) imageAppearance: String,
        @Field("heading", encoded = false) heading: String,
        @Field("discount", encoded = false) discount: String,
        @Field("brand_name", encoded = false) brandName: String,
        @Field("title", encoded = false) title: String,
        @Field("description", encoded = false) description: String,
        @Field("terms", encoded = false) terms: String,
        @Field("start_date", encoded = false) startDate: String,
        @Field("end_date", encoded = false) endDate: String,
        @Field("token", encoded = false) token: String
    ): Response< OfferUpdateModel>
   @Multipart
   @POST("api/vendor/offers-store")
   suspend fun addOffers(
       @PartMap data: Map<String, @JvmSuppressWildcards RequestBody>,
       @Part brandlogo: MultipartBody.Part?,
       @Part media: List<MultipartBody.Part?>  // Note: same name used repeatedly for media[]
   ):Response<success_model>
   @GET("api/vendor/offers")
   suspend fun homeOffers(@Query("token")token: String):Response<HomeOfferModel>
    @FormUrlEncoded
   @POST("api/vendor/cart-offers/details")
   suspend fun getQrOfferDetail(@Field("offer_id") offer_id:String,@Field("token")token:String,@Field("customer_id")customer_id:String):Response<QrModel>

    @FormUrlEncoded
    @POST("api/vendor/offers-destroy/{id}")
    suspend fun offer_destroy(@Path("id") id:String, @Field("token")token:String,@Field("deleted_reason")delete:String):Response<success_model>

    @POST("api/vendor/cart-offers/redeem")
    suspend fun offer_redeem(@Query("offer_id") offer_id:String,@Query("token")token:String,@Query("customer_id")customer_id:String):Response<offerRedeemModel>

    @GET("api/vendor/profile")
    suspend fun FetchProfile(@Query("token")token: String):Response<vendor_details_model>
    @Multipart
    @POST("api/vendor/profile/update")
    suspend fun UpdateProfile(
        @PartMap data:Map<String,@JvmSuppressWildcards RequestBody>,
        @Part image:MultipartBody.Part,
    ):Response<update_profile_model>


    @GET("api/subscriptions")
    suspend fun Fetch_subs():Response<newsubscriptionModel>

    @POST("api/vendor/select-subscription")
    @FormUrlEncoded
    suspend fun Vendor_subs_choose(
        @Field("payment_transaction_id")transaction_id:String,
        @Field("subscription_id")subscription_id:String,
        @Field("status")status:Int,
        @Field("token")token: String):Response<select_subs_model>


    @POST("api/vendor/campaigns/store")
    @Multipart
    suspend fun Addcampaigns(
        @PartMap data:Map<String,@JvmSuppressWildcards RequestBody>,
        @Part banner:MultipartBody.Part,
    ):Response<EventRegisterModel>

    @GET("api/vendor/campaigns")
    suspend fun getcampaigns(
        @Query("token")token:String
    ):Response<EventModel>


    @GET("api/vendor/campaigns/show/{id}")
    suspend fun getcampaignDetail(@Path("id")id:String,@Query("token")token:String):Response<EventRegisterModel>
    @POST("api/vendor/campaigns/destroy/{id}")
    @FormUrlEncoded
    suspend fun campaigns_destroy(@Path("id")id:String,@Field("token")token:String):Response<success_model>

    @POST("api/vendor/campaigns/update/{id}")
    @Multipart
    suspend fun campaignUpdate(@Path("id")id:String, @PartMap data:Map<String,@JvmSuppressWildcards RequestBody>, @Part banner:MultipartBody.Part,):Response<success_model>



    @GET("api/vendor/events")
    suspend fun getevents(
        @Query("token")token:String
    ):Response<NewEventmodel>


    @POST("api/vendor/events/store")
    @Multipart
    suspend fun Addevents(
        @PartMap data:Map<String,@JvmSuppressWildcards RequestBody>,
        @Part banner:MultipartBody.Part,
    ):Response<NewEventDetailModel>

    @GET("api/vendor/events/show/{id}")
    suspend fun getEventDetail(@Path("id")id:String,@Query("token")token:String):Response<NewEventDetailModel>

    @POST("api/vendor/events/update/{id}")
    @Multipart
    suspend fun eventsUpdate(@Path("id")id:String, @PartMap data:Map<String,@JvmSuppressWildcards RequestBody>, @Part banner:MultipartBody.Part,):Response<success_model>

    @POST("api/vendor/events/destroy/{id}")
    @FormUrlEncoded
    suspend fun events_destroy(@Path("id")id:String,@Field("token")token:String):Response<success_model>

}