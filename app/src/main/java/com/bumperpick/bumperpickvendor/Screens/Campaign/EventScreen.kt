package com.bumperpick.bumperpickvendor.Screens.Campaign

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.bumperpick.bumperpickvendor.R
import com.bumperpick.bumperpickvendor.Screens.Component.Campaign_EditDeleteBottomSheet
import com.bumperpick.bumperpickvendor.Screens.Component.EditDelete
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
fun EventScreen(
    onBackClick: () -> Unit,
    EditEvent:(id:String)->Unit,
    event_detail_click:(id:Int)->Unit,

    viewmodel: EventsViewmodel = koinViewModel(),
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

        }, name = "Campaign" )
    }

    if(showBottomSheet) {
        ModalBottomSheet(
            containerColor = Color.White, // Change this color
            contentColor = Color.Black,
            dragHandle = null,
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState
        ) {
            Campaign_EditDeleteBottomSheet(
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
    val systemUiController = rememberSystemUiController()
    val statusBarColor = Color(0xFF5A0E26) // Your desired color

    // Change status bar color
    SideEffect {
        systemUiController.setStatusBarColor(
            color = statusBarColor,
            darkIcons = false // true for dark icons on light background
        )
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
                Spacer(modifier = Modifier.height(16.dp))

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
                            text = "Campaigns",
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
                            text = "Search Campaigns",
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
                                "Ongoing Campaign",
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
                                "Expired Campaign",
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
                    text = "No Campaign available",
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
                    text = if (selectedTabIndex == 0) "${filteredList.size} ONGOING CAMPAIGN" else "${filteredList.size} EXPIRED CAMPAIGN",
                    fontSize = 16.sp,
                    fontFamily = satoshi_regular,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Events List with improved padding
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(filteredList) { event ->
                        EventCard(event, onClick = {
                            event_detail_click(it.id)
                        },
                            showBottomSheet = {
                                when(it){
                                    is EditDelete.DELETE -> {
                                        showDeleteDialog=true
                                        selectedId=it.offerId
                                    }
                                    is EditDelete.EDIT -> {
                                        selectedId=it.offerId
                                        showBottomSheet=true

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
fun EventCard(events: DataXXXXXXXX, onClick: (DataXXXXXXXX) -> Unit,  showBottomSheet:(EditDelete)->Unit={}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            // Banner Image with Badge
            Box {
                AsyncImage(
                    model = events.banner_image_url,
                    contentDescription = "CAMPAIGN Banner",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(20.dp))
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .background(
                            color = Color(0xFFFF6D00),
                            shape = RoundedCornerShape(50)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "MAX ${events.number_of_participant} PARTICIPANTS",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Event Details
            Column(modifier = Modifier.padding(16.dp)) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                       ,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Event Title
                    Text(
                        text = events.title,
                        fontFamily = satoshi_bold,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 28.sp,
                        modifier = Modifier.weight(1f) // So the text doesn't overlap the icon
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    // Action Icon
                    Box(
                        modifier = Modifier
                            .background(
                                color = Color.Black.copy(alpha = 0.1f),
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
                                if (events.expire) {
                                    showBottomSheet(EditDelete.DELETE(events.id.toString()))
                                } else {
                                    showBottomSheet(EditDelete.EDIT(events.id.toString()))
                                }
                            },
                            modifier = Modifier.size(36.dp) // consistent touch target
                        ) {
                            Icon(
                                imageVector = if (events.expire) Icons.Outlined.Delete else Icons.Outlined.MoreVert,
                                contentDescription = if (events.expire) "Delete" else "More Options",
                                tint = BtnColor,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }



                Spacer(modifier = Modifier.height(8.dp))

                InfoRow(
                    icon = painterResource(R.drawable.home),
                    text = events.description
                )

                InfoRow(
                    icon = painterResource(R.drawable.loading),
                    text = "Deadline: ${formatDate(events.end_date)}"
                )

                InfoRow(
                    icon = Icons.Outlined.LocationOn,
                    text = events.address,
                    vectorIcon = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                HorizontalDivider(
                    thickness = 1.dp,
                    color = Color.Gray.copy(alpha = 0.15f)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Registration / Expiry Info
                if (events.expire) {
                    InfoRow(
                        icon = painterResource(R.drawable.check),
                        text = "Campaign is expired.",
                        iconTint = Color(0xFF50C878),
                        textColor = Color(0xFF50C878)
                    )
                } else {
                    OutlinedButton(
                        onClick = { onClick(events) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        border = BorderStroke(1.dp, BtnColor),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = "See Participants",
                            color = BtnColor,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
 fun InfoRow(
    icon: Any,
    text: String,
    vectorIcon: Boolean = false,
    iconTint: Color = Color.Gray,
    textColor: Color = Color.Gray
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        if (vectorIcon && icon is ImageVector) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = iconTint
            )
        } else if (icon is Painter) {
            Icon(
                painter = icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = iconTint
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            fontSize = 16.sp,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            fontFamily = satoshi_regular,
            fontWeight = FontWeight.Medium,
            color = textColor.copy(alpha = 0.8f)
        )
    }
}
