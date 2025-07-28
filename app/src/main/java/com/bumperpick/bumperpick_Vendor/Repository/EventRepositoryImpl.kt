package com.bumperpick.bumperpick_Vendor.Repository

import DataStoreManager
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.bumperpick.bumperpick_Vendor.API.FinalModel.EventModel
import com.bumperpick.bumperpick_Vendor.API.FinalModel.EventRegisterModel
import com.bumperpick.bumperpick_Vendor.API.FinalModel.error_model
import com.bumperpick.bumperpick_Vendor.API.Model.success_model
import com.bumperpick.bumperpick_Vendor.API.Provider.ApiResult
import com.bumperpick.bumperpick_Vendor.API.Provider.ApiService
import com.bumperpick.bumperpick_Vendor.API.Provider.safeApiCall
import com.bumperpick.bumperpick_Vendor.API.Provider.toMultipartPart
import com.bumperpick.bumperpick_Vendor.Screens.Campaign.CreateCampaignModel

import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

fun uriToFile(context: Context, uri: Uri): File {
    val contentResolver = context.contentResolver
    val fileName = getFileNameFromUri(context, uri) ?: "upload_file"
    val fileExtension = fileName.substringAfterLast('.', ".tmp")
    val tempFile = File.createTempFile("upload_", fileExtension, context.cacheDir)

    contentResolver.openInputStream(uri)?.use { inputStream ->
        tempFile.outputStream().use { outputStream ->
            inputStream.copyTo(outputStream)
        }
    } ?: throw IllegalArgumentException("Unable to open URI: $uri")

    return tempFile
}
fun getFileNameFromUri(context: Context, uri: Uri): String? {
    val returnCursor = context.contentResolver.query(uri, null, null, null, null) ?: return null
    val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
    returnCursor.moveToFirst()
    val name = if (nameIndex != -1) returnCursor.getString(nameIndex) else null
    returnCursor.close()
    return name
}

class EventRepositoryImpl(val dataStoreManager: DataStoreManager,val apiService: ApiService,val context: Context):EventRepository {
    override suspend fun AddEvent(eventModel: CreateCampaignModel): Result<EventRegisterModel> {
        val token=dataStoreManager.getToken()!!.token
        val map = mutableMapOf<String, RequestBody>()
        val banner = uriToFile(context, eventModel.bannerImage!!)?.toMultipartPart(partName = "banner_image")
        map["title"] =  eventModel.title.toString().toRequestBody()
        map["description"] = eventModel.description.toString().toRequestBody()
        map["address"] = eventModel.address.toString().toRequestBody()
        map["start_date"] = eventModel.startDate.toString().toRequestBody()
        map["end_date"] = eventModel.endDate.toString().toRequestBody()
        map["token"] = token.toRequestBody()
        map["number_of_participant"]=eventModel.numberOfParticipant.toString().toRequestBody()

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


    fun String.toRequestBody(): RequestBody =
        this.toRequestBody("text/plain".toMediaTypeOrNull())
    override suspend fun updateEvent(eventModel: CreateCampaignModel): Result<success_model> {
        val map = mutableMapOf<String, RequestBody>()
        val token="Bearer ${dataStoreManager.getToken()!!.token}"
        val banner = eventModel.bannerImage?.let {
            uriToFile(context, it).toMultipartPart(partName = "banner_image")
        }


        map["title"] =  eventModel.title.toString().toRequestBody()
        map["description"] = eventModel.description.toString().toRequestBody()
        map["address"] = eventModel.address.toString().toRequestBody()
        map["start_date"] = eventModel.startDate.toString().toRequestBody()
        map["end_date"] = eventModel.endDate.toString().toRequestBody()
        map["number_of_participant"]=eventModel.numberOfParticipant.toString().toRequestBody()

        val event= safeApiCall(api = {
            if (banner != null)
                apiService.campaignUpdate(eventModel.id, token=token,map, banner)
            else
                apiService.campaignUpdateWithoutBanner(eventModel.id, token,map)},
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