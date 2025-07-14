package com.bumperpick.bumperpickvendor.Screens.CreateOfferScreen

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column



import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.bumperpick.bumperpickvendor.API.FinalModel.Subcategory
import com.bumperpick.bumperpickvendor.R
import com.bumperpick.bumperpickvendor.Repository.OfferTemplateType
import com.bumperpick.bumperpickvendor.Repository.TextType
import com.bumperpick.bumperpickvendor.Screens.Component.PrimaryButton
import com.bumperpick.bumperpickvendor.Screens.Component.SimpleImagePicker
import com.bumperpick.bumperpickvendor.Screens.Component.SubcategoryDropdown
import com.bumperpick.bumperpickvendor.Screens.Component.TextFieldView
import com.bumperpick.bumperpickvendor.ui.theme.BtnColor
import com.bumperpick.bumperpickvendor.ui.theme.grey
import com.bumperpick.bumperpickvendor.ui.theme.satoshi
import com.bumperpick.bumperpickvendor.ui.theme.satoshi_medium
import com.bumperpick.bumperpickvendor.ui.theme.satoshi_regular
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun BannerEditScreen(navController: NavController,viewmodel: CreateOfferViewmodel) {

    val user_choosed_banner by viewmodel.user_choosed_banner.collectAsState()
    val choosed_Template by viewmodel.choosed_Template.collectAsState()
    val offerDetails by viewmodel.offerDetails.collectAsState()
    val subcategories by viewmodel.subCatList.collectAsState()
    var showStartCalendar by remember { mutableStateOf(false) }
    var  showEndCalendar by remember { mutableStateOf(false) }
    var startDate by remember { mutableStateOf<LocalDate?>(null) }
    var endDate by remember { mutableStateOf<LocalDate?>(null) }
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    var quantity_enabled by remember { mutableStateOf(offerDetails.quantity.equals("until stock last")) }
    Log.d("quantity_enabled",quantity_enabled.toString())
    LaunchedEffect(quantity_enabled) {
        if(quantity_enabled){
            viewmodel.updateQuantity("until stock last")
        }
        else {
           // viewmodel.updateQuantity(null)

        }
    }
    LaunchedEffect(Unit) {
        viewmodel.getSubCategory()
    }
    BackHandler {
        navController.navigate(CreateOfferScreenViews.SelectBanner.route){
            popUpTo(CreateOfferScreenViews.SelectBanner.route){
                inclusive=true
            }
        }
    }

        Column(
            modifier = Modifier
                .background(grey)
                .fillMaxSize()
        )
        {
            // First Column - Scrollable content (takes remaining space)
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .weight(1f)
                    .background(grey, RoundedCornerShape(8.dp))
            ) {
                Column(
                    modifier = Modifier.background(Color.White).fillMaxWidth().padding(top = 0.dp)
                ) {
                    Spacer(Modifier.height(5.dp))
                    Text(
                        text = "Step 2 of 3",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = satoshi_regular,
                        color = Color.Black,
                        modifier = Modifier.padding(16.dp)
                    )
                    Text(
                        text = "Let's get started with offer banner",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = satoshi_medium,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(Modifier.height(20.dp))
                }
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                    println("user_choosed_banner $user_choosed_banner")
                    if (user_choosed_banner == startingChoose.UserBanner) {
                        ImageCardFromUri(
                            imageUri = offerDetails.BannerImage.toString()
                        )
                        Spacer(Modifier.height(10.dp))
                        Text(
                            text = "Enter Discount",
                            fontSize = 14.sp,
                            fontFamily = satoshi_regular,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.padding(vertical = 6.dp)
                        )
                        EditableTextTypeView(textType = offerDetails.discount, onTextChange = {
                            viewmodel.updateDissaount(it)
                        })

                        Spacer(modifier = Modifier.height(12.dp))
                        Box(modifier = Modifier.fillMaxWidth()){
                        Text(
                            text = "Offer Quantity",
                            fontSize = 14.sp,
                            fontFamily = satoshi_regular,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.align(Alignment.CenterStart)

                            )


                            RadioButton(selected = !quantity_enabled, onClick = {quantity_enabled=!quantity_enabled},
                                colors = RadioButtonDefaults.colors(selectedColor = BtnColor),
                                modifier = Modifier.align(Alignment.CenterEnd))
                        }
                        Spacer(Modifier.height(4.dp))
                        TextFieldView(
                            value = offerDetails.quantity ?: "",
                            onValueChange = {

                                viewmodel.updateQuantity(it)

                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            placeholder = "Enter Offer Quantity",
                            modifier = Modifier
                                .fillMaxWidth(),
                            singleLine = true,
                            isEnabled = !quantity_enabled
                        )

                        Box(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "Until stock last",
                                fontSize = 16.sp,
                                fontFamily = satoshi_regular,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.align(Alignment.CenterStart),
                                color = Color.Gray,

                                )

                            Switch(
                                modifier = Modifier.align(Alignment.CenterEnd),
                                checked = quantity_enabled,
                                onCheckedChange = { quantity_enabled = it
                                                  viewmodel.updateStockLast(it)
                                                  },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = BtnColor,
                                    uncheckedThumbColor = Color.White,
                                    uncheckedTrackColor = Color.Gray
                                )
                            )
                        }


                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = "Offer Sub Category",
                            fontSize = 14.sp,
                            fontFamily = satoshi_regular,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,

                            )
                        Spacer(Modifier.height(4.dp))
                        SubcategoryDropdown(
                            subcategories = subcategories,
                            selectedSubcategoryId = offerDetails.subcat_id,
                            onSubcategorySelected = {
                                viewmodel.updateSubCat(it)
                            }

                        )


                        Spacer(Modifier.height(10.dp))
                    } else {
                        choosed_Template?.let { EditTemplate(viewmodel, it, subcategories) }
                    }


                    OfferDateSelector(
                        offerStartDate = offerDetails.offerStartDate,
                        offerEndDate = offerDetails.offerEndDate,
                        onStartClick = { showStartCalendar = true },
                        onEndClick = {
                            if (offerDetails.offerStartDate.isNullOrEmpty()) {
                                viewmodel.showError("Please choose start date first")
                            } else {
                                showEndCalendar = true
                            }
                        }
                    )
                }
            }

            // Second Column - Bottom button (fixed at bottom)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                PrimaryButton(
                    text = "Next",
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        if (viewmodel.validateBannerDetail()) {
                            navController.navigate(CreateOfferScreenViews.MoreofferDetails.route) {
                                popUpTo(CreateOfferScreenViews.MoreofferDetails.route) {
                                    inclusive = true
                                }

                            }
                        }

                    }
                )

        }
    }
    CalendarBottomSheet(
        isVisible = showStartCalendar,
        selectedDate = startDate,
        onDateSelected = { startDate = it },
        onDismiss = { showStartCalendar = false },
        startDate = LocalDate.now(),
        text ="Offer start date",
        onConfirm = {
            if (it != null) {
                viewmodel.updateStartDate(it.format(formatter))
            }
            startDate = it
            showStartCalendar = false
        }
    )

    CalendarBottomSheet(
        isVisible = showEndCalendar,
        selectedDate = endDate,
        startDate = startDate ?: LocalDate.now(),
        onDateSelected = { endDate = it },
        onDismiss = { showEndCalendar = false },
        text ="Offer end date",
        onConfirm = {
            if (it != null) {
                viewmodel.updateEndDate(it.format(formatter))
            }
            endDate = it
            showEndCalendar = false
        }
    )


}

