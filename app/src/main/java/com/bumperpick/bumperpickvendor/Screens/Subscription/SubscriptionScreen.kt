package com.bumperpick.bumperpickvendor.Screens.Subscription



import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumperpick.bumperpickvendor.API.FinalModel.DataXXXX
import com.bumperpick.bumperpickvendor.API.FinalModel.FeatureValue
import com.bumperpick.bumperpickvendor.API.FinalModel.subscription_model
import com.bumperpick.bumperpickvendor.Repository.BillingCycle

import com.bumperpick.bumperpickvendor.ui.theme.BtnColor
import com.bumperpick.bumperpickvendor.ui.theme.grey
import com.bumperpick.bumperpickvendor.ui.theme.satoshi
import com.bumperpick.bumperpickvendor.ui.theme.satoshi_bold
import com.bumperpick.bumperpickvendor.ui.theme.satoshi_medium
import com.bumperpick.bumperpickvendor.ui.theme.satoshi_regular
import org.koin.androidx.compose.koinViewModel
// UI Models for better separation of concerns

data class FeatureUI(
    val id: Int,
    val name: String,
    val type: String,
    val isIncluded:FeatureValue,
    val FeatureValue:String=""
)



@Composable
fun SubscriptionPage(
    viewModel: SubscriptionViewModel = koinViewModel(),
    onClick: () -> Unit,
    gotoHome: () -> Unit = {}
) {
    val subscriptionState by viewModel.subscriptionState.collectAsState()
    val buySubscriptionState by viewModel.buySubscriptionState.collectAsState()
    val selectedBillingCycle by viewModel.selectedBillingCycle.collectAsState()
    val selectedSubsId by viewModel.selectedSubsId.collectAsState()

    // Handle buy subscription result
    LaunchedEffect(buySubscriptionState) {
        when (buySubscriptionState) {
            is SubscriptionViewModel.UiState.Success -> {
                // Navigate to success screen or show success message
                gotoHome()
                viewModel.clearBuySubscriptionState()
            }
            is SubscriptionViewModel.UiState.Error -> {
                // Show error message (you can implement SnackBar here)
                // For now, we'll just clear the error after some time
                kotlinx.coroutines.delay(3000)
                viewModel.clearBuySubscriptionState()
            }
            else -> { /* No action needed */ }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header
            SubscriptionHeader(onBackClick = onClick)

            // Content based on state
            when (subscriptionState) {
                is SubscriptionViewModel.UiState.Loading -> {
                    LoadingContent()
                }
                is SubscriptionViewModel.UiState.Error -> {
                    ErrorContent(
                        message = (subscriptionState as SubscriptionViewModel.UiState.Error).message,
                        onRetry = { viewModel.fetchSubscription() }
                    )
                }
                is SubscriptionViewModel.UiState.Success -> {
                    val data=
                        (subscriptionState as SubscriptionViewModel.UiState.Success<subscription_model>).data
                    val plans = data.data



                    if (plans.isNotEmpty()) {
                        SubscriptionContent(
                            modifier=Modifier.weight(1f),
                            plans = plans,
                            selectedBillingCycle = selectedBillingCycle,
                            selectedSubsId = selectedSubsId,
                            onBillingCycleChanged = viewModel::updateBillingCycle,
                            onContinueClick = { viewModel.buySubscription() },
                            calculatePrice = viewModel::calculatePrice,
                            getCycleDuration = viewModel::getCycleDuration,
                            isLoading = buySubscriptionState is SubscriptionViewModel.UiState.Loading
                        )
                    } else {
                        EmptyPlansContent()
                    }
                }
            }
        }

        // Error message overlay for buy subscription
        buySubscriptionState?.let { state ->
            if (state is SubscriptionViewModel.UiState.Error) {
                ErrorSnackbar(
                    message = state.message,
                    onDismiss = { viewModel.clearBuySubscriptionState() }
                )
            }
        }
    }
}

