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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
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
fun OfferDetailsPage(
    offerDetail: Offer,
    customer: Customer,
    viewmodel: QrScreenViewmodel,
    otpVerified: () -> Unit
) {
    val otpSent by viewmodel.is_otpsend.collectAsState()
    val otpVerified by viewmodel.verify_otpsend.collectAsState()
    val uiState by viewmodel.uiState.collectAsState()

    val context = LocalContext.current

    // Local state for OTP input
    var otpInput by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }

    // Handle OTP verification success
    LaunchedEffect(otpVerified) {
        if (otpVerified) {
            otpVerified() // Call the callback
        }
    }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .background(Color.White)
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Brand Card
        BrandCard(offerDetail)

        Spacer(modifier = Modifier.height(24.dp))
        Text("Offer Details", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(12.dp))
        Text(offerDetail.title, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = BtnColor)
        Spacer(modifier = Modifier.height(8.dp))
        Text(offerDetail.description, fontSize = 16.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(18.dp))

        Box(modifier = Modifier.fillMaxWidth()) {
            Text("Offer ID: ${offerDetail.id}", fontSize = 14.sp, color = Color.Gray, modifier = Modifier.align(Alignment.CenterStart))
            Text("Customer ID: ${customer.customer_id}", fontSize = 14.sp, color = Color.Gray,modifier = Modifier.align(Alignment.CenterEnd))
        }

        Spacer(modifier = Modifier.height(16.dp))
        Divider(thickness = 0.5.dp, color = Color.LightGray)
        Spacer(modifier = Modifier.height(16.dp))

        Text("Customer Info", fontSize = 22.sp, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(12.dp))
        Text("Phone Number: ${customer.phone_number}", fontSize = 16.sp,modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(16.dp))
        Divider(thickness = 0.5.dp, color = Color.LightGray)
        Spacer(modifier = Modifier.height(16.dp))
        // OTP Verification Section
        // OTP logic
        if (!otpSent) {
            ButtonView("Send OTP") {
                viewmodel.sendotp(customer.phone_number)
            }
        } else {
            // OTP Input Section
            Column (modifier = Modifier.fillMaxSize().align(Alignment.CenterHorizontally), verticalArrangement = Arrangement.Center){
                Text("Enter OTP sent to ${customer.phone_number}", fontSize = 14.sp, color = Color.Gray, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(8.dp))

                OtpView(numberOfOtp = 4,
                    value = otpInput,
                    onValueChange = {
                        otpInput = it
                    },
                    otpCompleted = {}
                )



                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Resend OTP button
                  /*  TextButton(
                        onClick = {
                            viewmodel.sendotp(customer.phone_number)
                            otpInput = "" // Clear input on resend
                        },


                    ) {
                        Text("Resend OTP")
                    }*/

                    // Verify OTP button
                    ButtonView(text = "Verify OTP") {
                        if (otpInput.length == 4) {
                            viewmodel.verifyOtp(customer.phone_number, otpInput)
                        } else {
                            Toast.makeText(context, "Please enter valid 4-digit OTP", Toast.LENGTH_SHORT).show()
                        }
                    }

                }
            }
        }

        // Handle UI State (Loading, Error)
        when (uiState) {
            is UiState.Loading -> {
                Spacer(modifier = Modifier.height(16.dp))
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
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
        if (otpVerified) {
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
@Composable
fun BrandCard(offer: Offer) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, color = Color.Gray),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)

    ) {
        ImageSliderItem(imageUrl = offer.brand_logo_url)

    }
}

@Composable
fun OtpInputSection(
    otp: String,
    onOtpChange: (String) -> Unit,
    onVerifyClick: () -> Unit
) {
    Text(
        "Enter OTP",
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center
    )
    Spacer(modifier = Modifier.height(12.dp))
    Box(
        modifier = Modifier
            .background(Color.Transparent)
            .fillMaxSize()
    ) {
        OtpView(
            numberOfOtp = 4,
            value = otp,
            onValueChange = onOtpChange,
            otpCompleted = {}, // Optional: auto-submit
            modifier = Modifier.align(Alignment.Center)
        )
    }
    Spacer(modifier = Modifier.height(24.dp))
    ButtonView("Verify OTP", onClick = onVerifyClick)
}


@Composable
fun OfferOtpScreen(
    userId: String,
    offerId: String,
    onGoHome: () -> Unit
) {
    val context = LocalContext.current

    val viewmodel:QrScreenViewmodel= koinViewModel()

    val uiState by viewmodel.uiState.collectAsState()
    LaunchedEffect(key1 = Unit){
        viewmodel.fetchOfferDetail(customerId = userId, offerId = offerId)
    }
     when(uiState){
         is UiState.Error -> Toast.makeText(context, (uiState as UiState.Error).message, Toast.LENGTH_SHORT).show()
         UiState.Loading -> {
             Box(modifier = Modifier.fillMaxSize()) {
                 CircularProgressIndicator(color = BtnColor, modifier = Modifier.size(60.dp).align(Alignment.Center))
             }
         }

         UiState.Empty -> {

         }
         is UiState.Success -> {
             OfferDetailsPage(
                 offerDetail = (uiState as UiState.Success<QrModel>).data.offer,
                 customer = (uiState as UiState.Success<QrModel>).data.customer,
                 viewmodel = viewmodel,
                 otpVerified = {
                     Toast.makeText(context, "OTP Verified Successfully", Toast.LENGTH_SHORT).show()
                     onGoHome()
                 })
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
        Surface(modifier = Modifier
            .fillMaxSize()
            .padding(padding)) {

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
                                    val cameraProvider = cameraProviderFuture.get()
                                    val preview = Preview.Builder().build().apply {
                                        setSurfaceProvider(previewView.surfaceProvider)
                                    }

                                    val analysis = ImageAnalysis.Builder().build().apply {
                                        setAnalyzer(ContextCompat.getMainExecutor(ctx)) { imageProxy ->
                                            val mediaImage = imageProxy.image
                                            if (mediaImage != null && !scanned) {
                                                val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                                                BarcodeScanning.getClient().process(image)
                                                    .addOnSuccessListener { barcodes ->
                                                        barcodes.forEach { barcode ->
                                                            val raw = barcode.rawValue
                                                            try {
                                                                if (!raw.isNullOrEmpty()) {
                                                                    val json = JSONObject(raw)
                                                                    val uid = json.optString("user_id", "")
                                                                    val oid = json.optString("offer_id", "")
                                                                    if (uid.isNotEmpty() && oid.isNotEmpty()) {
                                                                        scanned = true
                                                                        userId = uid
                                                                        offerId = oid
                                                                    } else {
                                                                        error = "QR code is missing required data."
                                                                    }
                                                                }
                                                            } catch (e: Exception) {
                                                                error = "Invalid QR format."
                                                            }
                                                        }
                                                        imageProxy.close()
                                                    }
                                                    .addOnFailureListener {
                                                        error = "Failed to scan QR code."
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