@Composable
fun EditTemplate(viewmodel: CreateOfferViewmodel, choosedTemplate: OfferTemplateType,subcategories:List<Subcategory>) {
    val templateData by viewmodel.templateData.collectAsState()
    val offerDetails by viewmodel.offerDetails.collectAsState()
    var brandnameenabled by remember {  mutableStateOf(false)}
    var quantity_enabled by remember { mutableStateOf(offerDetails.quantity.equals("until stock last")) }
    LaunchedEffect(quantity_enabled) {
        if(quantity_enabled){
            viewmodel.updateQuantity("Until stock last")
        }
        else{
           // viewmodel.updateQuantity(null)
        }
    }
    LaunchedEffect(offerDetails) {
        if(offerDetails.BannerImage!=null){
            brandnameenabled=false
        }
        else{
            brandnameenabled=true
        }
    }
    Column {
    when(choosedTemplate){
        OfferTemplateType.Template1 ->{

            Template1(data = templateData.copy())
        }
        OfferTemplateType.Template2 -> {

            Template2(data = templateData)
        }
        OfferTemplateType.Template3 -> {

            Template3(data =  templateData)
        }
        OfferTemplateType.Template4 -> {
            Template4(data =  templateData)
        }
    }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Image appearance",
            fontSize = 14.sp,
            fontFamily = satoshi_regular,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(vertical = 6.dp)
        )
        ColorSelector(
            selectedColorType = templateData.gradientType,
            onColorSelected = {
                viewmodel.updateGradientType(it)
            }
        )

        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Heading",
            fontSize = 14.sp,
            fontFamily = satoshi_regular,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(vertical = 6.dp)
        )
        EditableTextTypeView(textType = templateData.heading, onTextChange = {
            viewmodel.updateHeading(it)
        })
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Heading Size",
            fontSize = 14.sp,
            fontFamily = satoshi_regular,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(vertical = 6.dp)
        )

        HeadingSizeSelector(selectedSize = templateData.heading.fontSize, onSizeSelected = {
            viewmodel.changeHeadingSize(it)
        })
        Spacer(Modifier.height(10.dp))
        Text(
            text = "Enter Discount",
            fontSize = 14.sp,
            fontFamily = satoshi_regular,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(vertical = 6.dp)
        )
        EditableTextTypeView(textType = offerDetails.discount, onTextChange = {
            viewmodel.updateDissaount(it)
        })
        Spacer(modifier = Modifier.height(12.dp))


        Box(modifier = Modifier.fillMaxWidth()){
            Text(
                text = "Offer Quantity",
                fontSize = 14.sp,
                fontFamily = satoshi_regular,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.align(Alignment.CenterStart)

            )


            RadioButton(selected = !quantity_enabled, onClick = {quantity_enabled=!quantity_enabled},
                colors = RadioButtonDefaults.colors(selectedColor = BtnColor),
                modifier = Modifier.align(Alignment.CenterEnd))
        }
        Spacer(Modifier.height(4.dp))
        TextFieldView(
            value = offerDetails.quantity ?: "",
            onValueChange = {

                viewmodel.updateQuantity(it)

            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            placeholder = "Enter offer quantity",
            modifier = Modifier
                .fillMaxWidth(),
            singleLine = true,
            isEnabled = !quantity_enabled
        )

        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Until stock last",
                fontSize = 16.sp,
                fontFamily = satoshi_regular,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterStart),
                color = Color.Gray,

                )

            Switch(
                modifier = Modifier.align(Alignment.CenterEnd),
                checked = quantity_enabled,
                onCheckedChange = { quantity_enabled = it },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = BtnColor,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = Color.Gray
                )
            )
        }
        Spacer(Modifier.height(10.dp))

        Text(
            text = "Offer Sub Category",
            fontSize = 14.sp,
            fontFamily = satoshi_regular,
            fontWeight = FontWeight.Bold,
            color = Color.Black,

            )
        Spacer(Modifier.height(4.dp))
        SubcategoryDropdown(
            subcategories = subcategories,
            selectedSubcategoryId = offerDetails.subcat_id,
            onSubcategorySelected = {
                viewmodel.updateSubCat(it)
            }

        )

        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Sub Heading",
            fontSize = 14.sp,
            fontFamily = satoshi_regular,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(vertical = 6.dp)
        )
        EditableTextTypeView(textType = templateData.subHeading, onTextChange = {
            viewmodel.updateSubHeading(it)
        })
        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Brand Name",
            fontSize = 14.sp,
            fontFamily = satoshi_regular,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(vertical = 6.dp)
        )
        EditableTextTypeView(textType = templateData.brandName,
            onTextChange = {
            viewmodel.updateBrandName(it)
        }, enabled = brandnameenabled)
        Spacer(modifier = Modifier.height(6.dp))
        OrDividerRow()
        Spacer(modifier = Modifier.height(6.dp))

        SimpleImagePicker(
            text = "Upload brand logo",
            ImageUri = templateData.Logo,
            modifier = Modifier.padding(horizontal = 0.dp)
                .align(Alignment.CenterHorizontally).fillMaxWidth(),
            onImageSelected = {


                    viewmodel.update_Logo(Logo = it)



            })







    }





}
@Composable
fun ColorSelector(
    selectedColorType: ColorType,
    onColorSelected: (ColorType) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ColorType.values().forEach { type ->
            val gradient = getGradientByColorType(type)
            val isSelected = type == selectedColorType

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(brush = Brush.verticalGradient(gradient))
                        .clickable { onColorSelected(type) }
                        .border(
                            width = if (isSelected) 3.dp else 1.dp,
                            color = if (isSelected) BtnColor else Color.LightGray,
                            shape = RoundedCornerShape(12.dp)
                        )
                ) {
                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = BtnColor,
                            modifier = Modifier
                                .size(24.dp)
                                .padding(4.dp)
                                .align(Alignment.TopEnd)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = type.name.replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.DarkGray,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}




@Composable
fun OrDividerRow(
    modifier: Modifier = Modifier,
    text: String = "or",
    color: Color = Color.LightGray,
    thickness: Dp = 1.dp,
    padding: Dp = 16.dp
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth().padding(vertical = padding)
    ) {
        Divider(
            color = color,
            thickness = thickness,
            modifier = Modifier
                .weight(1f)
        )

        Text(
            text = text,
            color = Color.Gray,
            modifier = Modifier.padding(horizontal = 8.dp),
            fontSize = 16.sp
        )

        Divider(
            color = color,
            thickness = thickness,
            modifier = Modifier
                .weight(1f)
        )
    }
}

