package com.bumperpick.bumperpickvendor.Screens.Home

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.bumperpick.bumperpickvendor.Screens.Component.LocationPermissionScreen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumperpick.bumperpickvendor.R

@Composable
fun Homepage(){


        HomeScreen()



}






@Composable
fun HomeScreen() {
    var selectedTab by remember { mutableStateOf(0) }

        Column(
            modifier = Modifier
                .fillMaxSize()


                .background(Color.White)
        ) {
            // Header
            LocationCard()

            // Main content
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(40.dp)
                ) {
                    // Empty box illustration
                    EmptyBoxIllustration()

                    // Action buttons
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        // Create offer button
                        Button(
                            onClick = { },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFB91C3C)
                            ),
                            shape = RoundedCornerShape(8.dp)
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
                            onClick = { },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color(0xFFB91C3C)
                            ),
                            border = ButtonDefaults.outlinedButtonBorder.copy(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFFB91C3C),
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
            }

            // Bottom navigation
            BottomNavigation(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        }
    }



@Composable
fun EmptyBoxIllustration() {
    // Using drawable image instead of canvas
    Box(
        modifier = Modifier.size(200.dp, 200.dp),
        contentAlignment = Alignment.Center
    ) {
        // Replace this with your actual drawable
         Image(
             painter = painterResource(id = R.drawable._04_box),
             contentDescription = "Empty box",
             modifier = Modifier.fillMaxSize()
         )

    }
}

@Composable
fun LocationCard() {
    Card(
        modifier = Modifier

            .fillMaxWidth().height(150.dp),
        shape = RoundedCornerShape(topEnd = 0.dp,topStart = 0.dp, bottomEnd = 16.dp, bottomStart = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF8B1538) // Burgundy/Maroon color
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp, start = 20.dp, end = 20.dp, bottom = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left side with location icon and text
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                // Location pin icon
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Text content
                Column(
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = "Magic Dosa 51",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "Gurugram Sohna road",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        letterSpacing = 0.3.sp
                    )
                }
            }

            // Right side notification bell icon
            Icon(
                imageVector = Icons.Outlined.Notifications,
                contentDescription = "Notifications",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MagicDosaLocationCardPreview() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFFF5F5F5)
        ) {
            LocationCard()
        }
    }
}

@Composable
fun BottomNavigation(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        modifier = Modifier.shadow(0.dp)
    ) {
        NavigationBarItem(
            selected = selectedTab == 0,
            onClick = { onTabSelected(0) },
            icon = {
                Icon(
                    Icons.Outlined.Home,
                    contentDescription = "Home",
                    tint = if (selectedTab == 0) Color(0xFF3B82F6) else Color.Gray
                )
            },
            label = {
                Text(
                    "Home",
                    color = if (selectedTab == 0) Color(0xFF3B82F6) else Color.Gray,
                    fontSize = 12.sp,
                    fontWeight = if (selectedTab == 0) FontWeight.Medium else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = if (selectedTab == 0) Color.Transparent else Color.Transparent
            ),
            modifier = if (selectedTab == 0) {
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

        NavigationBarItem(
            selected = selectedTab == 1,
            onClick = { onTabSelected(1) },
            icon = {
                Icon(
                    Icons.Default.List,
                    contentDescription = "Create offers",
                    tint = if (selectedTab == 1) Color(0xFF3B82F6) else Color.Gray
                )
            },
            label = {
                Text(
                    "Create offers",
                    color = if (selectedTab == 1) Color(0xFF3B82F6) else Color.Gray,
                    fontSize = 12.sp,
                    fontWeight = if (selectedTab == 1) FontWeight.Medium else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = if (selectedTab == 1) Color.Transparent else Color.Transparent
            ),
            modifier = if (selectedTab == 1) {
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

        NavigationBarItem(
            selected = selectedTab == 2,
            onClick = { onTabSelected(2) },
            icon = {
                Icon(
                    Icons.Outlined.AccountCircle,
                    contentDescription = "Account",
                    tint = if (selectedTab == 2) Color(0xFF3B82F6) else Color.Gray
                )
            },
            label = {
                Text(
                    "Account",
                    color = if (selectedTab == 2) Color(0xFF3B82F6) else Color.Gray,
                    fontSize = 12.sp,
                    fontWeight = if (selectedTab == 2) FontWeight.Medium else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = if (selectedTab == 2) Color.Transparent else Color.Transparent
            ),
            modifier = if (selectedTab == 2) {
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


@Preview(showBackground = true)
@Composable
fun MagicDosaScreenPreview() {
    MaterialTheme {
        HomeScreen()
    }
}