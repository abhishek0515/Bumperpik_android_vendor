package com.bumperpick.bumperpickvendor.Screens.Component

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope

import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.bumperpick.bumperpickvendor.R
import com.bumperpick.bumperpickvendor.ui.theme.BtnColor
import com.bumperpick.bumperpickvendor.ui.theme.satoshi_regular

import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import coil.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.bumperpick.bumperpickvendor.Repository.HomeOffer
import com.bumperpick.bumperpickvendor.Repository.MarketingOption
import com.bumperpick.bumperpickvendor.Repository.OfferModel
import com.bumperpick.bumperpickvendor.Repository.OfferTemplateType
import com.bumperpick.bumperpickvendor.Repository.OfferValidation
import com.bumperpick.bumperpickvendor.Repository.Template_Data
import com.bumperpick.bumperpickvendor.Repository.TextType
import com.bumperpick.bumperpickvendor.Screens.CreateOfferScreen.ColorType
import com.bumperpick.bumperpickvendor.Screens.CreateOfferScreen.CreateOfferViewmodel
import com.bumperpick.bumperpickvendor.Screens.CreateOfferScreen.HeadingSize
import com.bumperpick.bumperpickvendor.Screens.CreateOfferScreen.RadialGradientType
import com.bumperpick.bumperpickvendor.Screens.CreateOfferScreen.Template1
import com.bumperpick.bumperpickvendor.Screens.CreateOfferScreen.Template2
import com.bumperpick.bumperpickvendor.Screens.CreateOfferScreen.Template3
import com.bumperpick.bumperpickvendor.Screens.CreateOfferScreen.Template4
import com.bumperpick.bumperpickvendor.Screens.CreateOfferScreen.getGradientByColorType
import com.bumperpick.bumperpickvendor.ui.theme.grey
import com.bumperpick.bumperpickvendor.ui.theme.satoshi_medium
import kotlinx.coroutines.delay
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape

import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import com.bumperpick.bumperpickvendor.API.FinalModel.Media
import com.bumperpick.bumperpickvendor.API.FinalModel.Subcategory
import com.bumperpick.bumperpickvendor.Screens.VendorDetailPage.VendorDetailViewmodel
import com.bumperpick.bumperpickvendor.ui.theme.satoshi
import org.koin.androidx.compose.koinViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale


/**
 * Primary action button with consistent styling
 */
@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = BtnColor
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontFamily = satoshi_medium
        )
    }
}

/**
 * Secondary action button with consistent styling
 */
@Composable
fun SignOutDialog(

    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Sign Out",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = "Are you sure you want to sign out?",
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm()
                    onDismiss()
                }
            ) {
                Text(
                    text = "Sign Out",
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )

}

