package com.bumperpick.bumperpickvendor.Screens.VendorDetailPage

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.bumperpick.bumperpickvendor.API.Provider.uriToFile
import com.bumperpick.bumperpickvendor.R
import com.bumperpick.bumperpickvendor.Repository.Vendor_Category
import com.bumperpick.bumperpickvendor.Repository.Vendor_Details
import com.bumperpick.bumperpickvendor.Screens.Component.ButtonView
import com.bumperpick.bumperpickvendor.Screens.Component.PrimaryButton
import com.bumperpick.bumperpickvendor.Screens.Component.ReviewItemView
import com.bumperpick.bumperpickvendor.Screens.Component.SecondaryButton
import com.bumperpick.bumperpickvendor.Screens.Component.TextFieldView
import com.bumperpick.bumperpickvendor.ui.theme.BtnColor
import com.bumperpick.bumperpickvendor.ui.theme.grey
import com.bumperpick.bumperpickvendor.ui.theme.satoshi
import com.bumperpick.bumperpickvendor.ui.theme.satoshi_bold
import com.bumperpick.bumperpickvendor.ui.theme.satoshi_medium
import com.bumperpick.bumperpickvendor.ui.theme.satoshi_regular
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.io.File

sealed class VendorDetailScreen(val route: String) {
    object EstablishmentInfo : VendorDetailScreen("establishment_info")
    object CategorySelection : VendorDetailScreen("category_selection")
    object AdditionalDetails : VendorDetailScreen("additional_details")
    object ReviewAndSubmit : VendorDetailScreen("review_and_submit")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VendorDetailPage(isMobile:Boolean,mobile:String,viewModel: VendorDetailViewmodel = koinViewModel(), gotoHome: () -> Unit,onBack:()->Unit) {
    val navController = rememberNavController()
    val vendorDetails by viewModel.vendorDetails.collectAsState()
    val categories by viewModel.categories.collectAsState()
    val success by viewModel.success.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val error by viewModel.error.collectAsState()
    Log.d("ISMOBILE","$isMobile || $mobile")
    BackHandler {
        onBack()
    }
    LaunchedEffect(Unit) {
        viewModel.fetchCategories()
    }

