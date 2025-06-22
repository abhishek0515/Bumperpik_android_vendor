package com.bumperpick.bumperpickvendor.Screens.CreateOfferScreen

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.bumperpick.bumperpickvendor.R
import com.bumperpick.bumperpickvendor.ui.theme.BtnColor
import com.bumperpick.bumperpickvendor.ui.theme.satoshi_medium
// Required imports
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.util.Log
import android.widget.VideoView
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.bumperpick.bumperpickvendor.Screens.Component.PrimaryButton
import com.bumperpick.bumperpickvendor.Screens.Component.SecondaryButton
import com.bumperpick.bumperpickvendor.Screens.Component.TextFieldView
import com.bumperpick.bumperpickvendor.Screens.VendorDetailPage.VendorDetailScreen
import com.bumperpick.bumperpickvendor.ui.theme.grey
import com.bumperpick.bumperpickvendor.ui.theme.satoshi_regular
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.androidx.compose.koinViewModel
import java.io.File

@Composable
fun MoreOfferDetailsScreen(navController: NavController,viewmodel: CreateOfferViewmodel){
    val offerDetails by viewmodel.offerDetails.collectAsState()
    BackHandler {
        navController.navigate(CreateOfferScreenViews.EditBanner.route){
            popUpTo(CreateOfferScreenViews.EditBanner.route)
        }
    }
    Column(
        modifier = Modifier
            .background(grey)
            .fillMaxSize()
    ) {
        // First Column - Scrollable content (takes remaining space)
        Column(
            modifier = Modifier
                .padding(bottom = 30.dp)
                .verticalScroll(rememberScrollState())
                .weight(1f)
                .background(grey, RoundedCornerShape(8.dp))
        ) {
            Column(
                modifier = Modifier.background(Color.White).fillMaxWidth().padding(top = 20.dp)
            ) {
                Spacer(Modifier.height(5.dp))
                Text(
                    text = "Step 3 of 3",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = satoshi_regular,
                    color = Color.Black,
                    modifier = Modifier.padding(16.dp)
                )
                Text(
                    text = "Tell us more about your offer",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = satoshi_medium,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(Modifier.height(20.dp))
            }
              Spacer(modifier = Modifier.height(10.dp))
              MultipleMediaPicker(
                  text = "Media",
                  viewModel = viewmodel,
                  modifier = Modifier.padding(horizontal = 16.dp))
            Spacer(Modifier.height(10.dp))
            Text(
                text = "Product Title *",
                fontSize = 14.sp,
                fontFamily = satoshi_regular,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(Modifier.height(4.dp))
            TextFieldView(
                value = offerDetails.productTittle?:"",
                onValueChange = { viewmodel.updateProductname(it) },
                placeholder = "Enter Product Title",
                modifier = Modifier.fillMaxWidth() .padding(horizontal = 16.dp),
                singleLine = true
            )
            Spacer(Modifier.height(10.dp))

            Spacer(Modifier.height(10.dp))
            Text(
                text = "Product Description *",
                fontSize = 14.sp,
                fontFamily = satoshi_regular,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(Modifier.height(4.dp))
            TextFieldView(
                value = offerDetails.productDiscription?:"",
                onValueChange = { viewmodel.updateProductDescription(it) },
                placeholder = "Enter Product Description",
                modifier = Modifier.fillMaxWidth().height(100.dp).padding(horizontal = 16.dp),
                singleLine = false
            )

            Spacer(Modifier.height(10.dp))
            Text(
                text = "Terms and conditions *",
                fontSize = 14.sp,
                fontFamily = satoshi_regular,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(Modifier.height(4.dp))
            TextFieldView(
                value = offerDetails.termsAndCondition?:"",
                onValueChange = {viewmodel.updateTermsAndCondition(it) },
                placeholder = "Enter Terms and conditions",
                modifier = Modifier.fillMaxWidth().height(100.dp).padding(horizontal = 16.dp),
                singleLine = false
            )
        }

        // Second Column - Bottom button (fixed at bottom)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SecondaryButton(
                    text = "Previous",
                    onClick = {
                        navController.navigate(CreateOfferScreenViews.EditBanner.route){
                            popUpTo(CreateOfferScreenViews.EditBanner.route)
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
                PrimaryButton(
                    text = "Next",
                    onClick = {
                      if( viewmodel.validateConfirmOfferScreen()) {
                          navController.navigate(CreateOfferScreenViews.OfferPreview.route){
                              popUpTo(CreateOfferScreenViews.EditBanner.route)
                          }
                      }
                    },
                    modifier = Modifier.weight(1f)
                )
            }

        }
    }
}

@Composable
fun MultipleMediaPicker(
    text: String,
    modifier: Modifier = Modifier,
    viewModel: CreateOfferViewmodel // Replace with your actual ViewModel
) {
   val offerDetails by viewModel.offerDetails.collectAsState()
    val context = LocalContext.current
    var selectedMediaList by remember { mutableStateOf<List<Uri>>(offerDetails.medialList) }
    var showPreviewDialog by remember { mutableStateOf(false) }
    var selectedPreviewMedia by remember { mutableStateOf<Uri?>(null) }
    var isSelectedMediaVideo by remember { mutableStateOf(false) }
    var showMediaTypeDialog by remember { mutableStateOf(false) }

    // Image picker launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri> ->
        val currentImages = selectedMediaList.filter { isImageFile(context, it) }
        val validImages = uris.filter { uri ->
            Log.d("URI",uri.path.toString())
            val size = context.contentResolver.openInputStream(uri)?.available()?.toLong() ?: 0L
            when {
                size > 10 * 1024 * 1024 -> {
                    Toast.makeText(context, "Image size exceeds 10MB", Toast.LENGTH_SHORT).show()
                    false
                }
                currentImages.size >= 10 -> {
                    Toast.makeText(context, "Maximum 10 images allowed", Toast.LENGTH_SHORT).show()
                    false
                }
                else -> true
            }
        }.take(10 - currentImages.size)

        if (validImages.isNotEmpty()) {
            val updatedList = selectedMediaList + validImages
            selectedMediaList = updatedList
            viewModel.updateOffermedialList(updatedList)
        }
        showMediaTypeDialog = false
    }

    // Video picker launcher
    val videoPickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val size = context.contentResolver.openInputStream(it)?.available()?.toLong() ?: 0L
            val currentVideos = selectedMediaList.filter { isVideoFile(context, it) }

            when {
                size > 50 * 1024 * 1024 -> {
                    Toast.makeText(context, "Video size exceeds 50MB", Toast.LENGTH_SHORT).show()
                }
                currentVideos.size >= 2 -> {
                    Toast.makeText(context, "Maximum 2 videos allowed", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    val updatedList = selectedMediaList + it
                    selectedMediaList = updatedList

                    viewModel.updateOffermedialList(updatedList)
                }
            }
        }
        showMediaTypeDialog = false
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontFamily = satoshi_medium,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(2.dp))

        // Media Count Info
        if (selectedMediaList.isNotEmpty()) {
            val imageCount = selectedMediaList.count { isImageFile(context, it) }
            val videoCount = selectedMediaList.count { isVideoFile(context, it) }

            Text(
                text = "Selected: $imageCount/10 images, $videoCount/2 videos",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }

        // Preview Section
        if (selectedMediaList.isNotEmpty()) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(selectedMediaList.size) { index ->
                    val mediaUri = selectedMediaList[index]
                    val isVideo = isVideoFile(context, mediaUri)

                    MediaPreviewItem(
                        mediaUri = mediaUri,
                        isVideo = isVideo,
                        onPreview = {
                        },
                        onDelete = {
                            val updatedList = selectedMediaList.toMutableList()
                            updatedList.removeAt(index)
                            selectedMediaList = updatedList
                            viewModel.updateOffermedialList(updatedList)
                        }
                    )
                }
            }
        }
        // Single Upload Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .border(1.dp, BtnColor, RoundedCornerShape(8.dp))
                .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                .clickable {
                    showMediaTypeDialog = true
                }
                .semantics { contentDescription = "Upload Media" },
            contentAlignment = Alignment.Center
        ) {

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter = painterResource(R.drawable.vector),
                        contentDescription = "Upload Icon",
                        tint = Color.Gray,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Upload Media",
                        fontSize = 18.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Images (max 10MB) • Videos (max 50MB)",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Up to 10 images & 2 videos",
                        fontSize = 11.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )

            }

        }


    }

    // Media Type Selection Dialog
    if (showMediaTypeDialog) {
        MediaTypeSelectionDialog(
            onImageSelected = {
                imagePickerLauncher.launch("image/*")
            },
            onVideoSelected = {
                videoPickerLauncher.launch("video/*")
            },
            onDismiss = {
                showMediaTypeDialog = false
            },
            currentImageCount = selectedMediaList.count { isImageFile(context, it) },
            currentVideoCount = selectedMediaList.count { isVideoFile(context, it) }
        )
    }

    // Preview Dialog
    if (showPreviewDialog && selectedPreviewMedia != null) {
        MediaPreviewDialog(
            mediaUri = selectedPreviewMedia!!,
            isVideo = isSelectedMediaVideo,
            onDismiss = {
                showPreviewDialog = false
                selectedPreviewMedia = null
            }
        )
    }
}

