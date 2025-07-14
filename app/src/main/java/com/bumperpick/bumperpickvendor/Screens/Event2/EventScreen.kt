package com.bumperpick.bumperpickvendor.Screens.Event2

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.bumperpick.bumperpickvendor.API.FinalModel.DataXXXXXXXX
import com.bumperpick.bumperpickvendor.API.FinalModel.DataXXXXXXXXX
import com.bumperpick.bumperpickvendor.API.FinalModel.EventModel
import com.bumperpick.bumperpickvendor.R
import com.bumperpick.bumperpickvendor.Repository.OfferValidation
import com.bumperpick.bumperpickvendor.Screens.Component.EditDelete
import com.bumperpick.bumperpickvendor.Screens.Component.Event_EditDeleteBottomSheet
import com.bumperpick.bumperpickvendor.Screens.Component.formatDate
import com.bumperpick.bumperpickvendor.Screens.OfferPage.DeleteExpiredOfferDialog
import com.bumperpick.bumperpickvendor.Screens.QrScreen.UiState
import com.bumperpick.bumperpickvendor.ui.theme.BtnColor
import com.bumperpick.bumperpickvendor.ui.theme.satoshi_bold
import com.bumperpick.bumperpickvendor.ui.theme.satoshi_regular
import com.google.accompanist.systemuicontroller.rememberSystemUiController

import org.koin.androidx.compose.koinViewModel

