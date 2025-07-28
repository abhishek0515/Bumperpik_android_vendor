package com.bumperpick.bumperpick_Vendor.Screens.Ads


import android.util.Log
import androidx.compose.foundation.BorderStroke

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumperpick.bumperpick_Vendor.API.FinalModel.AdsSubscription
import com.bumperpick.bumperpick_Vendor.R
import com.bumperpick.bumperpick_Vendor.Screens.Component.formatDate
import com.bumperpick.bumperpick_Vendor.Screens.QrScreen.UiState
import com.bumperpick.bumperpick_Vendor.Screens.Subscription.ErrorContent
import com.bumperpick.bumperpick_Vendor.Screens.Subscription.LoadingContent
import com.bumperpick.bumperpick_Vendor.ui.theme.BtnColor
import org.koin.androidx.compose.koinViewModel

@Composable
fun adsCurrentPackage(
    viewModel: AdsViewModel = koinViewModel(),
    onBackClick: () -> Unit,){

    val user_ad_sub by viewModel.user_ad_sub.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.AdsSub()
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Enhanced Header
            EnhancedHeader(
                onBackClick = onBackClick,
                title = "Your Ad Subscription Plan",
                subtitle = "Latest Ad Subscription"
            )
            when(user_ad_sub){
                UiState.Empty ->    // Show empty state
                    EmptyState(onRetry = { viewModel.AdsSub() })
                is UiState.Error -> {
                    ErrorContent(
                        message = (user_ad_sub as UiState.Error).message,
                        onRetry = { viewModel.getAdsSub() }
                    )
                }
                UiState.Loading ->    LoadingContent()
                is UiState.Success -> {
                    val data = (user_ad_sub as UiState.Success<AdsSubscription?>).data
                    if (data!!.id!= null) {
                        Log.d("data",data.toString())

                        EnhancedAdsSubscriptionCard2(
                            plan = data,
                        )
                    } else {
                        ErrorContent(
                            message = "Please Subscribe Ad Package.",
                            onRetry = { viewModel.getAdsSub() }
                        )
                    }
                }

                }
            }
        }
    }




@Composable
fun EnhancedAdsSubscriptionCard2(
    plan: AdsSubscription,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.padding(16.dp),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, BtnColor),
        colors = CardDefaults.cardColors(containerColor = Color.White),

        ) {
        Column(
            modifier = Modifier
                .background(
                    brush = getMetalBrush1(plan.name?:"")
                )
                .clip(shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
        ) {
            // Plan name header
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = (plan.name)?:"".toUpperCase(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            // Features content with improved visibility
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                )
            ) {

                FeaturesContent(
                    features = plan.features,
                    modifier = Modifier.wrapContentHeight()
                )

                Divider(
                    modifier = Modifier
                        .height(1.dp)
                        .fillMaxWidth(),
                    color = Color.Gray.copy(alpha = 0.3f)
                )

                // Enhanced Date Section
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),

                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        // Subscription Period Header
                        Text(
                            text = "Subscription Period",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF2C3E50),
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        // Start Date
                        DateInfoRow(
                            label = "Start Date",
                            date = formatDate(plan.start_date),
                            icon = painterResource(R.drawable.calendar_alt),
                            iconColor = Color.Gray
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // End Date
                        DateInfoRow(
                            label = "End Date",
                            date = formatDate(plan.end_date),
                            icon = painterResource(R.drawable.calendar_alt),
                            iconColor = Color.Gray)
                    }
                }
            }
        }
    }
}

@Composable
private fun DateInfoRow(
    label: String,
    date: String,
    icon: Painter,
    iconColor: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                painter = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF5D6D7E)
            )
        }

        Text(
            text = date,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF2C3E50)
        )
    }
}