@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    btnColor:Color= BtnColor.copy(alpha = 0.1f), textColor:Color= BtnColor,
    modifier: Modifier=Modifier,
    horizontal_padding:Dp=16.dp,

    enabled: Boolean = true
) {
    OutlinedButton (
        onClick = { onClick() },
        enabled=enabled,
        modifier = modifier

            .fillMaxWidth()
            .height(50.dp)
            .padding( bottom = 0.dp, start =  horizontal_padding),

        border = BorderStroke(0.dp, color = Color.Transparent),
        colors = ButtonDefaults.buttonColors(
            containerColor = btnColor,
        ),
        shape = RoundedCornerShape(16.dp)

    ) {
        Text(text, color = textColor, fontFamily = satoshi_regular, fontWeight = FontWeight.Bold, fontSize = 18.sp)
    }
}
@Composable
fun TextFieldView(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "Placeholder...",
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    textStyle: TextStyle = LocalTextStyle.current,
    modifier: Modifier = Modifier,
    containerColor: Color = Color.White, // Default light gray background
    singleLine: Boolean = true,
    isEnabled: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                style = textStyle.copy(color = Color.Gray)
            )
        },
        enabled = isEnabled,
        keyboardOptions = keyboardOptions,
        textStyle = textStyle,
        modifier = modifier.fillMaxWidth(),
        singleLine = singleLine,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = containerColor,
            disabledTextColor = Color.Black,

            focusedContainerColor = containerColor,
            disabledContainerColor = containerColor,
            cursorColor =  BtnColor,
            focusedBorderColor =  BtnColor,
            unfocusedBorderColor = Color.Gray,

        )
    )
}
@Composable
fun SubcategoryDropdown(
    subcategories: List<Subcategory>,
    selectedSubcategoryId: Int? = null,
    onSubcategorySelected: (Int) -> Unit,
    placeholder: String = "Select Subcategory...",
    textStyle: TextStyle = LocalTextStyle.current,
    modifier: Modifier = Modifier,
    containerColor: Color = Color.White,
    isEnabled: Boolean = true
) {
    var expanded by remember { mutableStateOf(false) }

    // Find selected subcategory name
    val selectedSubcategoryName = subcategories.find { it.id == selectedSubcategoryId }?.name ?: ""

    Box(modifier = modifier) {
        OutlinedTextField(
            value = selectedSubcategoryName,
            onValueChange = { }, // No direct text input
            placeholder = {
                Text(
                    text = placeholder,
                    style = textStyle.copy(color = Color.Gray)
                )
            },
            enabled = isEnabled,
            readOnly = true, // Make it read-only so users can only select from dropdown
            textStyle = textStyle,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = isEnabled) { expanded = true },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = containerColor,
                disabledTextColor = Color.Black,
                focusedContainerColor = containerColor,
                disabledContainerColor = containerColor,
                cursorColor = BtnColor,
                focusedBorderColor = BtnColor,
                unfocusedBorderColor = Color.Gray,
            ),
            trailingIcon = {
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = Color.Gray,
                    modifier = Modifier.clickable(enabled = isEnabled) { expanded = !expanded }
                )
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            subcategories.forEach { subcategory ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = subcategory.name,
                            style = textStyle
                        )
                    },
                    onClick = {
                        onSubcategorySelected(subcategory.id)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun ButtonView(text:String,
               enabled: Boolean=true,
               btnColor:Color= BtnColor,textColor:Color=Color.White,modifier: Modifier=Modifier,horizontal_padding:Dp=16.dp,onClick:()->Unit,) {
    Button(
        onClick = { onClick() },
        enabled=enabled,
        modifier = modifier

            .fillMaxWidth()
            .height(75.dp)
            .padding( bottom = 20.dp, start =  horizontal_padding, end = horizontal_padding),

        colors = ButtonDefaults.buttonColors(
            containerColor = btnColor
        ),
        shape = RoundedCornerShape(16.dp)

    ) {
        Text(text, color = textColor, fontFamily = satoshi_regular, fontWeight = FontWeight.Bold, fontSize = 18.sp)
    }
}

@Composable
fun Google_SigInButton(modifier: Modifier=Modifier,onCLick:()->Unit) {


    OutlinedButton(
        onClick = { onCLick()},
        border = BorderStroke(0.dp, Color.Transparent),
        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues(),
        modifier = modifier
            .clip(shape = RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .background(
                color = Color(0xFFF0F0F0),
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(vertical = 16.dp,)
        ) {
            Image(
                painter = painterResource(R.drawable.google_icon_logo_svgrepo_com),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(end = 10.dp,)
                    .clip(shape = RoundedCornerShape(8.dp))
                    .width(18.dp)
                    .height(18.dp)
            )
            Text(
                "Sign in with Google",
                color = Color(0xFF212427),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}
@Composable
fun OtpView(
    numberOfOtp: Int,
    value: String,
    onValueChange: (String) -> Unit,
    otpCompleted: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequesters = List(numberOfOtp) { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(numberOfOtp) { index ->
            val char = if (index < value.length) value[index].toString() else ""
            OutlinedTextField(
                value = char,
                onValueChange = { newChar ->
                    if (newChar.length <= 1) {
                        val newValue = buildString {
                            append(value.take(index))
                            append(newChar)
                            append(value.drop(index + 1))
                        }.take(numberOfOtp)
                        onValueChange(newValue)
                        if (newChar.isNotEmpty() && index < numberOfOtp - 1) {
                            focusRequesters[index + 1].requestFocus()
                        } else if (newChar.isEmpty() && index > 0) {
                            focusRequesters[index - 1].requestFocus()
                        }
                        if (newValue.length == numberOfOtp) {
                            otpCompleted(newValue)
                            keyboardController?.hide()
                        }
                    }
                },
                modifier = Modifier
                    .width(48.dp)
                    .focusRequester(focusRequesters[index]),
                textStyle = TextStyle(
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BtnColor,
                    unfocusedBorderColor = Color.Gray,
                    disabledBorderColor = Color.Black,
                    focusedContainerColor = grey,
                    unfocusedContainerColor = grey,
                    disabledContainerColor = grey,
                    cursorColor = BtnColor
                ),
                shape = RoundedCornerShape(10.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        if (index < numberOfOtp - 1) {
                            focusRequesters[index + 1].requestFocus()
                        }
                    }
                ),
                singleLine = true
            )
        }
    }

    LaunchedEffect(Unit) {
        focusRequesters[0].requestFocus()
    }
}

@Composable
fun LocationCard(
    modifier: Modifier = Modifier,
    locationTitle: String = "Sector 48, Sohna road",
    locationSubtitle: String = "Gurugram, Sohna road",
    onNotificationClick: () -> Unit = {},
    onCartClick: () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit = {} // Dynamic content support
) {
    var size by remember { mutableStateOf(IntSize.Zero) }
    val backgroundModifier = if (size.width > 0 && size.height > 0) {
        val radius = maxOf(size.width, size.height) / 1.5f
        Modifier.background(
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFF8B1538), Color(0xFF5A0E26)),
                center = Offset(size.width / 2f, size.height / 2f),
                radius = radius
            )
        )
    } else {
        Modifier.background(Color(0xFF8B1538))
    }
    Card(
        modifier = modifier
            .fillMaxWidth()

            .onSizeChanged { size = it },
        shape = RoundedCornerShape(
            topStart = 0.dp,
            topEnd = 0.dp,
            bottomStart = 16.dp,
            bottomEnd = 16.dp
        ),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {

        Column(modifier   .then(backgroundModifier)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()

                .padding(top = 24.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
        ) {

                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left - Location
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.LocationOn,
                            contentDescription = "Location",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text(
                                text = locationTitle,
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = 0.5.sp
                            )
                            Text(
                                text = locationSubtitle,
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 12.sp,
                                letterSpacing = 0.3.sp
                            )
                        }
                    }

                    // Right - Icons
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        IconButton(onClick = onNotificationClick, modifier = Modifier.size(36.dp)) {
                            Icon(
                                imageVector = Icons.Outlined.Notifications,
                                contentDescription = "Notifications",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                // Dynamic additional content

            }

            content()
        }
    }
}
data class NavigationItem(
    val label: String,
    val icon: ImageVector? = null,
    val icon_draw:Int?=null,
    val painter: Painter? = null,
    val contentDescription: String,
)
@Composable
fun BottomNavigationBar(
    items: List<NavigationItem>,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        modifier = Modifier.shadow(0.dp)
    ) {
        items.forEachIndexed { index, item ->

            NavigationBarItem(
                selected = selectedTab == index,
                onClick = { onTabSelected(index) },
                icon = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {


                        // Icon itself
                        item.icon?.let {
                            Icon(
                                imageVector = it,
                                contentDescription = item.contentDescription,
                                tint = if (selectedTab == index) Color(0xFF3B82F6) else Color.Gray
                            )
                        }
                        item.icon_draw?.let {
                            Icon(painter = painterResource(it), contentDescription = item.contentDescription, tint = if (selectedTab == index) Color(0xFF3B82F6) else Color.Gray, modifier = Modifier.size(30.dp))
                        }
                        item.painter?.let {
                            Icon(
                                painter = it,
                                contentDescription = item.contentDescription,
                                tint = if (selectedTab == index) Color(0xFF3B82F6) else Color.Gray
                            )
                        }
                    }
                },
                label = {
                    Text(
                        text = item.label,
                        color = if (selectedTab == index) Color(0xFF3B82F6) else Color.Gray,
                        fontSize = 12.sp,
                        fontWeight = if (selectedTab == index) FontWeight.Medium else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                ),
                modifier = if (selectedTab == index) {
                    Modifier.background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF3B82F6).copy(alpha = 0.1f),
                                Color.White
                            )
                        )
                    )
                } else Modifier
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun No_offer(onSelectedOffer:(marketingOption:MarketingOption,islater:Boolean)->Unit){
    val viewmodel=koinViewModel<VendorDetailViewmodel>()
    val savedetail=viewmodel.savedVendorDetail.collectAsState()
    LaunchedEffect(Unit) {
        viewmodel.getSavedVendorDetail()
    }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showBottomSheet by remember { mutableStateOf(false) }
    var islater by remember { mutableStateOf(false) }
   Surface(modifier = Modifier.background(Color.White), color = Color.White) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(40.dp),
        modifier = Modifier.fillMaxSize()
    ) {

        LocationCard(   locationTitle = if(savedetail.value!=null) {
            savedetail.value!!.establishment_name }
        else{""},
            locationSubtitle =if(savedetail.value!=null) {
                savedetail.value!!.establishment_address
            } else{""},)

        // Action buttons
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 16.dp).fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable._04_box,),
                contentDescription = null,
                modifier = Modifier.size(200.dp).align(Alignment.CenterHorizontally),
                alignment = Alignment.Center)
            Button(
                onClick = {showBottomSheet=true},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BtnColor
                ),

                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Create offer",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }

            // Create offer later button
            OutlinedButton(
                onClick = {
                    showBottomSheet =true
                    islater =true},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = BtnColor
                ),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            BtnColor,
                            Color(0xFFB91C3C)
                        )
                    )
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Create offer later",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }

       if (showBottomSheet) {
           ModalBottomSheet(
               containerColor = Color.White, // Change this color
               contentColor = Color.Black,
               dragHandle = null,
               onDismissRequest = { showBottomSheet = false },
               sheetState = sheetState
           ) {
               MarketingOptionBottomSheet(
                   onDoneClick = {
                      onSelectedOffer(it,islater)
                   },
                   onDismiss = { showBottomSheet = false }
               )
           }
       }

   }
}


