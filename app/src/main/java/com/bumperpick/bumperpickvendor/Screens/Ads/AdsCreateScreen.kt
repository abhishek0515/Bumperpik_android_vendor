package com.bumperpick.bumperpickvendor.Screens.Ads

import androidx.compose.runtime.Composable

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bumperpick.bumperpickvendor.Screens.Component.ButtonView
import com.bumperpick.bumperpickvendor.Screens.Component.SecondaryButton
import com.bumperpick.bumperpickvendor.Screens.Component.SimpleImagePicker
import com.bumperpick.bumperpickvendor.Screens.CreateOfferScreen.CalendarBottomSheet
import com.bumperpick.bumperpickvendor.Screens.CreateOfferScreen.ImageCardFromUri
import com.bumperpick.bumperpickvendor.Screens.CreateOfferScreen.OfferDateSelector
import com.bumperpick.bumperpickvendor.Screens.QrScreen.UiState
import com.bumperpick.bumperpickvendor.ui.theme.BtnColor
import com.bumperpick.bumperpickvendor.ui.theme.grey
import com.bumperpick.bumperpickvendor.ui.theme.satoshi_medium
import com.bumperpick.bumperpickvendor.ui.theme.satoshi_regular
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

sealed class CreateAdScreenViews(val route: String) {
    object CreateAd : CreateAdScreenViews("create_ad")
    object AdPreview : CreateAdScreenViews("ad_preview")
}

@Composable
fun CreateAd(
    onBack: () -> Unit,
    viewmodel: AdsViewModel = koinViewModel()
) {
    val navController = rememberNavController()
    val createAdsUiState by viewmodel.createAdsUiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    BackHandler { onBack() }

    // Handle error states
    LaunchedEffect(createAdsUiState) {
        when (createAdsUiState) {
            is UiState.Error -> {
                val result = snackbarHostState.showSnackbar(
                    message = (createAdsUiState as UiState.Error).message,
                    actionLabel = "OK",
                    withDismissAction = true
                )
                when (result) {
                    SnackbarResult.ActionPerformed -> {
                        // Handle action if needed
                    }
                    SnackbarResult.Dismissed -> {
                        // Handle dismissal if needed
                    }
                }
            }
            is UiState.Success -> {
                // Ad created successfully, navigate back
                onBack()
            }
            else -> {}
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = grey,
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.padding(16.dp),
                snackbar = { data ->
                    Snackbar(
                        action = {
                            TextButton(onClick = { data.performAction() }) {
                                Text(
                                    text = "OK",
                                    color = Color.White
                                )
                            }
                        },
                        containerColor = Color.Red.copy(alpha = 0.8f),
                        contentColor = Color.White
                    ) {
                        Text(text = data.visuals.message)
                    }
                }
            )
        },
    ) {
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues = it)) {
            NavHost(
                navController = navController,
                startDestination = CreateAdScreenViews.CreateAd.route,
                modifier = Modifier.fillMaxSize()
            ) {
                composable(CreateAdScreenViews.CreateAd.route) {
                    CreateAdScreen(navController, viewmodel)
                }
                composable(CreateAdScreenViews.AdPreview.route) {
                    AdPreviewScreen(navController, viewmodel = viewmodel, onAdDone = onBack)
                }
            }
        }
    }
}

@Composable
fun AdPreviewScreen(
    navController: NavController,
    onAdDone: () -> Unit,
    viewmodel: AdsViewModel
) {
    val createAdsUiState by viewmodel.createAdsUiState.collectAsState()
    val isLoading = createAdsUiState is UiState.Loading
    val bannerUri = viewmodel.getCurrentBannerUri()
    val startDate = viewmodel.getCurrentStartDate()
    val context= LocalContext.current
    val endDate = viewmodel.getCurrentEndDate()

    BackHandler {
        navController.navigate(CreateAdScreenViews.CreateAd.route) {
            popUpTo(CreateAdScreenViews.AdPreview.route) {
                inclusive = true
            }
        }
    }

    LaunchedEffect(createAdsUiState) {
        if (createAdsUiState is UiState.Success) {
            onAdDone()
        }
    }

    Scaffold(
        containerColor = grey,
        bottomBar = {
            Surface(color = Color.White) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    SecondaryButton(
                        text = "Edit details",
                        onClick = {
                            navController.navigate(CreateAdScreenViews.CreateAd.route) {
                                popUpTo(CreateAdScreenViews.CreateAd.route) { inclusive = true }
                            }
                        },
                        modifier = Modifier.width(170.dp)
                    )
                    if (isLoading) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = BtnColor)
                        }
                    } else {
                        ButtonView(
                            text = "Create Ad",
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Create the ad using the viewmodel
                            // You'll need to implement the actual ad creation logic here
                            // This is a placeholder for the actual implementation
                            if (bannerUri != null) {
                                // Prepare data and call createAds
                                viewmodel.createAd(context = context)
                            }
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .fillMaxSize()
                .background(Color.White, RoundedCornerShape(12.dp)),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "Ad Preview",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = satoshi_medium
                )
            }

            item {
                LabelledSection(label = "Ad Banner") {
                    if (bannerUri != null) {
                        ImageCardFromUri(
                            imageUri = bannerUri.toString(),
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        Text(
                            text = "No banner selected",
                            fontSize = 16.sp,
                            fontFamily = satoshi_medium,
                            color = Color.Gray
                        )
                    }
                }
            }

            item {
                LabelledSection(label = "Ad Start Date") {
                    Text(
                        text = startDate.ifEmpty { "Not selected" },
                        fontSize = 16.sp,
                        fontFamily = satoshi_medium
                    )
                }
            }

            item {
                LabelledSection(label = "Ad End Date") {
                    Text(
                        text = endDate.ifEmpty { "Not selected" },
                        fontSize = 16.sp,
                        fontFamily = satoshi_medium
                    )
                }
            }
        }
    }
}

