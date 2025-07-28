package com.bumperpick.bumperpick_Vendor.Screens.Splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumperpick.bumperpick_Vendor.Navigation.Screen
import com.bumperpick.bumperpick_Vendor.R
import org.koin.androidx.compose.koinViewModel


@Composable
fun Splash(gotoScreen: (Screen) -> Unit,
           viewmodel: SplashViewmodel=koinViewModel()){


    val state by viewmodel.loginCheckState.collectAsState()
    LaunchedEffect(state) {
        when (state) {
            is SplashState.Success -> {
                gotoScreen((state as SplashState.Success).screen)
            }

            is SplashState.Error -> {
                gotoScreen(Screen.Login)
            }

            SplashState.Loading -> {}
        }
    }


    Box (modifier = Modifier
        .fillMaxSize()
        .background(Color.White),
        contentAlignment = Alignment.Center)
    {

        Image(painter = painterResource(R.drawable.group),contentDescription = "splash", modifier = Modifier.fillMaxSize())
        Image(painter = painterResource(R.drawable.image_2),contentDescription = "splash",
            modifier = Modifier
                .padding( 100.dp)
                .fillMaxSize()
        )




    }
}

@Preview
@Composable
fun SplashPreview(){
    Splash(gotoScreen = {})
}