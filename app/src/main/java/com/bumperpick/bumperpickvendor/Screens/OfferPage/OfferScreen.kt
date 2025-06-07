package com.bumperpick.bumperpickvendor.Screens.OfferPage

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.bumperpick.bumperpickvendor.API.Provider.uriToFile
import com.bumperpick.bumperpickvendor.Screens.Component.LocationCard
import com.bumperpick.bumperpickvendor.Screens.Component.PrimaryButton
import com.bumperpick.bumperpickvendor.Screens.Component.SimpleImagePicker
import com.bumperpick.bumperpickvendor.Screens.Component.TemplateChoosingBottomSheet
import com.bumperpick.bumperpickvendor.Screens.Component.TextFieldView

import com.bumperpick.bumperpickvendor.Screens.VendorDetailPage.GSTCertificateUploadSection
import com.bumperpick.bumperpickvendor.Screens.VendorDetailPage.VendorDetailScreen
import com.bumperpick.bumperpickvendor.ui.theme.BtnColor
import com.bumperpick.bumperpickvendor.ui.theme.grey
import com.bumperpick.bumperpickvendor.ui.theme.satoshi
import com.bumperpick.bumperpickvendor.ui.theme.satoshi_medium
import com.bumperpick.bumperpickvendor.ui.theme.satoshi_regular
import okhttp3.Route
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.unit.dp
import com.bumperpick.bumperpickvendor.Repository.HomeOffer
import com.bumperpick.bumperpickvendor.Repository.OfferValidation
import com.bumperpick.bumperpickvendor.Screens.Component.EditDelete
import com.bumperpick.bumperpickvendor.Screens.Component.EditDeleteBottomSheet

