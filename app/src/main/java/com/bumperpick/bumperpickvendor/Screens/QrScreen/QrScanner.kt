package com.bumperpick.bumperpickvendor.Screens.QrScreen

import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import coil.compose.AsyncImage
import com.bumperpick.bumperpickvendor.API.FinalModel.Customer
import com.bumperpick.bumperpickvendor.API.FinalModel.Offer
import com.bumperpick.bumperpickvendor.API.FinalModel.QrModel
import com.bumperpick.bumperpickvendor.R
import com.bumperpick.bumperpickvendor.Repository.HomeOffer
import com.bumperpick.bumperpickvendor.Screens.Component.ButtonView
import com.bumperpick.bumperpickvendor.Screens.Component.ImageSliderItem
import com.bumperpick.bumperpickvendor.Screens.Component.OtpView
import com.bumperpick.bumperpickvendor.Screens.Login.LoginViewmodel
import com.bumperpick.bumperpickvendor.Screens.OTP.OtpViewModel
import com.bumperpick.bumperpickvendor.ui.theme.BtnColor
import com.bumperpick.bumperpickvendor.ui.theme.grey
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.koin.androidx.compose.koinViewModel
@Composable
fun OfferRedeemedSuccessScreen(
    offer: Offer,
    onGoBackHome: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.success_badge),
            contentDescription = "Offer Redeemed",
            modifier = Modifier.size(80.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Offer Redeemed Successfully! 🎉",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1F2937),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Enjoy your deal! Offer has been successfully applied",
            fontSize = 16.sp,
            color = Color(0xFF6B7280),
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.percentage_red),
                contentDescription = null,
                modifier = Modifier.size(36.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = offer.description,
                fontSize = 16.sp,
                color = Color(0xFF374151),
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(80.dp))

        ButtonView("Go back to home", onClick = onGoBackHome)
    }
}

@Composable
fun OfferDetailsPage(
    offerDetail: Offer,
    customer: Customer,
    viewmodel: QrScreenViewmodel,
    otpVerified: () -> Unit
) {
    val otpSent by viewmodel.is_otpsend.collectAsState()
    val otpVerifiedState by viewmodel.verify_otpsend.collectAsState()
    val offerredeemed by viewmodel._offer_redeemed.collectAsState()
    val uiState by viewmodel.uiState.collectAsState()
    var showSucessScreen by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var otpInput by remember { mutableStateOf("") }

    // Handle OTP verification and offer redemption flow
    LaunchedEffect(otpVerifiedState, offerredeemed) {
        if (otpVerifiedState && !showSucessScreen) {
            viewmodel.redeem_offer(customer.customer_id.toString(), offerDetail.id.toString())
            showSucessScreen=true
        }
        if (offerredeemed) {
            showSucessScreen = true
        }
    }

    if (showSucessScreen) {
        OfferRedeemedSuccessScreen(offer = offerDetail, onGoBackHome = { otpVerified() })
    } else {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .background(Color.White)
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            BrandCard(offerDetail)

            Spacer(modifier = Modifier.height(24.dp))
            Text("Offer Details", fontSize = 24.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(12.dp))
            Text(offerDetail.title, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = BtnColor)
            Spacer(modifier = Modifier.height(8.dp))
            Text(offerDetail.description, fontSize = 16.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(18.dp))

            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    "Offer ID: ${offerDetail.id}",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.CenterStart)
                )
                Text(
                    "Customer ID: ${customer.customer_id}",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Divider(thickness = 0.5.dp, color = Color.LightGray)
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Customer Info",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                "Phone Number: ${customer.phone_number}",
                fontSize = 16.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Divider(thickness = 0.5.dp, color = Color.LightGray)
            Spacer(modifier = Modifier.height(16.dp))

            // OTP logic
            if (!otpSent) {
                ButtonView("Send OTP") {
                    viewmodel.sendotp(customer.phone_number)
                }
            } else {
                // OTP Input Section
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        "Enter OTP sent to ${customer.phone_number}",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OtpView(
                        numberOfOtp = 4,
                        value = otpInput,
                        onValueChange = { newValue ->
                            // Only allow numeric input and limit to 4 digits
                            if (newValue.length <= 4 && newValue.all { it.isDigit() }) {
                                otpInput = newValue
                            }
                        },
                        otpCompleted = {},
                        modifier = Modifier
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        ButtonView(text = "Verify OTP") {
                            when {
                                otpInput.length != 4 -> {
                                    Toast.makeText(context, "Please enter valid 4-digit OTP", Toast.LENGTH_SHORT).show()
                                }
                                !otpInput.all { it.isDigit() } -> {
                                    Toast.makeText(context, "OTP should contain only numbers", Toast.LENGTH_SHORT).show()
                                }
                                else -> {
                                    viewmodel.verifyOtp(customer.phone_number, otpInput)
                                }
                            }
                        }
                    }
                }
            }

            // Handle UI State (Loading, Error)
            when (uiState) {
                is UiState.Loading -> {
                    Spacer(modifier = Modifier.height(16.dp))
                    Box(modifier = Modifier.fillMaxWidth()) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
                is UiState.Error -> {
                    Spacer(modifier = Modifier.height(16.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.Red.copy(alpha = 0.1f))
                    ) {
                        Text(
                            text = (uiState as UiState.Error).message,
                            color = Color.Red,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
                else -> {
                    // Success or Empty state - no additional UI needed
                }
            }

            // Success message when OTP is verified
            if (otpVerifiedState) {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.Green.copy(alpha = 0.1f))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Success",
                            tint = Color.Green
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "OTP Verified Successfully!",
                            color = Color.Green,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BrandCard(offer: Offer) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, color = Color.Gray),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        ImageSliderItem(imageUrl = offer.brand_logo_url)
    }
}

@Composable
fun ErrorMessage(message: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFE0E0)),
            elevation = CardDefaults.cardElevation(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Error",
                    tint = Color.Red,
                    modifier = Modifier.size(48.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Something went wrong",
                    style = MaterialTheme.typography.titleMedium.copy(color = Color.Red)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = Color.Black.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@Composable
fun OfferOtpScreen(
    userId: String,
    offerId: String,
    onGoHome: () -> Unit
) {
    val context = LocalContext.current
    val viewmodel: QrScreenViewmodel = koinViewModel()
    val uiState by viewmodel.uiState.collectAsState()

    LaunchedEffect(userId, offerId) {
        viewmodel.fetchOfferDetail(customerId = userId, offerId = offerId)
    }

    when (val currentState = uiState) {
        is UiState.Error -> {
            ErrorMessage(message = "Error in fetching offer details")
        }
        UiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    color = BtnColor,
                    modifier = Modifier
                        .size(60.dp)
                        .align(Alignment.Center)
                )
            }
        }
        UiState.Empty -> {
            ErrorMessage(message = "No offer details available")
        }
        is UiState.Success -> {
            OfferDetailsPage(
                offerDetail = (currentState.data as QrModel).offer,
                customer = (currentState.data as QrModel).customer,
                viewmodel = viewmodel,
                otpVerified = {
                    Toast.makeText(context, "Offer Successfully applied", Toast.LENGTH_SHORT).show()
                    onGoHome()
                }
            )
        }
    }
}