@Composable
fun ReviewItemView(
    fieldName: String,
    text: String,
    isExpanded: Boolean=false // External control
) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 10.dp)) {

        Text(
            text = fieldName,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = satoshi_regular
        )

        Spacer(Modifier.height(6.dp))

        Card(
            border = BorderStroke(1.dp, Color.Gray),
            shape = RoundedCornerShape(12.dp),

            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize()
                .height(if(!isExpanded) Dp.Unspecified else 100.dp)


        ) {
            Text(
                text = text,
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = satoshi_regular,
                maxLines = if (isExpanded) Int.MAX_VALUE else 5,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(12.dp)
            )
        }
        Spacer(Modifier.height(18.dp))
    }
}
@Composable
fun MarketingOptionBottomSheet(
    onDoneClick: (MarketingOption) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedOption by remember { mutableStateOf<MarketingOption?>(null) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)

    ) {
        // Close button at top
        Row(
            modifier = Modifier.fillMaxWidth().padding(end=16.dp,top=16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = onDismiss,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // Title below close button
        Text(
            text = "What would you like to choose from the below options?",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black,
            lineHeight = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp,start = 16.dp,end = 16.dp)
        )
        val offer_option=MarketingOption.OFFERS

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = if (selectedOption == offer_option)
                        Color(0xFFFFE5E5) // Light pink background
                    else
                        Color.White,
                    shape = RoundedCornerShape(8.dp)
                )
                .clickable { selectedOption = offer_option }
                .padding(horizontal = 24.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = offer_option.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )

            // Custom radio button
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .border(
                        width = 2.dp,
                        color = if (selectedOption == offer_option)
                            Color(0xFFD32F2F)
                        else
                            Color.Gray,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (selectedOption == offer_option) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(
                                Color(0xFFD32F2F),
                                CircleShape
                            )
                    )
                }
            }
        }

        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp)) {
            Text("Customer engagement", fontFamily = satoshi_medium, modifier = Modifier.align(Alignment.CenterVertically))
            Spacer(modifier = Modifier.width(8.dp))
            Divider(color = Color.Gray, modifier = Modifier.height(1.dp).align(Alignment.CenterVertically))
        }
        // Options list
        MarketingOption.allOptions.drop(1).forEach { option ->
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = if (selectedOption == option)
                                BtnColor.copy(alpha = 0.1f) // Light pink background
                            else
                                Color.White,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable { selectedOption = option }
                        .padding(horizontal = 24.dp, vertical = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = option.title,
                        fontFamily = satoshi_regular,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )

                    // Custom radio button
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .border(
                                width = 2.dp,
                                color = if (selectedOption == option)
                                   BtnColor
                                else
                                    Color.Gray,
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (selectedOption == option) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .background(
                                      BtnColor,
                                        CircleShape
                                    )
                            )
                        }
                    }
                }

                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp),
                    thickness = 1.dp,
                    color = Color(0xFFE0E0E0)
                )
            }
        }


        Spacer(modifier = Modifier.height(32.dp))

      ButtonView(text = "Next") {
          selectedOption?.let { onDoneClick(it) }
          onDismiss()
      }

    }
}

