package com.bumperpick.bumperpickvendor.Screens.Subscription

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumperpick.bumperpickvendor.API.FinalModel.DataXXXX
import com.bumperpick.bumperpickvendor.API.FinalModel.Feature
import com.bumperpick.bumperpickvendor.API.FinalModel.FeatureValue
import com.bumperpick.bumperpickvendor.API.FinalModel.FeatureXX
import com.bumperpick.bumperpickvendor.API.FinalModel.SubscriptionX
import com.bumperpick.bumperpickvendor.Repository.BillingCycle
import com.bumperpick.bumperpickvendor.Screens.Account.AccountUi_state
import com.bumperpick.bumperpickvendor.Screens.Account.AccountViewmodel
import com.bumperpick.bumperpickvendor.Screens.Component.getMetalBrush
import com.bumperpick.bumperpickvendor.ui.theme.satoshi_bold
import com.bumperpick.bumperpickvendor.ui.theme.satoshi_regular
import org.koin.androidx.compose.koinViewModel
@Composable
fun SubscriptionXDetailPage(
    accountViewModel: AccountViewmodel = koinViewModel(),
    onBackPressed: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val uiState by accountViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        accountViewModel.fetchProfile()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Top App Bar
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp, start = 16.dp, end = 16.dp, bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackPressed) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }
                Text(
                    text = "My Subscription",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    modifier = Modifier.padding(start = 12.dp)
                )
            }
        }

        when (uiState) {
            is AccountUi_state.Loading -> {
                Box(Modifier.fillMaxSize(), Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is AccountUi_state.Error -> {
                Box(Modifier.fillMaxSize(), Alignment.Center) {
                    Text(
                        text = (uiState as AccountUi_state.Error).message,
                        color = Color.Red,
                        fontSize = 16.sp
                    )
                }
            }

            is AccountUi_state.GetProfile -> {
                (uiState as AccountUi_state.GetProfile).vendorDetail.data.subscription?.let { subscription ->
                    SubscriptionContent(subscription = subscription)
                } ?: Box(Modifier.fillMaxSize(), Alignment.Center) {
                    Text("No subscription found", color = Color.Gray, fontSize = 16.sp)
                }
            }

            is AccountUi_state.Empty -> {
                Box(Modifier.fillMaxSize(), Alignment.Center) {
                    Text("Loading...", color = Color.Gray, fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
fun SubscriptionContent(
    subscription: DataXXXX,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        SubscriptionXCard(subscription)
      //  SubscriptionTimelineCard(subscription)
    }
}

@Composable
fun SubscriptionXCard(
    subscription: DataXXXX,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
           // .heightIn(min = 250.dp)
            .clip(RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(getMetalBrush(subscription.name ?: ""))
                .padding(top = 8.dp, bottom = 0.dp)
        ) {
            Text(
                text = "${subscription.name} Plan",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Features List",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Divider(color = Color.LightGray, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(12.dp))

                    subscription.features.forEach { feature ->
                        val featureUI = FeatureUI(
                            id = feature.id,
                            name = feature.name,
                            isIncluded = feature.value,
                            type = feature.type
                        )
                        FeatureItem(feature        = featureUI)
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                    androidx.compose.material.Divider(modifier = Modifier.height(1.dp), color = Color.Gray)
                    Spacer(modifier=Modifier.height(12.dp))
                    if (!subscription.price.isNullOrEmpty()) {
                        Row(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (!subscription.time_period.isNullOrEmpty()) {
                                Text(
                                    text = "Subscription Type:- ${subscription.time_period}",
                                    fontSize = 18.sp,
                                    fontFamily = satoshi_bold,
                                    color = Color.Black,
                                    modifier = Modifier.padding(start = 4.dp, bottom = 2.dp)
                                )
                            }
                        }
                    }
                }
            }


        }
    }
}

@Composable
fun SubscriptionTimelineCard(
    subscription: DataXXXX,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "Subscription Timeline",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (!subscription.start_date.isNullOrEmpty()) {
                TimelineItem(
                    title = "Start Date",
                    date = subscription.start_date,
                    icon = Icons.Default.PlayArrow,
                    iconColor = Color(0xFF4CAF50)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (!subscription.end_date.isNullOrEmpty()) {
                TimelineItem(
                    title = "End Date",
                    date = subscription.end_date,
                    icon = Icons.Default.Info,
                    iconColor = Color(0xFFFF5722)
                )
            }
        }
    }
}

@Composable
fun TimelineItem(
    title: String,
    date: String,
    icon: ImageVector,
    iconColor: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = CircleShape,
            color = iconColor.copy(alpha = 0.1f)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier
                    .size(40.dp)
                    .padding(8.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray
            )
            Text(
                text = date,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }
}
