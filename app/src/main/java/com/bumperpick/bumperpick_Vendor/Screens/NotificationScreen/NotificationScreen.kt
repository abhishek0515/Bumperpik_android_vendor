package com.bumperpick.bumperpick_Vendor.Screens.NotificationScreen

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumperpick.bumperickUser.Screens.Faq.FaqCard
import com.bumperpick.bumperpick_Vendor.API.FinalModel.DataXXXXXXXXXXXXXXXXXXX
import com.bumperpick.bumperpick_Vendor.API.FinalModel.Notification
import com.bumperpick.bumperpick_Vendor.R
import com.bumperpick.bumperpick_Vendor.Screens.QrScreen.UiState
import com.bumperpick.bumperpick_Vendor.ui.theme.BtnColor
import com.bumperpick.bumperpick_Vendor.ui.theme.satoshi
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import org.koin.androidx.compose.koinViewModel


@Composable
fun NotificationScreen(viewmodel: NotificationViewmodel = koinViewModel(),onBackClick:()-> Unit) {

    val notification_list = viewmodel.notification.collectAsState().value
    val systemUiController = rememberSystemUiController()
    val statusBarColor = Color(0xFF5A0E26) // Your desired color
    SideEffect {
        systemUiController.setStatusBarColor(
            color = statusBarColor,
            darkIcons = false // true for dark icons on light background
        )
    }
    LaunchedEffect(Unit) {
        viewmodel.fetchNotifcation()
    }

    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(Color(0xFFFAFAFA))
        )
        {
            var size by remember { mutableStateOf(IntSize.Zero) }
            val backgroundModifier = remember(size) {
                if (size.width > 0 && size.height > 0) {
                    val radius = kotlin.comparisons.maxOf(size.width, size.height) / 1.5f
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
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .onSizeChanged { size = it },
                shape = RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 0.dp,
                    bottomStart = 24.dp,
                    bottomEnd = 24.dp
                ),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            )
            {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .then(backgroundModifier)
                        .padding(bottom = 0.dp)
                ) {
                    Spacer(modifier = Modifier.height(12.dp))

                    // Top App Bar with improved spacing
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    )
                    {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.align(Alignment.CenterStart)
                        ) {
                            IconButton(
                                onClick = onBackClick,
                                modifier = Modifier.size(44.dp)
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
                                text = "Notifications",
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp,
                                color = Color.White
                            )
                        }


                    }

                    Spacer(modifier = Modifier.height(12.dp))

                }


            }
            Spacer(modifier = Modifier.height(12.dp))
            when(notification_list){
                UiState.Empty -> {}
                is UiState.Error -> {

                    Box(modifier = Modifier.fillMaxSize()){
                        Text(text = notification_list.message,
                            modifier = Modifier.align(Alignment.Center).padding(20.dp),
                            color = BtnColor)
                    }
                }
                UiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize()){
                        CircularProgressIndicator(color = BtnColor, modifier = Modifier.align(
                            Alignment.Center))
                    }
                }
                is UiState.Success-> {
                    val data = notification_list.data.data

                    NotificationListScreen(data)

                }
            }


        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationListScreen(
    notifications: List<DataXXXXXXXXXXXXXXXXXXX>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()

    ) {
        // Header



        // Notifications list
        if (notifications.isEmpty()) {
            EmptyNotificationsState()
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
            ) {
                items(
                    items = notifications,

                ) { notificationData ->

                  NotificationCard(
                        notificationData = notificationData,
                        modifier = Modifier.animateContentSize(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun NotificationCard(
    notificationData: DataXXXXXXXXXXXXXXXXXXX,
    modifier: Modifier = Modifier
){
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize()
            .padding(vertical = 6.dp),
        onClick = { isExpanded = !isExpanded },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = BorderStroke(0.5.dp, Color.Gray.copy(alpha = 0.5f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp) // Flat design
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Top right icon

            Row {
                Image(
                    painter =painterResource(R.drawable.percentage_red),
                    contentDescription = "Notification Icon",
                    modifier = Modifier
                        .align(Alignment.Top)
                        .size(36.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.Top)
                )
                {
                    // Title
                    Text(
                        text = notificationData.notification.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = Color.Black,
                        maxLines = if (isExpanded) Int.MAX_VALUE else 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Body
                    Text(
                        text = notificationData.notification.body,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            lineHeight = 20.sp
                        ),
                        color = Color.Black.copy(alpha = 0.85f),
                        maxLines = if (isExpanded) Int.MAX_VALUE else 3,
                        overflow = TextOverflow.Ellipsis
                    )

                    // Tap to expand hint
                    if (!isExpanded &&
                        (notificationData.notification.title.length > 50 ||
                                notificationData.notification.body.length > 100)
                    ) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Tap to read more",
                            style = MaterialTheme.typography.labelSmall,
                            color = BtnColor.copy(alpha = 0.6f),
                            modifier = Modifier.align(Alignment.End)
                        )
                    }
                }
                Icon(
                    imageVector = Icons.Outlined.KeyboardArrowRight,
                    contentDescription = "Notification Icon",
                    tint = Color.Black,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .size(20.dp)
                )

            }

        }
    }
}



// Helper function for timestamp formatting
private fun formatTimestamp(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp

    return when {
        diff < 60_000 -> "Now"
        diff < 3600_000 -> "${diff / 60_000}m ago"
        diff < 86400_000 -> "${diff / 3600_000}h ago"
        else -> "${diff / 86400_000}d ago"
    }
}

// Data classes you might need to add to your notification data
/*
data class NotificationAction(
    val label: String,
    val onClick: () -> Unit
)

// Add these fields to your existing data class:
// val isUnread: Boolean? = null
// val category: String? = null
// val timestamp: Long = System.currentTimeMillis()
// val actions: List<NotificationAction>? = null
*/



// Data classes you might need to add to your notification data
/*
data class NotificationAction(
    val label: String,
    val onClick: () -> Unit
)

// Add these fields to your existing data class:
// val isUnread: Boolean? = null
// val category: String? = null
// val timestamp: Long = System.currentTimeMillis()
// val actions: List<NotificationAction>? = null
*/

@Composable
fun EmptyNotificationsState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.Notifications,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.outline
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "No notifications yet",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "When you receive notifications, they'll appear here",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

