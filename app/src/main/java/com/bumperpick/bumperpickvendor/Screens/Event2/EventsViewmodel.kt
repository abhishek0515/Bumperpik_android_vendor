package com.bumperpick.bumperpickvendor.Screens.Event2

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumperpick.bumperpickvendor.API.FinalModel.DataXXXXXXXX
import com.bumperpick.bumperpickvendor.API.FinalModel.DataXXXXXXXXX
import com.bumperpick.bumperpickvendor.API.FinalModel.DataXXXXXXXXXX
import com.bumperpick.bumperpickvendor.Repository.Event2Repository
import com.bumperpick.bumperpickvendor.Repository.EventRepository
import com.bumperpick.bumperpickvendor.Repository.OfferModel
import com.bumperpick.bumperpickvendor.Repository.Result
import com.bumperpick.bumperpickvendor.Screens.Component.formatDate

import com.bumperpick.bumperpickvendor.Screens.QrScreen.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import java.io.File

data class CreateEventModel(
    val id: String="",
    val bannerImage: Uri?=null,
    val bannerImageUrl:String?=null,
    val title:String="",
    val startDate:String="",
    val startTime:String="",
    val endDate: String="",
    val facebookLiveLink:String="",
    val youtubeLiveLink:String="",
    val instagramLiveLink:String="",
    val address: String="",
    val description: String="",


)
class Events2Viewmodel(val eventRepository: Event2Repository) : ViewModel() {

    private val _eventDetails= MutableStateFlow(CreateEventModel())
    val eventDetails: StateFlow<CreateEventModel> = _eventDetails



    private val _error=MutableStateFlow("")
    val error:StateFlow<String> = _error
    private val _loading= MutableStateFlow(false)
    val loading:StateFlow<Boolean> = _loading

    private val _eventAdded=MutableStateFlow(false)
    val eventAdded:StateFlow<Boolean> = _eventAdded

