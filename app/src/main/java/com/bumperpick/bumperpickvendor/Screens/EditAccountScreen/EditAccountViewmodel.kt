package com.bumperpick.bumperpickvendor.Screens.EditAccountScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumperpick.bumperpickvendor.Repository.Result
import com.bumperpick.bumperpickvendor.Repository.VendorRepository
import com.bumperpick.bumperpickvendor.Repository.Vendor_Category
import com.bumperpick.bumperpickvendor.Repository.Vendor_Details
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
data class EditAccountUiState(
    val vendorDetails: Vendor_Details? = null,
    val openingTime: String = "",
    val closingTime: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSaveEnabled: Boolean = false,
    val showOpeningTimePicker: Boolean = false,
    val showClosingTimePicker: Boolean = false
)

class EditAccountViewModel(val vendorRepository: VendorRepository): ViewModel() {

    private val _uiState = MutableStateFlow(EditAccountUiState())
    val uiState: StateFlow<EditAccountUiState> = _uiState.asStateFlow()

    private var originalVendorDetails: Vendor_Details? = null

    fun getVendorDetails(){
        viewModelScope.launch {
            val data=vendorRepository.getProfile()
            when(data){
                is Result.Error ->
                    _uiState.value=_uiState.value.copy(isLoading = false, errorMessage = data.message)
                Result.Loading ->{
                    _uiState.value=_uiState.value.copy(isLoading = true)
                }
                is Result.Success-> {
                    val vd=data.data.data
                    val vendorDetails = Vendor_Details(
                        Vendor_Id = vd.id.toString(),
                        Vendor_EstablishName = vd.establishment_name,
                        Vendor_brand = vd.brand_name,
                        Vendor_Email = vd.email,
                        Vendor_Mobile = vd.phone_number,
                        Vendor_Category = Vendor_Category(cat_id = "", cat_name = ""),
                        Establisment_Adress =vd.establishment_address,
                        Outlet_Address = vd.outlet_address,
                        GstNumber = vd.gst_number?:"",
                        GstPicUrl = null,
                        openingTime = "",
                        closingTime = "",
                        url_profile_image = vd.image_url
                    )
                    initializeWithVendorDetails(vendorDetails)

                }
            }
        }


    }

    fun initializeWithVendorDetails(vendorDetails: Vendor_Details) {
        originalVendorDetails = vendorDetails
        _uiState.value = _uiState.value.copy(
            vendorDetails = vendorDetails,
            openingTime = vendorDetails.openingTime,
            closingTime = vendorDetails.closingTime
        )
        checkIfSaveEnabled()
    }

    fun updateEstablishName(value: String) {
        _uiState.value.vendorDetails?.let { current ->
            val updated = current.copy(Vendor_EstablishName = value)
            _uiState.value = _uiState.value.copy(vendorDetails = updated, isLoading = false, errorMessage = null)
            checkIfSaveEnabled()
        }
    }

    fun updateBrand(value: String) {
        _uiState.value.vendorDetails?.let { current ->
            val updated = current.copy(Vendor_brand = value)
            _uiState.value = _uiState.value.copy(vendorDetails = updated)
            checkIfSaveEnabled()
        }
    }

    fun updateEmail(value: String) {
        _uiState.value.vendorDetails?.let { current ->
            val updated = current.copy(Vendor_Email = value)
            _uiState.value = _uiState.value.copy(vendorDetails = updated)
            checkIfSaveEnabled()
        }
    }

    fun updateMobile(value: String) {
        if (value.length <= 10) {
            _uiState.value.vendorDetails?.let { current ->
                val updated = current.copy(Vendor_Mobile = value)
                _uiState.value = _uiState.value.copy(vendorDetails = updated)
                checkIfSaveEnabled()
            }
        }
    }

    fun updateEstablishmentAddress(value: String) {
        _uiState.value.vendorDetails?.let { current ->
            val updated = current.copy(Establisment_Adress = value)
            _uiState.value = _uiState.value.copy(vendorDetails = updated)
            checkIfSaveEnabled()
        }
    }

    fun updateOutletAddress(value: String) {
        _uiState.value.vendorDetails?.let { current ->
            val updated = current.copy(Outlet_Address = value)
            _uiState.value = _uiState.value.copy(vendorDetails = updated)
            checkIfSaveEnabled()
        }
    }

    fun updateGstNumber(value: String) {
        if (value.length <= 15) {
            _uiState.value.vendorDetails?.let { current ->
                val updated = current.copy(GstNumber = value)
                _uiState.value = _uiState.value.copy(vendorDetails = updated)
                checkIfSaveEnabled()
            }
        }
    }

    fun updateOpeningTime(time: String) {
        _uiState.value = _uiState.value.copy(openingTime = time)
        _uiState.value.vendorDetails?.let { current ->
            val updated = current.copy(openingTime = time)
            _uiState.value = _uiState.value.copy(vendorDetails = updated)
        }
        checkIfSaveEnabled()
    }

    fun updateClosingTime(time: String) {
        _uiState.value = _uiState.value.copy(closingTime = time)
        _uiState.value.vendorDetails?.let { current ->
            val updated = current.copy(closingTime = time)
            _uiState.value = _uiState.value.copy(vendorDetails = updated)
        }
        checkIfSaveEnabled()
    }

