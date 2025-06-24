package com.bumperpick.bumperpickvendor.Screens.Subscription



import android.annotation.SuppressLint
import android.widget.Toast

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumperpick.bumperpickvendor.API.FinalModel.DataXXXX
import com.bumperpick.bumperpickvendor.API.FinalModel.FeatureValue
import com.bumperpick.bumperpickvendor.API.FinalModel.subscription_model
import com.bumperpick.bumperpickvendor.Repository.BillingCycle
import com.bumperpick.bumperpickvendor.Screens.Component.getMetalBrush

import com.bumperpick.bumperpickvendor.ui.theme.BtnColor
import com.bumperpick.bumperpickvendor.ui.theme.grey
import com.bumperpick.bumperpickvendor.ui.theme.satoshi
import com.bumperpick.bumperpickvendor.ui.theme.satoshi_bold
import com.bumperpick.bumperpickvendor.ui.theme.satoshi_medium
import com.bumperpick.bumperpickvendor.ui.theme.satoshi_regular
import org.koin.androidx.compose.koinViewModel
// UI Models for better
data class FeatureUI(
    val id: Int,
    val name: String,
    val type: String,
    val isIncluded: FeatureValue,
    val featureValue: String = ""
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
    val context= LocalContext.current
    // Handle buy subscription result
    LaunchedEffect(buySubscriptionState) {
        when (buySubscriptionState) {
            is SubscriptionViewModel.UiState.Success -> {
                gotoHome()
                Toast.makeText(context,"Subscription purchased",Toast.LENGTH_SHORT).show()
                viewModel.clearBuySubscriptionState()
            }
            is SubscriptionViewModel.UiState.Error -> {
                kotlinx.coroutines.delay(3000)
                viewModel.clearBuySubscriptionState()
            }
            else -> { /* No action needed */ }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(grey)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Simple Header matching the image
            SimpleHeader(onBackClick = onClick)

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
                    val data = (subscriptionState as SubscriptionViewModel.UiState.Success<subscription_model>).data
                    val plans = data.data

                    if (plans.isNotEmpty()) {
                        SimpleSubscriptionContent(
                            modifier = Modifier.weight(1f),
                            plans = plans,
                            selectedBillingCycle = selectedBillingCycle,
                            selectedSubsId = selectedSubsId,
                            onBillingCycleChanged = viewModel::updateBillingCycle,
                            onPlanSelected = { planId ->
                                viewModel.updateBillingCycle(selectedBillingCycle, planId)
                            },
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

        // Error message overlay
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
private fun SimpleHeader(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .background(Color.White)
            .padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back",
            modifier = Modifier
                .size(24.dp)
                .clickable { onBackClick() },
            tint = Color.Black
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

@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = Color(0xFFD32F2F)
        )
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
            Text(
                text = "Something went wrong",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message,
                fontSize = 14.sp,
                color = Color(0xFF666666)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFD32F2F)
                )
            ) {
                Text("Try Again", color = Color.White)
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
            color = Color(0xFF666666)
        )
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
private fun SimpleSubscriptionContent(
    modifier: Modifier,
    plans: List<DataXXXX>,
    selectedBillingCycle: BillingCycle,
    selectedSubsId: String,
    onBillingCycleChanged: (BillingCycle, String) -> Unit,
    onPlanSelected: (String) -> Unit,
    onContinueClick: () -> Unit,
    calculatePrice: (Int, BillingCycle) -> Int,
    getCycleDuration: (BillingCycle) -> String,
    isLoading: Boolean
) {
    val pagerState = rememberPagerState(pageCount = { plans.size })
    val currentPlan by derivedStateOf { plans.getOrNull(pagerState.currentPage) }

    // Auto-select current plan
    LaunchedEffect(pagerState.currentPage) {
        currentPlan?.let { plan ->
            onPlanSelected(plan.id.toString())
        }
    }

    Column(modifier = modifier) {
        // Plan Cards
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxWidth(),
                pageSpacing = 16.dp
            ) { page ->
                SimpleSubscriptionCard(
                    plan = plans[page],
                    selectedBillingCycle = selectedBillingCycle,
                    isSelected = selectedSubsId == plans[page].id.toString(),
                    onCardClick = { onPlanSelected(plans[page].id.toString()) },
                    onBillingCycleChanged = { cycle ->
                        onBillingCycleChanged(cycle, plans[page].id.toString())
                    },
                    calculatePrice = calculatePrice
                )
            }
        }

        // Page indicators
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(plans.size) { index ->
                Box(
                    modifier = Modifier
                        .size(
                            if (index == pagerState.currentPage)  18.dp else 12.dp)
                        .background(
                            if (index == pagerState.currentPage) Color(0xFFD32F2F) else Color(0xFFCCCCCC),
                            CircleShape
                        )
                )
                if (index < plans.size - 1) {
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }

        // Bottom Continue Button
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RectangleShape,
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                currentPlan?.let { plan ->
                    val price = plan.price.toDouble().toInt()
                    val calculatedPrice = calculatePrice(price, selectedBillingCycle)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Column(modifier=Modifier.padding(start = 16.dp)) {
                            Text(
                                text = "₹ $calculatedPrice",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Text(
                                text = getCycleDuration(selectedBillingCycle),
                                fontSize = 12.sp,
                                color = Color(0xFF666666)
                            )
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        Button(
                            onClick = onContinueClick,
                            modifier = Modifier
                                .width(150.dp)
                                .height(48.dp),
                            enabled = !isLoading && selectedSubsId.isNotEmpty(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = BtnColor,
                                disabledContainerColor = Color(0xFFCCCCCC)
                            ),
                            shape = RoundedCornerShape(8.dp)
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
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}


@Composable
fun SimpleSubscriptionCard(
    plan: DataXXXX,
    selectedBillingCycle: BillingCycle,
    isSelected: Boolean,
    onCardClick: () -> Unit,
    onBillingCycleChanged: (BillingCycle) -> Unit,
    calculatePrice: (Int, BillingCycle) -> Int
) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .clickable { onCardClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(
                  brush = getMetalBrush(plan.name)
                )
                .clip(shape =RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
        ) {
            // Gold Plan Header
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${plan.name} plan",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            Card(

                modifier = Modifier.fillMaxSize().weight(1f),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(18.dp)

                    ) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Features list",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,


                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Divider(modifier = Modifier.height(1.dp), color = Color.Gray)
                        Spacer(modifier = Modifier.height(16.dp))
                        plan.features.forEach { feature ->
                            val featureUI = FeatureUI(
                                id = feature.id,
                                name = feature.name,
                                isIncluded = feature.value,
                                type = feature.type
                            )
                            SimpleFeatureItem(feature = featureUI)
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        Divider(modifier = Modifier.height(1.dp), color = Color.Gray)
                        Spacer(modifier = Modifier.height(16.dp))

                        // Billing Cycle Selection - Horizontal Layout
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            BillingCycle.values().forEach { cycle ->
                                SimpleRadioButton(
                                    cycle = cycle,
                                    isSelected = selectedBillingCycle == cycle,
                                    onClick = { onBillingCycleChanged(cycle) }
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
fun SimpleFeatureItem(feature: FeatureUI) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = feature.name,
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier.weight(1f)
        )

        when (feature.isIncluded) {
            is FeatureValue.BooleanValue -> {
                if (feature.isIncluded.value) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .background(
                                Color(0xFF4CAF50),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Included",
                            tint = Color.White,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .background(
                                Color(0xFFD32F2F),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Not included",
                            tint = Color.White,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }
            is FeatureValue.StringValue -> {
                Text(
                    text = feature.isIncluded.value,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2196F3)
                )
            }
        }
    }
}

@Composable
fun SimpleRadioButton(
    cycle: BillingCycle,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable { onClick() }
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = Color(0xFFD32F2F),
                unselectedColor = Color(0xFF666666)
            )
        )
        Text(
            text = when (cycle) {
                BillingCycle.MONTHLY -> "3 Months"
                BillingCycle.QUARTERLY -> "6 Months"
                BillingCycle.ANNUALLY -> "1 Year"
                else -> cycle.displayName
            },
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier.padding(start = 2.dp)
        )
    }
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
            colors = CardDefaults.cardColors(containerColor = Color(0xFFD32F2F)),
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