@Composable
fun MediaTypeSelectionDialog(
    onImageSelected: () -> Unit,
    onVideoSelected: () -> Unit,
    onDismiss: () -> Unit,
    currentImageCount: Int,
    currentVideoCount: Int
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
                        containerColor = if (currentImageCount < 10) Color.White else Color.Gray.copy(alpha = 0.3f)
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
                            tint = if (currentImageCount < 10) BtnColor else Color.Gray,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Images",
                                fontWeight = FontWeight.Medium,
                                color = if (currentImageCount < 10) Color.Black else Color.Gray
                            )
                            Text(
                                text = "Max 10MB each • $currentImageCount/10 selected",
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
                        if (currentVideoCount < 2) {
                            onVideoSelected()
                        }
                    },
                    enabled = currentVideoCount < 2,
                    modifier = Modifier.fillMaxWidth(),
                    border = BorderStroke(1.dp, BtnColor),
                    colors = CardDefaults.cardColors(
                        containerColor = if (currentVideoCount < 2) Color.White else Color.Gray.copy(alpha = 0.3f)
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
                            tint = if (currentVideoCount < 2)BtnColor else Color.Gray,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Videos",
                                fontWeight = FontWeight.Medium,
                                color = if (currentVideoCount < 2) Color.Black else Color.Gray
                            )
                            Text(
                                text = "Max 50MB each • $currentVideoCount/2 selected",
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

@Composable
fun MediaPreviewItem(
    mediaUri: Uri,
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
        Box(
        ) {
            if (isVideo) {
                VideoThumbnail(
                    videoUri = mediaUri,
                    modifier = Modifier.fillMaxSize()
                )

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
                AsyncImage(
                    model = mediaUri,
                    contentDescription = "Image Preview",
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
fun VideoThumbnail(
    videoUri: Uri,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var thumbnail by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(videoUri) {
        withContext(Dispatchers.IO) {
            try {
                val retriever = MediaMetadataRetriever()
                retriever.setDataSource(context, videoUri)
                thumbnail = retriever.getFrameAtTime(0)
                retriever.release()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    if (thumbnail != null) {
        Image(
            bitmap = thumbnail!!.asImageBitmap(),
            contentDescription = "Video Thumbnail",
            modifier = modifier,
            contentScale = ContentScale.Crop
        )
    } else {
        Box(
            modifier = modifier.background(Color.Gray.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(16.dp),
                strokeWidth = 2.dp
            )
        }
    }
}

@Composable
fun MediaPreviewDialog(
    mediaUri: Uri,
    isVideo: Boolean,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            if (isVideo) {
                // Video Player using AndroidView with VideoView
                AndroidView(
                    factory = { context ->
                        VideoView(context).apply {
                            setVideoURI(mediaUri)
                            setOnPreparedListener { mediaPlayer ->
                                mediaPlayer.isLooping = true
                                start()
                            }
                            setOnErrorListener { _, _, _ ->
                                Toast.makeText(context, "Error playing video", Toast.LENGTH_SHORT).show()
                                true
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            } else {
                AsyncImage(
                    model = mediaUri,
                    contentDescription = "Preview Image",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Fit
                )
            }

            // Close button
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .background(Color.Black.copy(alpha = 0.7f), CircleShape)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_close),
                    contentDescription = "Close",
                    tint = Color.White
                )
            }
        }
    }
}

// Helper functions
fun isImageFile(context: Context, uri: Uri): Boolean {
    val mimeType = context.contentResolver.getType(uri)
    return mimeType?.startsWith("image/") == true
}

fun isVideoFile(context: Context, uri: Uri): Boolean {
    val mimeType = context.contentResolver.getType(uri)
    return mimeType?.startsWith("video/") == true
}
