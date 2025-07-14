package com.bumperpick.bumperpickvendor.Screens.Event2

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
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.bumperpick.bumperpickvendor.R
import com.bumperpick.bumperpickvendor.Screens.Component.ButtonView
import com.bumperpick.bumperpickvendor.Screens.Component.SimpleImagePicker
import com.bumperpick.bumperpickvendor.Screens.Component.TextFieldView
import com.bumperpick.bumperpickvendor.Screens.CreateOfferScreen.CalendarBottomSheet
import com.bumperpick.bumperpickvendor.Screens.CreateOfferScreen.OfferDateSelector
import com.bumperpick.bumperpickvendor.Screens.EditAccountScreen.TimePickerDialog
import com.bumperpick.bumperpickvendor.ui.theme.BtnColor
import com.bumperpick.bumperpickvendor.ui.theme.grey
import com.bumperpick.bumperpickvendor.ui.theme.satoshi_medium
import com.bumperpick.bumperpickvendor.ui.theme.satoshi_regular
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
@Composable
fun EditEventScreen2(
    viewmodel: Events2Viewmodel = koinViewModel(),
    eventId: String,
    onBackClick: () -> Unit
) {
    val eventDetails by viewmodel.eventDetails.collectAsState()
    val isLoading by viewmodel.loading.collectAsState()
    var showStartCalendar by remember { mutableStateOf(false) }
    var showEndCalendar by remember { mutableStateOf(false) }
    var showStartTime by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var selectedEndDate by remember { mutableStateOf<LocalDate?>(null) }
    var showEndtimeCalendar by remember { mutableStateOf(false) }
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
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
    // Load event details when screen is first composed
    LaunchedEffect(eventId) {
        viewmodel.getEventDetail(eventId)
    }

    // Update local date states when event details are loaded
    LaunchedEffect(eventDetails.startDate) {
        if (eventDetails.startDate.isNotEmpty()) {
            try {
                selectedDate = LocalDate.parse(eventDetails.startDate, formatter)
            } catch (e: Exception) {
                Log.e("EditEventScreen", "Date parsing error: ${e.message}")
            }
        }
    }
    LaunchedEffect(eventDetails.endDate) {
        if (eventDetails.endDate.isNotEmpty()) {
            try {
                selectedEndDate = LocalDate.parse(eventDetails.endDate, formatter)
            } catch (e: Exception) {
                Log.e("EditEventScreen", "Date parsing error: ${e.message}")
            }
        }
    }

    if (isLoading) {
        LoadingScreen()
        return
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White,
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.padding(8.dp),
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
                .background(grey)
                .padding(it)
                .fillMaxSize()
        ) {
            // Main content
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .weight(1f)

                    .background(grey, RoundedCornerShape(8.dp))
            ) {
                // Header section
                EditHeaderSection(onBackClick)

                // Form content
                EditFormContent(
                    eventDetails = eventDetails,
                    viewmodel = viewmodel,
                    eventId = eventId,
                    onStartDateClick = { showStartCalendar = true },
                    onStartTimeClick = { showStartTime = true },
                    onBackClick = onBackClick,
                    onEndTimeClick = {
                        showEndtimeCalendar=true},
                    onEndDateClick = {showEndCalendar=true},



                    )
            }

            // Bottom sheets and dialogs
            CalendarBottomSheet(
                isVisible = showStartCalendar,
                selectedDate = selectedDate,
                onDateSelected = { selectedDate = it },
                onDismiss = { showStartCalendar = false },
                startDate = selectedDate?: LocalDate.now(),
                text = "Event Start Date",
                onConfirm = { date ->
                    date?.let {
                        viewmodel.updateStartDate(it.format(formatter))
                        selectedDate = it
                    }
                    showStartCalendar = false
                }
            )
            CalendarBottomSheet(
                isVisible = showEndCalendar,
                selectedDate=selectedEndDate,
                onDateSelected = {selectedEndDate=it},
                onDismiss = {showEndCalendar=false},
                startDate =selectedDate ?: LocalDate.now(),
                text = "Event End Date",
                onConfirm = {
                    date->
                    date?.let {
                        viewmodel.updateEndDate(it.format(formatter))
                        selectedEndDate = it
                    }
                    showEndCalendar = false
                }
            )
            if(showEndtimeCalendar){
                TimePickerDialog(
                    onTimeSelected = {
                            time->
                        viewmodel.updateEndTime(time)
                        showEndtimeCalendar =false

                    },
                    onDismiss = {
                        showEndtimeCalendar=false
                    }
                )
            }
            if (showStartTime) {
                TimePickerDialog(
                    onTimeSelected = { time ->
                        viewmodel.updateStartTime(time)
                        showStartTime = false
                    },
                    onDismiss = { showStartTime = false }
                )
            }
        }
    }
}

@Composable
private fun LoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(grey),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = BtnColor
        )
    }
}

@Composable
private fun EditHeaderSection(onBackClick: () -> Unit) {
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
                text = "Event Update",
                fontSize = 24.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = satoshi_medium,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
        Spacer(Modifier.height(10.dp))
    }
}

