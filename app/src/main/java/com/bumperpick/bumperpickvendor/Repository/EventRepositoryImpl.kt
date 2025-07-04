package com.bumperpick.bumperpickvendor.Repository

import DataStoreManager
import android.content.Context
import android.net.Uri
import com.bumperpick.bumperpickvendor.API.FinalModel.EventModel
import com.bumperpick.bumperpickvendor.API.FinalModel.EventRegisterModel
import com.bumperpick.bumperpickvendor.API.FinalModel.error_model
import com.bumperpick.bumperpickvendor.API.Model.success_model
import com.bumperpick.bumperpickvendor.API.Provider.ApiResult
import com.bumperpick.bumperpickvendor.API.Provider.ApiService
import com.bumperpick.bumperpickvendor.API.Provider.safeApiCall
import com.bumperpick.bumperpickvendor.API.Provider.toMultipartPart
import com.bumperpick.bumperpickvendor.Screens.Event2.CreateEventModel
import com.bumperpick.bumperpickvendor.Screens.Events.CreateCampaignModel

import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

fun uriToFile(context: Context, uri: Uri): File {
    val inputStream = context.contentResolver.openInputStream(uri)
    val tempFile = File.createTempFile("upload_", ".png", context.cacheDir)
    if (inputStream != null) {
        tempFile.outputStream().use { outputStream ->
            inputStream.copyTo(outputStream)
        }
    }
    return tempFile
}
class EventRepositoryImpl(val dataStoreManager: DataStoreManager,val apiService: ApiService,val context: Context):EventRepository {
    override suspend fun AddEvent(eventModel: CreateCampaignModel): Result<EventRegisterModel> {
        val token=dataStoreManager.getToken()!!.token
        val map = mutableMapOf<String, RequestBody>()
        val banner = uriToFile(context, eventModel.bannerImage!!)?.toMultipartPart(partName = "banner_image")
        map["title"] =  eventModel.title.toString().toRequestBody("text/plain".toMediaType())
        map["description"] = eventModel.description.toString().toRequestBody("text/plain".toMediaType())
        map["address"] = eventModel.address.toString().toRequestBody("text/plain".toMediaType())
        map["start_date"] = eventModel.startDate.toString().toRequestBody("text/plain".toMediaType())
        map["end_date"] = eventModel.endDate.toString().toRequestBody("text/plain".toMediaType())
        map["token"] = token.toRequestBody("text/plain".toMediaType())
        map["number_of_participant"]=eventModel.numberOfParticipant.toString().toRequestBody("text/plain".toMediaType())

        val addEvent= safeApiCall(api = {
            apiService.Addcampaigns(data = map, banner = banner!!)
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


    override suspend fun getEvents(): Result<EventModel> {
        val token=dataStoreManager.getToken()!!.token
        val event= safeApiCall(api = {
            apiService.getcampaigns(token)
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

    override suspend fun getEventDetail(id: String): Result<EventRegisterModel> {
        val token=dataStoreManager.getToken()!!.token
        val event= safeApiCall(api = {
            apiService.getcampaignDetail(id,token)
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
            apiService.campaigns_destroy(id,token)
        },
            errorBodyParser = {
                try {
                    Gson().fromJson(it, error_model::class.java)
                } catch (e: Exception) {
                    error_model(message = "Unknown error format: $it")
                }
            })

        return when(event){
            is ApiResult.Error -> Result.Error(event.error.message)
            is ApiResult.Success -> Result.Success(event.data)

        }
    }



    override suspend fun updateEvent(eventModel: CreateCampaignModel): Result<success_model> {
        val map = mutableMapOf<String, RequestBody>()
        val token=dataStoreManager.getToken()!!.token
        val banner = uriToFile(context, eventModel.bannerImage!!)?.toMultipartPart(partName = "banner_image")
        map["title"] =  eventModel.title.toString().toRequestBody("text/plain".toMediaType())
        map["description"] = eventModel.description.toString().toRequestBody("text/plain".toMediaType())
        map["address"] = eventModel.address.toString().toRequestBody("text/plain".toMediaType())
        map["start_date"] = eventModel.startDate.toString().toRequestBody("text/plain".toMediaType())
        map["end_date"] = eventModel.endDate.toString().toRequestBody("text/plain".toMediaType())
        map["token"] = token.toRequestBody("text/plain".toMediaType())
        map["number_of_participant"]=eventModel.numberOfParticipant.toString().toRequestBody("text/plain".toMediaType())
        val event= safeApiCall(api = {apiService.campaignUpdate(eventModel.id,map,banner!!)},
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