package com.bumperpick.bumperpick_Vendor.Screens.CreateOfferScreen

import android.net.Uri
import android.util.Log
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumperpick.bumperpick_Vendor.API.FinalModel.Subcategory
import com.bumperpick.bumperpick_Vendor.Repository.OfferModel
import com.bumperpick.bumperpick_Vendor.Repository.OfferTemplateType
import com.bumperpick.bumperpick_Vendor.Repository.Result
import com.bumperpick.bumperpick_Vendor.Repository.Template_Data
import com.bumperpick.bumperpick_Vendor.Repository.TextType
import com.bumperpick.bumperpick_Vendor.Repository.offerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class startingChoose(){
    object UserBanner:startingChoose()
            object Template:startingChoose()
}
enum class StringType{
    BOLD,ITALIC
}
enum class BannerText{
    BrandName,Heading,SubHeading
}
enum class HeadingSize(val value: Dp){
    Large(24.dp),Medium(18.dp),Small(16.dp)
}


class CreateOfferViewmodel(private val offerRepository: offerRepository) : ViewModel() {

         private val _offerDetails= MutableStateFlow(OfferModel())
        val offerDetails: StateFlow<OfferModel> = _offerDetails
    private val _offerDetail= MutableStateFlow(OfferModel())
    val offerDetail: StateFlow<OfferModel> = _offerDetail
    private val _loading =MutableStateFlow(false)
     val loading =MutableStateFlow(false)
    
    

   private val _offerAdded=MutableStateFlow(false)
    val offerAdded:StateFlow<Boolean> = _offerAdded
    
    private val _subCatList= MutableStateFlow<List<Subcategory>>(emptyList())
    val subCatList:StateFlow<List<Subcategory>> =_subCatList
    
    fun getSubCategory() {
        viewModelScope.launch {


            val data = offerRepository.getSubcategory()
            when(data){
                is Result.Error -> {
                    Log.d("ERROR",data.message)
                }
                Result.Loading -> {}
                is Result.Success -> {_subCatList.value=data.data}
            }
        }
    }



    private val _templateData= MutableStateFlow(Template_Data(gradientType = offerDetails.value.colorType))
    val templateData: StateFlow<Template_Data> = _templateData

    private val _user_choosed_banner= MutableStateFlow<startingChoose?>(null)
    val user_choosed_banner: StateFlow<startingChoose?> = _user_choosed_banner

    private val _choosed_Template= MutableStateFlow<OfferTemplateType?>(null)
    val choosed_Template: StateFlow<OfferTemplateType?> = _choosed_Template

    fun ChoosedBanner(template:startingChoose){
        _user_choosed_banner.value=template
    }

    fun updateOffermedialList(list: List<Uri>){
        _offerDetails.value=offerDetails.value.copy(medialList = ArrayList(list))
    }
    fun updateStartDate(date:String){
        _offerDetails.value=offerDetails.value.copy(offerStartDate = date)
    }
    fun updateEndDate(date:String){
        _offerDetails.value=offerDetails.value.copy(offerEndDate = date)
    }

    fun ChoosedTemplate(template:OfferTemplateType){
        when(template){
            OfferTemplateType.Template1 -> {
                _offerDetail.value=offerDetails.value.copy(colorType = ColorType.BLUE)
                _templateData.value=templateData.value.copy(gradientType = ColorType.BLUE)
            }
            OfferTemplateType.Template2 ->{
                _offerDetail.value=offerDetails.value.copy(colorType = ColorType.RED)
                _templateData.value=templateData.value.copy(gradientType = ColorType.RED)

            }
            OfferTemplateType.Template3 -> {
            _offerDetail.value=offerDetails.value.copy(colorType = ColorType.ORANGE)
                _templateData.value=templateData.value.copy(gradientType = ColorType.ORANGE)
        }
            OfferTemplateType.Template4 -> {
                _offerDetail.value=offerDetails.value.copy(colorType = ColorType.PURPLE)
                _templateData.value=templateData.value.copy(gradientType = ColorType.PURPLE)
            }
        }

        _choosed_Template.value=template
        _offerDetails.value=offerDetails.value.copy(gradientType = template)
        _user_choosed_banner.value=startingChoose.Template
    }
    fun updateUserBanner(BannerImage:Uri){
        _offerDetails.value=offerDetails.value.copy(BannerImage = BannerImage)
    }
    fun update_Logo(Logo:Uri?){
        _templateData.value=templateData.value.copy( Logo= Logo)
        updateBrandName(newTextType = TextType(text = ""))
    }
    fun updateBrandName(newTextType: TextType) {
        _templateData.value = _templateData.value.copy(brandName = newTextType)
        _offerDetail.value=_offerDetail.value.copy(brandName = newTextType)
    }

    fun updateHeading(newTextType: TextType) {
        _templateData.value = _templateData.value.copy(heading = newTextType)
        _offerDetail.value=offerDetails.value.copy(heading = newTextType)
    }

    fun updateGradientType(colorType: ColorType){
        _templateData.value=templateData.value.copy(gradientType = (colorType))
          }

    fun updateSubHeading(newTextType: TextType) {
        _templateData.value = _templateData.value.copy(subHeading = newTextType)
        _offerDetail.value=offerDetails.value.copy(subHeading = newTextType)
    }
    fun changeHeadingSize(HeadingSize:HeadingSize){
        val headding=templateData.value.heading
        _templateData.value=templateData.value.copy(heading = templateData.value.heading.copy(fontSize = HeadingSize))
       
    }