@Composable
private fun SubscriptionHeader(onBackClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .background(Color.White)
            .padding(top = 32.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onBackClick() }
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Choose subscription",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }
}

@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                color = Color(0xFF6366F1)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Loading subscription plans...",
                fontSize = 16.sp,
                color = Color(0xFF666666)
            )
        }
    }
}

@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Error",
                tint = Color(0xFFEF4444),
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Something went wrong",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message,
                fontSize = 14.sp,
                color = Color(0xFF666666),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6366F1)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Try Again",
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun EmptyPlansContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No subscription plans available",
            fontSize = 16.sp,
            color = Color(0xFF666666),
            textAlign = TextAlign.Center
        )
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
private fun SubscriptionContent(
   modifier : Modifier,
    plans: List<DataXXXX>,
    selectedBillingCycle: BillingCycle,
    selectedSubsId: String,
    onBillingCycleChanged: (BillingCycle, String) -> Unit,
    onContinueClick: () -> Unit,
    calculatePrice: (Int, BillingCycle) -> Int,
    getCycleDuration: (BillingCycle) -> String,
    isLoading: Boolean
) {
    val pagerState = rememberPagerState(pageCount = { plans.size })
    val currentPlan by derivedStateOf { plans.getOrNull(pagerState.currentPage) }

    Column(modifier = modifier) {
        // Subscription Cards Pager
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxWidth(),
                pageSpacing = 16.dp
            ) { page ->
                SubscriptionCard(
                    plan = plans[page],
                    selectedBillingCycle = selectedBillingCycle,
                    isSelected = selectedSubsId == plans[page].id.toString(),
                    onBillingCycleChanged = { cycle ->
                        onBillingCycleChanged(cycle, plans[page].id.toString())
                    }
                )
            }
        }

        // Page indicators
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(plans.size) { index ->
                EnhancedDotIndicator(
                    isSelected = index == pagerState.currentPage,
                    color = Color(0xFF6366F1)
                )
                if (index < plans.size - 1) {
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }

        // Bottom action card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(0.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = currentPlan?.let {
                            val price=it.price.toDouble().toInt()
                            "₹${calculatePrice(price, selectedBillingCycle)}"
                        } ?: "₹0",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1A1A)
                    )
                    Text(
                        text = getCycleDuration(selectedBillingCycle),
                        fontSize = 12.sp,
                        color = Color(0xFF666666)
                    )
                }

                Spacer(modifier = Modifier.width(30.dp))

                Button(
                    onClick = onContinueClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    enabled = !isLoading && selectedSubsId.isNotEmpty(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6366F1),
                        disabledContainerColor = Color(0xFFD1D5DB)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "Continue",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SubscriptionCard(
    plan: DataXXXX,
    selectedBillingCycle: BillingCycle,
    isSelected: Boolean,
    onBillingCycleChanged: (BillingCycle) -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxSize()
            .then(
                if (isSelected) {
                    Modifier.border(
                        2.dp,
                        Color(0xFF6366F1),
                        RoundedCornerShape(20.dp)
                    )
                } else {
                    Modifier
                }
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Plan Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = getGradient(plan.name),
                        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${plan.name} Plan",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(16.dp),
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
            }

            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .background(color = Color.White)
            ) {
                // Features Section
                Text(
                    text = "Features",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                HorizontalDivider(
                    modifier = Modifier.height(1.dp),
                    color = Color(0xFFE5E7EB)
                )

                Spacer(modifier = Modifier.height(16.dp))

                plan.features.forEach { feature ->
                    val featureUI=FeatureUI(id = feature.id,name=feature.name, isIncluded = feature.value,
                        type = feature.type)
                    EnhancedFeatureItem(feature = featureUI)
                }

                // Billing Cycle Selection
                Text(
                    text = "Billing Cycle",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    modifier = Modifier.padding(top = 24.dp, bottom = 12.dp)
                )

                Column {
                    BillingCycle.values().forEach { cycle ->
                        EnhancedRadioButtonItem(
                            cycle = cycle,
                            isSelected = selectedBillingCycle == cycle,
                            onClick = { onBillingCycleChanged(cycle) },
                            accentColor = Color(0xFF6366F1)
                        )
                    }
                }
            }
        }
    }
}