@Composable
fun SimpleImagePicker(
    text:String,
    modifier: Modifier = Modifier,
    onImageSelected: (Uri?) -> Unit = {},
    ImageUri: Uri? = null,
) {
    val context = LocalContext.current
    var selectedImageUri by remember { mutableStateOf<Uri?>(ImageUri) }

    // Image picker launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val size = context.contentResolver.openInputStream(it)?.available()?.toLong() ?: 0L
            if (size <= 10 * 1024 * 1024) {
                selectedImageUri = it
                onImageSelected(it)
            } else {
                Toast.makeText(context, "File size exceeds 10MB", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        Text(
            text =text ,
            fontSize = 16.sp,
            fontFamily = satoshi_medium,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .border(1.dp, BtnColor, RoundedCornerShape(8.dp))
                .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                .clickable {
                    imagePickerLauncher.launch("image/*")
                }
                .semantics { contentDescription = "Upload Image" },
            contentAlignment = Alignment.Center
        ) {
            if (selectedImageUri == null) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter = painterResource(R.drawable.vector),
                        contentDescription = "Upload Icon",
                        tint = Color.Gray
                    )

                    Text(
                        text = "Upload",
                        fontSize = 18.sp,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Image max size is 10MB",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            } else {
                AsyncImage(
                    model = selectedImageUri,
                    contentDescription = "Selected Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(125.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Fit
                )

                Icon(Icons.Outlined.Close, contentDescription = "Close",Modifier.align(Alignment.TopEnd).padding(10.dp).clickable {
                    selectedImageUri = null
                    onImageSelected(null)
                })
            }
        }
    }
}


