package com.bumperpick.bumperpickvendor.Repository

import com.bumperpick.bumperpickvendor.API.FinalModel.EventModel
import com.bumperpick.bumperpickvendor.API.FinalModel.EventRegisterModel
import com.bumperpick.bumperpickvendor.API.FinalModel.NewEventDetailModel
import com.bumperpick.bumperpickvendor.API.FinalModel.NewEventmodel
import com.bumperpick.bumperpickvendor.API.Model.success_model
import com.bumperpick.bumperpickvendor.Screens.Event2.CreateEventModel


interface Event2Repository {

    suspend fun AddEvent(eventModel: CreateEventModel): Result<NewEventDetailModel>
    suspend fun getEvents(): Result<NewEventmodel>

    suspend fun getEventDetail(id:String): Result<NewEventDetailModel>

    suspend fun deleteEvent(id:String): Result<success_model>
    suspend fun updateEvent(eventModel: CreateEventModel):Result<success_model>
}