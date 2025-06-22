package com.bumperpick.bumperpickvendor.API.Provider

import com.bumperpick.bumperpickvendor.API.FinalModel.HomeOfferModel
import com.bumperpick.bumperpickvendor.API.FinalModel.OfferUpdateModel
import com.bumperpick.bumperpickvendor.API.FinalModel.QrModel
import com.bumperpick.bumperpickvendor.API.FinalModel.VendorLoginModel
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


    @FormUrlEncoded
    @POST("api/vendor/offers-update/{id}")
    suspend fun updateOffer(
        @Path("id") offerId: String,
        @Field("vendor_id") vendorId: String,
        @Field("offer_template") offerTemplate: String,
        @Field("image_appearance") imageAppearance: String,
        @Field("heading") heading: String,
        @Field("discount") discount: String,
        @Field("brand_name") brandName: String,
        @Field("title") title: String,
        @Field("description") description: String,
        @Field("terms") terms: String,
        @Field("start_date") startDate: String,
        @Field("end_date") endDate: String,
        @Field("token") token: String,
   //     @Field("delete_media_ids") deleteMediaIds: String
    ): Response<OfferUpdateModel>

    @Multipart
    @POST("api/vendor/offers-update/{id}")
    suspend fun updateOffer2(
        @Path("id") id: String,
        @Part("vendor_id") vendorId: RequestBody,
        @Part("offer_template") offerTemplate: RequestBody,
        @Part("image_appearance") imageAppearance: RequestBody,
        @Part("heading") heading: RequestBody,
        @Part("discount") discount: RequestBody,
        @Part("brand_name") brandName: RequestBody,
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part("terms") terms: RequestBody,
        @Part("start_date") startDate: RequestBody,
        @Part("end_date") endDate: RequestBody,
        @Part("token") token: RequestBody,
       // @Part("delete_media_ids") deleteMediaIds: RequestBody, // Pass as comma-separated string
      //  @Part media: List<MultipartBody.Part>
    ): Response<OfferUpdateModel>
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
    suspend fun Fetch_subs():Response<subscription_model>

    @POST("api/vendor/select-subscription")
    @FormUrlEncoded
    suspend fun Vendor_subs_choose(@Field("subscription_id")subscription_id:String,@Field("token")token: String):Response<select_subs_model>



}