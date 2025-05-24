package com.bumperpick.bumperpickvendor.Navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bumperpick.bumperpickvendor.Screens.Home.Homepage
import com.bumperpick.bumperpickvendor.Screens.Login.Login
import com.bumperpick.bumperpickvendor.Screens.OTP.OtpScreen
import com.bumperpick.bumperpickvendor.Screens.Splash.Splash
import com.bumperpick.bumperpickvendor.Screens.VendorDetailPage.VendorDetailPage

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Splash.route) {

        // Splash
        composable(Screen.Splash.route) {
            Splash(gotoScreen = {
                navController.navigate(it.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true } // optional
                }
            })
        }

        // Login
        composable(Screen.Login.route) {
            Login(onLoginSuccess = { mobile,isMobile ->
                if(isMobile) {
                    navController.navigate(Screen.Otp.withMobile(mobile)) {
                        // optional: avoid stacking multiple login instances
                        launchSingleTop = true
                    }
                }
                else{
                    navController.navigate(Screen.AddVendorDetails.withMobile(mobile,false)) {
                        // optional: avoid stacking multiple login instances
                        launchSingleTop = true
                    }
                }
            },)
        }

        // OTP
        composable(
            route = Screen.Otp.route,
            arguments = listOf(navArgument(Screen.MOBILE_KEY) {
                type = NavType.StringType
            }
           )
        ) { backStackEntry ->
            val mobile = backStackEntry.arguments?.getString(Screen.MOBILE_KEY) ?: ""

            OtpScreen(
                mobile = mobile,
                onBackClick = {
                    navController.popBackStack() // Goes back to Login
                },
                onOtpVerify = { nextScreen ->
                    when (nextScreen) {
                        is Screen.AddVendorDetails -> {
                            navController.navigate(Screen.AddVendorDetails.withMobile(mobile)) {
                                launchSingleTop = true
                            }
                        }
                        else -> {
                            navController.navigate(nextScreen.route) {
                                popUpTo(Screen.Otp.route) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    }
                }
            )
        }

        // Add Vendor Details
        composable(
            route = Screen.AddVendorDetails.route,
            arguments = listOf(navArgument(Screen.MOBILE_KEY) {
                type = NavType.StringType
            }, navArgument(Screen.IS_MOBILE){
                type=NavType.BoolType
            })
        ) { backStackEntry ->
            val mobile = backStackEntry.arguments?.getString(Screen.MOBILE_KEY) ?: ""
            val isMobile=backStackEntry.arguments?.getBoolean(Screen.IS_MOBILE)?:true
            VendorDetailPage(
                isMobile=isMobile,
                mobile = mobile,
                onBack = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                gotoHome = {
                    navController.navigate(Screen.HomePage.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // Home Page
        composable(Screen.HomePage.route) {
            Homepage()
        }
    }
}

