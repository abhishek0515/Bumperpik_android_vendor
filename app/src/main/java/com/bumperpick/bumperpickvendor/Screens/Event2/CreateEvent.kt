package com.bumperpick.bumperpickvendor.Screens.Event2

import android.util.Log
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bumperpick.bumperpickvendor.R
import com.bumperpick.bumperpickvendor.Screens.Component.ButtonView
import com.bumperpick.bumperpickvendor.Screens.Component.PrimaryButton
import com.bumperpick.bumperpickvendor.Screens.Component.SecondaryButton
import com.bumperpick.bumperpickvendor.Screens.Component.SimpleImagePicker
import com.bumperpick.bumperpickvendor.Screens.Component.TextFieldView
import com.bumperpick.bumperpickvendor.Screens.CreateOfferScreen.CalendarBottomSheet
import com.bumperpick.bumperpickvendor.Screens.CreateOfferScreen.CreateOfferScreenViews
import com.bumperpick.bumperpickvendor.Screens.CreateOfferScreen.EditableTextTypeView
import com.bumperpick.bumperpickvendor.Screens.CreateOfferScreen.ImageCardFromUri
import com.bumperpick.bumperpickvendor.Screens.CreateOfferScreen.OfferDateSelector
import com.bumperpick.bumperpickvendor.Screens.EditAccountScreen.TimePickerDialog

import com.bumperpick.bumperpickvendor.ui.theme.BtnColor
import com.bumperpick.bumperpickvendor.ui.theme.grey
import com.bumperpick.bumperpickvendor.ui.theme.satoshi_medium
import com.bumperpick.bumperpickvendor.ui.theme.satoshi_regular
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter



sealed class CreateEvent2ScreenViews(val route:String){
    object CreateEvent2:CreateEvent2ScreenViews("create_Event2")
    object Event2Preview:CreateEvent2ScreenViews("Event2_preview")
}
@Composable
fun CreateEvent2(
    onBack:()->Unit,
    viewmodel: Events2Viewmodel= koinViewModel()){

    val navController= rememberNavController()
    val error by viewmodel.error.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    BackHandler { onBack() }
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
        containerColor = grey,
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.padding(8.dp),
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
                })
        },
    )
     {
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues = it)){
            NavHost (navController=navController,
                startDestination = CreateEvent2ScreenViews.CreateEvent2.route,
                modifier = Modifier.fillMaxSize()){

                composable(CreateEvent2ScreenViews.CreateEvent2.route){
                    CreateEvent2Screen(navController,viewmodel)
                }
                composable(CreateEvent2ScreenViews.Event2Preview.route){
                    Event2PreviewScreen(navController, viewmodel = viewmodel, onEvent2Done = onBack)
                }

            }
        }
     }






}

