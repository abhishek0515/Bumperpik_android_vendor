package com.bumperpick.bumperickUser.Repository

import DataStoreManager
import android.content.Context
import com.bumperpick.bumperickUser.API.New_model.tickerdetails
import com.bumperpick.bumperickUser.API.New_model.ticket_add_model
import com.bumperpick.bumperickUser.API.New_model.ticketmessage
import com.bumperpick.bumperpick_Vendor.API.FinalModel.error_model
import com.bumperpick.bumperpick_Vendor.API.Model.success_model
import com.bumperpick.bumperpick_Vendor.API.Provider.ApiResult
import com.bumperpick.bumperpick_Vendor.API.Provider.ApiService
import com.bumperpick.bumperpick_Vendor.API.Provider.safeApiCall
import com.google.gson.Gson
import com.bumperpick.bumperpick_Vendor.Repository.Result

class SupportRepositoryImpl( private val apiService: ApiService,
                             private val dataStoreManager: DataStoreManager,
                             private val context: Context ) : SupportRepository {
    override suspend fun ticketadd(
        subject: String,
        message: String
    ): Result<ticket_add_model> {
        val params = HashMap<String, String>()

        val token = dataStoreManager.getToken()?.token?:""
        params["token"] =token
        params["subject"] =subject
        params["message"] =message
      

        val response= safeApiCall(
            api = {apiService.ticketAdd(params)},
            errorBodyParser = { json ->
                try {
                    Gson().fromJson(json, error_model::class.java)
                } catch (e: Exception) {
                    error_model(message = "Unknown error format: $json")
                }
            }
           )

        return when(response){
            is ApiResult.Success -> {
                if (response.data.code  in 200..300) Result.Success(response.data)
                else Result.Error(response.data.message?:"")
            }

            is ApiResult.Error -> Result.Error(response.error.message)
        }
    }

    override suspend fun tickets(): Result<ticketmessage> {
        val token = dataStoreManager.getToken()?.token?:""
        val response = safeApiCall(
           
            api = { apiService.tickets(token) },
            errorBodyParser = { json ->
                try {
                    Gson().fromJson(json, error_model::class.java)
                } catch (e: Exception) {
                    error_model(message = "Unknown error format: $json")
                }
            }
        )
        return when (response) {
            is ApiResult.Success -> {
                if (response.data.code  in 200..300) Result.Success(response.data)
                else Result.Error("Tickets not found")
            }

            is ApiResult.Error -> Result.Error(response.error.message)
        }
    }

    override suspend fun tickerDetails(id: String): Result<tickerdetails> {
        val token = dataStoreManager.getToken()?.token?:""
        val response = safeApiCall(
           
            api = { apiService.ticket_detail(id=id,token=token) },
            errorBodyParser = { json ->
                try {
                    Gson().fromJson(json, error_model::class.java)
                } catch (e: Exception) {
                    error_model(message = "Unknown error format: $json")
                }
            }
        )
        return when (response) {
            is ApiResult.Success -> {
                if (response.data.code  in 200..300) Result.Success(response.data)
                else Result.Error("Tickets not found")
            }

            is ApiResult.Error -> Result.Error(response.error.message)
        }
    }

    override suspend fun ticketReply(id: String,message: String): Result<success_model> {
        val params = HashMap<String, String>()
        val token = dataStoreManager.getToken()?.token?:""

        params["token"]=token
        params["message"]=message
        val response = safeApiCall(
          
            api = { apiService.ticket_reply(id=id, map = params) },
            errorBodyParser = { json ->
                try {
                    Gson().fromJson(json, error_model::class.java)
                } catch (e: Exception) {
                    error_model(message = "Unknown error format: $json")
                }
            }
        )
        return when (response) {
            is ApiResult.Success -> {
                if (response.data.code in 200..300) Result.Success(response.data)
                else Result.Error("Tickets not found")
            }

            is ApiResult.Error -> Result.Error(response.error.message)
        }
    }


}