@androidx.annotation.OptIn(ExperimentalGetImage::class)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun QRScannerScreen(
    onGoHome: () -> Unit
) {
    val context = LocalContext.current
    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    var scanned by remember { mutableStateOf(false) }
    var userId by remember { mutableStateOf("") }
    var offerId by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    // Show Snackbar if error is set
    LaunchedEffect(error) {
        if (error.isNotEmpty()) {
            coroutineScope.launch {
                val result = snackbarHostState.showSnackbar(error, actionLabel = "Ok")
                if (result == SnackbarResult.ActionPerformed || result == SnackbarResult.Dismissed) {
                    error = ""
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (cameraPermissionState.status.isGranted) {
                if (scanned) {
                    OfferOtpScreen(
                        userId = userId,
                        offerId = offerId,
                        onGoHome = onGoHome
                    )
                } else {
                    Box(modifier = Modifier.fillMaxSize()) {
                        AndroidView(
                            modifier = Modifier.fillMaxSize(),
                            factory = { ctx ->
                                val previewView = PreviewView(ctx)
                                val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)

                                cameraProviderFuture.addListener({
                                    try {
                                        val cameraProvider = cameraProviderFuture.get()
                                        val preview = Preview.Builder().build().apply {
                                            setSurfaceProvider(previewView.surfaceProvider)
                                        }

                                        val analysis = ImageAnalysis.Builder()
                                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                                            .build()
                                            .apply {
                                                setAnalyzer(ContextCompat.getMainExecutor(ctx)) { imageProxy ->
                                                    val mediaImage = imageProxy.image
                                                    if (mediaImage != null && !scanned) {
                                                        val image = InputImage.fromMediaImage(
                                                            mediaImage,
                                                            imageProxy.imageInfo.rotationDegrees
                                                        )
                                                        BarcodeScanning.getClient().process(image)
                                                            .addOnSuccessListener { barcodes ->
                                                                barcodes.firstOrNull()?.let { barcode ->
                                                                    val raw = barcode.rawValue
                                                                    if (!raw.isNullOrEmpty()) {
                                                                        try {
                                                                            val json = JSONObject(raw)
                                                                            val uid = json.optString("user_id", "")
                                                                            val oid = json.optString("offer_id", "")

                                                                            when {
                                                                                uid.isEmpty() -> {
                                                                                    error = "QR code is missing user ID."
                                                                                }
                                                                                oid.isEmpty() -> {
                                                                                    error = "QR code is missing offer ID."
                                                                                }
                                                                                else -> {
                                                                                    scanned = true
                                                                                    userId = uid
                                                                                    offerId = oid
                                                                                }
                                                                            }
                                                                        } catch (e: Exception) {
                                                                            error = "Invalid QR format."
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                            .addOnFailureListener {
                                                                error = "Failed to scan QR code."
                                                            }
                                                            .addOnCompleteListener {
                                                                imageProxy.close()
                                                            }
                                                    } else {
                                                        imageProxy.close()
                                                    }
                                                }
                                            }

                                        cameraProvider.unbindAll()
                                        cameraProvider.bindToLifecycle(
                                            context as LifecycleOwner,
                                            CameraSelector.DEFAULT_BACK_CAMERA,
                                            preview,
                                            analysis
                                        )
                                    } catch (exc: Exception) {
                                        error = "Failed to start camera."
                                    }
                                }, ContextCompat.getMainExecutor(ctx))
                                previewView
                            }
                        )

                        Column(
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .fillMaxWidth()
                                .padding(top = 32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Scan the QR Code",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier
                                    .background(Color.Black.copy(alpha = 0.5f))
                                    .padding(8.dp)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(250.dp)
                                .align(Alignment.Center)
                                .border(2.dp, Color.White, RoundedCornerShape(12.dp))
                        )

                        Text(
                            text = "Align the QR inside the frame",
                            fontSize = 14.sp,
                            color = Color.White,
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 32.dp)
                                .background(Color.Black.copy(alpha = 0.5f))
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Camera permission is required to scan QR codes.",
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                        Text("Grant Permission")
                    }
                }
            }
        }
    }
}