package com.bumperpick.bumperpickvendor.Screens.Home

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import com.bumperpick.bumperpickvendor.R
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumperpick.bumperpickvendor.Repository.MarketingOption
import com.bumperpick.bumperpickvendor.Screens.Account.AccountScreen
import com.bumperpick.bumperpickvendor.Screens.Component.BottomNavigationBar
import com.bumperpick.bumperpickvendor.Screens.Component.LocationCard

import com.bumperpick.bumperpickvendor.Screens.Component.NavigationItem
import com.bumperpick.bumperpickvendor.Screens.Component.No_offer
import com.bumperpick.bumperpickvendor.Screens.OfferPage.OfferScreen
import com.bumperpick.bumperpickvendor.ui.theme.BtnColor
import com.bumperpick.bumperpickvendor.ui.theme.satoshi

sealed class HomeScreenClicked() {
    data class CreateOffer(val marketingOption: MarketingOption, val isLater: Boolean=false) : HomeScreenClicked()
    data class EditOffer(val offerId:String):HomeScreenClicked()
    data object Logout:HomeScreenClicked()
    data object ScanQR:HomeScreenClicked()



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
                    0-> {
                        OfferScreen(){
                            println(it)
                            onClick(HomeScreenClicked.EditOffer(it))
                        }
                    }
                    1->{
                        No_offer(onSelectedOffer = {marketingOption, islater ->
                            onClick(HomeScreenClicked.CreateOffer(marketingOption,islater))
                        })
                    }
                    2->{
                     AccountScreen(logout = {   onClick(HomeScreenClicked.Logout)})
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
                NavigationItem("Create offers", icon = Icons.Default.List, contentDescription = "Create offers"),
                NavigationItem("Account", icon = Icons.Outlined.AccountCircle, contentDescription = "Account")
            )

            BottomNavigationBar(
                items = navItems,
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        }
    }