import com.bumperpick.bumperpickvendor.Screens.Component.HomeOfferView
import com.bumperpick.bumperpickvendor.Screens.Component.RemoveOfferBottomSheet
import com.bumperpick.bumperpickvendor.Screens.VendorDetailPage.VendorDetailViewmodel
import org.koin.androidx.compose.koinViewModel
fun List<HomeOffer>.get_ActiveOffers()=this.filter { it.offerValid==OfferValidation.Valid }
fun List<HomeOffer>.getExpiredOffers()=this.filter { it.offerValid==OfferValidation.Expired }
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OfferScreen(viewmodel: OfferViewmodel = koinViewModel(),EditOffer:(id:String)->Unit) {
    val vendorDetailViewmodel= koinViewModel<VendorDetailViewmodel>()
    val savedetail=vendorDetailViewmodel.savedVendorDetail.collectAsState()
    val delete by viewmodel.delete.collectAsState()
    LaunchedEffect(Unit) {
        vendorDetailViewmodel.getSavedVendorDetail()
    }
    LaunchedEffect(Unit) { viewmodel.getOffers() }
    var selectedId by remember { mutableStateOf("") }
    val sheetState = rememberModalBottomSheetState()
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }
    var showDeleteBottomSheet by remember { mutableStateOf(false) }
    var selectedTabIndex by remember { mutableStateOf(0) }
    val context= LocalContext.current
    val offer by viewmodel.listOfOffers.collectAsState()
    LaunchedEffect(selectedId) {  println("selectedId $selectedId") }
    LaunchedEffect(delete) {
        if(delete.isNotEmpty()){
            Toast.makeText(context,delete,Toast.LENGTH_SHORT).show()
            viewmodel.cleardata()
        }
    }
   if(showDeleteDialog){
       DeleteExpiredOfferDialog(true, onDismiss = {showDeleteDialog=false}) {
           showDeleteDialog=false
           viewmodel.deleteOffer(selectedId,"Delete Expired Offer")
           Toast.makeText(context,"Offer Deleted",Toast.LENGTH_SHORT).show()
       }
   }

    // Compute list directly based on offer and selectedTabIndex
    val list = if (selectedTabIndex == 0) {
        offer.get_ActiveOffers()
    }
    else {
        offer.getExpiredOffers()
    }
    if(showBottomSheet) {
        ModalBottomSheet(
            containerColor = Color.White, // Change this color
            contentColor = Color.Black,
            dragHandle = null,
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState
        ) {
            EditDeleteBottomSheet(
                onDismiss = {
                    showBottomSheet = false
                },
                onEditClick = {

                 EditOffer(selectedId)
                },
                onDeleteClick={
                    showDeleteBottomSheet=true
                }
            )
        }
    }
    if(showDeleteBottomSheet){
        showBottomSheet=false
        RemoveOfferBottomSheet(onDismiss = {
            showDeleteBottomSheet=false
        },
            onRemoveOffer = {
                it->
                println(it)
                viewmodel.deleteOffer(selectedId,it)
                showDeleteBottomSheet=false
            })
    }

    Column {
        // LocationCard and TabRow remain unchanged (as per original code)
        LocationCard(
            content = {
            Card(
                border = BorderStroke(1.dp, Color.Black),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Search,
                        tint = Color.Black,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Search for Reliance Mart",
                        color = Color.Gray,
                        fontFamily = satoshi,
                        fontSize = 16.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(18.dp))
            val gradientBrush = Brush.verticalGradient(
                colors = listOf(Color.Transparent, Color.White.copy(alpha = 0.05f), Color.White.copy(alpha = 0.07f), Color.White.copy(alpha = 0.1f), Color.White.copy(alpha = 0.2f))
            )
            val transparenBrush = Brush.horizontalGradient(
                colors = listOf(Color.Transparent, Color.Transparent)
            )

            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = Color.Transparent,
                divider = {},
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier
                            .tabIndicatorOffset(tabPositions[selectedTabIndex])
                            .height(3.dp),
                        color = Color.White
                    )
                }
            ) {
                Tab(
                    selected = selectedTabIndex == 0,
                    onClick = { selectedTabIndex = 0 },
                    text = { Text("Current Offers", fontSize = 15.sp) },
                    selectedContentColor = Color.White,
                    unselectedContentColor = Color.White,
                    modifier = Modifier
                        .background(if (selectedTabIndex == 0) gradientBrush else transparenBrush)
                        .padding(horizontal = 16.dp)
                )

                Tab(
                    selected = selectedTabIndex == 1,
                    onClick = { selectedTabIndex = 1 },
                    text = { Text("Expired Offers", fontSize = 15.sp) },
                    selectedContentColor = Color.White,
                    unselectedContentColor = Color.White,
                    modifier = Modifier
                        .background(if (selectedTabIndex == 1) gradientBrush else transparenBrush)
                        .padding(horizontal = 16.dp)
                )
            }
        },

            locationTitle = if(savedetail.value!=null) {
                savedetail.value!!.establishment_name }
            else{""},
            locationSubtitle =if(savedetail.value!=null) {
                savedetail.value!!.establishment_address
            } else{""},




            )

        // Display the number of offers based on selectedTabIndex
        Spacer(modifier = Modifier.height(18.dp))
        Text(
            text = if (selectedTabIndex == 0) "${list.size} CURRENT OFFERS" else "${list.size} EXPIRED OFFERS",
            fontSize = 16.sp,
            fontFamily = satoshi_regular,
            fontWeight = FontWeight.SemiBold,
            color = Color.Gray,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(12.dp))

        // Display the offers in LazyColumn
        LazyColumn {
            items(list) {
                HomeOfferView(it, showBottomSheet = {
                    when(it){
                        is EditDelete.DELETE ->{
                showDeleteDialog=true
                            selectedId=it.offerId
                        }
                        is EditDelete.EDIT -> {
                            println(it.offerId)
                            selectedId=it.offerId
                            showBottomSheet=true
                        }
                    }
                })
            }
        }
    }
}




@Composable
fun DeleteExpiredOfferDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirmDelete: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(
                    text = "Delete Expired Offer?",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to delete this expired offer? This action cannot be undone.",
                    fontSize = 16.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = onConfirmDelete,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Yes", color = Color.White)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = onDismiss) {
                    Text("No")
                }
            }
        )
    }
}






