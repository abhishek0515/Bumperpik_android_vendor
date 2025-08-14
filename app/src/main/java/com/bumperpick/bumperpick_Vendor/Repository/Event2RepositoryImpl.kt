package com.bumperpick.bumperpick_Vendor.Repository

import DataStoreManager
import android.content.Context
import com.bumperpick.bumperpick_Vendor.API.FinalModel.NewEventDetailModel
import com.bumperpick.bumperpick_Vendor.API.FinalModel.NewEventmodel
import com.bumperpick.bumperpick_Vendor.API.FinalModel.error_model
import com.bumperpick.bumperpick_Vendor.API.Model.success_model
import com.bumperpick.bumperpick_Vendor.API.Provider.ApiResult
import com.bumperpick.bumperpick_Vendor.API.Provider.ApiService
import com.bumperpick.bumperpick_Vendor.API.Provider.safeApiCall
import com.bumperpick.bumperpick_Vendor.API.Provider.toMultipartPart
import com.bumperpick.bumperpick_Vendor.Screens.Event2.CreateEventModel

import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class EventRepository2Impl(val dataStoreManager: DataStoreManager,val apiService: ApiService,val context: Context):Event2Repository {
    override suspend fun AddEvent(eventModel: CreateEventModel): Result<NewEventDetailModel> {
        val token=dataStoreManager.getToken()!!.token
        val vendorid=dataStoreManager.get_Vendor_Details()!!.vendor_id
        val map = mutableMapOf<String, RequestBody>()
        val banner = uriToFile(context, eventModel.bannerImage!!)?.toMultipartPart(partName = "banner_image")
        map["title"] =  eventModel.title.toString().toRequestBody("text/plain".toMediaType())
        map["description"] = eventModel.description.toString().toRequestBody("text/plain".toMediaType())
        map["address"] = eventModel.address.toString().toRequestBody("text/plain".toMediaType())
        map["start_date"] = eventModel.startDate.toString().toRequestBody("text/plain".toMediaType())
        map["end_date"] = eventModel.endDate.toString().toRequestBody("text/plain".toMediaType())
        map["start_time"] = eventModel.startTime.toString().toRequestBody("text/plain".toMediaType())
        map["token"] = token.toRequestBody("text/plain".toMediaType())
        map["end_time"] = eventModel.endTime.toString().toRequestBody("text/plain".toMediaType())
        map["facebook_link"]=eventModel.facebookLiveLink.toString().toRequestBody("text/plain".toMediaType())
        map["instagram_link"]=eventModel.instagramLiveLink.toString().toRequestBody("text/plain".toMediaType())
        map["youtube_link"]=eventModel.youtubeLiveLink.toString().toRequestBody("text/plain".toMediaType())
        map["vendor_id"]=vendorid.toString().toRequestBody("text/plain".toMediaType())
        val addEvent= safeApiCall(api = {
            apiService.Addevents(data = map, banner = banner!!)
        }, errorBodyParser = {
            try {
                Gson().fromJson(it, error_model::class.java)
            } catch (e: Exception) {
                error_model(message = "Unknown error format: $it")
            }
        }
        )
        return when (addEvent) {
            is ApiResult.Error -> Result.Error(addEvent.error.message)
            is ApiResult.Success -> Result.Success(addEvent.data)
        }

    }

    override suspend fun getEvents(): Result<NewEventmodel> {
        val token=dataStoreManager.getToken()!!.token
        val event= safeApiCall(api = {
            apiService.getevents(token)
        },
            errorBodyParser = {
                try {
                    Gson().fromJson(it, error_model::class.java)
                } catch (e: Exception) {
                    error_model(message = "Unknown error format: $it")
                }
            })


        return when (event) {
            is ApiResult.Error -> Result.Error(event.error.message)
            is ApiResult.Success -> Result.Success(event.data)
        }
    }

    override suspend fun getEventDetail(id: String): Result<NewEventDetailModel> {
        val token=dataStoreManager.getToken()!!.token
        val event= safeApiCall(api = {
            apiService.getEventDetail(id,token)
        },
            errorBodyParser = {
                try {
                    Gson().fromJson(it, error_model::class.java)
                } catch (e: Exception) {
                    error_model(message = "Unknown error format: $it")
                }
            })
        return when (event) {
            is ApiResult.Error -> Result.Error(event.error.message)
            is ApiResult.Success -> Result.Success(event.data)
        }

    }

    override suspend fun deleteEvent(id: String): Result<success_model> {
        val token=dataStoreManager.getToken()!!.token
        val event= safeApiCall(api = {
            apiService.events_destroy(id,token)
        },
            errorBodyParser = {
                try {
                    Gson().fromJson(it, error_model::class.java)
                } catch (e: Exception) {
                    error_model(message = "Unknown error format: $it")
                }
            }
        )

        return when(event){
            is ApiResult.Error -> Result.Error(event.error.message)
            is ApiResult.Success -> Result.Success(event.data)

        }
    }

    override suspend fun updateEvent(eventModel: CreateEventModel): Result<success_model> {
        val map = mutableMapOf<String, RequestBody>()
        val token="Bearer ${dataStoreManager.getToken()!!.token}"
        val vendorid=dataStoreManager.get_Vendor_Details()!!.vendor_id
        val banner = eventModel.bannerImage?.let {
            uriToFile(context, it).toMultipartPart(partName = "banner_image")
        }
        map["title"] =  eventModel.title.toString().toRequestBody("text/plain".toMediaType())
        map["description"] = eventModel.description.toString().toRequestBody("text/plain".toMediaType())
        map["address"] = eventModel.address.toString().toRequestBody("text/plain".toMediaType())
        map["end_date"] =eventModel.endDate.toString().toRequestBody("text/plain".toMediaType())
        map["start_date"] = eventModel.startDate.toString().toRequestBody("text/plain".toMediaType())
        map["start_time"] = eventModel.startTime.toString().toRequestBody("text/plain".toMediaType())
        map["end_time"] = eventModel.endTime.toString().toRequestBody("text/plain".toMediaType())
        map["token"] = token.toRequestBody("text/plain".toMediaType())
        map["facebook_link"]=eventModel.facebookLiveLink.toString().toRequestBody("text/plain".toMediaType())
        map["instagram_link"]=eventModel.instagramLiveLink.toString().toRequestBody("text/plain".toMediaType())
        map["youtube_link"]=eventModel.youtubeLiveLink.toString().toRequestBody("text/plain".toMediaType())
        map["vendor_id"]=vendorid.toString().toRequestBody("text/plain".toMediaType())
        val event= safeApiCall(api = {
            if (banner != null) {
                apiService.eventsUpdate(eventModel.id,token,map,banner)
            }
            else apiService.eventsUpdateWithoutBanner(eventModel.id,token,map)

        },
            errorBodyParser = {
                try {

                    Gson().fromJson(it, error_model::class.java)
                } catch (e: Exception) {
                    error_model(message = "Unknown error format: $it")
                }
            })

        return when(event){
            is ApiResult.Error->Result.Error(event.error.message)
            is ApiResult.Success->Result.Success(event.data)
        }
    }

}