@Composable
fun TemplateChoosingBottomSheet(
    onDoneClick: (OfferTemplateType) -> Unit = {},
    onDismiss: () -> Unit = {},

    viewmodel:CreateOfferViewmodel
) {
    var selectedTemplate by remember { mutableStateOf<OfferTemplateType?>(null) }
    val dataModel = Template_Data(
        brandName = TextType(
            text = "Demo logo",
            bold = false,
            fontSize = HeadingSize.Small
        ),
        heading = TextType(
            text = "Offer Heading"
        ),
        subHeading = TextType(
            text = "Extra charges may be apply"
        )
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 16.dp)
    ) {
        // Header with title and close button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Choose from our templates",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                lineHeight = 26.sp,
                modifier = Modifier.weight(1f)
            )

            IconButton(
                onClick = onDismiss,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Template 1
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clickable { selectedTemplate = OfferTemplateType.Template1 }
        ) {
            Template1(dataModel.copy(gradientType = ColorType.BLUE))

            // Circular radio button
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .size(24.dp)
                    .background(
                        color = if (selectedTemplate == OfferTemplateType.Template1)
                            BtnColor else Color.White,
                        shape = CircleShape
                    )
                    .border(
                        width = 2.dp,
                        color = BtnColor,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (selectedTemplate == OfferTemplateType.Template1) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        // Template 2
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clickable { selectedTemplate = OfferTemplateType.Template2 }
        ) {
            Template2(dataModel.copy(gradientType = ColorType.RED))

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .size(24.dp)
                    .background(
                        color = if (selectedTemplate == OfferTemplateType.Template2)
                            BtnColor else Color.White,
                        shape = CircleShape
                    )
                    .border(
                        width = 2.dp,
                        color = BtnColor,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (selectedTemplate == OfferTemplateType.Template2) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        // Template 3
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clickable { selectedTemplate = OfferTemplateType.Template3 }
        ) {
            Template3(dataModel.copy(gradientType = ColorType.ORANGE))

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .size(24.dp)
                    .background(
                        color = if (selectedTemplate == OfferTemplateType.Template3)
                            BtnColor else Color.White,
                        shape = CircleShape
                    )
                    .border(
                        width = 2.dp,
                        color =
                            BtnColor ,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (selectedTemplate == OfferTemplateType.Template3) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        // Template 4
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clickable { selectedTemplate = OfferTemplateType.Template4 }
        ) {
            Template4(dataModel.copy(gradientType = ColorType.PURPLE))

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .size(24.dp)
                    .background(
                        color = if (selectedTemplate == OfferTemplateType.Template4)
                            BtnColor else Color.White,
                        shape = CircleShape
                    )
                    .border(
                        width = 2.dp,
                        color =
                            BtnColor ,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (selectedTemplate == OfferTemplateType.Template4) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        ButtonView("NEXT") {
            if(selectedTemplate!=null) {
                selectedTemplate?.let {
                    viewmodel.ChoosedTemplate(it)
                    onDoneClick(it)
                    onDismiss()
                }

            }
            else{
                viewmodel.showError("Please select one of template")
            }
        }
    }
}
sealed class EditDelete(){
    data class EDIT(val offerId: String):EditDelete()
    data class DELETE(val offerId: String):EditDelete()
}

@Composable
fun HomeOfferView(offerModel: HomeOffer,    showBottomSheet:(EditDelete)->Unit={},){

    Card (
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),

    ){
        Column() {
            Box(){
                AutoImageSlider(imageUrls = offerModel.Media_list)
                Box(
                    modifier = Modifier

                        .clip(
                            RoundedCornerShape(topStart = 0.dp, topEnd = 12.dp, bottomStart = 0.dp, bottomEnd = 0.dp)
                        )
                        .background(Color.White)
                        .align(Alignment.BottomStart)
                ) {
                    Text(text = offerModel.offerTag?:"", color = Color.Black, fontSize = 14.sp, modifier = Modifier.padding(vertical = 6.dp, horizontal = 12.dp))

                }

                if(offerModel.offerValid== OfferValidation.Expired){
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)


                            .padding(16.dp)
                            .background(color = Color.Gray.copy(0.3f), shape = RoundedCornerShape(8.dp))
                            .border(0.5.dp,Color.White, shape = RoundedCornerShape(8.dp)),
                    ){
                        Icon(Icons.Outlined.Delete, contentDescription = null,modifier = Modifier.align(Alignment.Center)    .clickable { showBottomSheet(EditDelete.DELETE(offerModel.offerId?:"")) }.padding(4.dp).size(26.dp), tint = Color.White)
                    }
                }
                else{
                    Box(
                        modifier = Modifier

                            .align(Alignment.TopEnd)

                            .padding(16.dp)
                            .background(color = Color.Gray.copy(0.3f), shape = RoundedCornerShape(8.dp))
                            .border(0.5.dp,Color.White, shape = RoundedCornerShape(8.dp)),
                    ){
                        Icon(Icons.Outlined.MoreVert, contentDescription = null,modifier = Modifier.align(Alignment.Center).clickable { showBottomSheet(EditDelete.EDIT(offerModel.offerId?:"")) } .padding(4.dp).size(26.dp), tint = Color.White)
                    }
                }


            }


            Column(modifier = Modifier.padding(12.dp)) {
                Spacer(Modifier.height(5.dp))
                Text(
                    text = offerModel.offerTitle?:"",
                    fontSize = 22.sp,
                    fontFamily = satoshi_regular,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = offerModel.offerDescription?:"",
                    fontSize = 14.sp,
                    fontFamily = satoshi_regular,
                )
                Spacer(modifier = Modifier.height(8.dp))
               Row (verticalAlignment = Alignment.CenterVertically) {
                   Icon(painter = painterResource(R.drawable.clock), contentDescription = "clock",Modifier.size(20.dp))
                   Spacer(modifier = Modifier.width(6.dp))
                   Text(
                       text = "Offer valid from ${ formatDate(offerModel.startDate!!)} to ${formatDate(offerModel.endDate!!)}",
                       fontSize = 15.sp,
                       fontFamily = satoshi_regular,
                   )
               }
                Spacer(modifier = Modifier.height(12.dp))
                DottedDivider(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.Gray,

                )
                Spacer(modifier = Modifier.height(12.dp))
                val is_approved=offerModel.approval.equals("accepted")
                val is_active=offerModel.approval.equals("active")
                val color = when {
                    is_approved && is_active -> Color.Green
                    is_approved && !is_active -> Color.Red
                    !is_approved -> Color(0xFFFFA500) // Orange color
                    else -> Color.Gray // fallback (optional)
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp)
                ) {
                    // Discount row (start)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(R.drawable.percentage_red),
                            contentDescription = "percentage",
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = offerModel.discount?:"",
                            fontSize = 15.sp,
                            fontFamily = satoshi_regular,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    // Active/Inactive card (end)
                    Card(
                        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.2f)),
                        shape = RoundedCornerShape(16.dp),
                    ) {
                        Text(
                            text = offerModel.active?:"",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black,
                            fontFamily = satoshi_regular,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }




            }



        }


    }


}