@Composable
fun HeadingSizeSelector(
    selectedSize: HeadingSize,
    onSizeSelected: (HeadingSize) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),


        verticalAlignment = Alignment.CenterVertically
    ) {
        HeadingSize.values().forEach { size ->
            Row(
                verticalAlignment = Alignment.CenterVertically,

                modifier = Modifier
                    .selectable(
                        selected = (size == selectedSize),
                        onClick = { onSizeSelected(size) },
                        role = Role.RadioButton
                    )
                    .padding(end = 20.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                RadioButton(
                    selected = (size == selectedSize),
                    colors = RadioButtonDefaults.colors(selectedColor = BtnColor, unselectedColor = Color.Black),
                    onClick = null // handled above in Row
                )
                Text(
                    text = size.name,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }
    }
}

@Composable
fun EditableTextTypeView(
    modifier: Modifier = Modifier,
    textType: TextType,
    onTextChange: (TextType) -> Unit,
    enabled:Boolean=true
) {
    var localText by remember { mutableStateOf(textType.text) }
    var isBold by remember { mutableStateOf(textType.bold) }
    var isItalic by remember { mutableStateOf(textType.italic) }

    val dynamicStyle = SpanStyle(
        fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
        fontStyle = if (isItalic) FontStyle.Italic else FontStyle.Normal,
        color = textType.color
    )
Card( modifier = modifier
    .fillMaxWidth(),

    colors = CardDefaults.cardColors(containerColor = Color.White),
    border = BorderStroke(1.dp, Color.LightGray)
) {
    Column(

    ) {
        BasicTextField(
            value = localText,
            onValueChange = {
                localText = it
                onTextChange(textType.copy(text = it, bold = isBold, italic = isItalic))
            },

            textStyle = TextStyle(
                fontSize =  18.sp,
                fontFamily = FontFamily.SansSerif,
                color = textType.color
            ).merge(dynamicStyle),
            enabled=enabled,

            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp, top = 12.dp,start = 12.dp, end = 12.dp),

        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.background(grey).fillMaxWidth().padding(8.dp)) {
            Card(
                onClick = {
                    isBold = !isBold
                    onTextChange(textType.copy(text = localText, bold = isBold, italic = isItalic))
                },
                border = BorderStroke(1.dp, if (isBold) BtnColor else Color.LightGray),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.height(36.dp).width(36.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "B",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontSize = 18.sp
                    )
                }
            }
            Card (
                onClick = {
                    isItalic = !isItalic
                    onTextChange(textType.copy(text = localText, bold = isBold, italic = isItalic))
                },
                border = BorderStroke(1.dp, if (isItalic) BtnColor else Color.LightGray),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.height(36.dp).width(36.dp)

            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "/",
                        fontStyle = FontStyle.Italic,
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}
}

@Composable
fun OfferDateSelector(
    offerStartDate: String?,
    offerEndDate: String?,
    text:String="Offer",
    onStartClick: () -> Unit,
    onEndClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Text(
            text = "$text Start Date",
            fontSize = 14.sp,
            fontFamily = satoshi_regular,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(4.dp))

        Card(
            onClick = onStartClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),

        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = offerStartDate ?: "Select start date",
                    fontFamily = satoshi_regular,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = if (offerStartDate == null) Color.Gray else Color.Black
                )
                Icon(
                    painter = painterResource(id = R.drawable.calendar_alt),
                    contentDescription = "Calendar Icon",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "$text End Date",
            fontSize = 14.sp,
            fontFamily = satoshi_regular,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(4.dp))

        Card(
            onClick = onEndClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),

        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = offerEndDate ?: "Select end date",
                    fontFamily = satoshi_regular,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = if (offerEndDate == null) Color.Gray else Color.Black
                )
                Icon(
                    painter = painterResource(id = R.drawable.calendar_alt),
                    contentDescription = "Calendar Icon",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Gray
                )
            }
        }
    }
}



@Composable
fun ImageCardFromUri(
    imageUri: String,
    cornerRadius: Dp = 16.dp,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(16.dp)
) {
    println("image $imageUri")
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp),
        shape = shape,

        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        AsyncImage(
            model = imageUri,
            contentDescription = "Image from URI",
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape)
        )
    }
}