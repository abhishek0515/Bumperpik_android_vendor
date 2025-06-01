package com.bumperpick.bumperpickvendor.Screens.CreateOfferScreen



import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*




import androidx.compose.runtime.*
import androidx.compose.ui.*

import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*

import androidx.compose.material.Icon

import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bumperpick.bumperpickvendor.ui.theme.BtnColor
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarBottomSheet(
    isVisible: Boolean,
    text:String,
    selectedDate: LocalDate?,
    startDate: LocalDate, // <- NEW PARAM
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: (LocalDate?) -> Unit
){
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    var tempSelectedDate by remember { mutableStateOf(selectedDate) }

    if (isVisible) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            containerColor = Color.White,
            modifier = Modifier.fillMaxHeight(0.8f),
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 0.dp)
            ) {
                // Header with title and close button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = text,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.Gray
                        )
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Divider(Modifier.height(1.dp), color = Color.Gray)
                Spacer(modifier = Modifier.height(6.dp))

                // Month/Year navigation
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { currentMonth = currentMonth.minusMonths(1) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowLeft,
                            contentDescription = "Previous month",
                            tint = Color.Gray
                        )
                    }

                    Text(
                        text = "${currentMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${currentMonth.year}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )

                    IconButton(
                        onClick = { currentMonth = currentMonth.plusMonths(1) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = "Next month",
                            tint = Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Day headers
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    val dayHeaders = listOf("SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT")
                    dayHeaders.forEach { day ->
                        Text(
                            text = day,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Gray,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Calendar grid
                CalendarGrid(
                    currentMonth = currentMonth,
                    selectedDate = tempSelectedDate,
                    onDateClick = { date ->
                        tempSelectedDate = date
                    },
                    startDate = startDate // <- pass it here
                )


                Spacer(modifier = Modifier.height(16.dp))

                // Select button
                Button(
                    onClick = {
                        tempSelectedDate?.let { onDateSelected(it) }
                        onConfirm(tempSelectedDate)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BtnColor
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Select",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

            }
        }
    }
}

@Composable
fun CalendarGrid(
    currentMonth: YearMonth,
    selectedDate: LocalDate?,
    onDateClick: (LocalDate) -> Unit,
    startDate: LocalDate // <- NEW PARAM
) {
    val firstDayOfMonth = currentMonth.atDay(1)
    val lastDayOfMonth = currentMonth.atEndOfMonth()
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7
    val daysInMonth = currentMonth.lengthOfMonth()

    // Previous month's trailing days
    val previousMonth = currentMonth.minusMonths(1)
    val daysInPreviousMonth = previousMonth.lengthOfMonth()
    val previousMonthDays = (daysInPreviousMonth - firstDayOfWeek + 1..daysInPreviousMonth).toList()

    // Current month days
    val currentMonthDays = (1..daysInMonth).toList()

    // Next month's leading days
    val totalCells = 42 // 6 rows * 7 days
    val remainingCells = totalCells - previousMonthDays.size - currentMonthDays.size
    val nextMonthDays = (1..remainingCells).toList()

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(2.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        // Previous month days (grayed out)
        items(previousMonthDays) { day ->
            CalendarDayCell(
                day = day,
                isCurrentMonth = false,
                isSelected = false,
                isToday = false,
                isEnabled = false,
                onClick = { }
            )
        }

        // Current month days
        items(currentMonthDays) { day ->
            val date = currentMonth.atDay(day)
            val isSelected = selectedDate == date
            val isToday = date == LocalDate.now()
            val isEnabled = !date.isBefore(startDate)
            CalendarDayCell(
                day = day,
                isCurrentMonth = true,
                isSelected = isSelected,
                isToday = isToday,
                isEnabled = isEnabled,
                onClick = { onDateClick(date) }
            )
        }

        // Next month days (grayed out)
        items(nextMonthDays) { day ->
            CalendarDayCell(
                day = day,
                isCurrentMonth = false,
                isSelected = false,
                isToday = false,
                onClick = { }
            )
        }
    }
}

@Composable
fun CalendarDayCell(
    day: Int,
    isCurrentMonth: Boolean,
    isSelected: Boolean,
    isToday: Boolean,
    isEnabled: Boolean = true, // <-- default true for other months
    onClick: () -> Unit
) {
    val backgroundColor = when {
        isSelected -> BtnColor // blue for selected
        isToday -> Color.Gray
        else -> Color.Transparent
    }

    val textColor = when {
        !isEnabled -> Color.LightGray
        isSelected || isToday -> Color.White
        else -> if (isCurrentMonth) Color.Black else Color.Gray
    }

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable(enabled = isEnabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.toString(),
            color = textColor,
            fontSize = 14.sp
        )
    }
}






data class OfferUiState(
    val productTitle: String = "",
    val productDescription: String = "",
    val termsAndConditions: String = "",
    val mediaUris: List<Uri> = emptyList()
)
class OfferViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(OfferUiState())
    val uiState: StateFlow<OfferUiState> = _uiState

    fun onTitleChange(title: String) {
        _uiState.value = _uiState.value.copy(productTitle = title)
    }

    fun onDescriptionChange(desc: String) {
        _uiState.value = _uiState.value.copy(productDescription = desc)
    }

    fun onTermsChange(terms: String) {
        _uiState.value = _uiState.value.copy(termsAndConditions = terms)
    }

    fun onMediaSelected(uris: List<Uri>) {
        _uiState.value = _uiState.value.copy(mediaUris = uris)
    }

    fun validateForm(): Boolean {
        val state = _uiState.value
        return state.productTitle.isNotBlank() &&
                state.productDescription.isNotBlank() &&
                state.termsAndConditions.isNotBlank() &&
                state.mediaUris.isNotEmpty()
    }
}
@Composable
fun MediaThumbnail(uri: Uri) {
    val context = LocalContext.current
    val isVideo = remember(uri) {
        val mimeType = context.contentResolver.getType(uri) ?: ""
        mimeType.startsWith("video")
    }

    Box(
        modifier = Modifier
            .size(80.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        if (isVideo) {
            Icon(Icons.Default.PlayArrow, contentDescription = "Video", tint = Color.White)
        } else {
            AsyncImage(
                model = uri,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}





@Composable
fun OfferScreen(
    viewModel: OfferViewModel = viewModel(),
    onPreviousClick: () -> Unit,
    onPreviewClick: (OfferUiState) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments()
    ) { uris ->
        if (!uris.isNullOrEmpty()) viewModel.onMediaSelected(uris)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Step 3 of 3", fontSize = 14.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Tell us more about your offer", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(24.dp))

        // Media picker area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .border(1.dp, Color(0xFF3399FF), RoundedCornerShape(8.dp))
                .background(Color(0xFFF7F7F7), RoundedCornerShape(8.dp))
                .clickable { launcher.launch(arrayOf("image/*", "video/*")) },
            contentAlignment = Alignment.Center
        ) {
            if (uiState.mediaUris.isEmpty()) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.PlayArrow, contentDescription = "Upload", tint = Color.Gray)
                    Text("Upload video or image", color = Color.Gray)
                    Text("Video max size 50MB and image max size is 10MB", fontSize = 12.sp, color = Color.Gray)
                }
            } else {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(uiState.mediaUris) {
                        MediaThumbnail(it)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = uiState.productTitle,
            onValueChange = viewModel::onTitleChange,
            label = { Text("Product Title *") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.productDescription,
            onValueChange = viewModel::onDescriptionChange,
            label = { Text("Product Description *") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.termsAndConditions,
            onValueChange = viewModel::onTermsChange,
            label = { Text("Terms and conditions *") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedButton(
                onClick = onPreviousClick,
                modifier = Modifier.weight(1f).padding(end = 8.dp)
            ) {
                Text("Previous")
            }

            Button(
                onClick = {
                    if (viewModel.validateForm()) {
                        onPreviewClick(uiState)
                    } else {
                        Toast.makeText(context, "Fill all fields & upload media", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.weight(1f).padding(start = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB80000))
            ) {
                Text("Preview offer", color = Color.White)
            }
        }
    }
}



@Composable
fun PreviewScreen(state: OfferUiState, onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Offer Preview", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Title: ${state.productTitle}")
        Text("Description: ${state.productDescription}")
        Text("Terms: ${state.termsAndConditions}")

        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(state.mediaUris) {
                MediaThumbnail(it)
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = onBack, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text("Back")
        }
    }
}

@Preview
@Composable
fun preview(){
    val navController = rememberNavController()
    NavHost(navController, startDestination = "form") {
        composable("form") {
            OfferScreen(
                onPreviousClick = { /* handle */ },
                onPreviewClick = {
                    navController.currentBackStackEntry
                        ?.savedStateHandle?.set("offer", it)
                    navController.navigate("preview")
                }
            )
        }

        composable("preview") {
            val offerState = navController.previousBackStackEntry
                ?.savedStateHandle?.get<OfferUiState>("offer")!!
            PreviewScreen(offerState) {
                navController.popBackStack()
            }
        }
    }

}

