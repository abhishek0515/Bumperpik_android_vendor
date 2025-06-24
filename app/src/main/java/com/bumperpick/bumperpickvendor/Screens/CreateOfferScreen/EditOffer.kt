package com.bumperpick.bumperpickvendor.Screens.CreateOfferScreen

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import coil.compose.AsyncImage
import com.bumperpick.bumperpickvendor.API.FinalModel.Media
import com.bumperpick.bumperpickvendor.R
import com.bumperpick.bumperpickvendor.Repository.HomeOffer

import com.bumperpick.bumperpickvendor.Screens.Component.PrimaryButton
import com.bumperpick.bumperpickvendor.Screens.Component.TextFieldView
import com.bumperpick.bumperpickvendor.ui.theme.BtnColor

import com.bumperpick.bumperpickvendor.ui.theme.grey
import com.bumperpick.bumperpickvendor.ui.theme.satoshi_medium
import com.bumperpick.bumperpickvendor.ui.theme.satoshi_regular
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun ProductDetailsSection(
    offerDetail: HomeOffer,
    viewmodel: EditOfferViewmodel
) {
    var showStartCalendar by remember { mutableStateOf(false) }
    var showEndCalendar by remember { mutableStateOf(false) }
    var startDate by remember { mutableStateOf<LocalDate?>(null) }
    var endDate by remember { mutableStateOf<LocalDate?>(null) }
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

    // Initialize dates from offer detail when available
    LaunchedEffect(offerDetail.startDate, offerDetail.endDate) {
        offerDetail.startDate?.let { dateString ->
            try {
                startDate = LocalDate.parse(dateString, formatter)
            } catch (e: Exception) {
                // Handle parsing error
            }
        }
        offerDetail.endDate?.let { dateString ->
            try {
                endDate = LocalDate.parse(dateString, formatter)
            } catch (e: Exception) {
                // Handle parsing error
            }
        }
    }

    Column {
        Text(
            text = "Product Title",
            fontSize = 14.sp,
            fontFamily = satoshi_regular,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(Modifier.height(4.dp))
        TextFieldView(
            value = offerDetail.offerTitle ?: "",
            onValueChange = { viewmodel.updateProductname(it) },
            placeholder = "Enter Product Title",
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Product Description",
            fontSize = 14.sp,
            fontFamily = satoshi_regular,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        // Product Description
        TextFieldView(
            value = offerDetail.offerDescription ?: "",
            onValueChange = { viewmodel.updateProductDescription(it) },
            placeholder = "Enter Product Description",
            singleLine = false,
            modifier = Modifier.height(100.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Terms and Conditions",
            fontSize = 14.sp,
            fontFamily = satoshi_regular,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        // Terms and Conditions
        TextFieldView(
            value = offerDetail.termsAndCondition ?: "",
            onValueChange = { viewmodel.updateTermsAndCondition(it) },
            placeholder = "Enter Terms and Conditions",
            singleLine = false,
        )

        Spacer(modifier = Modifier.height(16.dp))
        OfferDateSelector(
            offerStartDate = offerDetail.startDate,
            offerEndDate = offerDetail.endDate,
            onStartClick = { showStartCalendar = true },
            onEndClick = {
                if (offerDetail.startDate.isEmpty()) {
                    viewmodel.showError("Please choose start date first")
                } else {
                    showEndCalendar = true
                }
            }
        )
    }

    CalendarBottomSheet(
        isVisible = showStartCalendar,
        selectedDate = startDate,
        onDateSelected = { startDate = it },
        onDismiss = { showStartCalendar = false },
        startDate = LocalDate.now(),
        text = "Offer Start Date",
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
        text = "Offer End Date",
        onConfirm = {
            if (it != null) {
                viewmodel.updateEndDate(it.format(formatter))
            }
            endDate = it
            showEndCalendar = false
        }
    )
}

@Composable
fun EditOffer(
    viewmodel: EditOfferViewmodel = koinViewModel(),
    offerId: String,
    onOfferDone: () -> Unit,
    onBackPressed: () -> Unit = {}
) {
    val offerDetail by viewmodel.offerDetail.collectAsState()
    val uiState by viewmodel.uiState.collectAsState()
    val newLocalMediaList by viewmodel.newLocalMediaList.collectAsState()
    val deletedUrlMediaList by viewmodel.deletedUrlMediaList.collectAsState()
    val context= LocalContext.current
    var showMediaTypeDialog by remember { mutableStateOf(false) }

    // Media picker launchers
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        if (uris.isNotEmpty()) {
            uris.forEach{
                viewmodel.addNewLocalMedia(it)
            }

        }
    }

    val videoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            viewmodel.addNewLocalMedia(it)
        }
    }

    // Load offer details when component is first created
    LaunchedEffect(offerId) {
        viewmodel.getOfferDetails(offerId)
    }

    // Handle success callback
    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onOfferDone()
            viewmodel.clearSuccess()
        }
    }

    Column(
        modifier = Modifier
            .background(grey)
            .fillMaxSize()
    ) {
        // Header
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 60.dp, bottom = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackPressed,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Edit Offer Details",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = satoshi_medium,
                    color = Color.Black
                )
            }
        }

        // Show loading state
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            // Scrollable Content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // Banner Image
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                ) {
                    println("offer image :- ${offerDetail.brand_logo_url}")
                    AsyncImage(
                        model = offerDetail.brand_logo_url,
                        contentDescription = "Banner Image",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Fit
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))



                val context = LocalContext.current
                val combinedMediaList = getCombinedMediaList(
                   urlMediaList = offerDetail.media,
                    localMediaList = newLocalMediaList,
                    deletedUrlList = deletedUrlMediaList
                )

                Log.d("CombinedMediaList",combinedMediaList.toString())

                // Media Row with Add Button
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Add Media Button - only show if can add more media
                    if (viewmodel.canAddMoreMedia()) {
                        item {
                            AddMediaButton(
                                onClick = { showMediaTypeDialog = true }
                            )
                        }
                    }

                    items(combinedMediaList.size) { index ->
                        val mediaItem = combinedMediaList[index]
                        println("mediaItem $mediaItem")
                        val isVideo = if (mediaItem.isUrl) {
                            isVideoUrl(mediaItem.url!!.url)
                        } else {
                            isVideoFile(context, mediaItem.uri!!)
                        }
                        println("isVideo $isVideo")
                        EnhancedMediaPreviewItem(
                            mediaItem = mediaItem,
                            isVideo = isVideo,
                            onPreview = {
                                // Handle preview - you can implement full screen preview here
                            },
                            onDelete = {
                                if (mediaItem.isUrl) {
                                    // Add to deleted URLs list
                                    viewmodel.addToDeletedUrlMedia(mediaItem.url!!.url)
                                } else {
                                    // Remove from local media list
                                    viewmodel.removeNewLocalMedia(mediaItem.uri!!)
                                }
                            }
                        )
                    }


                }

                Spacer(modifier = Modifier.height(24.dp))

                // Product Details Section
                ProductDetailsSection(
                    offerDetail = offerDetail,
                    viewmodel = viewmodel
                )
            }
        }

        // Bottom Action Button
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Show error message if any
                if (uiState.isError && !uiState.errorMessage.isNullOrBlank()) {
                    Text(
                        text = uiState.errorMessage!!,
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                PrimaryButton(
                    text = if (uiState.isLoading) "Updating..." else "Update Offer Details",
                    onClick = {
                        viewmodel.updateOffer(
                            offerId = offerId,
                            deletedUrlMedia = deletedUrlMediaList,
                            newLocalMedia = newLocalMediaList,
                            onSuccess = {
                                Toast.makeText(context,"Offer Updated",Toast.LENGTH_SHORT).show()
                            }
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isLoading
                )
            }
        }
    }

    // Media Type Selection Dialog
    if (showMediaTypeDialog) {
        EditMediaTypeSelectionDialog(
            onImageSelected = {
                showMediaTypeDialog = false
                imagePickerLauncher.launch("image/*")
            },
            onVideoSelected = {
                showMediaTypeDialog = false
                videoPickerLauncher.launch("video/*")
            },
            onDismiss = { showMediaTypeDialog = false },
            currentImageCount = viewmodel.getTotalMediaCount(),
            currentVideoCount = viewmodel.getTotalMediaCount()
        )
    }
}

