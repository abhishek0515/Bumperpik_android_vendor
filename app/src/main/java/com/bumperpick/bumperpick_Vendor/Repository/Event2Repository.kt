package com.bumperpick.bumperpick_Vendor.Repository

import com.bumperpick.bumperpick_Vendor.API.FinalModel.NewEventDetailModel
import com.bumperpick.bumperpick_Vendor.API.FinalModel.NewEventmodel
import com.bumperpick.bumperpick_Vendor.API.Model.success_model
import com.bumperpick.bumperpick_Vendor.Screens.Event2.CreateEventModel


interface Event2Repository {

    suspend fun AddEvent(eventModel: CreateEventModel): Result<NewEventDetailModel>
    suspend fun getEvents(): Result<NewEventmodel>

    suspend fun getEventDetail(id:String): Result<NewEventDetailModel>

    suspend fun deleteEvent(id:String): Result<success_model>
    suspend fun updateEvent(eventModel: CreateEventModel):Result<success_model>
}