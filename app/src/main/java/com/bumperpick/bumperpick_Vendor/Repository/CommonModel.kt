package com.bumperpick.bumperpick_Vendor.Repository

import android.net.Uri
import androidx.compose.ui.graphics.Color
import com.bumperpick.bumperpick_Vendor.API.FinalModel.Media
import com.bumperpick.bumperpick_Vendor.R
import com.bumperpick.bumperpick_Vendor.Screens.CreateOfferScreen.ColorType
import com.bumperpick.bumperpick_Vendor.Screens.CreateOfferScreen.HeadingSize

import java.io.File


data class Vendor_Category(
    val cat_id:String="",
    val cat_name:String="",
)
data class Vendor_Details(
    val Vendor_Id:String,
    val profile_code:String="",
    val Vendor_EstablishName:String="",
    val Vendor_brand:String="",
    val Vendor_Email:String="",
    val Vendor_Mobile:String="",
    val Vendor_Category:Vendor_Category=Vendor_Category(),
    val Establisment_Adress:String="",
    val Outlet_Address:String="",
    val GstNumber:String="",
    val GstPicUrl: File?=null,
    val userImage:File?=null,
    val openingTime:String?="",
    val closingTime:String?="",
    val url_profile_image:String?=""

)
sealed class GoogleSignInState {
    data object Idle : GoogleSignInState()
    data object Loading : GoogleSignInState()
    data class Success(val email:String,val userAlreadyRegsitered:Boolean ) : GoogleSignInState()
    data class Error(val message: String) : GoogleSignInState()
}


// Data Classes

enum class BillingCycle(val displayName: String) {
    MONTHLY("Monthly"),
    QUARTERLY("Quarterly"),
    ANNUALLY("Annual")
}
enum class MarketingOption(val title: String) {
    OFFERS("Offers"),
   /* CONTEST_FOR_CUSTOMERS("Contest for customers"),
    SCRATCH_AND_WIN("Scratch & win"),
    LUCKY_DRAW("Lucky draw"),*/
    CAMPAIGNS("Campaigns"),
    EVENTS("Events");

    companion object {
        val allOptions = values().toList()
    }
}


data class TextType(
    val text: String = "",
    val bold: Boolean = false,
    val italic: Boolean = false,
    val fontSize: HeadingSize =HeadingSize.Medium, // Add font size control
    val color: Color = Color.Black // Add color control
)

data class Template_Data(
    val Logo: Uri? = null,
    val brandName: TextType = TextType(text = ""),
    val heading: TextType = TextType(text = ""),
    val subHeading: TextType = TextType(text = ""),
    val gradientType: ColorType = ColorType.BLUE,
    val decorativeImageRes: Int = R.drawable.template4
)
data class OfferTemplate(val type: OfferTemplateType, var colorType: ColorType)



enum class OfferTemplateType{
    Template1,Template2,Template3,Template4
}
data class OfferModel(
    val BannerImage: Uri?=null,
    val UrlBannerIMage:String="",
    val UrlMedialList:List<Media> =ArrayList(),
    val gradientType: OfferTemplateType?=null,
    val colorType: ColorType= ColorType.BLUE,
    val heading:TextType?=null,
    val subHeading: TextType?=null,
    val HeadingSize:HeadingSize?=null,
    val discount:TextType=TextType(text = ""),
    val toogleStockLast:Boolean?=null,
    val brandName:TextType?=null,
    val logo:Uri?=null,
    val subcat_id:Int?=null,
    val offerStartDate:String?=null,
    val offerEndDate:String?=null,
    val medialList:ArrayList<Uri> = ArrayList(),
    val productTittle:String?=null,
    val productDiscription:String?=null,
    val termsAndCondition:String?=null,
    val quantity:String?=null,

    )
enum class OfferValidation{
    Valid,Expired
}
data class HomeOffer(
    val offerId:String?="",
    val Type:MarketingOption?=null,
    val offerValid:OfferValidation?=null,
    val Media_list:List<String> = emptyList(),
    val discount:String?="",
    val startDate:String?="",
    val media:List<Media> =ArrayList(),
    val approval:String?="",
    val endDate:String?="",
    val active:String?="",
    val offerTitle:String?="",
    val brand_logo_url:String?="",
    val offerTag:String?="",
    val offerDescription:String?="",
    val termsAndCondition:String?="",
)
