package com.bumperpick.bumperpickvendor.Navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Otp : Screen("otp/{$MOBILE_KEY}") {
        fun withMobile(mobile: String): String {
            return "otp/$mobile"
        }
    }
    object HomePage : Screen("homePage")
    object AddVendorDetails:Screen("addVendorDetails/{$MOBILE_KEY}/{$IS_MOBILE}"){
        fun withMobile(mobile: String,isMobile:Boolean=true): String {
            return "addVendorDetails/$mobile/$isMobile"
        }

    }
    companion object {
        const val MOBILE_KEY = "mobile"
        const val IS_MOBILE="isMobile"
    }
}
