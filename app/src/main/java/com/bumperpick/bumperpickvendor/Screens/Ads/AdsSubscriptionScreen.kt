package com.bumperpick.bumperpickvendor.Screens.Ads

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumperpick.bumperpickvendor.API.FinalModel.DataXXXXXXXXXXXXXX
import com.bumperpick.bumperpickvendor.API.FinalModel.FeatureXXXXXX
import com.bumperpick.bumperpickvendor.API.FinalModel.ads_package_model
import com.bumperpick.bumperpickvendor.Screens.QrScreen.UiState
import com.bumperpick.bumperpickvendor.Screens.Subscription.ErrorContent
import com.bumperpick.bumperpickvendor.Screens.Subscription.LoadingContent
import com.bumperpick.bumperpickvendor.ui.theme.BtnColor
import org.koin.androidx.compose.koinViewModel

@Composable
fun AdsSubscriptionScreen(
    viewModel: AdsViewModel = koinViewModel(),
    onBackClick: () -> Unit,
    gotoAds: () -> Unit
) {
    val adsSubsState by viewModel.adsSubsUiState.collectAsState()
    val subscribePackageUiState by viewModel.subscribePackageUiState.collectAsState()
    val context = LocalContext.current

    // Handle subscription state changes
    LaunchedEffect(subscribePackageUiState) {
        when (subscribePackageUiState) {
            is UiState.Error -> {
                Toast.makeText(
                    context,
                    (subscribePackageUiState as UiState.Error).message,
                    Toast.LENGTH_SHORT
                ).show()
            }
            is UiState.Success -> {
                gotoAds()
            }
            else -> { /* Handle other states if needed */ }
        }
    }

    // Load ads subscriptions on first composition
    LaunchedEffect(Unit) {
        viewModel.getAdsSub()
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
                title = "Choose Your Plan",
                subtitle = "Select the perfect ads subscription for you"
            )

            // Content based on state
            when (adsSubsState) {
                UiState.Empty -> {
                    // Show empty state
                    EmptyState(onRetry = { viewModel.getAdsSub() })
                }
                is UiState.Error -> {
                    ErrorContent(
                        message = (adsSubsState as UiState.Error).message,
                        onRetry = { viewModel.getAdsSub() }
                    )
                }
                UiState.Loading -> {
                    LoadingContent()
                }
                is UiState.Success -> {
                    val data = (adsSubsState as UiState.Success<ads_package_model>).data.data
                    if (data.isNotEmpty()) {
                        SubscriptionContent(
                            plans = data,
                            isLoading = subscribePackageUiState is UiState.Loading,
                            onSubscribe = { planId ->
                                viewModel.subscribePackage(planId, "TEST12345")
                            }
                        )
                    } else {
                        EmptyState(onRetry = { viewModel.getAdsSub() })
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyState(onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = "No plans",
            modifier = Modifier.size(64.dp),
            tint = Color.Gray
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No subscription plans available",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(
               containerColor = BtnColor)
        ) {
            Text(
                text = "Retry",
                color = Color.White
            )
        }
    }
}

@Composable
fun EnhancedHeader(
    onBackClick: () -> Unit,
    title: String,
    subtitle: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFFE8E8E8),
                        Color(0xFFF5F5F5),
                        Color(0xFFE8E8E8)
                    )
                )
            )
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                if (subtitle.isNotEmpty()) {
                    Text(
                        text = subtitle,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun SubscriptionContent(
    plans: List<DataXXXXXXXXXXXXXX>,
    isLoading: Boolean,
    onSubscribe: (String) -> Unit
) {
    // Only create pager state if plans are not empty
    if (plans.isEmpty()) {
        EmptyState(onRetry = {})
        return
    }

    val pagerState = rememberPagerState(pageCount = { plans.size })
    val selectedPlan by remember {
        derivedStateOf {
            plans.getOrNull(pagerState.currentPage)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Plan Cards
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxWidth(),
                pageSpacing = 16.dp,
                contentPadding = PaddingValues(horizontal = 0.dp)
            ) { page ->
                if (page < plans.size) {
                    EnhancedAdsSubscriptionCard(
                        plan = plans[page],
                        isSelected = page == pagerState.currentPage,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }

        // Page Indicator (only show if more than 1 plan)
        if (plans.size > 1) {
            PageIndicator(
                currentPage = pagerState.currentPage,
                pageCount = plans.size
            )
        }

        // Bottom Action Bar
        BottomActionBar(
            selectedPlan = selectedPlan,
            isLoading = isLoading,
            onSubscribe = onSubscribe
        )
    }
}

@Composable
fun PageIndicator(
    currentPage: Int,
    pageCount: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount) { index ->
            val isSelected = index == currentPage
            Box(
                modifier = Modifier
                    .size(if (isSelected) 12.dp else 8.dp)
                    .background(
                        if (isSelected) Color(0xFFD32F2F) else Color(0xFFCCCCCC),
                        CircleShape
                    )
                    .animateContentSize()
            )
            if (index < pageCount - 1) {
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}

@Composable
fun EnhancedAdsSubscriptionCard(
    plan: DataXXXXXXXXXXXXXX,
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 4.dp
        ),
        border = if (isSelected) BorderStroke(2.dp, Color(0xFFD32F2F)) else null
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Plan Header
            PlanHeader(planName = plan.name)

            // Features Content
            FeaturesContent(
                features = plan.features,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun PlanHeader(planName: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = getMetalBrush(planName)
                )
                .padding(8.dp)
        ) {
            Text(
                text = planName,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}
@Composable
fun FeaturesContent(
    features: List<FeatureXXXXXX>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Features",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        Divider(
            color = Color.Gray.copy(alpha = 0.3f),
            thickness = 1.dp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Features List
        features.forEachIndexed { index, feature ->
            FeatureItem(
                feature = FeatureUI(
                    id = feature.id,
                    name = feature.name,
                    isIncluded = when (feature.type) {
                        "boolean" -> FeatureValue.BooleanValue(feature.value == "1")
                        else -> FeatureValue.StringValue(feature.value)
                    },
                    type = feature.type
                )
            )

            if (index < features.size - 1) {
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun BottomActionBar(
    selectedPlan: DataXXXXXXXXXXXXXX?,
    isLoading: Boolean,
    onSubscribe: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            selectedPlan?.let { plan ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "₹${plan.price}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Text(
                            text = "per month",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = {
                            if (!isLoading) {
                                onSubscribe(plan.id.toString())
                            }
                        },
                        modifier = Modifier
                            .width(160.dp)
                            .height(50.dp),
                        enabled = !isLoading,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFD32F2F),
                            disabledContainerColor = Color(0xFFCCCCCC)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ShoppingCart,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Subscribe",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            } ?: run {
                // Fallback when no plan is selected
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Please select a plan",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun FeatureItem(feature: FeatureUI) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = feature.name.replace("_"," "),
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(8.dp))

        when (feature.isIncluded) {
            is FeatureValue.BooleanValue -> {
                FeatureStatusIcon(isIncluded = feature.isIncluded.value)
            }
            is FeatureValue.StringValue -> {
                Text(
                    text = feature.isIncluded.value,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2196F3),
                    textAlign = TextAlign.End
                )
            }
        }
    }
}

@Composable
fun FeatureStatusIcon(isIncluded: Boolean) {
    Box(
        modifier = Modifier
            .size(24.dp)
            .background(
                if (isIncluded) Color(0xFF4CAF50) else Color(0xFFD32F2F),
                CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = if (isIncluded) Icons.Default.Check else Icons.Default.Close,
            contentDescription = if (isIncluded) "Included" else "Not included",
            tint = Color.White,
            modifier = Modifier.size(16.dp)
        )
    }
}

// Helper function to create metallic gradient based on plan name
fun getMetalBrush(planName: String): Brush {
    return when (planName.toUpperCase()) {
        "STARTER" -> Brush.horizontalGradient(
            colors = listOf(
                Color(0xFFFFD700),
                Color(0xFFFFF8DC),
                Color(0xFFFFD700)
            )
        )
        "STANDARD" -> Brush.horizontalGradient(
            colors = listOf(
                Color(0xFFC0C0C0),
                Color(0xFFF5F5F5),
                Color(0xFFC0C0C0)
            )
        )
        "STARTER" -> Brush.horizontalGradient(
            colors = listOf(
                Color(0xFFCD7F32),
                Color(0xFFDEB887),
                Color(0xFFCD7F32)
            )
        )
        "PREMIUM" -> Brush.horizontalGradient(
            colors = listOf(
                Color(0xFFE5E4E2),
                Color(0xFFF8F8FF),
                Color(0xFFE5E4E2)
            )
        )
        else -> Brush.horizontalGradient(
            colors = listOf(
                Color(0xFFE8E8E8),
                Color(0xFFF5F5F5),
                Color(0xFFE8E8E8)
            )
        )
    }
}

// Data classes for feature UI
data class FeatureUI(
    val id: Int,
    val name: String,
    val isIncluded: FeatureValue,
    val type: String
)

sealed class FeatureValue {
    data class BooleanValue(val value: Boolean) : FeatureValue()
    data class StringValue(val value: String) : FeatureValue()
}