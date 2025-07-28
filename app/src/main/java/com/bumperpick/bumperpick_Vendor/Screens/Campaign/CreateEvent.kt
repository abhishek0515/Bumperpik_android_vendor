package com.bumperpick.bumperpick_Vendor.Screens.Campaigns

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
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bumperpick.bumperpick_Vendor.Screens.Component.ButtonView
import com.bumperpick.bumperpick_Vendor.Screens.Component.SecondaryButton
import com.bumperpick.bumperpick_Vendor.Screens.Component.SimpleImagePicker
import com.bumperpick.bumperpick_Vendor.Screens.Component.TextFieldView
import com.bumperpick.bumperpick_Vendor.Screens.CreateOfferScreen.CalendarBottomSheet
import com.bumperpick.bumperpick_Vendor.Screens.CreateOfferScreen.ImageCardFromUri
import com.bumperpick.bumperpick_Vendor.Screens.CreateOfferScreen.OfferDateSelector
import com.bumperpick.bumperpick_Vendor.Screens.Campaign.EventsViewmodel
import com.bumperpick.bumperpick_Vendor.ui.theme.BtnColor
import com.bumperpick.bumperpick_Vendor.ui.theme.grey
import com.bumperpick.bumperpick_Vendor.ui.theme.satoshi_medium
import com.bumperpick.bumperpick_Vendor.ui.theme.satoshi_regular
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter



sealed class CreateCampaignScreenViews(val route:String){
    object CreateCampaign:CreateCampaignScreenViews("create_Campaign")
    object CampaignPreview:CreateCampaignScreenViews("Campaign_preview")
}
@Composable
fun CreateCampaign(
    onBack:()->Unit,
    viewmodel: EventsViewmodel= koinViewModel()
){

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
                })
        },
    )
     {
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues = it)){
            NavHost (navController=navController,
                startDestination = CreateCampaignScreenViews.CreateCampaign.route,
                modifier = Modifier.fillMaxSize()){

                composable(CreateCampaignScreenViews.CreateCampaign.route){
                    CreateCampaignScreen(navController,viewmodel)
                }
                composable(CreateCampaignScreenViews.CampaignPreview.route){
                    CampaignPreviewScreen(navController, viewmodel = viewmodel, onCampaignDone = onBack)
                }

            }
        }
     }






}

@Composable
fun CampaignPreviewScreen(
    navController: NavController,
    onCampaignDone: () -> Unit,
    viewmodel: EventsViewmodel
) {
    val CampaignDetails by viewmodel.eventDetails.collectAsState()
    val loading by viewmodel.loading.collectAsState()
    val CampaignAdded by viewmodel.eventAdded.collectAsState()

    BackHandler {
        navController.navigate(CreateCampaignScreenViews.CreateCampaign.route) {
            popUpTo(CreateCampaignScreenViews.CampaignPreview.route) {
                inclusive = true
            }
        }
    }

    LaunchedEffect(CampaignAdded) {
        if (CampaignAdded) {
            onCampaignDone()
        }
    }

    Scaffold(
        containerColor = grey,
        bottomBar = {
            Surface( color = Color.White) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    SecondaryButton(
                        text = "Edit details",
                        onClick = {
                            navController.navigate(CreateCampaignScreenViews.CreateCampaign.route) {
                                popUpTo(CreateCampaignScreenViews.CreateCampaign.route) { inclusive = true }
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
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .fillMaxSize()
                .background(Color.White, RoundedCornerShape(12.dp)),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "Campaign preview",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = satoshi_medium
                )
            }

            item {
                LabelledSection(label = "Banner") {
                    ImageCardFromUri(
                        imageUri = CampaignDetails.bannerImage.toString(),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            item {
                LabelledSection(label = "Campaign title") {
                    Text(CampaignDetails.title ?: "", fontSize = 16.sp, fontFamily = satoshi_medium)
                }
            }

            item {
                LabelledSection(label = "Campaign description") {
                    Text(CampaignDetails.description ?: "", fontSize = 16.sp, fontFamily = satoshi_medium)
                }
            }

            item {
                LabelledSection(label = "Campaign address") {
                    Text(CampaignDetails.address ?: "", fontSize = 16.sp, fontFamily = satoshi_medium)
                }
            }

            item {
                LabelledSection(label = "Campaign start date") {
                    Text(CampaignDetails.startDate ?: "", fontSize = 16.sp, fontFamily = satoshi_medium)
                }
            }

            item {
                LabelledSection(label = "Campaign end date") {
                    Text(CampaignDetails.endDate ?: "", fontSize = 16.sp, fontFamily = satoshi_medium)
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
fun CreateCampaignScreen(navController: NavController, viewmodel: EventsViewmodel) {
    val CampaignDetails by viewmodel.eventDetails.collectAsState()
    var showStartCalendar by remember { mutableStateOf(false) }
    var showEndCalendar by remember { mutableStateOf(false) }
    var startDate by remember { mutableStateOf<LocalDate?>(null) }
    var endDate by remember { mutableStateOf<LocalDate?>(null) }
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
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
                modifier = Modifier.background(Color.White).fillMaxWidth().padding(top = 20.dp)
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
                    text = "Let's get started with Campaign",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = satoshi_medium,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(Modifier.height(20.dp))
            }
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                SimpleImagePicker(
                    text = "Upload Banner Image",
                    ImageUri = CampaignDetails.bannerImage,
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
                    value = CampaignDetails.title,
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
                    value = CampaignDetails.description,
                    onValueChange = {
                        viewmodel.updateDescription(it)
                    })
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Campaign Address",
                    fontSize = 14.sp,
                    fontFamily = satoshi_regular,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(vertical = 6.dp)
                )
                Spacer(modifier = Modifier.height(6.dp))
                TextFieldView(
                    placeholder = "Enter Campaign Address",
                    value = CampaignDetails.address,
                    onValueChange = {
                        viewmodel.updateAddress(it)
                    })
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Maximum Number of Participants",
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
                    value = CampaignDetails.numberOfParticipant,
                    onValueChange = {
                        viewmodel.updateNumberOfParticipant(it)
                    })
                Spacer(modifier = Modifier.height(6.dp))

                OfferDateSelector(
                    text = "Campaign",
                    offerStartDate = CampaignDetails.startDate,
                    offerEndDate = CampaignDetails.endDate,
                    onStartClick = { showStartCalendar = true },
                    onEndClick = {
                        if (CampaignDetails.startDate.isEmpty()) {
                            viewmodel.showError("Please choose start date first")
                        } else {
                            showEndCalendar = true
                        }
                    }
                )

                Spacer(modifier = Modifier.height(6.dp))
                ButtonView(text = "Next", horizontal_padding = 0.dp) {
                    if (!viewmodel.validateEventDetails()) {
                        return@ButtonView
                    }
                    navController.navigate(CreateCampaignScreenViews.CampaignPreview.route) {
                        popUpTo(CreateCampaignScreenViews.CreateCampaign.route) {
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
