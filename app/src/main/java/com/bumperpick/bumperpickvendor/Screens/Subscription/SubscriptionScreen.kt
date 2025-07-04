package com.bumperpick.bumperpickvendor.Screens.Subscription

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumperpick.bumperpickvendor.API.FinalModel.Feature
import com.bumperpick.bumperpickvendor.API.FinalModel.FeatureValue
import com.bumperpick.bumperpickvendor.API.FinalModel.newsubscriptionModel
import com.bumperpick.bumperpickvendor.Repository.BillingCycle
import com.bumperpick.bumperpickvendor.Screens.Component.getMetalBrush
import com.bumperpick.bumperpickvendor.ui.theme.BtnColor
import com.bumperpick.bumperpickvendor.ui.theme.grey
import org.koin.androidx.compose.koinViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

// Data Models
data class SubscriptionPlan(
    val features: List<Feature>,
    val id: Int,
    val name: String,
    val price: String,
    val time_period: String
) {
    val priceAsInt: Int
        get() = price.toIntOrNull() ?: 0

    val billingCycle: BillingCycle
        get() = when (time_period.lowercase()) {
            "monthly", "1 month" -> BillingCycle.MONTHLY
            "quarterly", "3 months" -> BillingCycle.QUARTERLY
            "annually", "1 year" -> BillingCycle.ANNUALLY
            else -> BillingCycle.MONTHLY
        }
}

enum class SubscriptionType(val displayName: String) {
    GOLD("Gold"),
    PLATINUM("Platinum"),
    SILVER("Silver")
}

data class SubscriptionPlansResponse(
    val Gold: List<SubscriptionPlan>,
    val Platinum: List<SubscriptionPlan>,
    val Silver: List<SubscriptionPlan>
) {
    fun getAllPlans(): List<Pair<SubscriptionType, List<SubscriptionPlan>>> {
        return listOf(
            SubscriptionType.GOLD to Gold,
            SubscriptionType.PLATINUM to Platinum,
            SubscriptionType.SILVER to Silver
        ).filter { it.second.isNotEmpty() }
    }
}

data class FeatureUI(
    val id: Int,
    val name: String,
    val type: String,
    val isIncluded: FeatureValue,
    val featureValue: String = ""
)

