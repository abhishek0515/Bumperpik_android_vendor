package com.bumperpick.bumperpickvendor.Repository

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
    data class Success(val userData: GoogleUserData) : GoogleSignInState()
    data class Error(val message: String) : GoogleSignInState()
}

data class GoogleUserData(
    val userId: String,
    val displayName: String?,
    val email: String?,
    val profilePictureUrl: String?,
    val idToken: String
)