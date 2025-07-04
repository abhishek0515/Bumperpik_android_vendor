package com.bumperpick.bumperpickvendor.Screens.Events

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.bumperpick.bumperpickvendor.R
import com.bumperpick.bumperpickvendor.Screens.Component.ButtonView
import com.bumperpick.bumperpickvendor.Screens.Component.TextFieldView
import com.bumperpick.bumperpickvendor.Screens.CreateOfferScreen.CalendarBottomSheet
import com.bumperpick.bumperpickvendor.Screens.CreateOfferScreen.OfferDateSelector
import com.bumperpick.bumperpickvendor.Screens.Event2.Events2Viewmodel
import com.bumperpick.bumperpickvendor.ui.theme.grey
import com.bumperpick.bumperpickvendor.ui.theme.satoshi_medium
import com.bumperpick.bumperpickvendor.ui.theme.satoshi_regular
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun EditEventScreen(
    viewmodel: EventsViewmodel = koinViewModel(),
    eventId: String,
    onBackClick:()->Unit
) {
    val eventDetails by viewmodel.eventDetails.collectAsState()
    val isLoading by viewmodel.loading.collectAsState()
    var showStartCalendar by remember { mutableStateOf(false) }
    var showEndCalendar by remember { mutableStateOf(false) }
    var startDate by remember { mutableStateOf<LocalDate?>(null) }
    var endDate by remember { mutableStateOf<LocalDate?>(null) }
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

    // Load event details when screen is first composed
    LaunchedEffect(eventId) {
        viewmodel.getEventDetail(eventId)
    }

    // Update local date states when event details are loaded
    LaunchedEffect(eventDetails.startDate) {
        if (eventDetails.startDate.isNotEmpty()) {
            try {
                startDate = LocalDate.parse(eventDetails.startDate, formatter)
            } catch (e: Exception) {
                // Handle date parsing error
            }
        }
    }

    LaunchedEffect(eventDetails.endDate) {
        if (eventDetails.endDate.isNotEmpty()) {
            try {
                endDate = LocalDate.parse(eventDetails.endDate, formatter)
            } catch (e: Exception) {
                // Handle date parsing error
            }
        }
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }
    val error by viewmodel.error.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(error) {
        if (error.isNotEmpty()) {
            val result = snackbarHostState.showSnackbar(
                message = error,
                actionLabel = "OK",
                withDismissAction = true
            )
            when (result) {
                SnackbarResult.ActionPerformed -> {
                    viewmodel.clearError()
                }
                SnackbarResult.Dismissed -> {
                    viewmodel.clearError()

                }
            }
        }
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
                })
        },
    ) {
        Column(
            modifier = Modifier
                .padding(it)
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
                    modifier = Modifier.background(Color.White).fillMaxWidth()
                ) {
                    Spacer(Modifier.height(10.dp))
                    Row() {

                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null,
                            modifier = Modifier.clickable { onBackClick() }
                                .padding(horizontal = 12.dp).align(Alignment.CenterVertically)
                        )

                        Text(
                            text = "Campaign Update",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = satoshi_medium,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                    Spacer(Modifier.height(10.dp))
                }

                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                    EditableImagePicker(
                        text = "Update Banner Image",
                        imageUri = eventDetails.bannerImage,
                        imageUrl = eventDetails.bannerImageUrl, // Assuming this field exists for URL
                        modifier = Modifier.padding(horizontal = 0.dp)
                            .align(Alignment.CenterHorizontally).fillMaxWidth(),
                        onImageSelected = {
                            if (it != null) {
                                viewmodel.updateBannerImage(it)
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Campaign Title",
                        fontSize = 14.sp,
                        fontFamily = satoshi_regular,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(vertical = 6.dp)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    TextFieldView(
                        placeholder = "Enter Campaign Title",
                        value = eventDetails.title,
                        onValueChange = {
                            viewmodel.updateTitle(it)
                        })

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Campaign Description",
                        fontSize = 14.sp,
                        fontFamily = satoshi_regular,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(vertical = 6.dp)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    TextFieldView(
                        placeholder = "Enter Campaign Description",
                        value = eventDetails.description,
                        onValueChange = {
                            viewmodel.updateDescription(it)
                        })

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Campaign address",
                        fontSize = 14.sp,
                        fontFamily = satoshi_regular,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(vertical = 6.dp)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    TextFieldView(
                        placeholder = "Enter Campaign Address",
                        value = eventDetails.address,
                        onValueChange = {
                            viewmodel.updateAddress(it)
                        })

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Maximum number of participants",
                        fontSize = 14.sp,
                        fontFamily = satoshi_regular,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(vertical = 6.dp)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    TextFieldView(
                        placeholder = "Enter Number of Participants",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        value = eventDetails.numberOfParticipant,
                        onValueChange = {
                            viewmodel.updateNumberOfParticipant(it)
                        })

                    Spacer(modifier = Modifier.height(6.dp))

                    OfferDateSelector(
                        offerStartDate = eventDetails.startDate,
                        offerEndDate = eventDetails.endDate,
                        onStartClick = { showStartCalendar = true },
                        onEndClick = {
                            if (eventDetails.startDate.isEmpty()) {
                                viewmodel.showError("Please choose start date first")
                            } else {
                                showEndCalendar = true
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

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
                            // navController.popBackStack()
                        }

                        ButtonView(
                            text = "Update",
                            modifier = Modifier.weight(1f)
                        ) {
                            if (!viewmodel.validateEventDetails()) {
                                //   return@ButtonView
                            }
                            viewmodel.updateEvent(eventId) { success ->
                                if (success) {
                                    Log.d("Success", "success")
                                    onBackClick()
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
                text = "Campaign Start Date",
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
                text = "Campaign End Date",
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
}

@Composable
fun EditableImagePicker(
    text: String,
    imageUri: Uri?,
    imageUrl: String?,
    modifier: Modifier = Modifier,
    onImageSelected: (Uri?) -> Unit
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        onImageSelected(uri)
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clickable {
                    launcher.launch("image/*")
                },
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                when {
                    // Show selected image (URI) - takes priority
                    imageUri != null -> {
                        AsyncImage(
                            model = imageUri,
                            contentDescription = "Selected Banner",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    // Show existing image from URL
                    !imageUrl.isNullOrEmpty() -> {
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = "Current Banner",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                            error = painterResource(id = R.drawable.image_1),
                            placeholder = painterResource(id = R.drawable.image_1)
                        )
                    }
                    // Show placeholder
                    else -> {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                             imageVector   = Icons.Default.Add,
                                contentDescription = "Add Image",
                                modifier = Modifier.size(48.dp),
                                tint = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = text,
                                fontSize = 14.sp,
                                fontFamily = satoshi_regular,
                                color = Color.Gray,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                // Show overlay for changing image when image exists
                if (imageUri != null || !imageUrl.isNullOrEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Color.Black.copy(alpha = 0.3f)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector   = Icons.Default.Add,
                                contentDescription = "Change Image",
                                modifier = Modifier.size(32.dp),
                                tint = Color.White
                            )
                            Text(
                                text = "Tap to change",
                                fontSize = 12.sp,
                                fontFamily = satoshi_regular,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}