    fun updateEvent(eventId: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                _loading.value = true

                val currentDetails = _eventDetails.value

                // Call API to update event
                val success = eventRepository.updateEvent(currentDetails)

                when (success) {
                    is Result.Error -> {
                        showError(success.message)
                        onResult(false)
                    }
                    Result.Loading -> {
                        _loading.value = true
                    }
                    is Result.Success -> {
                        _loading.value = false
                        onResult(true)
                    }
                }


            } catch (e: Exception) {
                showError("Error updating event: ${e.message}")
                onResult(false)
            } finally {
                _loading.value = false
            }
        }
    }


    fun showError(message:String){
        _error.value=message
    }
    fun updateBannerImage(bannerImage:Uri){
        _eventDetails.value=eventDetails.value.copy(bannerImage = bannerImage)
    }
    fun updateTitle(title:String){
        _eventDetails.value=eventDetails.value.copy(title = title)
    }
    fun updateDescription(description:String){
        _eventDetails.value=eventDetails.value.copy(description = description)
    }
    fun updateAddress(address:String){
        _eventDetails.value=eventDetails.value.copy(address = address)
    }
   fun updateStartDate(date:String){
       _eventDetails.value=eventDetails.value.copy(startDate = date)
   }
    fun updateEndDate(date:String){
       _eventDetails.value=eventDetails.value.copy(endDate = date)
   }
    fun updateInstagramLink(link:String){
        _eventDetails.value=eventDetails.value.copy(instagramLiveLink = link)
    }
   fun updateStartTime(time:String){
       _eventDetails.value=eventDetails.value.copy(startTime = time)
   }
    fun updatefacebookLiveLink(facebookLiveLink:String){
        _eventDetails.value=eventDetails.value.copy(facebookLiveLink = facebookLiveLink)
    }
    fun updateYoutubeLiveLink(youtubeLiveLink:String){
        _eventDetails.value=eventDetails.value.copy(youtubeLiveLink = youtubeLiveLink)
    }

    fun validateEventDetails():Boolean {
     if (_eventDetails.value.title.isEmpty()) {
            showError("Please enter event title")
            return false
        } else if (_eventDetails.value.description.isEmpty()) {
            showError("Please enter event description")
            return false
        } else if (_eventDetails.value.address.isEmpty()) {
            showError("Please enter event address")
            return false
        }


        else if (_eventDetails.value.startDate.isEmpty()) {
            showError("Please select event start date")
            return false
        }
        else if (_eventDetails.value.endDate.isEmpty()) {
            showError("Please select event end date")
            return false
        } else if (_eventDetails.value.startTime.isEmpty()) {
            showError("Please select event time")
            return false
        }
     else
            return true
    }

    fun addEvent() {
        viewModelScope.launch {
            _loading.value=true
            val result = eventRepository.AddEvent(_eventDetails.value)
            when (result) {
                is Result.Error -> {
                    _loading.value=false
                    showError(result.message)
                }

                Result.Loading -> {_loading.value=true}
                is Result.Success -> {
                    _loading.value=false
                    if (result.data.code in 200..300) {
                        _eventAdded.value = true
                    } else {
                        showError(result.data.message)
                    }
                }

            }

        }
    }
    fun clearError(){
        _error.value=""

    }

    private val _uiEvents = MutableStateFlow<UiState<List<DataXXXXXXXXX>>>(UiState.Loading)
    val uiEvents: StateFlow<UiState<List<DataXXXXXXXXX>>> = _uiEvents

    fun getEvents() {
        viewModelScope.launch {
            _uiEvents.value = UiState.Loading
            val result = eventRepository.getEvents()
            when (result) {
                is Result.Error -> {
                    _uiEvents.value = UiState.Error(result.message)
                }
                Result.Loading -> {
                    _uiEvents.value = UiState.Loading
                }
                is Result.Success -> {
                    if (result.data.code in 200..299) {
                        _uiEvents.value = UiState.Success(result.data.data)
                    } else {
                        _uiEvents.value = UiState.Error(result.data.message)
                    }
                }
            }
        }
    }
    private val _uiEvent_Detail = MutableStateFlow<UiState<DataXXXXXXXXXX>>(UiState.Loading)
    val uiEvent_Detail: StateFlow<UiState<DataXXXXXXXXXX>> = _uiEvent_Detail

    fun getEventDetail(id:String) {
        viewModelScope.launch {
            _uiEvent_Detail.value = UiState.Loading
            val result = eventRepository.getEventDetail(id)
            when (result) {
                is Result.Error -> {
                    _loading.value=false
                    _uiEvent_Detail.value = UiState.Error(result.message)
                }
                Result.Loading -> {
                    _uiEvent_Detail.value = UiState.Loading
                }
                is Result.Success -> {
                    if (result.data.code in 200..299) {
                        val data=result.data.data
                        _loading.value=false

                        _eventDetails.value=_eventDetails.value.copy(
                            id=data.id.toString(),
                            bannerImageUrl=data.banner_image_url,
                            title=data.title?:"",
                            description=data.description?:"",
                            address=data.address?:"",
                            startDate=if(data.start_date.isNullOrEmpty())"" else   formatDate( data.start_date),
                            startTime = data.start_time?:"",
                            endDate  =if(data.end_date.isNullOrEmpty())"" else   formatDate( data.end_date),
                            facebookLiveLink = data.facebook_link?:"",
                            youtubeLiveLink = data.youtube_link?:"",
                            instagramLiveLink = data.instagram_link?:""


                      )
                        _uiEvent_Detail.value = UiState.Success(result.data.data)

                    } else {
                        _loading.value=false
                        _uiEvent_Detail.value = UiState.Error(result.data.message)
                    }
                }

            }

        }
    }
    fun cleardata(){
        _delete.value=""
    }

    private val _delete=MutableStateFlow<String>("")
    val delete: StateFlow<String> =_delete
    fun deleteOffer(selectedId: String, s: String) {

        viewModelScope.launch {
            val result=eventRepository.deleteEvent(selectedId)
            when(result){
                is Result.Error ->{ _error.value=result.message
                    _loading.value=false}
                Result.Loading ->_loading.value=true
                is Result.Success -> {
                    _loading.value=false
                    val data=result.data
                    if(data.code in 200..299){
                        _delete.value=data.message
                        getEvents()
                    }
                    else{
                        _error.value=data.message
                    }

                }

            }
        }
    }


}