@Composable
fun AutoImageSlider(
    imageUrls: List<String>,
    modifier: Modifier = Modifier,
    slideAnimationDuration: Int = 800 // Optional: Can remove if unused
) {
    if (imageUrls.isEmpty()) return

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { imageUrls.size }
    )

    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        // User-scrollable Image Pager
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .height(200.dp),
            pageSpacing = 8.dp,
            userScrollEnabled = true // ✅ Allow user scroll
        ) { page ->
            ImageSliderItem(
                imageUrl = imageUrls[page],
                modifier = Modifier.fillMaxSize()
            )
        }

        // Dot Indicators
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(8.dp)
        ) {
            repeat(imageUrls.size) { index ->
                val isSelected = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .size(if (isSelected) 12.dp else 8.dp)
                        .clip(CircleShape)
                        .background(
                            if (isSelected) BtnColor else BtnColor.copy(alpha = 0.5f)
                        )
                )
            }
        }
    }
}


@Composable
 fun ImageSliderItem(
    imageUrl: String,
    modifier: Modifier = Modifier
) {



        AsyncImage(
            model=imageUrl,
            contentDescription = "Slider Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

}


@Composable
fun DottedDivider(
    modifier: Modifier = Modifier,
    color: Color = Color.Gray,
    dotSize: Dp = 2.dp,
    spacing: Dp = 4.dp,
    isVertical: Boolean = false,
    dotCount: Int? = null
) {
    val density = LocalDensity.current

    Canvas(modifier = modifier) {
        val dotSizePx = with(density) { dotSize.toPx() }
        val spacingPx = with(density) { spacing.toPx() }

        if (isVertical) {
            drawVerticalDots(
                drawScope = this,
                color = color,
                dotSizePx = dotSizePx,
                spacingPx = spacingPx,
                dotCount = dotCount
            )
        } else {
            drawHorizontalDots(
                drawScope = this,
                color = color,
                dotSizePx = dotSizePx,
                spacingPx = spacingPx,
                dotCount = dotCount
            )
        }
    }
}

private fun drawHorizontalDots(
    drawScope: DrawScope,
    color: Color,
    dotSizePx: Float,
    spacingPx: Float,
    dotCount: Int?
) {
    val width = drawScope.size.width
    val height = drawScope.size.height
    val centerY = height / 2
    val radius = dotSizePx / 2

    val totalDotWidth = dotSizePx + spacingPx
    val calculatedDotCount = dotCount ?: (width / totalDotWidth).toInt()
    val actualWidth = calculatedDotCount * totalDotWidth - spacingPx
    val startX = (width - actualWidth) / 2

    repeat(calculatedDotCount) { index ->
        val x = startX + index * totalDotWidth + radius
        drawScope.drawCircle(
            color = color,
            radius = radius,
            center = Offset(x, centerY)
        )
    }
}

private fun drawVerticalDots(
    drawScope: DrawScope,
    color: Color,
    dotSizePx: Float,
    spacingPx: Float,
    dotCount: Int?
) {
    val width = drawScope.size.width
    val height = drawScope.size.height
    val centerX = width / 2
    val radius = dotSizePx / 2

    val totalDotHeight = dotSizePx + spacingPx
    val calculatedDotCount = dotCount ?: (height / totalDotHeight).toInt()
    val actualHeight = calculatedDotCount * totalDotHeight - spacingPx
    val startY = (height - actualHeight) / 2

    repeat(calculatedDotCount) { index ->
        val y = startY + index * totalDotHeight + radius
        drawScope.drawCircle(
            color = color,
            radius = radius,
            center = Offset(centerX, y)
        )
    }
}
@Composable
fun AdPackagesBottomSheet(
    buyNew:()->Unit={},
    viewCurrent:()->Unit={},
    onDismiss: () -> Unit = {},
){

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color.White,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            )
            .padding(bottom = 16.dp)
    ) {
        // Handle bar for visual feedback
        Box(
            modifier = Modifier
                .width(40.dp)
                .height(4.dp)
                .background(
                    Color.Gray.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(2.dp)
                )
                .align(Alignment.CenterHorizontally)
                .padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Header
        Text(
            text = "Choose an Option",
            fontSize = 20.sp,
            fontFamily = satoshi,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 16.dp),
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Edit option
        BottomSheetItem(
            icon = {
                Icon(
                    painter = painterResource(R.drawable.star_circle_svgrepo_com),
                    contentDescription = "Buy New Package",
                    modifier = Modifier.size(24.dp),
                    tint = BtnColor
                )
            },
            title = "Buy New Package",

            onClick = buyNew
        )

        // Divider
        Divider(
            modifier = Modifier.padding(horizontal = 16.dp),
            color = Color.Gray.copy(alpha = 0.2f),
            thickness = 0.5.dp
        )

        // Delete option
        BottomSheetItem(
            icon = {
                Icon(
                    painter = painterResource(R.drawable.star_circle_svgrepo_com),
                    contentDescription = "Buy New Package",
                    modifier = Modifier.size(24.dp),
                    tint = BtnColor
                )
            },
            title = "View Current Package",

            onClick = viewCurrent,
            titleColor = Color.Red
        )

        Spacer(modifier = Modifier.height(24.dp))
        ButtonView(text = "Cancel", onClick = onDismiss,)
    }
}
@Composable
fun EditDeleteBottomSheet(
    onEditClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color.White,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            )
            .padding(bottom = 16.dp)
    ) {
        // Handle bar for visual feedback
        Box(
            modifier = Modifier
                .width(40.dp)
                .height(4.dp)
                .background(
                    Color.Gray.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(2.dp)
                )
                .align(Alignment.CenterHorizontally)
                .padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Header
        Text(
            text = "Manage Offer",
            fontSize = 20.sp,
            fontFamily = satoshi,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 16.dp),
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Edit option
        BottomSheetItem(
            icon = {
                Icon(
                    painter = painterResource(R.drawable.pencil_svgrepo_com),
                    contentDescription = "Edit",
                    modifier = Modifier.size(24.dp),
                    tint = BtnColor
                )
            },
            title = "Edit offer",

            onClick = onEditClick
        )

        // Divider
        Divider(
            modifier = Modifier.padding(horizontal = 16.dp),
            color = Color.Gray.copy(alpha = 0.2f),
            thickness = 0.5.dp
        )

        // Delete option
        BottomSheetItem(
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "Delete",
                    modifier = Modifier.size(24.dp),
                    tint = BtnColor
                )
            },
            title = "Delete offer",

            onClick = onDeleteClick,
            titleColor = Color.Red
        )

        Spacer(modifier = Modifier.height(24.dp))
        ButtonView(text = "Cancel", onClick = onDismiss,)
    }
}
@Composable
fun Campaign_EditDeleteBottomSheet(
    onEditClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color.White,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            )
            .padding(bottom = 16.dp)
    ) {
        // Handle bar for visual feedback
        Box(
            modifier = Modifier
                .width(40.dp)
                .height(4.dp)
                .background(
                    Color.Gray.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(2.dp)
                )
                .align(Alignment.CenterHorizontally)
                .padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Header
        Text(
            text = "Manage Campaign",
            fontSize = 20.sp,
            fontFamily = satoshi_medium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 16.dp),
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Edit option
        BottomSheetItem(
            icon = {
                Icon(
                    painter = painterResource(R.drawable.pencil_svgrepo_com),
                    contentDescription = "Edit",
                    modifier = Modifier.size(24.dp),
                    tint = BtnColor
                )
            },
            title = "Edit Campaign Details",

            onClick = onEditClick
        )

        // Divider
        Divider(
            modifier = Modifier.padding(horizontal = 16.dp),
            color = Color.Gray.copy(alpha = 0.2f),
            thickness = 0.5.dp
        )

        // Delete option
        BottomSheetItem(
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "Delete",
                    modifier = Modifier.size(24.dp),
                    tint = BtnColor
                )
            },
            title = "Delete Campaign",

            onClick = onDeleteClick,
            titleColor = Color.Red
        )

        Spacer(modifier = Modifier.height(24.dp))
        ButtonView(text = "Cancel", onClick = onDismiss,)
    }
}

