package com.bumperpick.bumperpick_Vendor.Screens.Home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.bumperpick.bumperpick_Vendor.Repository.MarketingOption
import com.bumperpick.bumperpick_Vendor.Screens.Account.AccountClick
import com.bumperpick.bumperpick_Vendor.Screens.Account.AccountScreen
import com.bumperpick.bumperpick_Vendor.Screens.Component.BottomNavigationBar

import com.bumperpick.bumperpick_Vendor.Screens.Component.NavigationItem
import com.bumperpick.bumperpick_Vendor.Screens.Component.No_offer
import com.bumperpick.bumperpick_Vendor.Screens.VendorDetailPage.Dashboard
import com.bumperpick.bumperpick_Vendor.Screens.OfferPage.OfferScreen
import com.bumperpick.bumperpick_Vendor.ui.theme.BtnColor
import com.bumperpick.bumperpick_Vendor.R
sealed class HomeScreenClicked() {
    data class CreateOffer(val marketingOption: MarketingOption, val isLater: Boolean=false) : HomeScreenClicked()
    data class EditOffer(val offerId:String):HomeScreenClicked()
    data class ViewRating(val offerId:String):HomeScreenClicked()
    data object ScanQR:HomeScreenClicked()
    data class AccountClicked(val AccountClick:AccountClick):HomeScreenClicked()



}
@Composable
fun HomeScreen(onClick:(HomeScreenClicked) -> Unit) {
    var selectedTab by remember { mutableStateOf(0) }

        Column(
            modifier = Modifier
                .fillMaxSize()


                .background(Color.White)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),

            ) {
                when(selectedTab){
                    0->{
                        OfferScreen(EditOffer={
                            println(it)
                            onClick(HomeScreenClicked.EditOffer(it))
                        },
                            viewRating = {
                                onClick(HomeScreenClicked.ViewRating(it))
                            })
                    }
                    1-> {
                        Dashboard()

                    }
                    2->{
                        No_offer(onSelectedOffer = {marketingOption, islater ->
                            onClick(HomeScreenClicked.CreateOffer(marketingOption,islater))
                        })
                    }
                    3->{
                     AccountScreen(onClick = {it->onClick(HomeScreenClicked.AccountClicked(it))  })
                    }
                }
               if(selectedTab!=2) {
                   FloatingActionButton(
                       containerColor = BtnColor,
                       onClick = { onClick(HomeScreenClicked.ScanQR) },
                       content = {
                           Icon(
                               painter = painterResource(R.drawable.icon),
                               contentDescription = "Scan QR", tint = Color.White
                           )
                       },
                       modifier = Modifier.align(Alignment.BottomEnd)
                           .padding(bottom = 16.dp, end = 16.dp).size(60.dp),
                       shape = CircleShape,
                   )
               }
            }

            val navItems = listOf(
                NavigationItem("Home", icon = Icons.Outlined.Home, contentDescription = "Home"),
                NavigationItem("Dashboard", icon_draw =R.drawable.dashboard_2_svgrepo_com , contentDescription = "Dashboard"),
                   NavigationItem("Create offers", icon = Icons.Default.List, contentDescription = "Create offers"),
                NavigationItem("More", icon_draw = R.drawable.more_horizontal_square_svgrepo_com, contentDescription = "Account")
            )
          //  RazorpayPaymentButton(amountInPaise = 1000, email = "anuj@gmail.com", contact = "1234567889")
            BottomNavigationBar(
                items = navItems,
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        }
    }










