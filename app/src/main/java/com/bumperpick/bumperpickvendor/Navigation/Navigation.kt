package com.bumperpick.bumperpickvendor.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bumperpick.bumperpickvendor.Repository.MarketingOption

import com.bumperpick.bumperpickvendor.Screens.CreateOfferScreen.CreateOfferScreen
import com.bumperpick.bumperpickvendor.Screens.Home.HomeScreen
import com.bumperpick.bumperpickvendor.Screens.Home.HomeScreenClicked

import com.bumperpick.bumperpickvendor.Screens.Login.Login
import com.bumperpick.bumperpickvendor.Screens.OTP.OtpScreen
import com.bumperpick.bumperpickvendor.Screens.Splash.Splash
import com.bumperpick.bumperpickvendor.Screens.Subscription.SubscriptionPage
import com.bumperpick.bumperpickvendor.Screens.VendorDetailPage.VendorDetailPage
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.bumperpick.bumperpickvendor.Screens.Account.AccountClick
import com.bumperpick.bumperpickvendor.Screens.CreateOfferScreen.EditOffer
import com.bumperpick.bumperpickvendor.Screens.EditAccountScreen.EditAccount
import com.bumperpick.bumperpickvendor.Screens.QrScreen.QRScannerScreen

@Composable
fun snackbarHostExample(
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
): Pair<SnackbarHostState, suspend (String) -> Unit> {
    val scope = rememberCoroutineScope()

    val showSnackbar: suspend (String) -> Unit = { message ->
        snackbarHostState.showSnackbar(message)
    }

    SnackbarHost(hostState = snackbarHostState)

    return snackbarHostState to showSnackbar
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val (snackbarHostState, showSnackbar) = snackbarHostExample()

    var message by remember { mutableStateOf("") }
    LaunchedEffect(message) {
        if(message.isNotEmpty()){
            showSnackbar(message)
        }
    }

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
            },
                gotoHome = {
                    navController.navigate(Screen.HomePage.route) {
                        popUpTo(0) { inclusive = true }
                    }
                })
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
            HomeScreen {
                when (it) {
                   
                    is HomeScreenClicked.CreateOffer -> {
                        val islater=it.isLater
                        val marketingOption=it.marketingOption
                        when(marketingOption){
                            MarketingOption.OFFERS -> {
                                navController.navigate(Screen.CreateOfferScreen.route)
                            }
                            MarketingOption.CUSTOMER_ENGAGEMENT -> {
                                navController.navigate(Screen.Subscription.route)
                            }
                            MarketingOption.CONTEST_FOR_CUSTOMERS -> {}
                            MarketingOption.SCRATCH_AND_WIN -> {}
                            MarketingOption.LUCKY_DRAW -> {}
                            MarketingOption.CAMPAIGNS -> {}
                            MarketingOption.EVENTS -> {}
                        }
                       

                    }

                    is HomeScreenClicked.EditOffer -> {
                        navController.navigate(Screen.EditOffer.withOfferId(it.offerId))
                    }
                  is  HomeScreenClicked.AccountClicked->{
                      val accountclick=it.AccountClick
                      when(accountclick){
                          AccountClick.EditProfile -> {
                              navController.navigate(Screen.EditAccount.route)

                          }
                          AccountClick.Logout -> {
                              navController.navigate(Screen.Splash.route) {
                                  popUpTo(0) { inclusive = true }
                              }
                          }
                          AccountClick.Subscription ->
                              {

                              }
                      }
                  }



                    HomeScreenClicked.ScanQR -> navController.navigate(Screen.ScanQR.route)
                }
            }
        }
        composable(Screen.EditOffer.route
        , arguments = listOf (navArgument(Screen.offerId) {
                type = NavType.StringType
            })
        ){backStackEntry->
            val offerid = backStackEntry.arguments?.getString(Screen.offerId) ?: ""


            EditOffer(offerId =offerid , onBackPressed = {navController.popBackStack()}, onOfferDone = {})        }

        composable(Screen.Subscription.route){
            SubscriptionPage(gotoHome = {}, onClick = { navController.popBackStack()})
        }
        
        composable(Screen.CreateOfferScreen.route){
            CreateOfferScreen( onBack = {
                navController.popBackStack()
            })
        }
        composable(Screen.ScanQR.route){
             QRScannerScreen {
                 navController.popBackStack()
             }

        }

        composable(Screen.EditAccount.route){
            EditAccount {
                navController.popBackStack()
            }
        }
    }
}

