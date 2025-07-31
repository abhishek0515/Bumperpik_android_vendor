package com.bumperpick.bumperpick_Vendor.Screens.Account

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.bumperpick.bumperpick_Vendor.API.FinalModel.DataXXXX
import com.bumperpick.bumperpick_Vendor.R
import com.bumperpick.bumperpick_Vendor.Repository.HomeOffer
import com.bumperpick.bumperpick_Vendor.Repository.MarketingOption
import com.bumperpick.bumperpick_Vendor.Repository.OfferValidation
import com.bumperpick.bumperpick_Vendor.Screens.Component.AdPackagesBottomSheet
import com.bumperpick.bumperpick_Vendor.Screens.Component.ButtonView
import com.bumperpick.bumperpick_Vendor.Screens.Component.SignOutDialog
import com.bumperpick.bumperpick_Vendor.Screens.QrScreen.UiState
import com.bumperpick.bumperpick_Vendor.Screens.Subscription.ErrorContent
import com.bumperpick.bumperpick_Vendor.Screens.Subscription.LoadingContent
import com.bumperpick.bumperpick_Vendor.ui.theme.BtnColor
import com.bumperpick.bumperpick_Vendor.ui.theme.grey
import com.bumperpick.bumperpick_Vendor.ui.theme.satoshi_medium
import com.bumperpick.bumperpick_Vendor.ui.theme.satoshi_regular
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

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
    data object My_subs:AccountClick()
    data object Subscription:AccountClick()
    data class EngagementClick(val marketingOption: MarketingOption):AccountClick()
    data class AdsClick(val gotosub:Boolean):AccountClick()

    data object buyAdsSbus: AccountClick()

    data object viewCurrentAds: AccountClick()
    object FaqClick: AccountClick()

    object mailToAdmin: AccountClick()

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OfferNotificationBottomSheet(
    viewmodel: AccountViewmodel,
    onDismiss: () -> Unit,
    onSendNotification: (List<HomeOffer>) -> Unit
) {
    var selectedOfferId by remember { mutableStateOf<String?>(null) }
    val offers_uiState=viewmodel.offer_fetch_uistate.collectAsState().value
    LaunchedEffect(Unit) {
        viewmodel.fetchOffer()
    }
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = bottomSheetState,
        containerColor = Color.White,
        contentColor = Color.Black,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        windowInsets = WindowInsets(0)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Would you like us to remind you?",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.Gray
                    )
                }
            }

            // Subtitle
            Text(
                text = "Please select the offer that you want to send notification",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            when(offers_uiState){
                UiState.Empty -> {}
                is UiState.Error ->{
                    ErrorContent(offers_uiState.message) {
                        viewmodel.fetchOffer()

                    }
                }
                UiState.Loading ->
                    LoadingContent()
                is UiState.Success -> {
                    val offers=offers_uiState.data.filter { it.offerValid == OfferValidation.Valid }
                    // Offers List
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f, fill = false)
                            .heightIn(max = 400.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    )
                    {    if(offers.isEmpty()){
                        item {
                            Box(modifier = Modifier.fillMaxWidth().padding(12.dp)){
                                Text(text = "No offer available", color = BtnColor, fontSize = 18.sp, modifier = Modifier.fillMaxWidth().align(Alignment.Center), textAlign = TextAlign.Center)
                            }
                        }
                    }
                        else {
                        itemsIndexed(offers) { index, offer ->
                            val isSelected = selectedOfferId == offer.offerId

                            OfferItem(
                                offer = offer,
                                isSelected = isSelected,

                                onSelectionChange = { offerId ->
                                    selectedOfferId = if (selectedOfferId == offerId) {
                                        null // Deselect if clicking the same item
                                    } else {
                                        offerId // Select the new item
                                    }
                                }
                            )
                        }
                    }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Action Buttons
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    )
                    {
                        // No Button
                        Button(
                            onClick = onDismiss,
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFF5F5F5),
                                contentColor = Color.Gray
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "No",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        // Yes Button
                        Button(
                            onClick = {
                                val selectedOffer = offers.find { it.offerId == selectedOfferId }
                                selectedOffer?.let { offer ->
                                    onSendNotification(listOf(offer))
                                }
                            },
                            enabled = selectedOfferId != null,
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = BtnColor,
                                contentColor = Color.White,
                                disabledContainerColor = Color(0xFFCCCCCC),
                                disabledContentColor = Color(0xFF888888)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "Yes",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }


        }
    }
}

