package com.bumperpick.bumperpick_Vendor.Screens.OTP

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumperpick.bumperpick_Vendor.Navigation.Screen
import com.bumperpick.bumperpick_Vendor.Screens.Component.ButtonView
import com.bumperpick.bumperpick_Vendor.Screens.Component.OtpView
import com.bumperpick.bumperpick_Vendor.ui.theme.BtnColor
import com.bumperpick.bumperpick_Vendor.ui.theme.satoshi_medium
import com.bumperpick.bumperpick_Vendor.ui.theme.satoshi_regular
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun OtpScreen(
    mobile: String,
    onBackClick: () -> Unit,
    onOtpVerify: (Screen) -> Unit,
    OtpViewModel: OtpViewModel = koinViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val state by OtpViewModel.uiState.collectAsState()
    val gotoscren by OtpViewModel.gotoScreen.collectAsState()
    var secondsLeft by remember { mutableStateOf(60) }
    var isResendEnabled by remember { mutableStateOf(false) }
    BackHandler {
        onBackClick()
    }
    LaunchedEffect(gotoscren){
        if(gotoscren!=null){
            onOtpVerify(gotoscren!!)
        }
    }
    // Countdown timer logic
    LaunchedEffect(Unit) {
        while (secondsLeft > 0) {
            delay(1000)
            secondsLeft--
        }
        isResendEnabled = true
    }

    // Show success message
    state.message?.let {
        LaunchedEffect(it) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(it)
                OtpViewModel.clearMessage()


            }
        }
    }

    // Show error snackbar
    LaunchedEffect(state.error) {
        state.error?.takeIf { it.isNotBlank() }?.let { error ->
            coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    message = error,
                    actionLabel = "Dismiss",
                    duration = SnackbarDuration.Short
                )
                OtpViewModel.clearError()
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.padding(16.dp),
                snackbar = { data ->
                    Snackbar(
                        action = {
                            TextButton(onClick = { data.performAction() }) {
                                Text(text = "OK", color = Color.White)
                            }
                        },
                        containerColor = Color.Black.copy(alpha = 0.85f),
                        contentColor = Color.White
                    ) {
                        Text(text = data.visuals.message)
                    }
                }
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Top Content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 80.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .clickable { onBackClick() }
                )

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Verify your OTP",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = satoshi_medium,
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    buildAnnotatedString {
                        withStyle(SpanStyle(color = Color.Black)) {
                            append("Sent to ")
                        }
                        withStyle(SpanStyle(color = BtnColor)) {
                            append(mobile)
                        }
                    },
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = satoshi_regular
                )

                Spacer(modifier = Modifier.height(24.dp))

                OtpView(
                    numberOfOtp = 4,
                    value = state.otp,
                    onValueChange = { OtpViewModel.onOtpChanged(it) },
                    otpCompleted = {
                        OtpViewModel.verifyOtp(mobile)
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            // Bottom Content (Verify button + Resend section)
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        color = BtnColor
                    )
                } else {
                    ButtonView("Verify OTP", horizontal_padding = 0.dp) {
                        OtpViewModel.verifyOtp(mobile)
                    }
                }

                if (isResendEnabled) {
                    TextButton(
                        onClick = {
                            OtpViewModel.resendOtp(mobile)
                            isResendEnabled = false
                            secondsLeft = 60
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(text = buildAnnotatedString {
                            withStyle(SpanStyle(color = Color.Black)) {
                                append("Didn't receive OTP? ")
                            }
                            withStyle(SpanStyle(color = BtnColor)) {
                                append("Resend")
                            }
                        },
                           fontFamily = satoshi_regular,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                            )
                    }
                } else {
                    Text(
                        buildAnnotatedString {
                            withStyle(SpanStyle(color = Color.Black)) {
                                append("Didn't receive OTP? Retry in ")
                            }
                            withStyle(SpanStyle(color = BtnColor)) {
                                append(formatSeconds(secondsLeft))
                            }
                        },

                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        color = Color.Gray,
                        fontFamily = satoshi_regular,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }

}

fun formatSeconds(seconds: Int): String {
    val minutes = seconds / 60
    val secs = seconds % 60
    return String.format("%02d:%02d", minutes, secs)
}
