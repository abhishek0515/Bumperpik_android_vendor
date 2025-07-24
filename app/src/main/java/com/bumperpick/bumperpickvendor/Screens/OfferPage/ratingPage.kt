package com.bumperpick.bumperpickvendor.Screens.OfferPage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumperpick.bumperpickvendor.API.FinalModel.Review
import com.bumperpick.bumperpickvendor.API.FinalModel.getOfferDetailsModel
import com.bumperpick.bumperpickvendor.Screens.QrScreen.UiState
import com.bumperpick.bumperpickvendor.ui.theme.BtnColor
import com.bumperpick.bumperpickvendor.ui.theme.grey
import com.bumperpick.bumperpickvendor.ui.theme.satoshi_medium
import org.koin.androidx.compose.koinViewModel

// Data class for Review


@Composable
fun ratingPage(
    id: String,
    onBackClick: () -> Unit,
    offerViewmodel: OfferViewmodel = koinViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val offerDetails by offerViewmodel.offerDetails.collectAsState()

    LaunchedEffect(Unit) {
        offerViewmodel.getOfferDetails(id)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White,
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.padding(8.dp),
                snackbar = { data ->
                    Snackbar(
                        action = {
                            TextButton(onClick = { data.performAction() }) {
                                androidx.compose.material3.Text(
                                    text = "OK",
                                    color = Color.White
                                )
                            }
                        },
                        containerColor = Color.Red.copy(alpha = 0.8f),
                        contentColor = Color.White
                    ) {
                        androidx.compose.material3.Text(text = data.visuals.message)
                    }
                })
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .background(grey)
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            when(offerDetails) {
                UiState.Empty -> {
                    HeaderSection("Offer", "Reviews") { onBackClick() }
                    Box(modifier = Modifier.fillMaxSize()) {
                        androidx.compose.material3.Text(
                            text = "No Data available",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.align(Alignment.Center),
                            color = BtnColor
                        )
                    }
                }

                is UiState.Error -> {
                    HeaderSection("Offer", "Reviews") { onBackClick() }
                    Box(modifier = Modifier.fillMaxSize()) {
                        androidx.compose.material3.Text(
                            text = (offerDetails as UiState.Error).message,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.align(Alignment.Center),
                            color = BtnColor
                        )
                    }
                }

                UiState.Loading -> {
                    HeaderSection("Offer", "Reviews") { onBackClick() }
                    Box(modifier = Modifier.fillMaxSize()) {
                        CircularProgressIndicator(
                            color = BtnColor,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }

                is UiState.Success -> {
                    val data = (offerDetails as UiState.Success<getOfferDetailsModel>).data
                    val offerTitle = data.data.title ?: "Offer" // Assuming title exists in data

                    Column {
                        HeaderSection(offerTitle, "Reviews") { onBackClick() }

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Average Rating Card
                            item {
                                AverageRatingCard(
                                    averageRating = data.data.average_rating?.toFloatOrNull() ?: 0f,
                                    totalReviews = data.data.reviews?.size ?: 0
                                )
                            }

                            // Reviews Section Header
                            item {
                                androidx.compose.material3.Text(
                                    text = "Customer Reviews",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = satoshi_medium,
                                    color = Color.Black,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }

                            // Reviews List
                            val reviews = data.data.reviews ?: emptyList()
                            if (reviews.isEmpty()) {
                                item {
                                    Card(
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = CardDefaults.cardColors(containerColor = Color.White)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(32.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            androidx.compose.material3.Text(
                                                text = "No reviews yet",
                                                color = Color.Gray,
                                                fontSize = 16.sp
                                            )
                                        }
                                    }
                                }
                            } else {
                                items(reviews) { review ->
                                    ReviewCard(review)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HeaderSection(
    offerTitle: String,
    subtitle: String,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth()
    ) {
        Spacer(Modifier.height(10.dp))
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .clickable { onBackClick() }
                    .padding(8.dp),
                tint = Color.Black
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                androidx.compose.material3.Text(
                    text = offerTitle,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = satoshi_medium,
                    color = Color.Black
                )
                androidx.compose.material3.Text(
                    text = subtitle,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray
                )
            }
        }
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun AverageRatingCard(
    averageRating: Float,
    totalReviews: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            androidx.compose.material3.Text(
                text = "Overall Rating",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontFamily = satoshi_medium
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Large rating number
            androidx.compose.material3.Text(
                text = String.format("%.1f", averageRating),
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = BtnColor
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Star Rating
            StarRating(
                rating = averageRating,
                starSize = 32.dp
            )

            Spacer(modifier = Modifier.height(8.dp))

            androidx.compose.material3.Text(
                text = "Based on $totalReviews reviews",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
private fun StarRating(
    rating: Float,
    starSize: androidx.compose.ui.unit.Dp = 24.dp
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        repeat(5) { index ->
            val isFilled = index < rating.toInt()
            val isHalfFilled = index == rating.toInt() && rating % 1 != 0f

            Icon(
                imageVector = if (isFilled || isHalfFilled) Icons.Filled.Star else Icons.Outlined.Star,
                contentDescription = null,
                tint = if (isFilled || isHalfFilled) Color(0xFFFFD700) else Color.Gray,
                modifier = Modifier.size(starSize)
            )
        }
    }
}

@Composable
private fun ReviewCard(review: Review) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Customer Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Customer Avatar
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(BtnColor.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Customer",
                        tint = BtnColor,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    androidx.compose.material3.Text(
                        text = "Customer Name: ${review.customer_name}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    StarRating(
                        rating = review.rating.toFloat(),
                        starSize = 16.dp
                    )
                }

                // Rating Badge
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = BtnColor.copy(alpha = 0.1f)
                ) {
                    androidx.compose.material3.Text(
                        text = "${review.rating}.0",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = BtnColor,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Review Text
            val reviewText = when (review.review) {
                is String -> review.review
                else -> review.review.toString()
            }

            if (reviewText.isNotEmpty() && reviewText != "null") {
                androidx.compose.material3.Text(
                    text = reviewText,
                    fontSize = 14.sp,
                    color = Color.Black.copy(alpha = 0.8f),
                    lineHeight = 20.sp
                )
            } else {
                androidx.compose.material3.Text(
                    text = "No written review",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            }
        }
    }
}