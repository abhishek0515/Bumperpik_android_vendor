package com.bumperpick.bumperpick_Vendor.Screens.CreateOfferScreen

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.bumperpick.bumperpick_Vendor.Misc.CaptureHost
import com.bumperpick.bumperpick_Vendor.Misc.saveBitmapToInternalImagesDir
import com.bumperpick.bumperpick_Vendor.R
import com.bumperpick.bumperpick_Vendor.Repository.OfferTemplateType
import com.bumperpick.bumperpick_Vendor.Repository.Template_Data
import com.bumperpick.bumperpick_Vendor.Screens.Component.PrimaryButton
import com.bumperpick.bumperpick_Vendor.Screens.Component.SecondaryButton
import com.bumperpick.bumperpick_Vendor.ui.theme.BtnColor
import com.bumperpick.bumperpick_Vendor.ui.theme.grey
import com.bumperpick.bumperpick_Vendor.ui.theme.satoshi_medium
import com.bumperpick.bumperpick_Vendor.ui.theme.satoshi_regular

@Composable
fun OfferPreviewScreen(navController: NavController, viewmodel: CreateOfferViewmodel,
                       onOfferDone:  ()->Unit){
    val offerDetails by viewmodel.offerDetails.collectAsState()
    var loading by remember { mutableStateOf(false) }
    val offerAdded by viewmodel.offerAdded.collectAsState()
    val error by viewmodel.error.collectAsState()
    val userChoosedBanner by viewmodel.user_choosed_banner.collectAsState()
    val templateData by viewmodel.templateData.collectAsState()
    val choosed_Template by viewmodel.choosed_Template.collectAsState()
    var showPreviewDialog by remember { mutableStateOf(false) }
    var selectedPreviewMedia by remember { mutableStateOf<Uri?>(null) }
    var isSelectedMediaVideo by remember { mutableStateOf(false) }
    var triggerCapture by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    BackHandler {
        navController.navigate(CreateOfferScreenViews.MoreofferDetails.route){
            popUpTo(CreateOfferScreenViews.EditBanner.route)
        }
    }
    LaunchedEffect(offerAdded){
        if(offerAdded){
            onOfferDone()
        }
    }
    LaunchedEffect(error) {
        if (error.isNotEmpty()) {
            loading=false
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
    val context= LocalContext.current
    if(userChoosedBanner == startingChoose.Template){
        CaptureHost(
            onCaptured = {
                bitmap: Bitmap ->
                val file = saveBitmapToInternalImagesDir(context, bitmap)
                Log.d("IMAGE SAVED",file.absolutePath)
                val uri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider", // This should resolve to "com.bumperpick.bumperpickvendor.fileprovider"
                    file
                )
                viewmodel.updateUserBanner(uri)
                triggerCapture=false
            },
            content = {
                renderTemplate(choosed_Template, templateData)
            }
        )
    }




        Column(
            modifier = Modifier

                .background(grey)
                .fillMaxSize()
        )
        {
            // First Column - Scrollable content (takes remaining space)
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
                    Text(
                        text = "Offer Preview",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = satoshi_medium,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp)
                    )
                    Spacer(Modifier.height(20.dp))
                }

                Spacer(Modifier.height(10.dp))
                Text(
                    text = "Banner",
                    fontSize = 14.sp,
                    fontFamily = satoshi_regular,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                if (userChoosedBanner == startingChoose.UserBanner) {
                    ImageCardFromUri(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        imageUri = offerDetails.BannerImage.toString()
                    )
                } else if (userChoosedBanner == startingChoose.Template) {
                    renderTemplate(choosed_Template, templateData)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Divider(
                    modifier = Modifier.height(1.dp).padding(horizontal = 16.dp),
                    color = Color.Gray.copy(0.2f)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Media (${offerDetails.medialList.size}",
                    fontSize = 14.sp,
                    fontFamily = satoshi_regular,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(vertical = 6.dp, horizontal = 16.dp)
                )
                val selectedMediaList = offerDetails.medialList
                val context = LocalContext.current
                if (offerDetails.medialList.isNotEmpty()) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp),
                        modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 3.dp)
                    ) {
                        items(offerDetails.medialList.size) { index: Int ->
                            val mediaUri = selectedMediaList[index]
                            val isVideo = isVideoFile(context, mediaUri)

                            MediaPreview(
                                mediaUri = mediaUri,
                                isVideo = isVideo,
                                onPreview = {}
                            )


                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Divider(
                    modifier = Modifier.height(1.dp).padding(horizontal = 16.dp),
                    color = Color.Gray.copy(0.2f)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Product Title",
                    fontSize = 14.sp,
                    fontFamily = satoshi_regular,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${offerDetails.productTittle}",
                    fontSize = 16.sp,
                    fontFamily = satoshi_medium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Divider(
                    modifier = Modifier.height(1.dp).padding(horizontal = 16.dp),
                    color = Color.Gray.copy(0.2f)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Product Description",
                    fontSize = 14.sp,
                    fontFamily = satoshi_regular,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${offerDetails.productDiscription}",
                    fontSize = 16.sp,
                    fontFamily = satoshi_medium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Divider(
                    modifier = Modifier.height(1.dp).padding(horizontal = 16.dp),
                    color = Color.Gray.copy(0.2f)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Terms and Conditions",
                    fontSize = 14.sp,
                    fontFamily = satoshi_regular,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${offerDetails.termsAndCondition}",
                    fontSize = 16.sp,
                    fontFamily = satoshi_medium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Divider(
                    modifier = Modifier.height(1.dp).padding(horizontal = 16.dp),
                    color = Color.Gray.copy(0.2f)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Offer Quantity",
                    fontSize = 14.sp,
                    fontFamily = satoshi_regular,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${offerDetails.quantity}",
                    fontSize = 16.sp,
                    fontFamily = satoshi_medium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))


            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 8.dp, vertical = 16.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SecondaryButton(
                        text = "Edit details",
                        onClick = {
                            navController.navigate(CreateOfferScreenViews.MoreofferDetails.route) {
                                popUpTo(CreateOfferScreenViews.MoreofferDetails.route) {
                                    inclusive = true
                                }
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )

                    if (loading) {
                        Box(
                            modifier = Modifier.weight(1f).height(30.dp).fillMaxWidth()
                                .align(Alignment.CenterVertically),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = BtnColor,

                                )
                        }
                    } else {
                        PrimaryButton(
                            text = "Publish",
                            onClick = {
                                Log.d("userType", userChoosedBanner.toString())
                                loading = true

                                triggerCapture = true

                                viewmodel.AddDatatoServer()

                            },
                            modifier = Modifier.weight(1f)
                        )

                    }
                }
            }

        }

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
fun MediaPreview(
    mediaUri: Uri,
    isVideo: Boolean,
    onPreview: () -> Unit,
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
        }
    }

}

@Composable
fun renderTemplate(choosed_Template: OfferTemplateType?,templateData: Template_Data){
    when(choosed_Template){
        OfferTemplateType.Template1 ->{
            Template1(data = templateData,
                modifier = Modifier.padding(horizontal = 16.dp),)
        }
        OfferTemplateType.Template2 -> {
            Template2(data = templateData,
                modifier = Modifier.padding(horizontal = 16.dp),)
        }
        OfferTemplateType.Template3 -> {
            Template3(data = templateData,
                modifier = Modifier.padding(horizontal = 16.dp),)
        }
        OfferTemplateType.Template4 -> {
            Template4(data = templateData,
                modifier = Modifier.padding(horizontal = 16.dp),)
        }

        null -> {}
    }
}