package com.bumperpick.bumperpickvendor.Screens.VendorDetailPage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumperpick.bumperpickvendor.Repository.Result
import com.bumperpick.bumperpickvendor.Repository.VendorRepository
import com.bumperpick.bumperpickvendor.Repository.Vendor_Category
import com.bumperpick.bumperpickvendor.Repository.Vendor_Details
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File


class VendorDetailViewmodel(val vendorRepository: VendorRepository) : ViewModel() {

    private val _vendorDetails = MutableStateFlow(Vendor_Details(Vendor_Id = ""))
    val vendorDetails: StateFlow<Vendor_Details> = _vendorDetails.asStateFlow()



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
        gstNumber: String
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
                    _success.value = result.data
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