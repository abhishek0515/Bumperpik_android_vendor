package com.bumperpick.bumperpick_Vendor.Screens.Ads

import android.widget.Toast
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.bumperpick.bumperpick_Vendor.API.FinalModel.DataXXXXXXXXXXXX
import com.bumperpick.bumperpick_Vendor.API.Model.success_model
import com.bumperpick.bumperpick_Vendor.R
import com.bumperpick.bumperpick_Vendor.Screens.Component.BottomSheetItem
import com.bumperpick.bumperpick_Vendor.Screens.Component.ButtonView
import com.bumperpick.bumperpick_Vendor.Screens.Component.formatDate
import com.bumperpick.bumperpick_Vendor.Screens.Campaign.InfoRow

import com.bumperpick.bumperpick_Vendor.Screens.OfferPage.DeleteExpiredOfferDialog
import com.bumperpick.bumperpick_Vendor.Screens.QrScreen.UiState
import com.bumperpick.bumperpick_Vendor.ui.theme.BtnColor
import com.bumperpick.bumperpick_Vendor.ui.theme.satoshi_medium
import com.bumperpick.bumperpick_Vendor.ui.theme.satoshi_regular
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdsScreen(
    onBackClick:()->Unit,
    EditAd:(id:String)->Unit,
    Add_Ad:()->Unit,
    viewModel: AdsViewModel = koinViewModel())
{
    
    val adsState by viewModel.vendorAdsUiState.collectAsState()
    LaunchedEffect(Unit) {viewModel.getVendorAds() }

    val sheetState = rememberModalBottomSheetState()
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedId by remember { mutableStateOf("") }
    val context= LocalContext.current
    val delete by viewModel.deleteAdsUiState.collectAsState()

    LaunchedEffect(delete) {
        when(delete){
            UiState.Empty -> {}
            is UiState.Error ->{
                Toast.makeText(context,
                    (delete as UiState.Error).message,Toast.LENGTH_SHORT).show()
            }
            UiState.Loading -> {}
            is UiState.Success -> {
                Toast.makeText(context,
                   (delete as UiState.Success<success_model>).data.message,Toast.LENGTH_SHORT).show()
            }
        }
      
    }
    if(showDeleteDialog){
        DeleteExpiredOfferDialog(true, onDismiss = {showDeleteDialog=false}, onConfirmDelete ={
            showDeleteDialog=false
            viewModel.deleteAds(selectedId)

        }, name = "Ads" )
    }
    if(showBottomSheet) {
        ModalBottomSheet(
            containerColor = Color.White, // Change this color
            contentColor = Color.Black,
            dragHandle = null,
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState
        ) {
            Ad_EditDeleteBottomSheet(
                onDismiss = {
                    showBottomSheet = false
                },
                onDeleteClick = {
                    showDeleteDialog=true
                },
                onEditClick = {
                    EditAd(selectedId)
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
    Scaffold() {
        Box(modifier = Modifier.fillMaxSize().padding(it)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
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
                    )
                    {
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
                                    text = "Ads And Banner",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 22.sp,
                                    color = Color.White
                                )
                            }


                        }

                        Spacer(modifier = Modifier.height(24.dp))


                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                when (adsState) {
                    UiState.Empty -> {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Text(
                                text = "No Ads available",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.align(Alignment.Center),
                                color = BtnColor
                            )
                        }
                    }

                    is UiState.Error -> {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Text(
                                text = (adsState as UiState.Error).message,
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
                        val adsList = (adsState as UiState.Success).data.data

                        Text(
                            text = "${adsList.size} ADS",
                            fontSize = 16.sp,
                            fontFamily = satoshi_regular,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Gray,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        LazyColumn(
                            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {

                            items(adsList) { ad ->
                                AdCard(ad) {

                                    selectedId = it
                                    showBottomSheet = true
                                }

                            }


                        }
                    }
                }


            }
            FloatingActionButton(
                containerColor = BtnColor,
                onClick = { Add_Ad() },
                content = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        tint = Color.White,
                        contentDescription = "Scan QR",
                    )
                },
                modifier = Modifier.align(Alignment.BottomEnd)
                    .padding(bottom = 16.dp, end = 16.dp).size(60.dp),
                shape = CircleShape,
            )
        }
    }

   }

@Composable
fun AdCard(events: DataXXXXXXXXXXXX, showBottomSheet:(id:String)->Unit={}) {
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
                    model = events.banner,
                    contentDescription = "CAMPAIGN Banner",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(20.dp))
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
                            showBottomSheet((events.id.toString()))
                        },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector =  Icons.Outlined.MoreVert,
                            contentDescription ="More options",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }


            }

            // Event Details
            Column(modifier = Modifier.padding(16.dp)) {


                InfoRow(
                    icon = painterResource(R.drawable.calendar_alt),
                    text ="Start Date: ${formatDate( events.start_date)}"
                )

                InfoRow(
                    icon = painterResource(R.drawable.calendar_alt),
                    text = "End Date: ${formatDate( events.end_date)}"
                )


            }
        }
    }
}

@Composable
fun Ad_EditDeleteBottomSheet(
    onEditClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color.White,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            )
            .padding(bottom = 16.dp)
    ) {
        // Handle bar for visual feedback
        Box(
            modifier = Modifier
                .width(40.dp)
                .height(4.dp)
                .background(
                    Color.Gray.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(2.dp)
                )
                .align(Alignment.CenterHorizontally)
                .padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Header
        Text(
            text = "Manage Ads",
            fontSize = 20.sp,
            fontFamily = satoshi_medium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 16.dp),
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Edit option
        BottomSheetItem(
            icon = {
                Icon(
                    painter = painterResource(R.drawable.pencil_svgrepo_com),
                    contentDescription = "Edit",
                    modifier = Modifier.size(24.dp),
                    tint = BtnColor
                )
            },
            title = "Edit Ads Details",

            onClick = onEditClick
        )

        // Divider
        Divider(
            modifier = Modifier.padding(horizontal = 16.dp),
            color = Color.Gray.copy(alpha = 0.2f),
            thickness = 0.5.dp
        )

        // Delete option
        BottomSheetItem(
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "Delete",
                    modifier = Modifier.size(24.dp),
                    tint = BtnColor
                )
            },
            title = "Delete Ads",

            onClick = onDeleteClick,
            titleColor = Color.Red
        )

        Spacer(modifier = Modifier.height(24.dp))
        ButtonView(text = "Cancel", onClick = onDismiss,)
    }
}
