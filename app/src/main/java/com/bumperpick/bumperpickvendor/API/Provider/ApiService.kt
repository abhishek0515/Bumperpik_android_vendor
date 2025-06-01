package com.bumperpick.bumperpickvendor.API.Provider

import com.bumperpick.bumperpickvendor.API.Model.Category_Model
import com.bumperpick.bumperpickvendor.API.Model.Subscription
import com.bumperpick.bumperpickvendor.API.Model.Vendor_Register_Model
import com.bumperpick.bumperpickvendor.API.Model.success_model
import com.bumperpick.bumperpickvendor.API.Model.vendor_register_confirm
import com.bumperpick.bumperpickvendor.API.Model.vendor_subscription
import com.bumperpick.bumperpickvendor.API.Model.verify_otp
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

interface ApiService {
    @FormUrlEncoded
    @POST("api/vendor/send-otp")
    suspend  fun vendor_send_otp(@Field("phone_number") mobile_number: String): Response<success_model>

    @FormUrlEncoded
    @POST("api/vendor/resend-otp")
    suspend fun vendor_re_send_otp(@Field("phone_number") mobile_number: String): Response<success_model>

    @FormUrlEncoded
    @POST("api/vendor/verify-otp")
    suspend fun vendor_verify_otp(@Field("phone_number") mobile_number: String,@Field("otp") otp: String): Response<verify_otp>

    @Multipart
   @POST("api/vendor/register")
   suspend fun register_vendor(
        @PartMap data: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part gst_certificate: MultipartBody.Part
   ):Response<Vendor_Register_Model>


   @GET("api/categories")
   suspend fun getCategory():Response<Category_Model>

   @GET("api/subscriptions")
   suspend fun getSubcsription():Response<Subscription>

   @FormUrlEncoded
   @POST("api/vendor/select-subscription")
   suspend fun select_subscription(@Field("subscription_id")subscription_id:String,@Field("token")token:String):Response<vendor_subscription>

   @FormUrlEncoded
   @POST("api/vendor/registration-confirm")
   suspend fun registration_confirm(@FieldMap map: Map<String, String>):Response<vendor_register_confirm>

}