@Composable
private fun EditFormContent(
    eventDetails: CreateEventModel,
    viewmodel: Events2Viewmodel,
    eventId: String,
    onStartDateClick: () -> Unit,
    onStartTimeClick: () -> Unit,
    onEndTimeClick:()->Unit,
    onEndDateClick:()->Unit,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp)
    ) {
        // Editable image picker
        EditableImagePicker(
            text = "Update banner image",
            imageUri = eventDetails.bannerImage,
            imageUrl = eventDetails.bannerImageUrl,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(),
            onImageSelected = { uri ->
                uri?.let { viewmodel.updateBannerImage(it) }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Title field
        EditFormField(
            label = "Event Title",
            placeholder = "Enter event title",
            value = eventDetails.title,
            onValueChange = { viewmodel.updateTitle(it) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Date and time selector
        StartDateTimeSelector(
            offerStartDate = eventDetails.startDate,
            offerStartTime = eventDetails.startTime,
            offerEndTime = eventDetails.endTime,
            offerEndDate = eventDetails.endDate,
            onStartDateClick = onStartDateClick,
            onStartTimeClick = onStartTimeClick,
            onEndTimeClick = onEndTimeClick,
            onEndDateClick = onEndDateClick
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Facebook live field
        EditFormField(
            label = "Facebook Live",
            placeholder = "Enter facebook live link",
            value = eventDetails.facebookLiveLink,
            onValueChange = { viewmodel.updatefacebookLiveLink(it) }
        )

        Spacer(modifier = Modifier.height(8.dp))
        // YouTube live field
        EditFormField(
            label = "Instagram Live",
            placeholder = "Enter instagram live link",
            value = eventDetails.instagramLiveLink, // Fixed: was using facebookLiveLink
            onValueChange = { viewmodel.updateInstagramLink(it) }
        )
        Spacer(modifier = Modifier.height(8.dp))
        // YouTube live field
        EditFormField(
            label = "Youtube Live",
            placeholder = "Enter youtube live Id",
            value = eventDetails.youtubeLiveLink, // Fixed: was using facebookLiveLink
            onValueChange = { viewmodel.updateYoutubeLiveLink(it) }
        )
        Spacer(modifier = Modifier.height(4.dp))


        // YouTube hint with highlighted video ID
        Text(
            text = buildAnnotatedString {
                append("Hint: Copy your live YouTube video ID from the URL.\n")
                append("Example: https://www.youtube.com/watch?v=")
                withStyle(
                    style = SpanStyle(
                        color = Color(0xFF64B5F6), // Light blue color
                        fontWeight = FontWeight.Bold,
                        background = Color(0xFFE3F2FD) // Very light blue background
                    )
                ) {
                    append("abcd1234")
                }
                append("\n\nEnter only the video ID part (like ")
                withStyle(
                    style = SpanStyle(
                        color = Color(0xFF64B5F6),
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append("abcd1234")
                }
                append(") in the field above.")
            },
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            textAlign = TextAlign.Start
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Address field
        EditFormField(
            label = "Event Address",
            placeholder = "Enter event address",
            value = eventDetails.address,
            onValueChange = { viewmodel.updateAddress(it) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Description field
        EditFormField(
            label = "Event Description",
            placeholder = "Enter event description",
            value = eventDetails.description,
            onValueChange = { viewmodel.updateDescription(it) },
            maxLines = 4
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Action buttons
        ActionButtons(
            viewmodel = viewmodel,
            eventId = eventId,
            onBackClick = onBackClick
        )
    }
}

@Composable
private fun EditFormField(
    label: String,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    maxLines: Int = 1
) {
    Column {
        Text(
            text = label,
            fontSize = 14.sp,
            fontFamily = satoshi_regular,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(vertical = 6.dp)
        )

        TextFieldView(
            placeholder = placeholder,
            value = value,
            onValueChange = onValueChange,

        )
    }
}

@Composable
private fun ActionButtons(
    viewmodel: Events2Viewmodel,
    eventId: String,
    onBackClick: () -> Unit
) {
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
            onBackClick()
        }

        ButtonView(
            text = "Update",
            modifier = Modifier.weight(1f)
        ) {
            if (viewmodel.validateEventDetails()) {
                viewmodel.updateEvent(eventId) { success ->
                    if (success) {
                        Log.d("EditEventScreen", "Event updated successfully")
                        onBackClick()
                    } else {
                        Log.e("EditEventScreen", "Failed to update event")
                    }
                }
            }
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
                        ImagePlaceholder(text = text)
                    }
                }

                // Show overlay for changing image when image exists
                if (imageUri != null || !imageUrl.isNullOrEmpty()) {
                    ImageOverlay()
                }
            }
        }
    }
}

@Composable
private fun ImagePlaceholder(text: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Add,
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

@Composable
private fun ImageOverlay() {
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
                imageVector = Icons.Default.Edit,
                contentDescription = "Change Image",
                modifier = Modifier.size(32.dp),
                tint = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Tap to change",
                fontSize = 12.sp,
                fontFamily = satoshi_regular,
                color = Color.White
            )
        }
    }
}