    LaunchedEffect(success) {
        if (success!=null) {
            Log.d("ID",success.toString())
            gotoHome()
        }
    }
    LaunchedEffect(error){
        if (error!=null){
            coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    message = error?:"",
                    actionLabel = "Dismiss",
                    duration = SnackbarDuration.Short
                )
                // Clear error after showing snackbar
                viewModel.clearError()
            }
        }

    }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = grey,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState,  modifier = Modifier.padding( 16.dp),  snackbar = { data ->
                Snackbar(
                    action = {
                        TextButton(onClick = { data.performAction() }) {
                            Text(
                                text = "OK",
                                color = Color.White
                            )
                        }
                    },
                    containerColor = Color.Red.copy(alpha = 0.8f),
                    contentColor = Color.White
                ) {
                    Text(text = data.visuals.message)
                }
            })
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            NavHost(
                navController = navController,
                startDestination = VendorDetailScreen.EstablishmentInfo.route,
                modifier = Modifier.fillMaxSize()
            ) {
                composable(VendorDetailScreen.EstablishmentInfo.route) {
                    EstablishmentInfoScreen(
                        navController = navController,
                        viewModel = viewModel,
                        vendorDetails = vendorDetails,
                        onBack=onBack,
                        isMobile,
                        mobile
                    )
                }
                composable(VendorDetailScreen.CategorySelection.route) {
                    CategorySelectScreen(
                        navController = navController,
                        viewModel = viewModel,
                        categories = categories,
                        selectedCategory = vendorDetails.Vendor_Category
                    )
                }
                composable(VendorDetailScreen.AdditionalDetails.route) {
                    AdditionalDetailsScreen(
                        navController = navController,
                        viewModel = viewModel,
                        vendorDetails = vendorDetails
                    )
                }
                composable(VendorDetailScreen.ReviewAndSubmit.route) {
                    ReviewScreen(
                        mobile=mobile,
                        isMobile=isMobile,
                        navController = navController,
                        viewModel = viewModel,
                        vendorDetails = vendorDetails
                    )
                }
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorySelectScreen(
    navController: NavController,
    viewModel: VendorDetailViewmodel,
    categories: List<Vendor_Category>,
    selectedCategory: Vendor_Category
) {
    BackHandler {
        navController.navigate(VendorDetailScreen.EstablishmentInfo.route)
    }
    var searchQuery by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 0.dp)
            .background(grey)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f) // Take available space, pushing buttons to bottom
                .verticalScroll(rememberScrollState())
                .background(Color.White, RoundedCornerShape(8.dp))
                .padding(vertical = 16.dp)
        ) {
            Spacer(Modifier.height(20.dp))
            Text(
                text = "Step 2 of 3",
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = satoshi_regular,
                color = Color.Black,
                modifier = Modifier.padding(16.dp)
            )
            Text(
                text = "Select your category",
                fontSize = 24.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = satoshi_medium,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = "Search",
                        tint = Color.Gray
                    )
                },
                placeholder = { Text(text = "Search for category", color = Color.Gray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color.Black,
                    focusedBorderColor = BtnColor,
                    containerColor = Color.White
                )
            )
            Spacer(Modifier.height(16.dp))

            val filteredCategories = categories.filter {
                it.cat_name.contains(searchQuery, ignoreCase = true)
            }
            Card(
                modifier = Modifier
                    .background(grey)
                    .fillMaxSize()
                    .padding(16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                filteredCategories.forEach { category ->
                    val isSelected = category.cat_id == selectedCategory.cat_id
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 0.dp, vertical = 1.dp)
                            .clickable { viewModel.updateCategory(category) },
                        shape = RoundedCornerShape(0.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) BtnColor.copy(alpha = 0.1f) else Color.White
                        ),
                        elevation = CardDefaults.cardElevation(0.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 16.dp, horizontal = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = category.cat_name,
                                    fontSize = 16.sp,
                                    fontFamily = satoshi_regular,
                                    color = Color.Black
                                )
                                RadioButton(
                                    selected = isSelected,
                                    onClick = { viewModel.updateCategory(category) },
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = BtnColor,
                                        unselectedColor = Color.Gray
                                    )
                                )
                            }
                        }
                        Divider(Modifier.height(1.dp), color = grey)

                    }
                }
            }
        }


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF5F5F5))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SecondaryButton(
                    text = "Previous",
                    onClick = { navController.navigate(VendorDetailScreen.EstablishmentInfo.route) },
                    modifier = Modifier.weight(1f)
                )
                PrimaryButton(
                    text = "Next",
                    onClick = {
                        if (selectedCategory.cat_id.isNotBlank()) {
                            navController.navigate(VendorDetailScreen.AdditionalDetails.route){
                                popUpTo(0) { inclusive = true }
                            }
                        }
                        else{
                            viewModel.update_error(error = "Please select Category")

                        }
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun EstablishmentInfoScreen(
    navController: NavController,
    viewModel: VendorDetailViewmodel,
    vendorDetails: Vendor_Details,
    onBack: () -> Unit,
    isMobile: Boolean,
    mobile: String
) {
    var establishNameState by remember { mutableStateOf(vendorDetails.Vendor_EstablishName) }
    var brandState by remember { mutableStateOf(vendorDetails.Vendor_brand) }
    var emailState by remember { mutableStateOf(if(isMobile)vendorDetails.Vendor_Email else mobile) }
    var mobileState by remember { mutableStateOf(if(!isMobile)vendorDetails.Vendor_Mobile else mobile) }

    BackHandler {
        onBack()

    }

    LaunchedEffect(establishNameState, brandState,emailState,mobileState) {
        viewModel.updateEstablishmentInfo(establishNameState, brandState, email = emailState,mobileState)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 0.dp)
    ) {

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .background(Color.White, RoundedCornerShape(8.dp))
                .padding(vertical = 16.dp),

            ) {

            Spacer(Modifier.height(20.dp))
            Text(
                text = "Step 1 of 3",
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = satoshi_regular,
                color = Color.Black,
                modifier = Modifier.padding(16.dp)
            )
            Text(
                text = "Let's get started with the name of your establishment",
                fontSize = 24.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = satoshi_medium,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(Modifier.height(20.dp))

            Column(modifier = Modifier.background(grey).fillMaxSize().padding(16.dp)) {
                Text(
                    text = "Name of Your Establishment",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = satoshi_regular
                )
                Spacer(Modifier.height(4.dp))
                TextFieldView(
                    value = establishNameState,
                    onValueChange = { establishNameState = it },
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
                    value = brandState,
                    onValueChange = { brandState = it },
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
                    value = if(isMobile) emailState else mobile,
                    onValueChange = { if(isMobile){ emailState = it } },
                    placeholder = "Enter your Email",
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isEnabled = isMobile
                )

                if(!isMobile){
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = "Mobile Number",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = satoshi_regular
                    )
                    Spacer(Modifier.height(4.dp))
                    TextFieldView(
                        value =mobileState,
                        onValueChange = {
                            if (it.length <= 10) mobileState = it
                        },
                        placeholder = "Enter your Mobile Number",
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)

                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(grey)
                .padding(16.dp)
        ) {
            PrimaryButton(
                text = "Next",
                onClick = {
                    viewModel.validateEstablishmentInfo(establishNameState, brandState,emailState,mobileState)

                    if (viewModel.check_errorIs_null()) {
                        navController.navigate(VendorDetailScreen.CategorySelection.route){
                            popUpTo(0) { inclusive = true }
                        }
                    }

                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
@Composable
fun AdditionalDetailsScreen(
    navController: NavController,
    viewModel: VendorDetailViewmodel,
    vendorDetails: Vendor_Details
) {
    BackHandler {
        navController.navigate(VendorDetailScreen.CategorySelection.route)
    }
    var establishmentAddressState by remember { mutableStateOf(vendorDetails.Establisment_Adress) }
    var outletAddressState by remember { mutableStateOf(vendorDetails.Outlet_Address) }
    var gstNumberState by remember { mutableStateOf(vendorDetails.GstNumber) }
    var gstPicUrlState by remember { mutableStateOf(vendorDetails.GstPicUrl) }
    var isSameAsAbove by remember { mutableStateOf(true) } // To handle "Same as above" checkbox
    val context = LocalContext.current

    LaunchedEffect(establishmentAddressState, outletAddressState, gstNumberState, gstPicUrlState) {
        viewModel.updateAdditionalDetails(
            establishmentAddressState,
            outletAddressState,
            gstNumberState,
            gstPicUrlState
        )
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Scrollable content section
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp) // Leave space for the bottom button
                .verticalScroll(rememberScrollState())
                .background(Color.White, RoundedCornerShape(8.dp))
                .padding(vertical = 16.dp)
        ) {
            Spacer(Modifier.height(20.dp))
            Text(
                text = "Step 3 of 3",
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = satoshi_regular,
                color = Color.Black,
                modifier = Modifier.padding(16.dp)
            )
            Text(
                text = "Your Address",
                fontSize = 24.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = satoshi_medium,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(Modifier.height(20.dp))

            Column(
                modifier = Modifier
                    .background(grey)
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Establishment Address",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = satoshi_regular
                )
                Spacer(Modifier.height(6.dp))
                TextFieldView(
                    value = establishmentAddressState,
                    onValueChange = { establishmentAddressState = it },
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
                            outletAddressState = establishmentAddressState
                        }
                    )
                }
                Spacer(Modifier.height(6.dp))
                TextFieldView(
                    value = outletAddressState,
                    onValueChange = { outletAddressState = it },
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
                    value = gstNumberState,
                    onValueChange = { if (it.length <= 15) gstNumberState = it },
                    placeholder = "Enter gst number",
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(Modifier.height(8.dp))

                GSTCertificateUploadSection(
                    onImageSelected = {
                        gstPicUrlState = it?.let { uri -> context.uriToFile(uri) }!!
                    }
                )
            }
        }

        // Fixed bottom button section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(Color(0xFFF5F5F5))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ButtonView("Continue") {
                navController.navigate(VendorDetailScreen.ReviewAndSubmit.route){
                    popUpTo(0) { inclusive = true }
                }
            }

        }
    }

}

@Composable
fun GSTCertificateUploadSection(
    modifier: Modifier = Modifier,
    onImageSelected: (Uri?) -> Unit = {}
) {
    val context = LocalContext.current

    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    var isPermissionGranted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        )
    }

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

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

    // Permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        isPermissionGranted = granted
        if (granted) {
            imagePickerLauncher.launch("image/*")
        } else {
            Toast.makeText(context, "Permission denied. Cannot pick image.", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        Text(
            text = "Upload GST Certificate (Optional)",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(125.dp)
                .border(1.dp, BtnColor, RoundedCornerShape(8.dp))
                .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple()
                ) {
                    if (!isPermissionGranted) {
                        permissionLauncher.launch(permission)
                    } else {
                        imagePickerLauncher.launch("image/*")
                    }
                }
                .semantics { contentDescription = "Upload GST Certificate" },
            contentAlignment = Alignment.Center
        ) {
            when {
                !isPermissionGranted -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = painterResource(R.drawable.vector),
                            contentDescription = "Permission Required",
                            tint = Color.Gray
                        )
                        Text(
                            text = "Permission Required",
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                        Text(
                            text = "Grant storage permission to upload",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }

                selectedImageUri == null -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = painterResource(R.drawable.vector),
                            contentDescription = "Upload Icon",
                            tint = Color.Gray
                        )
                        Text(
                            text = "Upload",
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                        Text(
                            text = "Image max size is 10MB",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }

                else -> {
                    AsyncImage(
                        model = selectedImageUri,
                        contentDescription = "Selected GST Certificate",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(125.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }
    }
}



@Preview
@Composable
fun previewVendor(){
    val viewmodel:VendorDetailViewmodel= koinViewModel()
    val sampleVendor = Vendor_Details(
        Vendor_Id = "VND123456",
        Vendor_EstablishName = "Sunrise Café",
        Vendor_brand = "Sunrise Foods Pvt Ltd",
        Vendor_Email = "contact@sunrisecafe.com",
        Vendor_Mobile = "+919876543210",
        Vendor_Category = Vendor_Category(
            cat_id = "CAT001",
            cat_name = "Food & Beverages"
        ),
        Establisment_Adress = "123 MG Road, Koramangala, Bengaluru, Karnataka 560034",
        Outlet_Address = "Plot 45, HSR Layout, Bengaluru, Karnataka 560102",
        GstNumber = "29ABCDE1234F1Z5",
        GstPicUrl = File("/storage/emulated/0/Documents/sample_gst.jpg")
    )

    ReviewScreen(mobile = "9129883621", isMobile = true, rememberNavController(), viewmodel, sampleVendor)
}
@Composable
fun ReviewScreen(  mobile: String,
                   isMobile: Boolean,
                   navController: NavController,
                   viewModel: VendorDetailViewmodel,
                   vendorDetails: Vendor_Details){
    BackHandler {navController.navigate(VendorDetailScreen.AdditionalDetails.route)  }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 0.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .verticalScroll(rememberScrollState())


            ) {
            Row (modifier = Modifier.background(Color.White).fillMaxWidth().padding(top=40.dp, bottom = 20.dp,start = 16.dp, end = 16.dp),){
                Icon(
                    imageVector = Icons.Outlined.ArrowBack,
                    contentDescription = "back",
                    tint = Color.Black,
                    modifier = Modifier.clickable {
                        navController.navigate(VendorDetailScreen.AdditionalDetails.route)}
                        .align(Alignment.CenterVertically)
                        .size(24.dp)
                    )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Preview Information",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = satoshi_medium,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
                Spacer(Modifier.height(20.dp))
            }
            Column(modifier = Modifier.background(grey).fillMaxSize().padding(16.dp)) {
                ReviewItemView("Name of Establishment",vendorDetails.Vendor_EstablishName)
                ReviewItemView("Outlet / Brand Name",vendorDetails.Vendor_brand)
                ReviewItemView("Email",vendorDetails.Vendor_Email)
                ReviewItemView("Mobile Number",vendorDetails.Vendor_Mobile)
                ReviewItemView("Selected Category",vendorDetails.Vendor_Category.cat_name)
                ReviewItemView("Establishment Address",vendorDetails.Establisment_Adress,isExpanded = true)
                ReviewItemView("Outlet Address",vendorDetails.Outlet_Address,isExpanded = true)
                ReviewItemView("Gst Number",vendorDetails.GstNumber)


            }

        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF5F5F5))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                PrimaryButton(
                    text = "Continue the Sign up",
                    onClick = {
                        viewModel.submitRegistration(if(isMobile)mobile else vendorDetails.Vendor_Mobile)},
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