// Define UiState sealed class if not already defined


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventScreen2(
    onBackClick: () -> Unit,
    EditEvent:(id:String)->Unit,
    event_detail_click:(id:Int)->Unit,

    viewmodel: Events2Viewmodel = koinViewModel(),
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedTabIndex by remember { mutableStateOf(0) }
    val eventsState by viewmodel.uiEvents.collectAsState()
    var selectedId by remember { mutableStateOf("") }
    val sheetState = rememberModalBottomSheetState()
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }
    val context= LocalContext.current
    val delete by viewmodel.delete.collectAsState()
    // Load events when the screen is first composed or tab changes
    LaunchedEffect(delete) {
        if(delete.isNotEmpty()){
            Toast.makeText(context,delete,Toast.LENGTH_SHORT).show()
            viewmodel.cleardata()
        }
    }
    LaunchedEffect(selectedTabIndex) {
        viewmodel.getEvents()

    }
    if(showDeleteDialog){
        DeleteExpiredOfferDialog(true, onDismiss = {showDeleteDialog=false}, onConfirmDelete ={
            showDeleteDialog=false
            showBottomSheet=false
            viewmodel.deleteOffer(selectedId,"Delete Expired Event")

        }, name = "Event" )
    }

    if(showBottomSheet) {
        ModalBottomSheet(
            containerColor = Color.White, // Change this color
            contentColor = Color.Black,
            dragHandle = null,
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState
        ) {
            Event_EditDeleteBottomSheet(
                onDismiss = {
                    showBottomSheet = false
                },
                onDeleteClick = {
                    showDeleteDialog=true
                },
                onEditClick = {
                    EditEvent(selectedId)
                }

            )
        }
    }

    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            Color.Transparent,
            Color.White.copy(alpha = 0.05f),
            Color.White.copy(alpha = 0.07f),
            Color.White.copy(alpha = 0.1f),
            Color.White.copy(alpha = 0.2f)
        )
    )
    val transparentBrush = Brush.horizontalGradient(
        colors = listOf(Color.Transparent, Color.Transparent)
    )
    val systemUiController = rememberSystemUiController()
    val statusBarColor = Color(0xFF5A0E26) // Your desired color

    // Change status bar color
    SideEffect {
        systemUiController.setStatusBarColor(
            color = statusBarColor,
            darkIcons = false // true for dark icons on light background
        )
    }

    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(Color(0xFFFAFAFA))
        )
        {
            var size by remember { mutableStateOf(IntSize.Zero) }
            val backgroundModifier = remember(size) {
                if (size.width > 0 && size.height > 0) {
                    val radius = maxOf(size.width, size.height) / 1.5f
                    Modifier.background(
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0xFF8B1538), Color(0xFF5A0E26)),
                            center = Offset(size.width / 2f, size.height / 2f),
                            radius = radius
                        )
                    )
                } else {
                    Modifier.background(Color(0xFF8B1538))
                }
            }

            // Header Card with improved padding and structure
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .onSizeChanged { size = it },
                shape = RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 0.dp,
                    bottomStart = 24.dp,
                    bottomEnd = 24.dp
                ),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .then(backgroundModifier)
                        .padding(bottom = 0.dp)
                ) {
                    Spacer(modifier = Modifier.height(12.dp))

                    // Top App Bar with improved spacing
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.align(Alignment.CenterStart)
                        ) {
                            IconButton(
                                onClick = onBackClick,
                                modifier = Modifier.size(44.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Back",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Events",
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp,
                                color = Color.White
                            )
                        }


                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Search Field with improved styling
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Search,
                                contentDescription = "Search",
                                tint = Color.Gray.copy(alpha = 0.7f)
                            )
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(
                                    onClick = { searchQuery = "" }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Clear",
                                        tint = Color.Gray.copy(alpha = 0.7f)
                                    )
                                }
                            }
                        },
                        placeholder = {
                            Text(
                                text = "Search Events",
                                color = Color.Gray.copy(alpha = 0.6f)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            cursorColor = BtnColor,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                            focusedBorderColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Tab Row with improved styling
                    TabRow(
                        selectedTabIndex = selectedTabIndex,
                        containerColor = Color.Transparent,
                        divider = {},
                        indicator = { tabPositions ->
                            TabRowDefaults.Indicator(
                                modifier = Modifier
                                    .tabIndicatorOffset(tabPositions[selectedTabIndex])
                                    .height(4.dp)
                                    .padding(horizontal = 0.dp),
                                color = Color.White
                            )
                        },
                        modifier = Modifier.padding(horizontal = 0.dp)
                    ) {
                        Tab(
                            selected = selectedTabIndex == 0,
                            onClick = { selectedTabIndex = 0 },
                            text = {
                                Text(
                                    "Ongoing Event",
                                    fontSize = 16.sp,
                                    fontWeight = if (selectedTabIndex == 0) FontWeight.SemiBold else FontWeight.Normal
                                )
                            },
                            selectedContentColor = Color.White,
                            unselectedContentColor = Color.White.copy(alpha = 0.7f),
                            modifier = Modifier
                                .background(if (selectedTabIndex == 0) gradientBrush else transparentBrush)
                        )

                        Tab(
                            selected = selectedTabIndex == 1,
                            onClick = { selectedTabIndex = 1 },
                            text = {
                                Text(
                                    "Expired Event",
                                    fontSize = 16.sp,
                                    fontWeight = if (selectedTabIndex == 1) FontWeight.SemiBold else FontWeight.Normal
                                )
                            },
                            selectedContentColor = Color.White,
                            unselectedContentColor = Color.White.copy(alpha = 0.7f),
                            modifier = Modifier
                                .background(if (selectedTabIndex == 1) gradientBrush else transparentBrush)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            when (eventsState) {
                UiState.Empty -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = "No Event available",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.align(Alignment.Center),
                            color = BtnColor
                        )
                    }
                }

                is UiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = (eventsState as UiState.Error).message,
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
                    val eventList = (eventsState as UiState.Success).data

// Filter based on selectedTabIndex
                    val filteredEventsByTab = if (selectedTabIndex == 0) {
                        eventList.filter { !it.expire }
                    } else {
                        eventList.filter { it.expire }
                    }

// Further filter based on search query
                    val filteredList = filteredEventsByTab.filter {
                        it.title.contains(searchQuery, ignoreCase = true)
                    }


                    // Events count with improved styling
                    Text(
                        text = if (selectedTabIndex == 0) "${filteredList.size} ONGOING EVENT" else "${filteredList.size} EXPIRED EVENT",
                        fontSize = 16.sp,
                        fontFamily = satoshi_regular,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Gray,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Events List with improved padding
                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(filteredList) { event ->
                            EventCard(
                                event, onClick = {
                                    event_detail_click(it.id)
                                },
                                showBottomSheet = {
                                    when (it) {
                                        is EditDelete.DELETE -> {
                                            showDeleteDialog = true
                                            showBottomSheet=false
                                            selectedId = it.offerId
                                        }

                                        is EditDelete.EDIT -> {
                                            selectedId = it.offerId
                                            showBottomSheet = true

                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }


        }
    }
}
@Composable
fun EventCard(
    events: DataXXXXXXXXX,
    onClick: (DataXXXXXXXXX) -> Unit,
    showBottomSheet: (EditDelete) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick(events) }, // Added clickable functionality
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Banner Image with Action Button
            EventBanner(
                bannerUrl = events.banner_image_url,
                isExpired = events.expire,
                eventId = events.id.toString(),
                onActionClick = showBottomSheet
            )

            // Event Details
            EventDetails(
                title = events.title,
                startDate = events.start_date,
                endDate=events.end_date,
                starttime=events.start_time,
                deadline = events.end_time,
                isExpired = events.expire
            )
        }
    }
}

@Composable
private fun EventBanner(
    bannerUrl: String,
    isExpired: Boolean,
    eventId: String,
    onActionClick: (EditDelete) -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        // Banner Image
        AsyncImage(
            model = bannerUrl,
            contentDescription = "Event Banner",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
            placeholder = painterResource(R.drawable.image_1), // Add placeholder
            error = painterResource(R.drawable.image_1) // Add error image
        )

        // Action Button (positioned at top-right)
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp)
                .background(
                    color = Color.Black.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(8.dp)
                )
                .border(
                    width = 0.5.dp,
                    color = BtnColor,
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            IconButton(
                onClick = {
                    val action = if (isExpired) {
                        EditDelete.DELETE(eventId)
                    } else {
                        EditDelete.EDIT(eventId)
                    }
                    onActionClick(action)
                },
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = if (isExpired) Icons.Outlined.Delete else Icons.Outlined.MoreVert,
                    contentDescription = if (isExpired) "Delete expired event" else "More options",
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        // Expired Badge (if expired)
        if (isExpired) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(12.dp)
                    .background(
                        color = Color.Red.copy(alpha = 0.9f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "EXPIRED",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun EventDetails(
    title: String,
    startDate: String,
    deadline: String,
    endDate:String,
    starttime:String,
    isExpired: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Event Title
        Text(
            text = title,
            fontFamily = satoshi_bold,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 28.sp,
            color = if (isExpired) Color.Gray else Color.Black,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Event Info
        InfoRow(
            icon = painterResource(R.drawable.calendar_alt),
            text = "Start Date: ${formatDate(startDate)}",
            isExpired = isExpired
        )

        Spacer(modifier = Modifier.height(8.dp))
        InfoRow(
            icon = painterResource(R.drawable.clock),
            text = "Start Time: $starttime",
            isExpired = isExpired
        )
        // Event Info

        Spacer(modifier = Modifier.height(8.dp))


        InfoRow(
            icon = painterResource(R.drawable.calendar_alt),
            text = "End Date: ${formatDate(endDate)}",
            isExpired = isExpired
        )

        Spacer(modifier = Modifier.height(8.dp))
        InfoRow(
            icon = painterResource(R.drawable.clock),
            text = "End Time: $deadline",
            isExpired = isExpired
        )

        // Expiry Status
        if (isExpired) {
            Spacer(modifier = Modifier.height(12.dp))
            InfoRow(
                icon = painterResource(R.drawable.check),
                text = "Event has expired",
                iconTint = Color(0xFF50C878),
                textColor = Color(0xFF50C878)
            )
        }
    }
}

@Composable
private fun InfoRow(
    icon: Painter,
    text: String,
    modifier: Modifier = Modifier,
    iconTint: Color = Color.Gray,
    textColor: Color = Color.Gray,
    isExpired: Boolean = false
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = icon,
            contentDescription = null,
            tint = if (isExpired && iconTint == Color.Gray) Color.Gray.copy(alpha = 0.6f) else iconTint,
            modifier = Modifier.size(16.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = text,
            fontSize = 14.sp,
            color = if (isExpired && textColor == Color.Gray) Color.Gray.copy(alpha = 0.6f) else textColor,
            fontFamily = satoshi_regular // Assuming you have this font family
        )
    }
}