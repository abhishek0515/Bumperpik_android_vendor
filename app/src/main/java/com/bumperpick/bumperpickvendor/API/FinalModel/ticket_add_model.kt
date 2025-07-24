package com.bumperpick.bumperickUser.API.New_model

import com.bumperpick.bumperpickvendor.API.FinalModel.Sender

data class ticket_add_model(
    val code: Int,
    val `data`: ticket_add_model_data,
    val message: String
)
data class ticket_add_model_data(
    val created_at: String,
    val id: Int,
    val sender: Sender,
    val status: String,
    val subject: String
)