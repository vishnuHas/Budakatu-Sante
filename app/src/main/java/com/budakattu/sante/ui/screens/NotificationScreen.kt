package com.budakattu.sante.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.budakattu.sante.data.model.Notification
import com.budakattu.sante.data.model.NotificationType
import com.budakattu.sante.data.model.sampleNotifications
import com.budakattu.sante.ui.components.BottomNavBar
import com.budakattu.sante.ui.components.GlassCard
import com.budakattu.sante.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    userRole: String = "buyer",
    onBack: () -> Unit,
    onNavigate: (String) -> Unit
) {
    val notifications = remember { mutableStateListOf(*sampleNotifications.toTypedArray()) }
    
    Scaffold(
        bottomBar = {
            BottomNavBar(
                currentRoute = Screen.Notifications.route,
                onNavigate = onNavigate,
                userRole = userRole
            )
        },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Budakattu-Sante",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111827)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .size(40.dp)
                            .background(Color.White.copy(alpha = 0.8f), CircleShape)
                            .border(1.dp, Color.White, CircleShape)
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF006D36),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        containerColor = Color.Transparent
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFF3FDF5), // Light greenish white
                            Color.White
                        )
                    )
                )
        ) {
            // Decorative decorative blur circles mimicking radial gradients
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(250.dp)
                    .offset(x = 80.dp, y = (-80).dp)
                    .background(Color(0xFF83FBA5).copy(alpha = 0.15f), CircleShape)
            )
            
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .size(250.dp)
                    .offset(x = (-100).dp, y = 100.dp)
                    .background(Color(0xFFFFE16D).copy(alpha = 0.1f), CircleShape)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 24.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Notifications",
                                style = MaterialTheme.typography.displaySmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF111827)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Stay updated with your forest journey.",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color(0xFF6B7280),
                                fontWeight = FontWeight.Medium
                            )
                        }
                        
                        Spacer(modifier = Modifier.width(12.dp))
                        
                        Surface(
                            onClick = {
                                val updated = notifications.map { it.copy(isRead = true) }
                                notifications.clear()
                                notifications.addAll(updated)
                            },
                            shape = CircleShape,
                            color = Color(0xFFE8F5E9),
                            border = BorderStroke(1.dp, Color(0xFF006D36).copy(alpha = 0.1f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.DoneAll,
                                    contentDescription = null,
                                    tint = Color(0xFF006D36),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    "Mark all read",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFF006D36),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(36.dp))
                }

                val groups = notifications.groupBy { it.type }
                
                groups.forEach { (type, typeNotifications) ->
                    item {
                        Text(
                            text = when(type) {
                                NotificationType.ORDER_UPDATE -> "ORDER UPDATES"
                                NotificationType.TRIBAL_STORY -> "TRIBAL STORIES"
                                NotificationType.SYSTEM_ALERT -> "SYSTEM ALERTS"
                                NotificationType.PROMOTION -> "PROMOTIONS"
                            },
                            fontSize = 11.sp,
                            letterSpacing = 0.5.sp,
                            color = Color(0xFF4B5563),
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(bottom = 16.dp, top = 12.dp)
                        )
                    }
                    
                    items(typeNotifications) { notification ->
                        NotificationCard(
                            notification = notification,
                            onClick = {
                                val index = notifications.indexOfFirst { it.id == notification.id }
                                if (index != -1) {
                                    notifications[index] = notifications[index].copy(isRead = true)
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    
                    item { Spacer(modifier = Modifier.height(16.dp)) }
                }
                
                item {
                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }
    }
}

@Composable
fun NotificationCard(
    notification: Notification,
    onClick: () -> Unit
) {
    val isStory = notification.type == NotificationType.TRIBAL_STORY
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .alpha(if (notification.isRead) 0.85f else 1f),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.7f)),
        border = BorderStroke(1.dp, Color.White)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box {
                if (notification.imageUrl != null) {
                    AsyncImage(
                        model = notification.imageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(if (isStory) 72.dp else 60.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    val bgColor = when (notification.type) {
                        NotificationType.ORDER_UPDATE -> Color(0xFFFFF8E1)
                        NotificationType.TRIBAL_STORY -> Color(0xFFE8F5E9)
                        NotificationType.SYSTEM_ALERT -> Color(0xFFEEEEEE)
                        NotificationType.PROMOTION -> Color(0xFFE8F5E9)
                    }
                    
                    val iconTint = when (notification.type) {
                        NotificationType.ORDER_UPDATE -> Color(0xFFFFA000)
                        NotificationType.TRIBAL_STORY -> Color(0xFF006D36)
                        NotificationType.SYSTEM_ALERT -> Color(0xFF666666)
                        NotificationType.PROMOTION -> Color(0xFF006D36)
                    }

                    // If specific message content indicates success, override to checkmark color logic
                    val isSuccess = notification.title.contains("Delivered") || notification.title.contains("Success")
                    val finalBg = if(isSuccess) Color(0xFFE8F5E9) else bgColor
                    val finalTint = if(isSuccess) Color(0xFF006D36) else iconTint
                    val iconVec = if(isSuccess) Icons.Default.CheckCircle else when (notification.type) {
                        NotificationType.ORDER_UPDATE -> Icons.Default.LocalShipping
                        NotificationType.TRIBAL_STORY -> Icons.Default.MenuBook
                        NotificationType.SYSTEM_ALERT -> Icons.Default.CloudSync
                        NotificationType.PROMOTION -> Icons.Default.Celebration
                    }

                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(finalBg, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = iconVec,
                            contentDescription = null,
                            tint = finalTint,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
                
                if (!notification.isRead) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .align(Alignment.TopEnd)
                            .offset(x = 4.dp, y = (-4).dp)
                            .background(Color(0xFFFFC107), CircleShape)
                            .border(2.dp, Color.White, CircleShape)
                    )
                }
            }
            
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = notification.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111827),
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = notification.time,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF9CA3AF),
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = notification.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF4B5563),
                    lineHeight = 20.sp
                )
                
                if (notification.extraInfo != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        color = Color(0xFFF9FAFB),
                        shape = CircleShape,
                        border = BorderStroke(1.dp, Color(0xFFE5E7EB))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${notification.extraInfo.substringBefore(":")}: ",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF6B7280),
                                letterSpacing = 0.5.sp
                            )
                            Text(
                                text = notification.extraInfo.substringAfter(":").trim(),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF111827),
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                }
                
                if (notification.type == NotificationType.TRIBAL_STORY) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Read Story",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF006D36)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            Icons.Default.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = Color(0xFF006D36)
                        )
                    }
                }
            }
        }
    }
}