    fun updateProfileImage(file:File){
        _uiState.value.vendorDetails?.let {current->
            val updated=current.copy(userImage = file)
            _uiState.value=_uiState.value.copy(vendorDetails = updated)
        }
    }

    fun copyEstablishmentToOutletAddress() {
        _uiState.value.vendorDetails?.let { current ->
            val updated = current.copy(Outlet_Address = current.Establisment_Adress)
            _uiState.value = _uiState.value.copy(vendorDetails = updated)
            checkIfSaveEnabled()
        }
    }

    fun showOpeningTimePicker() {
        _uiState.value = _uiState.value.copy(showOpeningTimePicker = true)
    }

    fun hideOpeningTimePicker() {
        _uiState.value = _uiState.value.copy(showOpeningTimePicker = false)
    }

    fun showClosingTimePicker() {
        _uiState.value = _uiState.value.copy(showClosingTimePicker = true)
    }

    fun hideClosingTimePicker() {
        _uiState.value = _uiState.value.copy(showClosingTimePicker = false)
    }

    private fun checkIfSaveEnabled() {
        val currentDetails = _uiState.value.vendorDetails
        val currentState = _uiState.value

        if (currentDetails == null) {
            _uiState.value = _uiState.value.copy(isSaveEnabled = false)
            return
        }

        val hasRequiredFields = currentDetails.Vendor_EstablishName.isNotBlank() &&
                currentDetails.Vendor_brand.isNotBlank() &&
                currentDetails.Vendor_Email.isNotBlank() &&
                currentDetails.Vendor_Mobile.isNotBlank() &&
                currentDetails.Establisment_Adress.isNotBlank() &&
                currentDetails.Outlet_Address.isNotBlank() &&
                currentState.openingTime.isNotBlank() &&
                currentState.closingTime.isNotBlank()

        val hasChanges = originalVendorDetails?.let { original ->
            currentDetails.Vendor_EstablishName != original.Vendor_EstablishName ||
                    currentDetails.Vendor_brand != original.Vendor_brand ||
                    currentDetails.Vendor_Email != original.Vendor_Email ||
                    currentDetails.Vendor_Mobile != original.Vendor_Mobile ||
                    currentDetails.Establisment_Adress != original.Establisment_Adress ||
                    currentDetails.Outlet_Address != original.Outlet_Address ||
                    currentDetails.GstNumber != original.GstNumber ||
                    currentState.openingTime != original.openingTime ||
                    currentState.closingTime != original.closingTime
        } ?: true

        _uiState.value = _uiState.value.copy(
            isSaveEnabled = hasRequiredFields && hasChanges
        )
    }

    fun saveProfile(onSuccess: (Vendor_Details) -> Unit, onError: (String) -> Unit) {
        val currentDetails = _uiState.value.vendorDetails
        if (currentDetails == null) {
            onError("Vendor details not found")
            return
        }

        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

                // Validate email format
                if (!isValidEmail(currentDetails.Vendor_Email)) {
                    throw IllegalArgumentException("Please enter a valid email address")
                }

                // Create final vendor details with updated opening/closing times
                val finalDetails = currentDetails.copy(
                    openingTime = _uiState.value.openingTime,
                    closingTime = _uiState.value.closingTime
                )

                val updateProfile=vendorRepository.updateProfile(finalDetails)
                when(updateProfile){
                    is Result.Error -> {
                        _uiState.value=_uiState.value.copy(errorMessage = updateProfile.message, isLoading = false)
                    }
                    Result.Loading -> {
                        _uiState.value=_uiState.value.copy(isLoading = true)

                    }
                    is Result.Success ->{
                        _uiState.value = _uiState.value.copy(isLoading = false, vendorDetails = finalDetails)
                        onSuccess(finalDetails)
                    }
                }



            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "An error occurred while saving"
                )
                onError(e.message ?: "An error occurred while saving")
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // Getter methods for UI
    fun getEstablishName(): String = _uiState.value.vendorDetails?.Vendor_EstablishName ?: ""
    fun getBrand(): String = _uiState.value.vendorDetails?.Vendor_brand ?: ""
    fun getEmail(): String = _uiState.value.vendorDetails?.Vendor_Email ?: ""
    fun getMobile(): String = _uiState.value.vendorDetails?.Vendor_Mobile ?: ""
    fun getEstablishmentAddress(): String = _uiState.value.vendorDetails?.Establisment_Adress ?: ""
    fun getOutletAddress(): String = _uiState.value.vendorDetails?.Outlet_Address ?: ""
    fun getGstNumber(): String = _uiState.value.vendorDetails?.GstNumber ?: ""
    fun getOpeningTime(): String = _uiState.value.openingTime
    fun getClosingTime(): String = _uiState.value.closingTime
    fun isLoading(): Boolean = _uiState.value.isLoading
    fun isSaveEnabled(): Boolean = _uiState.value.isSaveEnabled
    fun shouldShowOpeningTimePicker(): Boolean = _uiState.value.showOpeningTimePicker
    fun shouldShowClosingTimePicker(): Boolean = _uiState.value.showClosingTimePicker
    fun getProfileImage(): String ?= _uiState.value.vendorDetails?.url_profile_image
    fun getFileProfileImage():File?=_uiState.value.vendorDetails?.userImage


}