fun getGradient(name: String):Brush {
    return when(name){
        "Gold"-> goldenBrush
        "Silver"-> silverBrush
        "Platinum"-> platinumBrush
        else -> {
            goldenBrush}
    }


}
// Platinum Gradient
val platinumGradient = listOf(
    Color(0xFFE5E4E2), // Light platinum
    Color(0xFFD3D3D3), // Light gray
    Color(0xFFC0C0C0), // Silver
    Color(0xFFB8B8B8), // Medium gray
    Color(0xFF9E9E9E), // Gray
    Color(0xFF808080), // Dark gray
    Color(0xFF696969)  // Dim gray
)

// Silver Gradient
val silverGradient = listOf(
    Color(0xFFF8F8FF), // Ghost white
    Color(0xFFE6E6FA), // Lavender
    Color(0xFFDCDCDC), // Gainsboro
    Color(0xFFD3D3D3), // Light gray
    Color(0xFFC0C0C0), // Silver
    Color(0xFFA9A9A9), // Dark gray
    Color(0xFF808080)  // Gray
)

// Golden Gradient
val goldenGradient = listOf(
    Color(0xFFFFFAF0), // Floral white
    Color(0xFFFFEFD5), // Papaya whip
    Color(0xFFFFE135), // Lemon yellow
    Color(0xFFFFD700), // Gold
    Color(0xFFDAA520), // Golden rod
    Color(0xFFB8860B), // Dark golden rod
    Color(0xFF8B7355)  // Dark khaki
)

// Alternative richer golden gradient

// Usage example with Brush for Compose
val platinumBrush = Brush.linearGradient(
    colors = platinumGradient,
    start = Offset(0f, 0f),
    end = Offset(1000f, 1000f)
)

val silverBrush = Brush.linearGradient(
    colors = silverGradient,
    start = Offset(0f, 0f),
    end = Offset(1000f, 1000f)
)

val goldenBrush = Brush.linearGradient(
    colors = goldenGradient,
    start = Offset(0f, 0f),
    end = Offset(1000f, 1000f)
)

@Composable
fun EnhancedFeatureItem(feature: FeatureUI) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = feature.name,
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier.weight(1f),
            lineHeight = 20.sp
        )

        when (feature.isIncluded) {

            is FeatureValue.BooleanValue -> {
                if (feature.isIncluded.value) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(
                                Color(0xFF10B981),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Included",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                else {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(
                                Color(0xFFEF4444),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Not included",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
            is FeatureValue.StringValue -> {

                Text(
                    text = feature.isIncluded.value,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFDC2626),
                    modifier = Modifier
                        .background(
                            Color(0xFFFEE2E2),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun EnhancedRadioButtonItem(
    cycle: BillingCycle,
    isSelected: Boolean,
    onClick: () -> Unit,
    accentColor: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 4.dp)
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = accentColor,
                unselectedColor = Color(0xFFD1D5DB)
            )
        )
        Text(
            text = cycle.displayName,
            fontSize = 16.sp,
            color = if (isSelected) accentColor else Color.Black,
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
fun EnhancedDotIndicator(isSelected: Boolean, color: Color) {
    Box(
        modifier = Modifier
            .size(if (isSelected) 12.dp else 8.dp)
            .background(
                if (isSelected) color else Color(0xFFD1D5DB),
                CircleShape
            )
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
    )
}

@Composable
private fun ErrorSnackbar(
    message: String,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFEF4444)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = message,
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f)
                )
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Dismiss",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}