@Composable
fun  EditMediaTypeSelectionDialog(
    onImageSelected: () -> Unit,
    onVideoSelected: () -> Unit,
    onDismiss: () -> Unit,
    currentImageCount: Int,
    currentVideoCount: Int,
    maxImageCount:Int=10,
    maxVideoCount:Int=10
) {
    AlertDialog(
        modifier = Modifier.background(Color.Transparent),
        containerColor = Color.White,
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Select Media Type",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                Text(
                    text = "Choose what type of media you want to upload:",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Image Option
                Card(
                    onClick = {
                        if (currentImageCount < 10) {
                            onImageSelected()
                        }
                    },
                    border = BorderStroke(1.dp, BtnColor),
                    enabled = currentImageCount < 10,
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (currentImageCount < maxImageCount) Color.White else BtnColor.copy(alpha = 0.3f)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_image),
                            contentDescription = "Images",
                            tint = if (currentImageCount < maxImageCount) BtnColor else Color.Gray,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Images",
                                fontWeight = FontWeight.Medium,
                                color = if (currentImageCount < maxImageCount) Color.Black else Color.Gray
                            )
                            Text(
                                text = "Max 10MB each • $currentImageCount/$maxImageCount selected",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Video Option
                Card(
                    onClick = {
                        if (currentVideoCount < maxVideoCount) {
                            onVideoSelected()
                        }
                    },
                    enabled = currentVideoCount < maxVideoCount,
                    modifier = Modifier.fillMaxWidth(),
                    border = BorderStroke(1.dp, BtnColor),
                    colors = CardDefaults.cardColors(
                        containerColor = if (currentVideoCount < maxVideoCount) Color.White else BtnColor.copy(alpha = 0.3f)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_video),
                            contentDescription = "Videos",
                            tint = if (currentVideoCount < maxVideoCount)BtnColor else Color.Gray,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Videos",
                                fontWeight = FontWeight.Medium,
                                color = if (currentVideoCount < maxVideoCount) Color.Black else Color.Gray
                            )
                            Text(
                                text = "Max 50MB each • $currentVideoCount/$maxVideoCount selected",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = BtnColor)
            }
        }
    )
}
// Data class to represent media items
data class MediaItem(
    val isUrl: Boolean,
    val url: Media? = null,
    val uri: Uri? = null
)
// Combines URL media and local media (excluding deleted ones)
fun getCombinedMediaList(
    urlMediaList: List<Media>,
    localMediaList: List<Uri>,
    deletedUrlList: List<String>
): List<MediaItem> {
    val combinedList = mutableListOf<MediaItem>()

    // Add URL media items, skipping those marked for deletion
    urlMediaList.forEach { media ->
        if (!deletedUrlList.contains(media.id.toString())) {
            combinedList.add(MediaItem(isUrl = true, url = media))
        }
    }

    // Add local media items
    localMediaList.forEach { uri ->
        combinedList.add(MediaItem(isUrl = false, uri = uri))
    }

    return combinedList
}