@Preview
@Composable
private fun  previewSub(){
    SubscriptionPage(onBackClick = {})
}
// Main Composable
@Composable
fun SubscriptionPage(
    viewModel: SubscriptionViewModel = koinViewModel(),
    onBackClick: () -> Unit,
    gotoHome: () -> Unit = {}
) {
    val subscriptionState by viewModel.subscriptionState.collectAsState()
    val buySubscriptionState by viewModel.buySubscriptionState.collectAsState()
    val selectedSubsId by viewModel.selectedSubsId.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(buySubscriptionState) {
        when (buySubscriptionState) {
            is SubscriptionViewModel.UiState.Success -> {
                gotoHome()
                Toast.makeText(context, "Subscription purchased successfully", Toast.LENGTH_SHORT).show()
                viewModel.clearBuySubscriptionState()
            }
            is SubscriptionViewModel.UiState.Error -> {
                Toast.makeText(context, (buySubscriptionState as SubscriptionViewModel.UiState.Error).message, Toast.LENGTH_SHORT).show()
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
            SimpleHeader(onBackClick = onBackClick)
            when(subscriptionState){
                is SubscriptionViewModel.UiState.Error ->{
                    ErrorContent(
                        message = (subscriptionState as SubscriptionViewModel.UiState.Error).message,
                        onRetry = { viewModel.fetchSubscription() }
                    )
                }
                SubscriptionViewModel.UiState.Idle -> TODO()
                SubscriptionViewModel.UiState.Loading -> {
                    LoadingContent()
                }
                is SubscriptionViewModel.UiState.Success<*> -> {
                    val data = (subscriptionState as SubscriptionViewModel.UiState.Success<newsubscriptionModel>).data
                    val planGroups = data.data.getAllPlans()

                    if (planGroups.isNotEmpty()) {
                        SubscriptionContent(
                            modifier = Modifier.weight(1f),
                            planGroups = planGroups,
                            selectedSubsId = selectedSubsId,
                            onSubscriptionSelected = { subsId ->
                                viewModel.updateSelectedSubscription(subsId)
                            },
                            onContinueClick = { viewModel.buySubscription() },
                            getCycleDuration = viewModel::getCycleDuration,
                            isLoading = buySubscriptionState is SubscriptionViewModel.UiState.Loading
                        )
                    } else {
                        ErrorContent(
                            message = "No subscription plans available",
                            onRetry = { viewModel.fetchSubscription() }
                        )
                    }
                }
            }

        }

        if (buySubscriptionState is SubscriptionViewModel.UiState.Error) {
            ErrorSnackbar(
                message = (buySubscriptionState as SubscriptionViewModel.UiState.Error).message,
                onDismiss = { viewModel.clearBuySubscriptionState() }
            )
        }
    }
}

@Composable
private fun SimpleHeader(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()

            .background(Color.White)
            .padding(start = 16.dp, end = 16.dp, top = 0.dp, bottom = 24.dp),
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
private fun SubscriptionContent(
    modifier: Modifier,
    planGroups: List<Pair<SubscriptionType, List<SubscriptionPlan>>>,
    selectedSubsId: String,
    onSubscriptionSelected: (String) -> Unit,
    onContinueClick: () -> Unit,
    getCycleDuration: (BillingCycle) -> String,
    isLoading: Boolean
) {
    val pagerState = rememberPagerState(pageCount = { planGroups.size })
    var selectedPlan by remember { mutableStateOf<SubscriptionPlan?>(null) }

    LaunchedEffect(pagerState.currentPage, selectedSubsId) {
        val currentPlanGroup = planGroups.getOrNull(pagerState.currentPage)
        currentPlanGroup?.let { (_, plans) ->
            selectedPlan = plans.find { it.id.toString() == selectedSubsId } ?: plans.firstOrNull()
        }
    }

    Column(modifier = modifier) {
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
                val (subscriptionType, plans) = planGroups[page]
                SubscriptionCard(
                    subscriptionType = subscriptionType,
                    plans = plans,
                    selectedSubsId = selectedSubsId,
                    onSubscriptionSelected = onSubscriptionSelected
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(planGroups.size) { index ->
                Box(
                    modifier = Modifier
                        .size(if (index == pagerState.currentPage) 18.dp else 12.dp)
                        .background(
                            if (index == pagerState.currentPage) Color(0xFFD32F2F) else Color(0xFFCCCCCC),
                            CircleShape
                        )
                )
                if (index < planGroups.size - 1) {
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }

        BottomActionBar(
            selectedPlan = selectedPlan,
            selectedSubsId = selectedSubsId,
            onContinueClick = onContinueClick,
            getCycleDuration = getCycleDuration,
            isLoading = isLoading
        )
    }
}

@Composable
private fun BottomActionBar(
    selectedPlan: SubscriptionPlan?,
    selectedSubsId: String,
    onContinueClick: () -> Unit,
    getCycleDuration: (BillingCycle) -> String,
    isLoading: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RectangleShape,
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(9.dp)
        ) {
            selectedPlan?.let { plan ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column(modifier = Modifier.padding(start = 16.dp)) {
                        Text(
                            text = "₹ ${plan.price}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Text(
                            text = getCycleDuration(plan.billingCycle),
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
            } ?: run {
                Text(
                    text = "Please select a plan",
                    fontSize = 16.sp,
                    color = Color(0xFF666666),
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun SubscriptionCard(
    subscriptionType: SubscriptionType,
    plans: List<SubscriptionPlan>,
    selectedSubsId: String,
    onSubscriptionSelected: (String) -> Unit
) {
    val selectedPlan = plans.find { it.id.toString() == selectedSubsId } ?: plans.firstOrNull()
    val isSelected = selectedSubsId == selectedPlan?.id?.toString()

    Card(
        modifier = Modifier
            .fillMaxSize()
            .clickable { selectedPlan?.let { onSubscriptionSelected(it.id.toString()) } },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 2.dp
        ),
        border = if (isSelected) BorderStroke(2.dp, Color(0xFFD32F2F)) else null
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(
                    brush = getMetalBrush(subscriptionType.displayName)
                )
                .clip(shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${subscriptionType.displayName} Plan",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(18.dp)
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Features",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Divider(modifier = Modifier.height(1.dp), color = Color.Gray)
                    Spacer(modifier = Modifier.height(16.dp))

                    selectedPlan?.features?.forEach { feature ->
                        val featureUI = FeatureUI(
                            id = feature.id,
                            name = feature.name,
                            isIncluded = feature.value,
                            type = feature.type
                        )
                        FeatureItem(feature = featureUI)
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Divider(modifier = Modifier.height(1.dp), color = Color.Gray)
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Select Duration",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),

                    ) {
                        for(plan in plans){
                            PlanPeriodItem(
                                plan = plan,
                                isSelected = selectedSubsId == plan.id.toString(),
                                onClick = { onSubscriptionSelected(plan.id.toString()) }
                            )
                        }

                    }

                }
            }
        }
    }
}

@Composable
fun PlanPeriodItem(
    plan: SubscriptionPlan,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier

    ) {
        RadioButton(
            selected = isSelected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = Color(0xFFD32F2F),
                unselectedColor = Color(0xFFCCCCCC)
            )
        )

        Spacer(modifier = Modifier.width(2.dp))

        Text(
            text = plan.time_period,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black,

        )
    }
}

@Composable
fun FeatureItem(feature: FeatureUI) {
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
                            .background(Color(0xFF4CAF50), CircleShape),
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
                            .background(Color(0xFFD32F2F), CircleShape),
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

@Composable
fun getMetalBrush(planName: String): Brush {
    return when (planName.lowercase()) {
        "gold" -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFFFFD700),
                Color(0xFFFFA500)
            )
        )
        "platinum" -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFFE5E4E2),
                Color(0xFFC0C0C0)
            )
        )
        "silver" -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFFC0C0C0),
                Color(0xFF808080)
            )
        )
        else -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFFF5F5F5),
                Color(0xFFE0E0E0)
            )
        )
    }
}