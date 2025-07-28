package com.bumperpick.bumperickUser.API.New_model

import com.bumperpick.bumperpick_Vendor.API.FinalModel.Message
import com.bumperpick.bumperpick_Vendor.API.FinalModel.Sender

data class ticketmessage(
    val code: Int,
    val `data`: List<ticketmessagedata>
)
data class ticketmessagedata(
    val created_at: String,
    val id: Int,
    val messages: List<Message>,
    val sender: Sender,
    val status: String,
    val subject: String
)