    fun updateSubCat(subcatId:Int){
        _offerDetails.value=offerDetails.value.copy(subcat_id =subcatId)
    }




    fun validateBannerDetail():Boolean{
        if(_user_choosed_banner.value == startingChoose.UserBanner){
            if(_offerDetails.value.offerStartDate.isNullOrEmpty()){
                showError("Please select start date")
                return false
            }
         else if(_offerDetails.value.quantity==null){
                showError("Please enter quantity")
                return false
            }
            else if(_offerDetails.value.subcat_id==null){
                showError("Please select Sub Category")
                return false
            }
            else if(_offerDetails.value.BannerImage==null){
                showError("Please select banner")
                return false
            }
        }
        else if(_user_choosed_banner.value==startingChoose.Template){
                if(_templateData.value.brandName.text.isEmpty() ){
                    if(_templateData.value.Logo==null) {
                        showError("Please enter brand name or choose logo")
                        return false
                    }
                }
                else if(_offerDetails.value.quantity==null){
                    showError("Please enter quantity")
                    return false
                }
                else if(_templateData.value.heading.text.isNullOrEmpty()){
                    showError("Please enter heading")
                    return false
                }
                else if(_templateData.value.subHeading.text.isNullOrEmpty()){
                    showError("Please enter sub heading")
                    return false
                }
                else if(_offerDetails.value.subcat_id==null){
                    showError("Please select Sub Category")
                    return false
                }
                else if(offerDetails.value.discount.text.isEmpty()){
                    showError("Please enter discount")
                    return false
                }
            else if(offerDetails.value.offerStartDate.isNullOrEmpty()){
                showError("Please select start date")
                return false
            }


            }

        return true


    }

    fun validateConfirmOfferScreen():Boolean{
        if(_offerDetails.value.productTittle.isNullOrEmpty()){
            showError("Please enter product name")
            return false
        }

        else if(_offerDetails.value.productDiscription.isNullOrEmpty()){
            showError("Please enter product description")
            return false
        }
        else if(_offerDetails.value.termsAndCondition.isNullOrEmpty()){
            showError("Please enter terms and condition")
            return false
        }

        if(_offerDetails.value.medialList.isEmpty()){
            showError("Please select media")
            return false
        }
        return true


    }

    fun updateProductname(name:String){
        _offerDetails.value=offerDetails.value.copy(productTittle = name)
    }
    fun updateProductDescription(name:String){
        _offerDetails.value=offerDetails.value.copy(productDiscription = name)
    }
    fun updateTermsAndCondition(name:String){
        _offerDetails.value=offerDetails.value.copy(termsAndCondition = name)
    }
    fun updateQuantity(name:String?){
        _offerDetails.value=offerDetails.value.copy(quantity = name)
    }
    fun updateDissaount(name:TextType){
        _offerDetails.value=offerDetails.value.copy(discount = name)
        _offerDetail.value=offerDetails.value.copy(discount = name)
    }

    fun updateStockLast(boolean: Boolean){
        _offerDetails.value=offerDetails.value.copy(toogleStockLast = boolean)
    }

    fun AddDatatoServer() {
        viewModelScope.launch {
                _loading.value=true

               Log.d("AddOffer_vm",offerDetails.value.toString())
            val result = offerRepository.AddOffer(offerDetails.value,templateData.value)

            Log.d("result",result.toString())
            when(result){
                is Result.Error ->{
                    _loading.value=false
                    showError(result.message)
                }
                Result.Loading -> {
                    _loading.value=true
                }
                is Result.Success -> {
                    if(result.data.code in 200..300) {
                        _offerAdded.value = true
                        _loading.value=false
                    }
                    else{
                        _loading.value=false
                        showError(result.data.message)
                    }
                }

            }

        }
    }

    private val _error=MutableStateFlow("")
    val error:StateFlow<String> = _error



    fun showError(message:String){
        _error.value=message
    }
    fun clearError(){
        _error.value=""

    }


    /*fun getOfferDetails(offerId:String){
        viewModelScope.launch {
            val result=offerRepository.getOfferDetails(offerId)
            when(result){
                is Result.Error ->{
                    showError(result.message)
                }
                Result.Loading -> {

                }
                is Result.Success -> {
                    _offerDetail.value=result.data

                }
            }
        }

    }
*/
    private val _newMediaList = MutableStateFlow<List<MediaItem>>(emptyList())
    val newMediaList = _newMediaList.asStateFlow()

    private val _deletedMediaList = MutableStateFlow<List<String>>(emptyList())
    val deletedMediaList = _deletedMediaList.asStateFlow()

    fun addNewMedia(mediaItem: MediaItem) {
        _newMediaList.value = _newMediaList.value + mediaItem
    }

    fun removeNewMedia(mediaItem: MediaItem) {
        _newMediaList.value = _newMediaList.value - mediaItem
    }

    fun markMediaAsDeleted(mediaUrl: String) {
        _deletedMediaList.value = _deletedMediaList.value + mediaUrl
    }

    fun restoreDeletedMedia(mediaUrl: String) {
        _deletedMediaList.value = _deletedMediaList.value - mediaUrl
    }

    fun updateOffer(
        offerId: String,
        onSuccess: () -> Unit,
        deletedUrlMedia: ArrayList<String>,
        newLocalMedia: ArrayList<Uri>
    ) {
        // Implementation for updating offer with new and deleted media
    }

    fun updateNewLocalMediaList(newLocalMediaList: ArrayList<Uri>) {

    }

    fun updateDeletedUrlMediaList(deletedUrlMediaList: ArrayList<String>) {

    }


}