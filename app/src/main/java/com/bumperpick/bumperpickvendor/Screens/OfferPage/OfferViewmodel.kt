package com.bumperpick.bumperpickvendor.Screens.OfferPage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumperpick.bumperpickvendor.Repository.HomeOffer
import com.bumperpick.bumperpickvendor.Repository.OfferModel
import com.bumperpick.bumperpickvendor.Repository.OfferValidation
import com.bumperpick.bumperpickvendor.Repository.Result
import com.bumperpick.bumperpickvendor.Repository.VendorRepository
import com.bumperpick.bumperpickvendor.Repository.offerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OfferViewmodel(val offerRepository: offerRepository): ViewModel() {

   private val _listOfOffers =MutableStateFlow<List<HomeOffer>>(emptyList())
    val listOfOffers: StateFlow<List<HomeOffer>> =_listOfOffers


   private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading


   private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun getOffers(){
        viewModelScope.launch {
           val result= offerRepository.GetOffers()
            when(result){
                is Result.Error ->{ _error.value=result.message
                _loading.value=false}
                Result.Loading ->_loading.value=true
                is Result.Success -> {
                    _loading.value=false
                    _listOfOffers.value=result.data
                }
            }
        }

    }

}