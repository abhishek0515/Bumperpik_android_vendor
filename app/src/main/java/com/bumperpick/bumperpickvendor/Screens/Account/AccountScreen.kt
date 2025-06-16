package com.bumperpick.bumperpickvendor.Screens.Account

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.bumperpick.bumperpickvendor.Screens.Component.ButtonView
import org.koin.androidx.compose.koinViewModel

@Composable
fun AccountScreen(logout:()->Unit,viewmodel: AccountViewmodel= koinViewModel()){
    val isLogout by viewmodel.isLogout.collectAsState()

    if (isLogout) {
        logout()
    }


    Box(modifier = Modifier.fillMaxSize()){
        ButtonView(text = "Logout", onClick = {
            viewmodel.logout() }, modifier = Modifier.align(androidx.compose.ui.Alignment.Center))
    }

}