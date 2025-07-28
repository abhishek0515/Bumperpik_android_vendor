package com.bumperpick.bumperickUser.Repository

import com.bumperpick.bumperickUser.API.New_model.tickerdetails
import com.bumperpick.bumperickUser.API.New_model.ticket_add_model
import com.bumperpick.bumperickUser.API.New_model.ticketmessage
import com.bumperpick.bumperpick_Vendor.API.Model.success_model
import com.bumperpick.bumperpick_Vendor.Repository.Result
interface SupportRepository {

    suspend fun ticketadd(subject: String,message: String): Result<ticket_add_model>

    suspend fun tickets(): Result<ticketmessage>

    suspend fun tickerDetails(id: String): Result<tickerdetails>

    suspend fun ticketReply(id: String,message: String): Result<success_model>


}