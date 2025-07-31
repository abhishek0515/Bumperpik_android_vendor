package com.bumperpick.bumperpick_Vendor.Screens.Home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class HomePageviewmodel():ViewModel() {
    var selectedTab by mutableStateOf(0)
    fun onTabSelected(index:Int){
        selectedTab=index
    }

}