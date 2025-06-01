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
import com.bumperpick.bumperpickvendor.Repository.BillingCycle
import com.bumperpick.bumperpickvendor.Repository.Feature
import com.bumperpick.bumperpickvendor.Repository.FeatureType
import com.bumperpick.bumperpickvendor.Repository.Plan
import com.bumperpick.bumperpickvendor.ui.theme.BtnColor
import com.bumperpick.bumperpickvendor.ui.theme.grey
import com.bumperpick.bumperpickvendor.ui.theme.satoshi
import com.bumperpick.bumperpickvendor.ui.theme.satoshi_bold
import com.bumperpick.bumperpickvendor.ui.theme.satoshi_medium
import com.bumperpick.bumperpickvendor.ui.theme.satoshi_regular
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnrememberedMutableState")
@Composable

fun SubscriptionPage(
    viewModel: SubscriptionViewModel = koinViewModel(),
    onClick: () -> Unit
) {
    val plans by viewModel.plans.collectAsState()
    val selectedBillingCycle by viewModel.selectedBillingCycle.collectAsState()
    val pagerState = rememberPagerState(pageCount = { plans.size })
    val currentPlan by derivedStateOf { plans.getOrNull(pagerState.currentPage) }




    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)) // Replace 'grey' with a specific color
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .background(Color.White)
                    .padding(top = 32.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
            ) {
                Row(horizontalArrangement = Arrangement.Start) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { onClick() }
                            .align(Alignment.CenterVertically)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Choose subscription",
                        fontSize = 20.sp,
                        fontFamily = satoshi_medium,
                        fontWeight = FontWeight.Bold,

                        color = Color.Black,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                }
            }

            // Subscription Cards Pager - Positioned between header and bottom card
            Column(modifier = Modifier.weight(1f)) {


                Box(
                    modifier = Modifier // Takes remaining space
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    contentAlignment = Alignment.Center

                ) {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 0.dp),
                        pageSpacing = 16.dp
                    ) { page ->
                        SubscriptionCard(
                            plan = plans[page],
                            selectedBillingCycle = selectedBillingCycle,
                            onBillingCycleChanged = viewModel::updateBillingCycle,
                            viewModel = viewModel
                        )
                    }



                }

            }
            Column() {
                if (plans.isNotEmpty()) { // Ensure plans is not empty
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
                                color = BtnColor
                            )
                            Spacer(modifier = Modifier.width(8.dp)) // Add spacing between dots
                        }
                    }
                }
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(0.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp,top=10.dp,start = 20.dp,end=20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = if (currentPlan != null) {
                                    "₹ ${
                                        viewModel.calculatePrice(
                                            currentPlan!!.basePrice,
                                            selectedBillingCycle
                                        )
                                    }"
                                } else {
                                    "₹ 0" // Fallback if currentPlan is null
                                },
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1A1A1A)
                            )
                            Text(
                                text = viewModel.getCycleDuration(selectedBillingCycle),
                                fontSize = 12.sp,
                                color = Color(0xFF666666)
                            )
                        }

                        Button(
                            onClick = { /* Handle continue */ },
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 30.dp)
                                .height(52.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = BtnColor
                            ),
                            shape = RoundedCornerShape(12.dp),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                        ) {
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
}


@Composable
fun SubscriptionCard(
    plan: Plan,
    selectedBillingCycle: BillingCycle,
    onBillingCycleChanged: (BillingCycle) -> Unit,
    viewModel: SubscriptionViewModel
) {
    Card(
        modifier = Modifier
            .fillMaxSize(),
        shape = RoundedCornerShape(20.dp),

        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Plan Header with Gradient
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.linearGradient(
                            colors = plan.gradientColors,
                            start = Offset(0f, 0f),
                            end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                        ),
                        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${plan.name} plan",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(8.dp),
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    fontFamily = satoshi_regular
                )
            }

            Column(
                modifier = Modifier.padding(20.dp).background( color = Color.White)
            ) {
                Text(
                    text = "Features list",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = satoshi_regular,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Divider(modifier = Modifier.height(1.dp), color = Color.Gray)
                Spacer(Modifier.height(10.dp))

                plan.features.forEach { feature ->
                    EnhancedFeatureItem(feature = feature)
                }

                // Billing Cycle Selection
                Text(
                    text = "Billing Cycle",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = satoshi_regular,
                    color = Color.Black,
                    modifier = Modifier.padding(top = 20.dp, bottom = 12.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth().align(Alignment.Start),
                    horizontalArrangement = Arrangement.Start

                ) {
                    BillingCycle.values().forEach { cycle ->
                        EnhancedRadioButtonItem(
                            cycle = cycle,
                            isSelected = selectedBillingCycle == cycle,
                            onClick = { onBillingCycleChanged(cycle) },
                            accentColor = BtnColor
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EnhancedFeatureItem(feature: Feature) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = feature.name,
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier.weight(1f),
            lineHeight = 18.sp
        )

        when (feature.type) {
            FeatureType.INCLUDED -> {
                Box(
                    modifier = Modifier
                        .size(22.dp)
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
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
            FeatureType.UNLIMITED -> {
                Text(
                    text = "Unlimited",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF0EA5E9),
                    modifier = Modifier
                        .background(
                            Color(0xFFEBF8FF),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
            FeatureType.LIMITED -> {
                Text(
                    text = feature.value ?: "0",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF0EA5E9),
                    modifier = Modifier
                        .background(
                            Color(0xFFEBF8FF),
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
            .clickable { onClick() }
            .padding(2.dp)
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
            modifier = Modifier.padding(start = 0.dp)
        )
    }
}

@Composable
fun EnhancedDotIndicator(isSelected: Boolean, color: Color) {
    Box(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .size(if (isSelected) 16.dp else 8.dp)
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