// Helper function to check if URL is video
fun isVideoUrl(url: String): Boolean {
    val videoExtensions = listOf(".mp4", ".avi", ".mov", ".wmv", ".flv", ".webm", ".mkv")
    return videoExtensions.any { url.lowercase().contains(it) }
}


@Composable
fun EnhancedMediaPreviewItem(
    mediaItem: MediaItem,
    isVideo: Boolean,
    onPreview: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .size(100.dp)
            .clickable { onPreview() },
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color.Gray.copy(0.2f)),
        colors = CardDefaults.cardColors(containerColor = Color.Gray.copy(alpha = 0.1f))
    ) {
        Box {
            if (isVideo) {
                if (mediaItem.isUrl) {
                    // For URL videos, show placeholder with play icon
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Gray.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_video),
                            contentDescription = "Video",
                            tint = Color.Gray,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                } else {
                    // For local videos, show thumbnail
                    VideoThumbnail(
                        videoUri = mediaItem.uri!!,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Play icon overlay
                Icon(
                    painter = painterResource(R.drawable.ic_play_circle),
                    contentDescription = "Play Video",
                    tint = Color.White,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(24.dp)
                )
            } else {
                // Display image (both URL and local)
                println("mediaItemII ${mediaItem.url.toString()}")
                AsyncImage(
                    model = if (mediaItem.isUrl) mediaItem.url?.url else mediaItem.uri,
                    contentDescription = "Media Preview",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            // Delete button
            IconButton(
                onClick = onDelete,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(24.dp)
                    .background(
                        Color.Red.copy(alpha = 0.8f),
                        CircleShape
                    )
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_close),
                    contentDescription = "Delete",
                    tint = Color.White,
                    modifier = Modifier.size(12.dp)
                )
            }
        }
    }
}

@Composable
fun AddMediaButton(
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .size(100.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(
            1.dp,
            BtnColor
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Media",
                    tint = BtnColor,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Add",
                    fontSize = 12.sp,
                    color = BtnColor,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}