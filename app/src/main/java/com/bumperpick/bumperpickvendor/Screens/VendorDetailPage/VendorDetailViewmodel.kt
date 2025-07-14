package com.bumperpick.bumperpickvendor.Screens.VendorDetailPage

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.compose.AsyncImage
import com.bumperpick.bumperpickvendor.API.FinalModel.Data
import com.bumperpick.bumperpickvendor.API.FinalModel.Media
import com.bumperpick.bumperpickvendor.API.FinalModel.dasboard_modek
import com.bumperpick.bumperpickvendor.R
import com.bumperpick.bumperpickvendor.Repository.HomeOffer
import com.bumperpick.bumperpickvendor.Repository.Result
import com.bumperpick.bumperpickvendor.Repository.VendorRepository
import com.bumperpick.bumperpickvendor.Repository.Vendor_Category
import com.bumperpick.bumperpickvendor.Repository.Vendor_Details
import com.bumperpick.bumperpickvendor.Screens.Component.PrimaryButton
import com.bumperpick.bumperpickvendor.Screens.Component.TextFieldView
import com.bumperpick.bumperpickvendor.Screens.CreateOfferScreen.CalendarBottomSheet
import com.bumperpick.bumperpickvendor.Screens.CreateOfferScreen.EditOfferViewmodel
import com.bumperpick.bumperpickvendor.Screens.CreateOfferScreen.OfferDateSelector
import com.bumperpick.bumperpickvendor.Screens.CreateOfferScreen.VideoThumbnail
import com.bumperpick.bumperpickvendor.Screens.CreateOfferScreen.isVideoFile
import com.bumperpick.bumperpickvendor.Screens.QrScreen.UiState
import com.bumperpick.bumperpickvendor.Screens.Subscription.SubscriptionViewModel
import com.bumperpick.bumperpickvendor.ui.theme.BtnColor
import com.bumperpick.bumperpickvendor.ui.theme.grey
import com.bumperpick.bumperpickvendor.ui.theme.satoshi_medium
import com.bumperpick.bumperpickvendor.ui.theme.satoshi_regular
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class VendorDetailViewmodel(val vendorRepository: VendorRepository) : ViewModel() {

    private val _vendorDetails = MutableStateFlow(Vendor_Details(Vendor_Id = ""))
    val vendorDetails: StateFlow<Vendor_Details> = _vendorDetails.asStateFlow()

    private val _savedVendorDetail=MutableStateFlow<Data?>(null)
    val savedVendorDetail:StateFlow<Data?> = _savedVendorDetail.asStateFlow()

    private val _dashboard =MutableStateFlow< UiState<dasboard_modek>>(
       UiState.Empty)
    val dashboardStats:StateFlow<UiState<dasboard_modek>> =_dashboard.asStateFlow()

    fun get_dashboard(){
        viewModelScope.launch {
            val result=vendorRepository.getDashboard()
          _dashboard.value=  when(result){
              is Result.Error ->{
                  UiState.Error(result.message)
              }
              Result.Loading ->UiState.Loading
              is Result.Success -> UiState.Success(result.data)
          }
        }
    }
    fun getSavedVendorDetail() {
        viewModelScope.launch {
            val result = vendorRepository.getSavedVendorDetail()
            when (result) {
                is Result.Success -> {
                    _savedVendorDetail.value = result.data
                }

                is Result.Error -> {
                    _savedVendorDetail.value = null
                }

                Result.Loading -> {}
            }
        }
    }



    private val _success = MutableStateFlow<String?>(null)
    val success: StateFlow<String?> = _success.asStateFlow()

    // Category state
    private val _categories = MutableStateFlow<List<Vendor_Category>>(emptyList())
    val categories: StateFlow<List<Vendor_Category>> = _categories.asStateFlow()

    private val _error =MutableStateFlow<String?>(null)
    val error:StateFlow<String?> = _error.asStateFlow()

    // Function to fetch categories from repository
    fun fetchCategories() {
        viewModelScope.launch {
            // Start with loading state if needed

            val result = vendorRepository.FetchCategory()
            when(result) {
                is Result.Success -> {
                    _categories.value = result.data
                }
                is Result.Error -> {
                    _categories.value = emptyList()
                    // Handle error, maybe set an error state
                }
                is Result.Loading -> {
                    // Handle loading state if needed
                }
            }
        }
    }

    // Total number of steps in registration process
     fun validateEstablishmentInfo(establishName: String, brand: String, email: String,mobileState: String) {
        when {
            establishName.isBlank() -> {
                _error.value = "Establishment name cannot be empty."
            }
            brand.isBlank() -> {
                _error.value = "Brand cannot be empty."
            }
            email.isBlank() -> {
                _error.value = "Email cannot be empty."
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                _error.value = "Invalid email format."
            }

            mobileState.isBlank() -> {
                _error.value = "Mobile number cannot be empty."
            }


            else -> {
                _error.value = null
            }
        }
    }

    fun updateEstablishmentInfo(
        establishName: String,
        brand: String,
        email: String,
        mobileState: String
    ) {

        _vendorDetails.update {
            it.copy(
                Vendor_EstablishName = establishName,
                Vendor_brand = brand,
                Vendor_Email = email,
                Vendor_Mobile = mobileState

            )

        }
    }

    fun updateCategory(category: Vendor_Category) {
        _vendorDetails.update { it.copy(Vendor_Category = category) }
    }

    fun updateAdditionalDetails(
        establishmentAddress: String,
        outletAddress: String,
        gstNumber: String,
        gstPicUrl: File?
    ) {
        _vendorDetails.update {
            it.copy(
                Establisment_Adress = establishmentAddress,
                Outlet_Address = outletAddress,
                GstNumber = gstNumber,
                GstPicUrl = gstPicUrl
            )
        }
    }
     fun validateAdditionalDetails(
        establishmentAddress: String,
        outletAddress: String,
        gstNumber: String,
        gstImage:File?
    ) {
        when {
            establishmentAddress.isBlank() -> {
                _error.value = "Establishment address cannot be empty."
            }
            outletAddress.isBlank() -> {
                _error.value = "Outlet address cannot be empty."
            }
            gstNumber.isBlank() -> {
                _error.value = "GST number cannot be empty."
            }
            !gstNumber.matches(Regex("^[A-Za-z0-9]{15}$")) -> {
                _error.value = "GST number must be 15 alphanumeric characters only."
            }
            gstImage==null->{
                _error.value ="GST Image is not selected"
            }

            else -> {
                _error.value = null
            }
        }
    }

    fun clearError(){

    _error.value = null
}
    fun check_errorIs_null():Boolean{
        return _error.value==null
    }
    fun update_error(error:String){
        _error.value = error
    }


    fun submitRegistration(number:String) {

        viewModelScope.launch {
            val result = vendorRepository.SavedDetail(details = _vendorDetails.value,number.replace(" ",""))
            when (result) {
                is Result.Success -> {
                    _success.value = "done"
                    _vendorDetails.update { it.copy(Vendor_Id = result.data) }
                }

                is Result.Error -> {
                    Log.d("ERROR_submit", result.message)
                    _success.value = null
                }

                Result.Loading -> _success.value=null
            }
        }
    }

}