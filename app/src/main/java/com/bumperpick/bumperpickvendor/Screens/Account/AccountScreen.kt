package com.bumperpick.bumperpickvendor.Screens.Account

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.bumperpick.bumperpickvendor.API.FinalModel.SubscriptionX
import com.bumperpick.bumperpickvendor.R
import com.bumperpick.bumperpickvendor.Screens.Component.ButtonView
import com.bumperpick.bumperpickvendor.ui.theme.BtnColor
import com.bumperpick.bumperpickvendor.ui.theme.grey
import com.bumperpick.bumperpickvendor.ui.theme.satoshi_regular
import org.koin.androidx.compose.koinViewModel
fun shareReferral(context: Context) {
    val intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, "Try this APP")
        type = "text/plain"
    }
    context.startActivity(Intent.createChooser(intent, "Share via"))
}

sealed class AccountClick(){
    data  object Logout:AccountClick()
    data object EditProfile:AccountClick()
    data object Subscription:AccountClick()

}
@Composable
fun AccountScreen(onClick:(AccountClick)->Unit,viewmodel: AccountViewmodel= koinViewModel()){
        val context= LocalContext.current
    val uiState=viewmodel.uiState.collectAsState().value
    LaunchedEffect(Unit) {
        viewmodel.fetchProfile()
    }





    val isLogout by viewmodel.isLogout.collectAsState()

    if (isLogout) {
        onClick(AccountClick.Logout)
    }



    Scaffold(containerColor = grey) {paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {

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

                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Account",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = satoshi_regular,
                        color = Color.Black
                    )
                }
            }
            when(uiState){
                AccountUi_state.Empty -> {}
                is AccountUi_state.Error -> {
                    Box(){
                        Text(text = uiState.message, color = BtnColor, fontSize = 18.sp, modifier = Modifier.fillMaxSize(), textAlign =TextAlign.Center)
                    }
                }
                is AccountUi_state.GetProfile ->{
                    val data=uiState.vendorDetail
                    Log.d("DATA",data.toString())
                    Card(
                        modifier = Modifier.padding(16.dp).height(100.dp),
                        colors = CardDefaults.cardColors(containerColor = BtnColor)
                    ) {
                        Box(modifier = Modifier.padding(12.dp).fillMaxWidth(),){
                            Row(modifier = Modifier.align(Alignment.CenterStart).fillMaxHeight()){
                                Card(shape = CircleShape, modifier = Modifier.size(60.dp).align(Alignment.CenterVertically)) {
                                    AsyncImage(
                                        modifier = Modifier.fillMaxSize().align(Alignment.CenterHorizontally),
                                        model = data.data.image_url,
                                        contentScale = ContentScale.FillBounds, contentDescription = null)
                                }
                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                                    Text(text = data.data.establishment_name, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(text = data.data.phone_number, fontSize = 14.sp, fontWeight = FontWeight.Normal, color = Color.White)


                                }
                            }
                            Box( modifier = Modifier.align(Alignment.TopEnd).padding(6.dp)){


                                Icon(painter = painterResource(R.drawable.pencil_svgrepo_com),
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(27.dp).clickable { onClick(AccountClick.EditProfile) },)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(0.dp))
                    SubscriptionCard(
                        subs_data=data.data.subscription,

                        daysRemaining = -10,
                        totalDays = 365,
                        onClick = onClick
                    )

                }
                AccountUi_state.Loading ->{
                    Box(modifier = Modifier.fillMaxWidth().padding(vertical = 18.dp)) {
                        CircularProgressIndicator(color = BtnColor, modifier = Modifier.align(Alignment.Center).size(24.dp))
                    }

                }
            }

            Spacer(modifier = Modifier.height(18.dp))
            Box (modifier = Modifier.fillMaxWidth().background(Color.White),){
                Row (modifier = Modifier.padding(12.dp).align(Alignment.CenterStart)){
                    Image(painter = painterResource(R.drawable.bell_01), contentDescription = null, modifier = Modifier.size(30.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = "Send instant reminder", color = Color.Black, fontSize = 16.sp,)
                }
                TextButton(onClick = {}, modifier = Modifier.align(Alignment.CenterEnd)) {
                    Text(text = "Send now", color = BtnColor, fontSize = 16.sp,)
                }

            }
            Spacer(modifier = Modifier.height(18.dp))

            Column (modifier = Modifier.fillMaxWidth().background(Color.White)){
                Spacer(modifier = Modifier.height(16.dp))
                Row (verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()){
                    Image(painter = painterResource(R.drawable.left), contentDescription = null, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "REFERRAL",
                        letterSpacing = 4.sp, // Use sp for text spacing, not dp
                        color = Color.Gray,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 4.dp),

                        )
                    Spacer(modifier = Modifier.width(10.dp))
                    Image(painter = painterResource(R.drawable.right), contentDescription = null, modifier = Modifier.size(12.dp))


                }
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Refer this to your friends and family and enjoy extra benefits ",
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,)
                Spacer(modifier = Modifier.height(24.dp))
                ButtonView(text = "Refer now", btnColor = BtnColor.copy(alpha = 0.2f), textColor = BtnColor) {
                      shareReferral(context)
                }

            }
            Spacer(modifier = Modifier.height(18.dp))
            Box (modifier = Modifier.fillMaxWidth().clickable {
                viewmodel.logout()

            }.background(Color.White), ){
                Row (modifier = Modifier.padding(12.dp).align(Alignment.CenterStart)){
                    Image(imageVector = Icons.Outlined.ExitToApp, contentDescription = null, modifier = Modifier.size(30.dp),)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = "Sign out", color = Color.Black, fontSize = 16.sp,)
                }

                Image(imageVector = Icons.Outlined.KeyboardArrowRight, contentDescription = null, modifier = Modifier.size(24.dp).align(Alignment.CenterEnd),)

            }






        }

    }
}
@Composable
fun SubscriptionCard(
    daysRemaining: Int,
    totalDays: Int,
    modifier: Modifier = Modifier,
    onClick: (AccountClick) -> Unit,
    subs_data: SubscriptionX
) {
    val isExpired = daysRemaining <= 0
    val progressPercentage = if (totalDays > 0) (daysRemaining.toFloat() / totalDays.toFloat()).coerceIn(0f, 1f) else 0f
    val expiredDays = Math.abs(daysRemaining)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp)

            .padding(horizontal = 12.dp)
            .clickable { onClick(AccountClick.Subscription) },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFFFD4CC),
                            Color(0xFFFFBFB3),
                            Color(0xFFFFAA99)
                        ),
                        start = Offset(0f, 0f),
                        end = Offset(1000f, 500f)
                    )
                )
        ) {
            // Expired Badge - Top Left with rounded corner design
            if (isExpired) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFFFF693C),
                                    Color(0xFFFA8A62)
                                )
                            ),
                            shape = RoundedCornerShape(bottomEnd = 18.dp, topStart = 16.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "Expired $expiredDays days ago",
                        fontSize = 11.sp,
                        color = Color.White,
                        fontWeight = FontWeight.W400
                    )
                }
            }

            // Crown Icon - Bottom Right
            Image(
                painter = painterResource(id = R.drawable.crown),
                contentDescription = "Crown",
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(120.dp)
                    .offset(x = 10.dp, y = 0.dp)
            )

            // Main Content
            Column(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 16.dp, top = if (isExpired) 50.dp else 20.dp, end = 16.dp)
                    .fillMaxWidth(0.75f)
            ) {
                // Membership Title
                Text(
                    text = subs_data.name?:"",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.W600,
                    color = Color(0xFF8D4E3C),
                    lineHeight = 24.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Description
                Text(
                    text = "Subscription type: ${subs_data.time_period?:""}",
                    fontSize = 12.sp,
                    color = Color(0xFF8D4E3C).copy(alpha = 0.8f),
                    fontWeight = FontWeight.W400,
                    lineHeight = 16.sp
                )
                // Progress Section - Bottom Left
                Column(
                    modifier = Modifier

                        .padding( bottom = 16.dp, top = 16.dp)
                        .fillMaxWidth(0.7f)
                ) {
                    // Progress Bar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .background(
                                color = Color.White.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(3.dp)
                            )
                    ) {
                        if (!isExpired && progressPercentage > 0f) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(progressPercentage)
                                    .fillMaxHeight()
                                    .background(
                                        brush = Brush.horizontalGradient(
                                            colors = listOf(
                                                Color(0xFFFF8A65),
                                                Color(0xFFFF7043)
                                            )
                                        ),
                                        shape = RoundedCornerShape(3.dp)
                                    )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Days Text
                    Text(
                        text = if (isExpired) "0/$totalDays days" else "$daysRemaining/$totalDays days",
                        fontSize = 14.sp,
                        color = Color(0xFF8D4E3C).copy(alpha = 0.9f),
                        fontWeight = FontWeight.W500
                    )
                }
            }


            // Subtle decorative elements
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(40.dp)
                    .offset(x = 15.dp, y = (-10).dp)
                    .background(
                        color = Color.White.copy(alpha = 0.2f),
                        shape = CircleShape
                    )
            )

            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(20.dp)
                    .offset(x = 5.dp, y = (-20).dp)
                    .background(
                        color = Color.White.copy(alpha = 0.15f),
                        shape = CircleShape
                    )
            )
        }
    }
}