@Composable
fun Event_EditDeleteBottomSheet(
    onEditClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color.White,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            )
            .padding(bottom = 16.dp)
    ) {
        // Handle bar for visual feedback
        Box(
            modifier = Modifier
                .width(40.dp)
                .height(4.dp)
                .background(
                    Color.Gray.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(2.dp)
                )
                .align(Alignment.CenterHorizontally)
                .padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Header
        Text(
            text = "Manage Event",
            fontSize = 20.sp,
            fontFamily = satoshi_medium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 16.dp),
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Edit option
        BottomSheetItem(
            icon = {
                Icon(
                    painter = painterResource(R.drawable.pencil_svgrepo_com),
                    contentDescription = "Edit",
                    modifier = Modifier.size(24.dp),
                    tint = BtnColor
                )
            },
            title = "Edit Event Details",

            onClick = onEditClick
        )

        // Divider
        Divider(
            modifier = Modifier.padding(horizontal = 16.dp),
            color = Color.Gray.copy(alpha = 0.2f),
            thickness = 0.5.dp
        )

        // Delete option
        BottomSheetItem(
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "Delete",
                    modifier = Modifier.size(24.dp),
                    tint = BtnColor
                )
            },
            title = "Delete Event",

            onClick = onDeleteClick,
            titleColor = Color.Red
        )

        Spacer(modifier = Modifier.height(24.dp))
        ButtonView(text = "Cancel", onClick = onDismiss,)
    }
}

