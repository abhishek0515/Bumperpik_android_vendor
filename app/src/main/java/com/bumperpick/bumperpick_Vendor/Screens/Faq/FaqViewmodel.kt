package com.bumperpick.bumperickUser.Screens.Faq

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope


import com.bumperpick.bumperpick_Vendor.API.FinalModel.faq
import com.bumperpick.bumperpick_Vendor.Repository.offerRepository
import com.bumperpick.bumperpick_Vendor.Screens.QrScreen.UiState
import com.bumperpick.bumperpick_Vendor.Screens.QrScreen.UiState.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import com.bumperpick.bumperpick_Vendor.Repository.Result

class FaqViewmodel(private val OfferRepository: offerRepository) : ViewModel(){

    private val _faq_uistate = MutableStateFlow<UiState<List<faq>>>(UiState.Empty)
    val faq_uistate: MutableStateFlow<UiState<List<faq>>> = _faq_uistate

    fun loadFaq(){
        viewModelScope.launch {
            val result=OfferRepository.FaqModel()
            _faq_uistate.value=
            when(result){
                is Result.Error -> Error(result.message)
                Result.Loading -> UiState.Loading
                is Result.Success ->{
                    val res=result.data
                    if(res.code>=200 && res.code<300){
                        Success(res.data)
                    }
                    else{
                        Error(res.message)
                    }
                }


            }

        }
    }
}