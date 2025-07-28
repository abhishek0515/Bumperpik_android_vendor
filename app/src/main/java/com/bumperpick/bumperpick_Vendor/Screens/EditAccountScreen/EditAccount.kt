package com.bumperpick.bumperpick_Vendor.Screens.EditAccountScreen

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.IconButton
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.bumperpick.bumperpick_Vendor.R
import com.bumperpick.bumperpick_Vendor.Screens.Component.ButtonView
import com.bumperpick.bumperpick_Vendor.Screens.Component.TextFieldView
import com.bumperpick.bumperpick_Vendor.ui.theme.BtnColor
import com.bumperpick.bumperpick_Vendor.ui.theme.grey
import com.bumperpick.bumperpick_Vendor.ui.theme.satoshi_regular
import org.koin.androidx.compose.koinViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun getFileFromUri(context: Context, uri: android.net.Uri): File? {
    val inputStream = context.contentResolver.openInputStream(uri) ?: return null
    val file = File(context.cacheDir, "picked_image_${System.currentTimeMillis()}.jpg")
    file.outputStream().use { outputStream ->
        inputStream.copyTo(outputStream)
    }
    return file
}
@Composable
fun EditAccount(
    viewModel: EditAccountViewModel= koinViewModel(),
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: android.net.Uri? ->
        uri?.let {
            val file = getFileFromUri(context, it)
            viewModel.updateProfileImage(file!!)
        }
    }

    LaunchedEffect (Unit){
        viewModel.getVendorDetails()
    }

    // Early return if vendor details are not available
    if (uiState.vendorDetails == null) {
       Box(modifier = Modifier.fillMaxSize()){
           CircularProgressIndicator(color = BtnColor, modifier = Modifier.align(Alignment.Center))
       }

    }

    Scaffold(containerColor = Color(0xFFF8F8F8)) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Header Section
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.White,
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, top = 40.dp, bottom = 18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier
                                .size(24.dp)
                                .clickable { onBackClick() }
                        )
                        Text(
                            text = "Your Profile",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = satoshi_regular,
                            color = Color.Black
                        )
                    }
                }

                // Single Content Area
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .background(grey)
                        .padding(24.dp)
                ) {
                    // Profile Image Section
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .align(Alignment.CenterHorizontally)
                            .clip(CircleShape)
                    ) {
                        AsyncImage(
                            model =
                                if(viewModel.getFileProfileImage()==null)
                                viewModel.getProfileImage()
                                else viewModel.getFileProfileImage(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Card(
                            modifier = Modifier.align(Alignment.BottomEnd).padding(8.dp),
                            shape = CircleShape,
                            colors = CardDefaults.cardColors(Color.White)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.pencil_svgrepo_com),
                                contentDescription = null,
                                modifier = Modifier
                                    .clickable {
                                        launcher.launch("image/*")
                                    }
                                    .padding(4.dp)
                                    .size(24.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Vendor ID: ${viewModel.getId()}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = satoshi_regular,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text = "Name of Your Establishment",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = satoshi_regular
                    )
                    Spacer(Modifier.height(4.dp))
                    TextFieldView(
                        value = viewModel.getEstablishName(),
                        onValueChange = { viewModel.updateEstablishName(it) },
                        placeholder = "Enter Establishment Name",
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = "Outlet / brand name",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = satoshi_regular
                    )
                    Spacer(Modifier.height(4.dp))
                    TextFieldView(
                        value = viewModel.getBrand(),
                        onValueChange = { viewModel.updateBrand(it) },
                        placeholder = "Enter Brand Name",
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = "Email",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = satoshi_regular
                    )
                    Spacer(Modifier.height(4.dp))
                    TextFieldView(
                        value = viewModel.getEmail(),
                        onValueChange = { viewModel.updateEmail(it) },
                        placeholder = "Enter your Email",
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        isEnabled = true
                    )

                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = "Mobile Number",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = satoshi_regular
                    )
                    Spacer(Modifier.height(4.dp))
                    TextFieldView(
                        value = viewModel.getMobile(),
                        onValueChange = { viewModel.updateMobile(it) },
                        placeholder = "Enter your Mobile Number",
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        isEnabled = false,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = "Establishment Address",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = satoshi_regular
                    )
                    Spacer(Modifier.height(6.dp))
                    TextFieldView(
                        value = viewModel.getEstablishmentAddress(),
                        onValueChange = { viewModel.updateEstablishmentAddress(it) },
                        placeholder = "Enter Establishment Address",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        singleLine = false
                    )
                    Spacer(Modifier.height(10.dp))
                    Row {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = "info",
                            tint = Color.Gray
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            "Invoice will generate on this address",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                    Spacer(Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Outlet Address",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = satoshi_regular
                        )
                        Text(
                            text = "Same as above",
                            fontSize = 16.sp,
                            color = Color.Red,
                            fontWeight = FontWeight.Normal,
                            fontFamily = satoshi_regular,
                            modifier = Modifier.clickable {
                                viewModel.copyEstablishmentToOutletAddress()
                            }
                        )
                    }
                    Spacer(Modifier.height(6.dp))
                    TextFieldView(
                        value = viewModel.getOutletAddress(),
                        onValueChange = { viewModel.updateOutletAddress(it) },
                        placeholder = "Enter Brand Name",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        singleLine = false
                    )
                    Spacer(Modifier.height(20.dp))
                    Text(
                        text = "Gst Number (Optional)",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = satoshi_regular
                    )
                    Spacer(Modifier.height(6.dp))
                    TextFieldView(
                        value = viewModel.getGstNumber(),
                        onValueChange = { viewModel.updateGstNumber(it) },
                        placeholder = "Enter gst number",
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(Modifier.height(20.dp))
                    Text(
                        text = "Select opening time",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = satoshi_regular
                    )
                    Spacer(Modifier.height(6.dp))
                    Box {
                        TextFieldView(
                            value = viewModel.getOpeningTime(),
                            onValueChange = { },
                            placeholder = "Select opening time",
                            isEnabled = false,
                            modifier = Modifier.clickable { viewModel.showOpeningTimePicker() }
                        )
                        // Clock icon overlay
                        IconButton(
                            onClick = { viewModel.showOpeningTimePicker() },
                            modifier = Modifier.align(Alignment.CenterEnd)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.clock),
                                contentDescription = "Select opening time",
                                tint = BtnColor
                            )
                        }
                    }
                    Spacer(Modifier.height(20.dp))
                    Text(
                        text = "Select closing time",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = satoshi_regular
                    )
                    Spacer(Modifier.height(6.dp))
                    // Closing Time Field
                    Box {
                        TextFieldView(
                            value = viewModel.getClosingTime(),
                            onValueChange = { },
                            placeholder = "Select closing time",
                            isEnabled = false,
                            modifier = Modifier.clickable { viewModel.showClosingTimePicker() }
                        )
                        // Clock icon overlay
                        IconButton(
                            onClick = { viewModel.showClosingTimePicker() },
                            modifier = Modifier.align(Alignment.CenterEnd)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.clock),
                                contentDescription = "Select closing time",
                                tint = BtnColor
                            )
                        }
                    }
                    Spacer(Modifier.height(60.dp))
                }
            }

            // Update Button at Bottom
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                color = Color.White,
                shadowElevation = 8.dp
            ) {
                Spacer(modifier = Modifier.height(12.dp))
                ButtonView(
                    text = if (viewModel.isLoading()) "Updating..." else "Update Profile",
                  //  enabled = viewModel.isSaveEnabled() && !viewModel.isLoading(),
                    modifier = Modifier.padding(top=16.dp)
                ) {
                    viewModel.saveProfile(
                        onSuccess = { updatedVendorDetails ->
                            Handler(Looper.getMainLooper()).post {
                                Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                                onBackClick()
                            }
                        },
                        onError = { errorMessage ->
                            Handler(Looper.getMainLooper()).post {
                                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                            }
                        }
                    )


                }
            }
        }

        // Time Picker Dialogs
        if (viewModel.shouldShowOpeningTimePicker()) {
            TimePickerDialog(
                onTimeSelected = { time ->
                    viewModel.updateOpeningTime(time)
                    viewModel.hideOpeningTimePicker()
                },
                onDismiss = { viewModel.hideOpeningTimePicker() }
            )
        }

        if (viewModel.shouldShowClosingTimePicker()) {
            TimePickerDialog(
                onTimeSelected = { time ->
                    viewModel.updateClosingTime(time)
                    viewModel.hideClosingTimePicker()
                },
                onDismiss = { viewModel.hideClosingTimePicker() }
            )
        }

        // Show error message if any
        uiState.errorMessage?.let { errorMessage ->
            LaunchedEffect(errorMessage) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                viewModel.clearError()
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    onTimeSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val timePickerState = rememberTimePickerState()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Time") },
        text = {
            TimePicker(state = timePickerState)
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val hour = timePickerState.hour
                    val minute = timePickerState.minute
                    val calendar = Calendar.getInstance()
                    calendar.set(Calendar.HOUR_OF_DAY, hour)
                    calendar.set(Calendar.MINUTE, minute)

                    val timeFormat = SimpleDateFormat("hh:mm a", Locale.US)
                    val formattedTime = timeFormat.format(calendar.time)
                    onTimeSelected(formattedTime)
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}