@Composable
fun BottomSheetItem(
    icon: @Composable () -> Unit,
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit,
    titleColor: Color = Color.Black
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon container
        Box(
            modifier = Modifier
                .size(40.dp),
            contentAlignment = Alignment.Center
        ) {
            icon()
        }

        Spacer(modifier = Modifier.width(6.dp))

        // Text content
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontFamily = satoshi_regular,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )


        }

        // Arrow icon
        Icon(
            imageVector = Icons.Outlined.KeyboardArrowRight,
            contentDescription = "Navigate",
            modifier = Modifier.size(24.dp),
            tint =Color.Black
        )
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemoveOfferBottomSheet(
    onDismiss: () -> Unit = {},
    onRemoveOffer: (String) -> Unit = {}
) {
    var selectedReason by remember { mutableStateOf("Out of Stock") }
    var otherReasonText by remember { mutableStateOf("") }

    val reasons = listOf(
        "Out of Stock",
        "Incorrectly Posted",
        "Issue with Terms and Conditions",
        "Low Customer Response on Offer"
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Reason for removing offer",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
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

            Spacer(modifier = Modifier.height(18.dp))

            // Radio button options
            reasons.forEach { reason ->
                val isSelected = selectedReason == reason

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            color = if (isSelected)
                                Color(0xFFFFE5E5) // Light pink background
                            else
                                Color.White
                        )
                        .clickable {
                            selectedReason = reason
                            // Clear other reason text when selecting a predefined reason
                            otherReasonText = ""
                        }
                        .padding(horizontal = 24.dp, vertical = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = reason,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )

                    // Custom radio button
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .border(
                                width = 2.dp,
                                color = if (isSelected)
                                    Color(0xFFD32F2F)
                                else
                                    Color.Gray,
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .background(
                                        Color(0xFFD32F2F),
                                        CircleShape
                                    )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Other reasons section
            Text(
                text = "Other reasons (if any)",
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = otherReasonText,
                onValueChange = {
                    otherReasonText = it
                    // If user starts typing, clear the selected predefined reason
                    if (it.isNotBlank()) {
                        selectedReason = ""
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.LightGray,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                shape = RoundedCornerShape(8.dp),
                placeholder = {
                    Text(
                        text = "Please specify your reason...",
                        color = Color.Gray
                    )
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Remove offer button
            Button(
                onClick = {
                    val finalReason = if (otherReasonText.isNotBlank()) {
                        otherReasonText
                    } else {
                        selectedReason.ifBlank { "No reason specified" }
                    }
                    onRemoveOffer(finalReason)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BtnColor
                ),
                shape = RoundedCornerShape(8.dp),
                enabled = selectedReason.isNotBlank() || otherReasonText.isNotBlank()
            ) {
                Text(
                    text = "Remove offer",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}


enum class MetalType {
    GOLDEN,
    SILVER,
    PLATINUM
}

fun getMetalBrush(metalType: MetalType): Brush {
    return when (metalType) {
        MetalType.GOLDEN -> Brush.horizontalGradient(
            colors = listOf(
                Color(0xFFD4AF37), // Dark gold
                Color(0xFFFFD700), // Bright gold
                Color(0xFFDAA520)  // Goldenrod
            )
        )

        MetalType.SILVER -> Brush.horizontalGradient(
            colors = listOf(
                Color(0xFFC0C0C0), // Silver
                Color(0xFFFFFFFF), // White highlight
                Color(0xFF999999), // Dark silver
                Color(0xFFE5E5E5)  // Light silver
            )
        )

        MetalType.PLATINUM -> Brush.horizontalGradient(
            colors = listOf(
                Color(0xFF8C92AC), // Platinum blue-gray
                Color(0xFFE5E4E2), // Platinum white
                Color(0xFF71797E), // Dark platinum
                Color(0xFFBCC6CC)  // Light platinum
            )
        )
    }
}

// Alternative function with string parameter
fun getMetalBrush(metalType: String): Brush {
    return when (metalType) {
        "Gold", "GOLD" -> getMetalBrush(MetalType.GOLDEN)
        "Silver" -> getMetalBrush(MetalType.SILVER)
        "Platinum" -> getMetalBrush(MetalType.PLATINUM)
        else -> getMetalBrush(MetalType.GOLDEN) // Default to golden
    }
}

// Individual functions for each metal type
fun getGoldenBrush(): Brush = Brush.horizontalGradient(
    colors = listOf(
        Color(0xFFD4AF37),
        Color(0xFFFFD700),
        Color(0xFFDAA520)
    )
)

fun getSilverBrush(): Brush = Brush.horizontalGradient(
    colors = listOf(
        Color(0xFFC0C0C0),
        Color(0xFFFFFFFF),
        Color(0xFF999999),
        Color(0xFFE5E5E5)
    )
)

fun getPlatinumBrush(): Brush = Brush.horizontalGradient(
    colors = listOf(
        Color(0xFF8C92AC),
        Color(0xFFA3EBFA),
        Color(0xFF71797E),
        Color(0xFFDEEDFF)
    )
)

fun formatDate(inputDate: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val date = inputFormat.parse(inputDate)
        outputFormat.format(date!!)
    } catch (e: Exception) {
        "Invalid date"
    }
}
