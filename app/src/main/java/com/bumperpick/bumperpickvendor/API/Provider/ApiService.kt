package com.bumperpick.bumperpickvendor.API.Provider

import com.bumperpick.bumperpickvendor.API.FinalModel.HomeOfferModel
import com.bumperpick.bumperpickvendor.API.FinalModel.OfferUpdateModel
import com.bumperpick.bumperpickvendor.API.FinalModel.VendorLoginModel
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
        @Part gst_certificate: MultipartBody.Part?
   ):Response<VendorLoginModel>
   @GET("api/categories")
   suspend fun getCategory():Response<Category_Model>
   @FormUrlEncoded
   @POST("api/vendor/auth-google")
   suspend fun auth_google(@Field("email") email:String):Response<VendorLoginModel>
   @Multipart
   @POST("api/vendor/offers-update/{id}")
   suspend fun updateOffer(  @PartMap data: Map<String, @JvmSuppressWildcards RequestBody>,
                           @Part deletedmedia: List<MultipartBody.Part?>,
       @Part media: List<MultipartBody.Part?>):Response<OfferUpdateModel>
   @Multipart
   @POST("api/vendor/offers-store")
   suspend fun addOffers(
       @PartMap data: Map<String, @JvmSuppressWildcards RequestBody>,
       @Part brandlogo: MultipartBody.Part?,
       @Part media: List<MultipartBody.Part?>  // Note: same name used repeatedly for media[]
   ):Response<success_model>
   @GET("api/offers")
   suspend fun homeOffers(@Query("token")token: String):Response<HomeOfferModel>
    @FormUrlEncoded
    @POST("api/vendor/offers-destroy/{id}")
    suspend fun offer_destroy(@Path("id") id:String, @Field("token")token:String,@Field("deleted_reason")delete:String):Response<success_model>
}