@Composable
fun Event2PreviewScreen(
    navController: NavController,
    onEvent2Done: () -> Unit,
    viewmodel: Events2Viewmodel
) {
    val Event2Details by viewmodel.eventDetails.collectAsState()
    val loading by viewmodel.loading.collectAsState()
    val Event2Added by viewmodel.eventAdded.collectAsState()

    BackHandler {
        navController.navigate(CreateEvent2ScreenViews.CreateEvent2.route) {
            popUpTo(CreateEvent2ScreenViews.Event2Preview.route) {
                inclusive = true
            }
        }
    }

    LaunchedEffect(Event2Added) {
        if (Event2Added) {
            onEvent2Done()
        }
    }

    Scaffold(
        containerColor = grey,
        bottomBar = {
            Surface( color = Color.White) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    SecondaryButton(
                        text = "Edit details",
                        onClick = {
                            navController.navigate(CreateEvent2ScreenViews.CreateEvent2.route) {
                                popUpTo(CreateEvent2ScreenViews.CreateEvent2.route) { inclusive = true }
                            }
                        },
                        modifier = Modifier.width(170.dp)
                    )
                    if (loading) {
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
                            text = "Publish",
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            viewmodel.addEvent()
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 8.dp, vertical = 12.dp)
                .fillMaxSize()
                .background(Color.White, RoundedCornerShape(12.dp)),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "Event Preview",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = satoshi_medium
                )
            }

            item {
                LabelledSection(label = "Banner") {
                    ImageCardFromUri(
                        imageUri = Event2Details.bannerImage.toString(),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            item {
                LabelledSection(label = "Title") {
                    Text(Event2Details.title ?: "", fontSize = 8.sp, fontFamily = satoshi_medium)
                }
            }
            item {
                LabelledSection(label = "Start Date") {
                    Text(Event2Details.startDate ?: "", fontSize = 8.sp, fontFamily = satoshi_medium)
                }
            }
            item {
                LabelledSection(label = "Start Time") {
                    Text(Event2Details.startTime ?: "", fontSize = 8.sp, fontFamily = satoshi_medium)
                }
            }
            item {
                LabelledSection(label = "Facebook live") {
                    Text(Event2Details.facebookLiveLink?: "", fontSize = 8.sp, fontFamily = satoshi_medium)
                }
            }
            item {
                LabelledSection(label = "Youtube live") {
                    Text(Event2Details.youtubeLiveLink ?: "", fontSize = 8.sp, fontFamily = satoshi_medium)
                }
            }
            item {
                LabelledSection(label = "Event Address") {
                    Text(Event2Details.address ?: "", fontSize = 8.sp, fontFamily = satoshi_medium)
                }
            }

            item {
                LabelledSection(label = "Event Description") {
                    Text(Event2Details.description ?: "", fontSize = 8.sp, fontFamily = satoshi_medium)
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
fun CreateEvent2Screen(
    navController: NavController,
    viewmodel: Events2Viewmodel
) {
    val eventDetails by viewmodel.eventDetails.collectAsState()
    var showStartCalendar by remember { mutableStateOf(false) }
    var showStartTime by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }

    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

    Column(
        modifier = Modifier
            .background(grey)
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
            HeaderSection()

            // Form content
            FormContent(
                eventDetails = eventDetails,
                viewmodel = viewmodel,
                onStartDateClick = { showStartCalendar = true },
                onStartTimeClick = { showStartTime = true },
                navController = navController
            )
        }

        // Bottom sheets and dialogs
        CalendarBottomSheet(
            isVisible = showStartCalendar,
            selectedDate = selectedDate,
            onDateSelected = { selectedDate = it },
            onDismiss = { showStartCalendar = false },
            startDate = LocalDate.now(),
            text = "Event Start Date",
            onConfirm = { date ->
                date?.let {
                    viewmodel.updateStartDate(it.format(formatter))
                    selectedDate = it
                }
                showStartCalendar = false
            }
        )

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

@Composable
private fun HeaderSection() {
    Column(
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth()
            .padding(top = 0.dp)
    ) {
        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = "Step 1 of 2",
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = satoshi_regular,
            color = Color.Black,
            modifier = Modifier.padding(8.dp)
        )

        Text(
            text = "Create Event",
            fontSize = 24.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = satoshi_medium,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
private fun FormContent(
    eventDetails: CreateEventModel,
    viewmodel: Events2Viewmodel,
    onStartDateClick: () -> Unit,
    onStartTimeClick: () -> Unit,
    navController: NavController
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // Image picker
        SimpleImagePicker(
            text = "Upload Image",
            ImageUri = eventDetails.bannerImage,
            modifier = Modifier
                .padding(horizontal = 0.dp)
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(),
            onImageSelected = { uri ->
                uri?.let { viewmodel.updateBannerImage(it) }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Title field
        FormField(
            label = "Title",
            placeholder = "Enter Event Title",
            value = eventDetails.title,
            onValueChange = { viewmodel.updateTitle(it) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Date and time selector
        StartDateTimeSelector(
            offerStartDate = eventDetails.startDate,
            offerStartTime = eventDetails.startTime,
            onStartDateClick = onStartDateClick,
            onStartTimeClick = onStartTimeClick
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Facebook live field
        FormField(
            label = "Facebook Live",
            placeholder = "Enter Facebook live link",
            value = eventDetails.facebookLiveLink,
            onValueChange = { viewmodel.updatefacebookLiveLink(it) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // YouTube live field
        FormField(
            label = "YouTube Live",
            placeholder = "Enter YouTube live link",
            value = eventDetails.youtubeLiveLink,
            onValueChange = { viewmodel.updateYoutubeLiveLink(it) }
        )
        Spacer(modifier = Modifier.height(4.dp))
        androidx.compose.material.Text(
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
        FormField(
            label = "Event Address",
            placeholder = "Enter Event Address",
            value = eventDetails.address,
            onValueChange = { viewmodel.updateAddress(it) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Description field
        FormField(
            label = "Event Description",
            placeholder = "Enter Event Description",
            value = eventDetails.description,
            onValueChange = { viewmodel.updateDescription(it) },
            maxLines = 4
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Next button
        ButtonView(text = "Next", horizontal_padding = 0.dp) {
            if (viewmodel.validateEventDetails()) {
                navController.navigate(CreateEvent2ScreenViews.Event2Preview.route) {
                    popUpTo(CreateEvent2ScreenViews.CreateEvent2.route)
                }
            }
        }
    }
}

@Composable
private fun FormField(
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
fun StartDateTimeSelector(
    offerStartDate: String?,
    offerStartTime: String?,
    onStartDateClick: () -> Unit,
    onStartTimeClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // Start Date Section
        Text(
            text = "Event Start Date",
            fontSize = 14.sp,
            fontFamily = satoshi_regular,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        DateTimeCard(
            text = offerStartDate ?: "Select start date",
            isSelected = offerStartDate != null,
            iconRes = R.drawable.calendar_alt,
            contentDescription = "Calendar Icon",
            onClick = onStartDateClick
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Start Time Section
        Text(
            text = "Event Start Time",
            fontSize = 14.sp,
            fontFamily = satoshi_regular,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        DateTimeCard(
            text = offerStartTime ?: "Select start time",
            isSelected = offerStartTime != null,
            iconRes = R.drawable.clock,
            contentDescription = "Clock Icon",
            onClick = onStartTimeClick
        )
    }
}

@Composable
private fun DateTimeCard(
    text: String,
    isSelected: Boolean,
    iconRes: Int,
    contentDescription: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                fontFamily = satoshi_regular,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = if (isSelected) Color.Black else Color.Gray
            )

            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = contentDescription,
                modifier = Modifier.size(24.dp),
                tint = if (isSelected) BtnColor else Color.Gray
            )
        }
    }
}