@Composable
fun OfferItem(
    offer: HomeOffer,
    isSelected: Boolean,

    onSelectionChange: (String) -> Unit
) {
    val backgroundColor = when {

        isSelected -> BtnColor.copy(alpha = 0.1f)
        else -> Color(0xFFF8F9FA)
    }

    val borderColor = when {

        isSelected -> BtnColor.copy(alpha = 1f)
        else -> Color(0xFFE9ECEF)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                offer.offerId?.let { onSelectionChange(it) }
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = offer.offerTitle ?: "Untitled Offer",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = offer.discount ?: "No discount specified",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Selection Indicator
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(
                        if (isSelected) BtnColor else Color.Transparent
                    )
                    .border(
                        width = 2.dp,
                        color = if (isSelected) BtnColor else Color.Gray,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = Color.White,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(onClick:(AccountClick)->Unit, viewmodel: AccountViewmodel= koinViewModel()){
        val context= LocalContext.current
    var show_renewbtn by remember { mutableStateOf(false) }
    val uiState=viewmodel.uiState.collectAsState().value
    var is_user_ads_subscribed by remember { mutableStateOf(false) }
    var show_signoutDialog by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        viewmodel.fetchProfile()
    }
    var showBottomSheet by remember { mutableStateOf(false) }
    var shownotificationReminder by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()


    if(shownotificationReminder){
        OfferNotificationBottomSheet(viewmodel=viewmodel,
            onDismiss = {
                shownotificationReminder=false
            }, onSendNotification = {
                Log.d("SELECTED NOTIFY",it.toString())
                shownotificationReminder=false
            })
    }
    if(showBottomSheet){
        ModalBottomSheet(
            containerColor = Color.White, // Change this color
            contentColor = Color.Black,
            dragHandle = null,
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState
        ) {
            AdPackagesBottomSheet(
                onDismiss = {
                    showBottomSheet=false
                },
                buyNew = {
                    showBottomSheet=false
                  onClick(AccountClick.buyAdsSbus)
                },
                viewCurrent = {
                    showBottomSheet=false
                    onClick(AccountClick.viewCurrentAds)

                }
            )
        }
    }

    if (show_signoutDialog){
        SignOutDialog(onConfirm = {
            viewmodel.logout()
        }, onDismiss = {
            show_signoutDialog=false
        }
        )
    }


    val isLogout by viewmodel.isLogout.collectAsState()

    if (isLogout) {
        onClick(AccountClick.Logout)
    }



    Scaffold(containerColor = grey) {paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).verticalScroll(rememberScrollState())) {

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
                        Text(text = uiState.message, color = BtnColor, fontSize = 18.sp, modifier = Modifier.fillMaxWidth(), textAlign =TextAlign.Center)
                    }
                }
                is AccountUi_state.GetProfile ->{
                    val data=uiState.vendorDetail
                    Log.d("adsSubscription", (data.data.adsSubscription?.payment_transaction_id == null).toString())
                    is_user_ads_subscribed = data.data.adsSubscription?.payment_transaction_id != null
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
                    val subscriberdata=data.data.subscription

                    if (subscriberdata != null) {
                        SubscriptionCard(
                            subs_data=subscriberdata,
                            is_subs_10 = {show_renewbtn=it}
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = "View Membership", color = BtnColor, fontSize = 14.sp, fontFamily = satoshi_medium,
                        modifier = Modifier.align(Alignment.End).padding(vertical = 2.dp, horizontal = 16.dp)
                            .clickable {
                                onClick(AccountClick.My_subs)
                            }
                        , textDecoration = TextDecoration.Underline)

                    if(show_renewbtn) {

                        Spacer(modifier = Modifier.height(12.dp))
                        ButtonView(text = "Renew now") {
                            onClick(AccountClick.Subscription)
                        }

                    }


                }
                AccountUi_state.Loading ->{
                    Box(modifier = Modifier.fillMaxWidth().padding(vertical = 18.dp)) {
                        CircularProgressIndicator(color = BtnColor, modifier = Modifier.align(Alignment.Center).size(24.dp))
                    }

                }
            }



            Spacer(modifier = Modifier.height(12.dp))

            Box (modifier = Modifier.fillMaxWidth().clickable {
            showBottomSheet=true

            }.background(Color.White), )
            {
                Row (modifier = Modifier.padding(12.dp).align(Alignment.CenterStart)){
                    Image(painter = painterResource(R.drawable.star_circle_svgrepo_com), contentDescription = null, modifier = Modifier.size(30.dp),)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = "Purchase ads package", color = Color.Black, fontSize = 16.sp,)
                }

                Image(imageVector = Icons.Outlined.KeyboardArrowRight, contentDescription = null, modifier = Modifier.size(24.dp).align(Alignment.CenterEnd),)

            }
            Spacer(modifier = Modifier.height(12.dp))

            Box (modifier = Modifier.fillMaxWidth().background(Color.White),){
                Row (modifier = Modifier.padding(12.dp).align(Alignment.CenterStart)){
                    Image(painter = painterResource(R.drawable.bell_01), contentDescription = null, modifier = Modifier.size(30.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = "Send instant reminder", color = Color.Black, fontSize = 16.sp,)
                }
                TextButton(onClick = {
                    shownotificationReminder=true
                }, modifier = Modifier.align(Alignment.CenterEnd)) {
                    Text(text = "Send now", color = BtnColor, fontSize = 16.sp,)
                }

            }

            Spacer(modifier = Modifier.height(12.dp))


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
            Spacer(modifier = Modifier.height(12.dp))
            Row (verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()){
                Icon(painter = painterResource(R.drawable.left), contentDescription = null, modifier = Modifier.size(12.dp), tint = BtnColor.copy(alpha = 0.2f))
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Customer engagement",
                    letterSpacing = 4.sp, // Use sp for text spacing, not dp
                    color = BtnColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 4.dp),

                    )
                Spacer(modifier = Modifier.width(10.dp))
                Icon(painter = painterResource(R.drawable.right), contentDescription = null, modifier = Modifier.size(12.dp), tint = BtnColor.copy(alpha = 0.2f))


            }
            Spacer(modifier = Modifier.height(12.dp))
            Box (modifier = Modifier.fillMaxWidth().clickable {
                onClick(AccountClick.EngagementClick(MarketingOption.EVENTS))
            }.background(Color.White), )
            {
                Row (modifier = Modifier.padding(12.dp).align(Alignment.CenterStart)){
                    Image(painter = painterResource(R.drawable.star_circle_svgrepo_com), contentDescription = null, modifier = Modifier.size(30.dp),)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = "Events", color = Color.Black, fontSize = 16.sp,)
                }

                Image(imageVector = Icons.Outlined.KeyboardArrowRight, contentDescription = null, modifier = Modifier.size(24.dp).align(Alignment.CenterEnd),)

            }

            Spacer(modifier = Modifier.height(12.dp))
            Box (modifier = Modifier.fillMaxWidth().clickable {
                onClick(AccountClick.EngagementClick(MarketingOption.CAMPAIGNS))
            }.background(Color.White), )
            {
                Row (modifier = Modifier.padding(12.dp).align(Alignment.CenterStart)){
                    Image(painter = painterResource(R.drawable.star_circle_svgrepo_com), contentDescription = null, modifier = Modifier.size(30.dp),)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = "Campaigns", color = Color.Black, fontSize = 16.sp,)
                }

                Image(imageVector = Icons.Outlined.KeyboardArrowRight, contentDescription = null, modifier = Modifier.size(24.dp).align(Alignment.CenterEnd),)

            }


            Spacer(modifier = Modifier.height(12.dp))

            Box (modifier = Modifier.fillMaxWidth().clickable {
                onClick(AccountClick.AdsClick(!is_user_ads_subscribed))
            }.background(Color.White), )
            {
                Row (modifier = Modifier.padding(12.dp).align(Alignment.CenterStart)){
                    Image(painter = painterResource(R.drawable.star_circle_svgrepo_com), contentDescription = null, modifier = Modifier.size(30.dp),)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = "Ads and banner", color = Color.Black, fontSize = 16.sp,)
                }

                Image(imageVector = Icons.Outlined.KeyboardArrowRight, contentDescription = null, modifier = Modifier.size(24.dp).align(Alignment.CenterEnd),)

            }

            Spacer(modifier = Modifier.height(12.dp))
            Box (modifier = Modifier.fillMaxWidth().clickable {
                onClick(AccountClick.FaqClick)
            }.background(Color.White), )
            {
                Row (modifier = Modifier.padding(12.dp).align(Alignment.CenterStart)){
                    Image(painter = painterResource(R.drawable.faq_svgrepo_com), contentDescription = null, modifier = Modifier.size(24.dp),)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = "FAQs", color = Color.Black, fontSize = 16.sp, modifier = Modifier.align(
                        Alignment.CenterVertically))
                }

                Image(imageVector = Icons.Outlined.KeyboardArrowRight, contentDescription = null, modifier = Modifier.size(24.dp).align(Alignment.CenterEnd),)

            }
            Spacer(modifier = Modifier.height(12.dp))
            Box (modifier = Modifier.fillMaxWidth().clickable {
                onClick(AccountClick.mailToAdmin)
            }.background(Color.White), )
            {
                Row (modifier = Modifier.padding(12.dp).align(Alignment.CenterStart)){
                    Image(painter = painterResource(R.drawable.email_9_svgrepo_com), contentDescription = null, modifier = Modifier.size(24.dp),)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = "Email to admin", color = Color.Black, fontSize = 16.sp, modifier = Modifier.align(
                        Alignment.CenterVertically))
                }

                Image(imageVector = Icons.Outlined.KeyboardArrowRight, contentDescription = null, modifier = Modifier.size(24.dp).align(Alignment.CenterEnd),)

            }
            Spacer(modifier = Modifier.height(12.dp))
            Box (modifier = Modifier.fillMaxWidth().clickable {
                show_signoutDialog=true

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
data class DateInfo(
    val totalDays: Long,
    val daysLeft: Long,
    val startDateFormatted: String,
    val endDateFormatted: String,
    val todayFormatted: String,
    val isIn10PercentRange: Boolean
)
fun calculateDateInfo(startDate: String?, endDate: String?): DateInfo {
    val start = LocalDate.parse(startDate)
    val end = LocalDate.parse(endDate)
    val today = LocalDate.now()

    val totalDays = ChronoUnit.DAYS.between(start, end)
    val daysLeft = ChronoUnit.DAYS.between(today, end)
    Log.d("Days","")

    // Check if only 10% or less days are left
    val tenPercentOfTotal = (totalDays * 0.1).toLong()
    val isIn10PercentRange = daysLeft<=5

    return DateInfo(
        totalDays = totalDays,
        daysLeft = maxOf(0, daysLeft), // Don't show negative days left
        startDateFormatted = formatDateToDDMMYYYY(start),
        endDateFormatted = formatDateToDDMMYYYY(end),
        todayFormatted = formatDateToDDMMYYYY(today),
        isIn10PercentRange = isIn10PercentRange
    )
}
/**
 * Formats LocalDate to dd-MM-yyyy format
 */
fun formatDateToDDMMYYYY(date: LocalDate): String {
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    return date.format(formatter)
}

/**
 * Formats date string from yyyy-MM-dd to dd-MM-yyyy
 */
fun formatDateStringToDDMMYYYY(dateString: String): String {
    val date = LocalDate.parse(dateString)
    return formatDateToDDMMYYYY(date)
}
@Composable
fun SubscriptionCard(
    is_subs_10: (Boolean) -> Unit,
    subs_data: DataXXXX
) {
    val dateInfo = calculateDateInfo(subs_data.start_date, subs_data.end_date)
    val progressPercentage: Float = if (dateInfo.totalDays > 0) {
        ((dateInfo.totalDays - dateInfo.daysLeft).toFloat() / dateInfo.totalDays.toFloat()).coerceIn(0f, 1f)
    } else {
        0f
    }

    // Call the callback with the subscription status
    is_subs_10(dateInfo.isIn10PercentRange)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .padding(horizontal = 12.dp)
            .clickable { },
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
                    .padding(horizontal = 12.dp, vertical = 3.dp)
            ) {
                Text(
                    text = "Expires on ${formatDateStringToDDMMYYYY(subs_data.end_date ?: "")}",
                    fontSize = 11.sp,
                    color = Color.White,
                    fontWeight = FontWeight.W400
                )
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
                    .padding(start = 16.dp, top = 40.dp, end = 16.dp)
                    .fillMaxWidth(0.75f)
            ) {
                // Membership Title
                Text(
                    text = subs_data.name ?: "",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.W600,
                    color = Color(0xFF8D4E3C),
                    lineHeight = 24.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Description
                Text(
                    text = "Complete ${dateInfo.totalDays} days to earn XX% on next subscription",
                    fontSize = 14.sp,
                    color = Color(0xFF8D4E3C).copy(alpha = 0.8f),
                    fontWeight = FontWeight.W400,
                    lineHeight = 16.sp
                )
                // Progress Section - Bottom Left
                Column(
                    modifier = Modifier
                        .padding(bottom = 16.dp, top = 16.dp)
                        .fillMaxWidth(0.7f)
                ) {
                    // Progress Bar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .background(
                                color = Color.White.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(3.dp)
                            )
                    ) {
                        if (progressPercentage > 0f) {
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
                    val completedDays = (dateInfo.totalDays - dateInfo.daysLeft).coerceAtLeast(0)
                    Text(
                        text = "$completedDays/${dateInfo.totalDays} days",
                        fontSize = 14.sp,
                        color = Color(0xFF8D4E3C).copy(alpha = 0.9f),
                        fontWeight = FontWeight.Bold
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

