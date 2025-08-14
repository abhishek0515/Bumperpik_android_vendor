package com.bumperpick.bumperpick_Vendor.Screens.CreateOfferScreen

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bumperpick.bumperpick_Vendor.Screens.Component.PrimaryButton
import com.bumperpick.bumperpick_Vendor.Screens.Component.SimpleImagePicker
import com.bumperpick.bumperpick_Vendor.Screens.Component.TemplateChoosingBottomSheet
import com.bumperpick.bumperpick_Vendor.ui.theme.BtnColor
import com.bumperpick.bumperpick_Vendor.ui.theme.grey
import com.bumperpick.bumperpick_Vendor.ui.theme.satoshi_medium
import com.bumperpick.bumperpick_Vendor.ui.theme.satoshi_regular
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

sealed class CreateOfferScreenViews(val route: String){
    object SelectBanner:CreateOfferScreenViews("select_banner")
    object EditBanner:CreateOfferScreenViews("edit_banner")
    object MoreofferDetails:CreateOfferScreenViews("more_offer_details")
    object OfferPreview:CreateOfferScreenViews("offer_preview")

}
@Composable
fun CreateOfferScreen(onBack:()->Unit, offerViewmodel: CreateOfferViewmodel= koinViewModel()){
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val error by offerViewmodel.error.collectAsState()
    var offerAdded by remember { mutableStateOf(false) }
    val context= LocalContext.current

    BackHandler { onBack() }
    LaunchedEffect(error) {
        if (error.isNotEmpty()) {
            val result = snackbarHostState.showSnackbar(
                message = error,
                actionLabel = "OK",
                withDismissAction = true
            )
            when (result) {
                SnackbarResult.ActionPerformed -> {
                    offerViewmodel.clearError()
                }
                SnackbarResult.Dismissed -> {
                    offerViewmodel.clearError()

                }
            }
        }
    }
    LaunchedEffect(offerAdded) {
        if (offerAdded) {

            Toast.makeText(context,"Your offer request has been sent successfully.", Toast.LENGTH_SHORT).show()
            delay(3000)
            onBack()
           /* val result = snackbarHostState.showSnackbar(
                message = "Your offer request has been sent successfully.",
                actionLabel = "OK",
                withDismissAction = true
            )
            when (result) {
                SnackbarResult.ActionPerformed -> {
                    offerAdded = false

                    onBack()

                }

                SnackbarResult.Dismissed -> {
                    offerAdded = false
                    onBack()
                }
            }
            delay(3000)*/

        }
    }

       Scaffold(
           modifier = Modifier.fillMaxSize(),
           containerColor = grey,
           snackbarHost = {
               SnackbarHost(
                   hostState = snackbarHostState,
                   modifier = Modifier.padding(16.dp),
                   snackbar = { data ->
                       Snackbar(
                           action = {
                               TextButton(onClick = { data.performAction() }) {
                                   Text(
                                       text = "OK",
                                       color = Color.White
                                   )
                               }
                           },
                           containerColor = Color(0xff238c03),
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
                   startDestination = CreateOfferScreenViews.SelectBanner.route,
                   modifier = Modifier.fillMaxSize()
               ) {
                    composable(CreateOfferScreenViews.SelectBanner.route){
                        Offer_Banner_selector(navController, viewmodel = offerViewmodel)
                    }

                   composable(CreateOfferScreenViews.EditBanner.route){
                       BannerEditScreen(navController, viewmodel = offerViewmodel)
                   }

                   composable(CreateOfferScreenViews.MoreofferDetails.route){
                       MoreOfferDetailsScreen(navController, viewmodel = offerViewmodel)
                   }
                   composable(CreateOfferScreenViews.OfferPreview.route) {
                       OfferPreviewScreen(navController, viewmodel = offerViewmodel) {  ->
                           offerAdded=true
                          // onBack()
                       }
                   }

               }


               }
           }
       }








@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun Offer_Banner_selector(navController: NavController,viewmodel: CreateOfferViewmodel) {

    val offeDetails by viewmodel.offerDetails.collectAsState()
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )
    var showBottomSheet by remember { mutableStateOf(false) }

    val  user_choosed_banner by viewmodel.user_choosed_banner.collectAsState()

    Box(
        modifier = Modifier
            .background(grey)
            .fillMaxSize()
    )
    {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(grey, RoundedCornerShape(8.dp))


        ) {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            ) {
                Spacer(Modifier.height(5.dp))
                Text(
                    text = "Step 1 of 3",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = satoshi_regular,
                    color = Color.Black,
                    modifier = Modifier.padding(16.dp)
                )
                Text(
                    text = "Let’s get started with offer banner",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = satoshi_medium,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(Modifier.height(20.dp))
            }

            Column()
            {
                Spacer(Modifier.height(16.dp))
                SimpleImagePicker(
                    ImageUri = offeDetails.BannerImage,
                    text = "Media Banner",
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth(),
                    onImageSelected = {
                        if (it != null) {
                            viewmodel.ChoosedBanner(startingChoose.UserBanner)
                            viewmodel.updateUserBanner(BannerImage = it)
                        }

                    })
                Spacer(Modifier.height(12.dp))
                Text(
                    "Or",
                    color = Color.Black,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(Modifier.height(12.dp))

                OutlinedButton(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .height(56.dp)
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    onClick = {showBottomSheet=true},
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, BtnColor),
                    colors = ButtonColors(
                        containerColor = BtnColor.copy(alpha = 0.1f),
                        contentColor = Color.Black,
                        disabledContainerColor = BtnColor.copy(alpha = 0.3f),
                        disabledContentColor = Color.Black
                    )
                ) {
                    Text(text = "Choose from our templates", color = BtnColor, fontSize = 18.sp)

                }

            }

        }



        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(Color.White)
                .padding(16.dp),

            ) {
            PrimaryButton(text = "Next", modifier = Modifier.fillMaxWidth(), onClick = {
                when(user_choosed_banner){
                    startingChoose.UserBanner -> {
                        if( viewmodel.offerDetails.value.BannerImage ==null){
                            viewmodel.showError("Please select banner")
                            return@PrimaryButton

                        }
                        else {
                            viewmodel.ChoosedBanner(startingChoose.UserBanner)
                            navController.navigate(CreateOfferScreenViews.EditBanner.route){
                                popUpTo(CreateOfferScreenViews.SelectBanner.route){

                                }
                            }
                        }
                    }
                    startingChoose.Template ->{
                        viewmodel.ChoosedBanner(startingChoose.Template)
                        navController.navigate(CreateOfferScreenViews.EditBanner.route){
                            popUpTo(CreateOfferScreenViews.SelectBanner.route){

                            }
                        }

                    }
                    null -> {
                        viewmodel.showError("Please select bannernull")
                    }
                }




            })
        }
        if (showBottomSheet) {
            ModalBottomSheet(
                containerColor = Color.White, // Change this color
                contentColor = Color.Black,

                dragHandle = null,
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState
            ) {
                TemplateChoosingBottomSheet(onDismiss = { showBottomSheet = false }, onDoneClick = {

                    navController.navigate(CreateOfferScreenViews.EditBanner.route){
                        popUpTo(CreateOfferScreenViews.SelectBanner.route){

                        }
                    }

                }, viewmodel = viewmodel)
            }


        }
    }
}
