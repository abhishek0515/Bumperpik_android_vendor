package com.bumperpick.bumperpickvendor.Repository

import com.bumperpick.bumperpickvendor.API.FinalModel.EventModel
import com.bumperpick.bumperpickvendor.API.FinalModel.EventRegisterModel
import com.bumperpick.bumperpickvendor.API.Model.success_model
import com.bumperpick.bumperpickvendor.Screens.Campaign.CreateCampaignModel

interface EventRepository {

    suspend fun AddEvent(eventModel: CreateCampaignModel): Result<EventRegisterModel>
    suspend fun getEvents(): Result<EventModel>

    suspend fun getEventDetail(id:String): Result<EventRegisterModel>

    suspend fun deleteEvent(id:String): Result<success_model>
    suspend fun updateEvent(eventModel: CreateCampaignModel):Result<success_model>
}