@Composable
fun LabelledSection(label: String, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontFamily = satoshi_regular,
            fontWeight = FontWeight.SemiBold,
            color = Color.Gray
        )
        content()
        Divider(color = Color.Gray.copy(alpha = 0.2f))
    }
}

@Composable
fun CreateAdScreen(navController: NavController, viewmodel: AdsViewModel) {
    var showStartCalendar by remember { mutableStateOf(false) }
    var showEndCalendar by remember { mutableStateOf(false) }
    var startDate by remember { mutableStateOf<LocalDate?>(null) }
    var endDate by remember { mutableStateOf<LocalDate?>(null) }
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    val context= LocalContext.current
    val bannerUri = viewmodel.getCurrentBannerUri()
    val startDateString = viewmodel.getCurrentStartDate()
    val endDateString = viewmodel.getCurrentEndDate()

    Column(
        modifier = Modifier
            .background(grey)
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .weight(1f)
                .background(grey, RoundedCornerShape(8.dp))
        ) {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            ) {
                Spacer(Modifier.height(5.dp))
                Text(
                    text = "Step 1 of 2",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = satoshi_regular,
                    color = Color.Black,
                    modifier = Modifier.padding(16.dp)
                )
                Text(
                    text = "Let's create your Ad",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = satoshi_medium,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(Modifier.height(20.dp))
            }

            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                SimpleImagePicker(
                    text = "Upload Ad Banner",
                    ImageUri = bannerUri,
                    modifier = Modifier
                        .padding(horizontal = 0.dp)
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth(),
                    onImageSelected = {
                        if (it != null) {
                            viewmodel.updateBannerImage(it)
                        }
                    }
                )

                Spacer(modifier = Modifier.height(20.dp))

                OfferDateSelector(
                    text="Ads",
                    offerStartDate = startDateString,
                    offerEndDate = endDateString,
                    onStartClick = { showStartCalendar = true },
                    onEndClick = {
                        if (startDateString.isEmpty()) {
                            Toast.makeText(context,"Choose start date first",Toast.LENGTH_SHORT).show()
                        } else {
                            showEndCalendar = true
                        }
                    }
                )

                Spacer(modifier = Modifier.height(20.dp))

                ButtonView(text = "Next") {
                    if (!viewmodel.validateAdDetails(false)) {
                      Toast.makeText(context,"Please fill all fields",Toast.LENGTH_SHORT).show()
                    }
                    else{
                    navController.navigate(CreateAdScreenViews.AdPreview.route) {
                        popUpTo(CreateAdScreenViews.CreateAd.route) {
                        }
                    }
                        }
                }
            }
        }

        CalendarBottomSheet(
            isVisible = showStartCalendar,
            selectedDate = startDate,
            onDateSelected = { startDate = it },
            onDismiss = { showStartCalendar = false },
            startDate = LocalDate.now(),
            text = "Ad Start Date",
            onConfirm = {
                if (it != null) {
                    viewmodel.updateStartDate(it.format(formatter))
                }
                startDate = it
                showStartCalendar = false
            }
        )

        CalendarBottomSheet(
            isVisible = showEndCalendar,
            selectedDate = endDate,
            startDate = startDate ?: LocalDate.now(),
            onDateSelected = { endDate = it },
            onDismiss = { showEndCalendar = false },
            text = "Ad End Date",
            onConfirm = {
                if (it != null) {
                    viewmodel.updateEndDate(it.format(formatter))
                }
                endDate = it
                showEndCalendar = false
            }
        )
    }
}