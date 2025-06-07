package com.bumperpick.bumperpickvendor.Screens.Account

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bumperpick.bumperpickvendor.Screens.Component.ButtonView
import org.koin.androidx.compose.koinViewModel

@Composable
fun AccountScreen(logout:()->Unit,viewmodel: AccountViewmodel= koinViewModel()){
    Text(text = "Account Screen")
    Box(modifier = Modifier.fillMaxSize()){
        ButtonView(text = "Logout", onClick = {
            viewmodel.logout()
            logout() }, modifier = Modifier.align(androidx.compose.ui.Alignment.Center))
    }

}