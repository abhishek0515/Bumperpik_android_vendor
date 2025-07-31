package com.bumperpick.bumperpick_Vendor.Screens.VendorDetailPage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumperpick.bumperpick_Vendor.API.FinalModel.dasboard_modek
import com.bumperpick.bumperpick_Vendor.Screens.Component.LocationCard
import com.bumperpick.bumperpick_Vendor.Screens.QrScreen.UiState
import com.bumperpick.bumperpick_Vendor.Screens.Subscription.ErrorContent
import com.bumperpick.bumperpick_Vendor.Screens.Subscription.LoadingContent
import com.bumperpick.bumperpick_Vendor.ui.theme.BtnColor
import org.koin.androidx.compose.koinViewModel

@Composable
fun Dashboard(){
    val viewmodel = koinViewModel<VendorDetailViewmodel>()
    val savedetail by viewmodel.savedVendorDetail.collectAsState()
    val dashboardStats by viewmodel.dashboardStats.collectAsState()


    LaunchedEffect(Unit) {
        viewmodel.getSavedVendorDetail()
        viewmodel.get_dashboard()
    }
    var size by remember { mutableStateOf(IntSize.Zero) }
    val backgroundModifier = if (size.width > 0 && size.height > 0) {
        val radius = maxOf(size.width, size.height) / 1.5f
        Modifier.background(
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFF8B1538), Color(0xFF5A0E26)),
                center = Offset(size.width / 2f, size.height / 2f),
                radius = radius
            )
        )
    } else {
        Modifier.background(Color(0xFF8B1538))
    }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF5F7FA)
    ) {
        when(dashboardStats){
            UiState.Empty -> {}
            is UiState.Error -> {
                ErrorContent((dashboardStats as UiState.Error).message){
                    viewmodel.get_dashboard()
                }
            }
            UiState.Loading -> {
                LoadingContent()
            }
            is UiState.Success<dasboard_modek> -> {
                val data=(dashboardStats as UiState.Success<dasboard_modek>).data.dashboard_stats
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()

                    ,
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                )
                {
                    item {
                        // Header Section
                        LocationCard(
                            locationTitle = if (savedetail != null) {
                                savedetail!!.establishment_name
                            } else {
                                ""
                            },
                            locationSubtitle = if (savedetail != null) {
                                savedetail!!.establishment_address
                            } else {
                                ""
                            },
                        )
                    }
                    item {
                        // Welcome Message
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .clip(RoundedCornerShape(16.dp)),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            ),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .then(backgroundModifier)
                                    .padding(20.dp)
                            ) {
                                Column {
                                    Text(
                                        text = "Welcome Back!",
                                        color = Color.White,
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Here's your activities overview",
                                        color = Color.White.copy(alpha = 0.8f),
                                        fontSize = 14.sp,
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                }
                            }
                        }
                    }


                    item {
                        // Events Section
                        DashboardSection(
                            title = "Events",

                            activeCount =data.active_events_count , // Replace with actual data
                            expiredCount = data.expired_events_count,

                            )
                    }

                    item {
                        // Campaigns Section
                        DashboardSection(
                            title = "Campaigns",

                            activeCount = data.active_campaigns_count,
                            expiredCount = data.expired_events_count,

                            )
                    }
                    item {
                        // Campaigns Section
                        DashboardSection(
                            title = "Ads",

                            activeCount = data.active_ads_count,
                            expiredCount = data.expired_ads_count,

                            )
                    }

                    item {
                        DashboardSection_Offfer(
                            title = "Offers",
                            offer_availed_by_user=data.availed_offers_count,
                            activeCount = data.active_offers_count,
                            expiredCount = data.expired_offers_count,

                            )

                    }
                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                    }


                }

            }
        }

    }
}

@Composable
fun DashboardSection(
    title: String,


    activeCount: Int,
    expiredCount: Int,

) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Section Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {

                    Text(
                        text = title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1A1A)
                    )
                }
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "View All",
                    tint = Color(0xFF9E9E9E),
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Statistics Cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    title = "Active",
                    count = activeCount,
                    color = BtnColor,
                    modifier = Modifier.weight(1f)
                )

                StatCard(
                    title = "Expired",
                    count = expiredCount,
                    color = BtnColor,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}@Composable
fun DashboardSection_Offfer(
    title: String,
    offer_availed_by_user:Int,

    activeCount: Int,
    expiredCount: Int,

) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Section Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {

                    Text(
                        text = title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1A1A)
                    )
                }
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "View All",
                    tint = Color(0xFF9E9E9E),
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Statistics Cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    title = "Active",
                    count = activeCount,
                    color = BtnColor,
                    modifier = Modifier.weight(1f)
                )

                StatCard(
                    title = "Expired",
                    count = expiredCount,
                    color = BtnColor,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    title = "Offer Availed By Users",
                    count = offer_availed_by_user,
                    color = BtnColor,
                    modifier = Modifier.weight(1f)
                )

            }
        }
    }
}

@Composable
fun StatCard(
    title: String,
    count: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = count.toString(),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                text = title,
                fontSize = 12.sp,
                color = color.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun QuickActionsSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Quick Actions",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A1A)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickActionButton(
                    title = "Add Event",
                    icon = Icons.Default.Add,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
                QuickActionButton(
                    title = "New Campaign",
                    icon = Icons.Default.Add,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
                QuickActionButton(
                    title = "Create Offer",
                    icon = Icons.Default.Add,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun QuickActionButton(
    title: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                fontSize = 12.sp,
                color = color,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium
            )
        }
    }
}