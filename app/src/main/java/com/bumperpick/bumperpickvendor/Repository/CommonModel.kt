package com.bumperpick.bumperpickvendor.Repository

import android.net.Uri
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bumperpick.bumperpickvendor.API.FinalModel.Data
import com.bumperpick.bumperpickvendor.API.FinalModel.Media
import com.bumperpick.bumperpickvendor.R
import com.bumperpick.bumperpickvendor.Screens.CreateOfferScreen.ColorType
import com.bumperpick.bumperpickvendor.Screens.CreateOfferScreen.HeadingSize
import com.bumperpick.bumperpickvendor.Screens.CreateOfferScreen.RadialGradientType
import com.bumperpick.bumperpickvendor.Screens.CreateOfferScreen.getGradientByColorType
import org.w3c.dom.Text

import java.io.File


data class Vendor_Category(
    val cat_id:String="",
    val cat_name:String="",
)
data class Vendor_Details(
    val Vendor_Id:String,
    val Vendor_EstablishName:String="",
    val Vendor_brand:String="",
    val Vendor_Email:String="",
    val Vendor_Mobile:String="",
    val Vendor_Category:Vendor_Category=Vendor_Category(),
    val Establisment_Adress:String="",
    val Outlet_Address:String="",
    val GstNumber:String="",
    val GstPicUrl: File?=null

)
sealed class GoogleSignInState {
    data object Idle : GoogleSignInState()
    data object Loading : GoogleSignInState()
    data class Success(val email:String,val userAlreadyRegsitered:Boolean ) : GoogleSignInState()
    data class Error(val message: String) : GoogleSignInState()
}


// Data Classes
data class Plan(
    val name: String,
    val basePrice: Int,
    val gradientColors: List<Color>,
    val features: List<Feature>
)

data class Feature(
    val name: String,
    val type: FeatureType,
    val value: String? = null
)

enum class FeatureType {
    INCLUDED,
    UNLIMITED,
    LIMITED
}

enum class BillingCycle(val displayName: String) {
    MONTHLY("Monthly"),
    QUARTERLY("Quarterly"),
    ANNUALLY("Annual")
}
enum class MarketingOption(val title: String) {
    OFFERS("Offers"),
    CUSTOMER_ENGAGEMENT("Customer engagement"),
    CONTEST_FOR_CUSTOMERS("Contest for customers"),
    SCRATCH_AND_WIN("Scratch & win"),
    LUCKY_DRAW("Lucky draw"),
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
    val HeadingSize:HeadingSize?=null,
    val discount:TextType=TextType(text = ""),
    val brandName:TextType?=null,
    val logo:Uri?=null,
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
    val offerId:String="",
    val Type:MarketingOption?=null,
    val offerValid:OfferValidation?=null,
    val Media_list:List<String> = emptyList(),
    val discount:String="",
    val startDate:String="",
    val media:List<Media> =ArrayList(),
    val approval:String="",
    val endDate:String="",
    val active:String="",
    val offerTitle:String="",
    val brand_logo_url:String?="",
    val offerTag:String="",
    val offerDescription:String="",
    val termsAndCondition:String="",
)