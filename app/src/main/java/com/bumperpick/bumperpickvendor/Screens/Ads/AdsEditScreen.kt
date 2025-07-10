package com.bumperpick.bumperpickvendor.Screens.Ads

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumperpick.bumperpickvendor.API.FinalModel.AdsDetailModel
import com.bumperpick.bumperpickvendor.API.Model.success_model
import com.bumperpick.bumperpickvendor.Screens.Component.ButtonView
import com.bumperpick.bumperpickvendor.Screens.CreateOfferScreen.CalendarBottomSheet
import com.bumperpick.bumperpickvendor.Screens.CreateOfferScreen.OfferDateSelector
import com.bumperpick.bumperpickvendor.Screens.Campaign.EditableImagePicker
import com.bumperpick.bumperpickvendor.Screens.QrScreen.UiState
import com.bumperpick.bumperpickvendor.ui.theme.BtnColor
import com.bumperpick.bumperpickvendor.ui.theme.grey
import com.bumperpick.bumperpickvendor.ui.theme.satoshi_medium
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@Composable
fun adsEditScreen(
    onBack: () -> Unit,
    adsId: String,
    viewmodel: AdsViewModel = koinViewModel()
) {
    val eventDetails by viewmodel.vendorAdsDetailUiState.collectAsState()
    val updateState by viewmodel.updateVendorAdsUiState.collectAsState()
    var startDate by remember { mutableStateOf<LocalDate?>(null) }
    var endDate by remember { mutableStateOf<LocalDate?>(null) }
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

    var error by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    var showStartCalendar by remember { mutableStateOf(false) }
    var showEndCalendar by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    // Load ad details when screen opens
    LaunchedEffect(Unit) {
        viewmodel.getVendorAdsDetail(adsId)
    }

    // Handle update response
    LaunchedEffect(updateState) {
        when (updateState) {
            UiState.Empty -> {
                loading = false
            }
            is UiState.Error -> {
                loading = false
                error = (updateState as UiState.Error).message
            }
            UiState.Loading -> {
                loading = true
            }
            is UiState.Success -> {
                loading = false
                Toast.makeText(
                    context,
                    (updateState as UiState.Success<success_model>).data.message,
                    Toast.LENGTH_SHORT
                ).show()
                viewmodel.resetUpdateState()
                onBack()
            }
        }
    }

    // Handle error snackbar
    LaunchedEffect(error) {
        if (error.isNotEmpty()) {
            val result = snackbarHostState.showSnackbar(
                message = error,
                actionLabel = "OK",
                withDismissAction = true
            )
            if (result == SnackbarResult.ActionPerformed || result == SnackbarResult.Dismissed) {
                error = ""
            }
        }
    }

    // Handle date picker results
    LaunchedEffect(showStartCalendar, showEndCalendar) {
        // You can implement date picker logic here
        // For now, we'll use placeholder logic
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White,
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.padding(16.dp),
                snackbar = { data ->
                    Snackbar(
                        action = {
                            TextButton(onClick = { data.performAction() }) {
                                androidx.compose.material3.Text(
                                    text = "OK",
                                    color = Color.White
                                )
                            }
                        },
                        containerColor = Color.Red.copy(alpha = 0.8f),
                        contentColor = Color.White
                    ) {
                        androidx.compose.material3.Text(text = data.visuals.message)
                    }
                }
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .background(grey)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .weight(1f)
                    .background(grey, RoundedCornerShape(8.dp))
            ) {
                // Header
                Column(
                    modifier = Modifier
                        .background(Color.White)
                        .fillMaxWidth()
                ) {
                    Spacer(Modifier.height(10.dp))
                    Row {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null,
                            modifier = Modifier
                                .clickable { onBack() }
                                .padding(horizontal = 12.dp)
                                .align(Alignment.CenterVertically)
                        )

                        androidx.compose.material.Text(
                            text = "Ads Update",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = satoshi_medium,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                    Spacer(Modifier.height(10.dp))
                }

                // Content
                when (eventDetails) {
                    UiState.Empty -> {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Text(
                                text = "No Ad available",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.align(Alignment.Center),
                                color = BtnColor
                            )
                        }
                    }

                    is UiState.Error -> {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Text(
                                text = (eventDetails as UiState.Error).message,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.align(Alignment.Center),
                                color = BtnColor
                            )
                        }
                    }

                    UiState.Loading -> {
                        Box(modifier = Modifier.fillMaxSize()) {
                            CircularProgressIndicator(
                                color = BtnColor,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }

                    is UiState.Success -> {
                        val data = (eventDetails as UiState.Success<AdsDetailModel>).data.data

                        Column(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                        ) {
                            // Image Picker
                            EditableImagePicker(
                                text = "Update Banner Image",
                                imageUri = viewmodel.getCurrentBannerUri(),
                                imageUrl = data.banner,
                                modifier = Modifier
                                    .padding(horizontal = 0.dp)
                                    .align(Alignment.CenterHorizontally)
                                    .fillMaxWidth(),
                                onImageSelected = { uri ->
                                    if (uri != null) {
                                        viewmodel.updateBannerImage(uri)
                                    }
                                }
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            // Date Selector
                            OfferDateSelector(
                                offerStartDate = viewmodel.getCurrentStartDate().ifEmpty { data.start_date },
                                offerEndDate = viewmodel.getCurrentEndDate().ifEmpty { data.end_date },
                                onStartClick = {
                                    showStartCalendar = true
                                    // You can implement date picker dialog here
                                    // For now, we'll use a placeholder
                                },
                                onEndClick = {
                                    val currentStartDate = viewmodel.getCurrentStartDate().ifEmpty { data.start_date }
                                    if (currentStartDate.isEmpty()) {
                                        error = "Please choose start date first"
                                    } else {
                                        showEndCalendar = true
                                        // You can implement date picker dialog here
                                    }
                                }
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Action Buttons
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                ButtonView(
                                    text = "Cancel",
                                    modifier = Modifier.weight(1f),
                                    btnColor = Color.Gray,
                                    textColor = Color.White
                                ) {
                                    onBack()
                                }

                                ButtonView(
                                    text = if (loading) "Updating..." else "Update",
                                    modifier = Modifier.weight(1f),
                                    enabled = !loading
                                ) {
                                    if (viewmodel.validateAdDetails(true)) {
                                        viewmodel.updateAd(data.id,context)
                                    } else {
                                        error = "Please fill all required fields"
                                    }
                                }
                            }
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



