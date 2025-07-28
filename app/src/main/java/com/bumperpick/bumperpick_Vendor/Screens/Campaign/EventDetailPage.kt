package com.bumperpick.bumperpick_Vendor.Screens.Campaign

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumperpick.bumperpick_Vendor.API.FinalModel.DataXXXXXXXX
import com.bumperpick.bumperpick_Vendor.API.FinalModel.Registration
import com.bumperpick.bumperpick_Vendor.Screens.QrScreen.UiState
import com.bumperpick.bumperpick_Vendor.ui.theme.BtnColor
import org.koin.androidx.compose.koinViewModel
import kotlin.math.max

@Composable
fun EventDetailPage(
    id: String,
    onBack: () -> Unit,
    viewmodel: EventsViewmodel = koinViewModel()
) {
    val eventData by viewmodel.uiEvent_Detail.collectAsState()

    LaunchedEffect(Unit) {
        viewmodel.getEventDetail(id)
    }

    when (eventData) {
        is UiState.Empty -> {
            EmptyStateScreen(
                message = "No events available",
                onBack = onBack
            )
        }

        is UiState.Error -> {
            ErrorStateScreen(
                message = (eventData as UiState.Error).message,
                onBack = onBack
            )
        }

        UiState.Loading -> {
            LoadingStateScreen(onBack = onBack)
        }

        is UiState.Success -> {
            EventParticipantScreen(
                eventData = (eventData as UiState.Success<DataXXXXXXXX>).data,
                onBackClick = onBack
            )
        }
    }
}

@Composable
fun EventParticipantScreen(
    eventData: DataXXXXXXXX,
    onBackClick: () -> Unit
) {
    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFF8F9FA),
                            Color(0xFFE9ECEF)
                        )
                    )
                )
        )
        {
            // Original Header
            EventDetailHeader(
                title = eventData.title,
                onBackClick = onBackClick
            )
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(0.dp),
                modifier = Modifier.fillMaxSize()
            ) {

                // Event Statistics Cards
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    EventStatisticsSection(eventData = eventData)
                }
                item {
                    Spacer(modifier = Modifier.height(20.dp))

                    // Event Info Cards
                    EventInfoSection(eventData = eventData)
                }
                item {
                    Spacer(modifier = Modifier.height(24.dp))

                    // Participants Section
                    ParticipantsSection(
                        registrations = eventData.registrations
                    )
                }
            }
        }
    }
}

@Composable
fun EventDetailHeader(title: String, onBackClick: () -> Unit) {
    var size by remember { mutableStateOf(IntSize.Zero) }
    val backgroundModifier = remember(size) {
        if (size.width > 0 && size.height > 0) {
            val radius = max(size.width, size.height) / 1.5f
            Modifier.background(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFF8B1538), Color(0xFF5A0E26)),
                    center = Offset(size.width / 2f, size.height / 2f),
                    radius = radius
                )
            )
        } else Modifier.background(Color(0xFF8B1538))
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .onSizeChanged { size = it },
        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .then(backgroundModifier)
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    IconButton(
                        onBackClick, Modifier.size(44.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun EventStatisticsSection(eventData: DataXXXXXXXX) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard(
            title = "Max Participants",
            value = eventData.number_of_participant.toString(),
            icon = Icons.Default.Person,
            color = Color(0xFF4CAF50),
            modifier = Modifier.weight(1f)
        )

        StatCard(
            title = "Registered",
            value = eventData.registrations.size.toString(),
            icon = Icons.Default.Person,
            color = Color(0xFF2196F3),
            modifier = Modifier.weight(1f)
        )

        val remainingSlots = eventData.number_of_participant.toInt() - eventData.registrations.size
        StatCard(
            title = "Available",
            value = remainingSlots.toString(),
            icon = Icons.Default.Person,
            color = if (remainingSlots > 0) Color(0xFFFF9800) else Color(0xFFF44336),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(120.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                modifier = Modifier.size(32.dp),
                shape = CircleShape,
                color = color.copy(alpha = 0.1f)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.padding(6.dp)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = value,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.Black
            )

            Text(
                text = title,
                fontSize = 10.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun EventInfoSection(eventData: DataXXXXXXXX) {
    Column(
        modifier = Modifier.padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        InfoCard(
            icon = Icons.Default.DateRange,
            title = "Start Date",
            value = eventData.start_date,
            color = Color(0xFF4CAF50)
        )

        InfoCard(
            icon = Icons.Default.DateRange,
            title = "End Date",
            value = eventData.end_date,
            color = Color(0xFFF44336)
        )
    }
}

@Composable
fun InfoCard(
    icon: ImageVector,
    title: String,
    value: String,
    color: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = CircleShape,
                color = color.copy(alpha = 0.1f)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.padding(8.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = title,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = value,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun ParticipantsSection(
    registrations: List<Registration>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        // Section Header
        Text(
            text = "Registered Users",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Participants List
        if (registrations.isEmpty()) {
            EmptyParticipantsCard(
                message = "No participants registered yet."
            )
        } else {
            registrations.forEach { registration ->
                ParticipantCard(registration = registration)
                Spacer(modifier = Modifier.height(12.dp))
            }
            Spacer(modifier = Modifier.height(20.dp))

        }
    }
}

@Composable
fun ParticipantCard(
    registration: Registration
) {
    var isExpanded by remember { mutableStateOf(false) }
    val rotationAngle by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        animationSpec = tween(300)
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Main Info Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar
                Surface(
                    modifier = Modifier.size(48.dp),
                    shape = CircleShape,
                    color = Color(0xFF8B1538).copy(alpha = 0.1f)
                ) {
                    Text(
                        text = registration.name.take(2).uppercase(),
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF8B1538),
                        fontSize = 16.sp,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Name and Email
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = registration.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.Black
                    )

                    Text(
                        text = registration.email,
                        fontSize = 14.sp,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Expand/Collapse Icon
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                    tint = Color.Gray,
                    modifier = Modifier
                        .size(20.dp)
                        .graphicsLayer(rotationZ = rotationAngle)
                )
            }

            // Expanded Content
            AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Column(
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    // Divider
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Color.Gray.copy(alpha = 0.2f))
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Contact Details
                    ContactDetailRow(
                        icon = Icons.Default.Email,
                        label = "Email",
                        value = registration.email,
                        color = Color(0xFF2196F3)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    ContactDetailRow(
                        icon = Icons.Default.Phone,
                        label = "Phone",
                        value = registration.phone,
                        color = Color(0xFF4CAF50)
                    )
                }
            }
        }
    }
}

@Composable
fun ContactDetailRow(
    icon: ImageVector,
    label: String,
    value: String,
    color: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(32.dp),
            shape = CircleShape,
            color = color.copy(alpha = 0.1f)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.padding(6.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = label,
                fontSize = 12.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
        }
    }
}

@Composable
fun EmptyParticipantsCard(message: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = Color.Gray.copy(alpha = 0.5f),
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = message,
                color = Color.Gray,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun EmptyStateScreen(message: String, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        SimpleHeader(title = "Event", onBack = onBack)

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = message,
                textAlign = TextAlign.Center,
                color = BtnColor,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun ErrorStateScreen(message: String, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        SimpleHeader(title = "Event", onBack = onBack)

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = message,
                textAlign = TextAlign.Center,
                color = Color.Red,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun LoadingStateScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        SimpleHeader(title = "Event", onBack = onBack)

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = BtnColor,
                strokeWidth = 3.dp
            )
        }
    }
}

@Composable
fun SimpleHeader(title: String, onBack: () -> Unit) {
    EventDetailHeader(title = title, onBackClick = onBack)
}