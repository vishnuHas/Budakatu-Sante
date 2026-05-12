package com.budakattu.sante.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.budakattu.sante.data.model.*
import com.budakattu.sante.ui.components.GlassCard
import com.budakattu.sante.ui.navigation.Screen
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackingScreen(
    onBack: () -> Unit,
    onNavigate: (String) -> Unit
) {
    var currentOrderIndex by remember { mutableStateOf(0) }
    val orders = sampleOrders
    val activeOrder = orders.getOrNull(currentOrderIndex) ?: orders[0]
    var showSupportModal by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Cinematic Hero Map Background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(442.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            AsyncImage(
                model = "https://lh3.googleusercontent.com/aida-public/AB6AXuAY6FjE6kVVlzCcqey3gEdt4-_VOyMn18qToHy1tUXy8eDKQX8u6i8V7s9YxUf-wtMktI6-oLB3NrwESO4Nbl_72oPhwOOzejF8C4bhld_xbxfrsgY1KPIDJAPB8zEEN1n5zOuBFKwnukRDf9fcSV6lZRwzc3ZnUDZvb8GXsyM8Y90_4lMcVpk5COwwKh9iIUPTNZmx7Cgkk2tfpIBEWBEYA5NqQHhDMZR-RjPi_zra3ax-xUZyqopcdpzl4sCcdpEQ9inK6JSmIWtd",
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.5f),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.1f),
                                Color.White.copy(alpha = 0.9f)
                            )
                        )
                    )
            )
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "Tracking",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = onBack,
                            modifier = Modifier
                                .size(48.dp)
                                .background(Color.White.copy(alpha = 0.6f), CircleShape)
                                .border(1.dp, Color.White.copy(alpha = 0.4f), CircleShape)
                        ) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = { showSupportModal = true },
                            modifier = Modifier
                                .size(48.dp)
                                .background(Color.White.copy(alpha = 0.6f), CircleShape)
                                .border(1.dp, Color.White.copy(alpha = 0.4f), CircleShape)
                        ) {
                            Icon(Icons.Default.SupportAgent, contentDescription = "Support")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            },
            containerColor = Color.Transparent
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(padding)
                    .padding(horizontal = 24.dp)
            ) {
                Spacer(modifier = Modifier.height(140.dp))

                // Order Selector
                if (orders.size > 1) {
                    Text(
                        "Active Orders",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 8.dp, bottom = 12.dp)
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        for (index in orders.indices) {
                            item {
                                val order = orders[index]
                                OrderItemSelector(
                                    isActive = index == currentOrderIndex,
                                    orderId = order.id,
                                    onClick = { currentOrderIndex = index }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }

                // Tracking ETA Glass Card
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(32.dp),
                    containerAlpha = 0.7f,
                    borderAlpha = 0.5f
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column {
                                Text(
                                    "Estimated Arrival",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.secondary,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    text = if (activeOrder.status == "Delivered") "Arrived" else "Tomorrow",
                                    style = MaterialTheme.typography.headlineLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "by 4:30 PM",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Surface(
                                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f),
                                shape = CircleShape,
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f))
                            ) {
                                Text(
                                    text = "#${activeOrder.id}",
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                        }
                        Divider(modifier = Modifier.padding(vertical = 16.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                modifier = Modifier.size(40.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = CircleShape
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                                }
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text("Home Delivery Address", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                                Text(activeOrder.deliveryAddress, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Journey Timeline Card
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(32.dp),
                    containerAlpha = 0.5f,
                    borderAlpha = 0.4f
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text("Journey", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        TrackingTimeline(activeOrder.status)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Package Contents
                Text(
                    "Package Contents",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 12.dp)
                )
                for (item in activeOrder.items) {
                    GlassCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        shape = RoundedCornerShape(20.dp),
                        containerAlpha = 0.4f,
                        borderAlpha = 0.3f
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            val product = sampleProducts.find { it.id == item.productId }
                            AsyncImage(
                                model = product?.image ?: "raw_forest_honey.png",
                                contentDescription = item.title,
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(RoundedCornerShape(12.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(item.title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                                Text("${item.quantity}x • ₹${item.price} / unit", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }

    if (showSupportModal) {
        SupportChatModal(
            orderId = activeOrder.id,
            onDismiss = { showSupportModal = false }
        )
    }
}

@Composable
fun TrackingTimeline(status: String) {
    val steps = listOf("Order Placed", "Collected from Origin", "Quality Checked & Packaged", "In Transit", "Delivered")
    val statusIndex = steps.indexOf(status).let { if (it == -1) steps.indexOf("Shipped").takeIf { s -> s != -1 } ?: 0 else it }
    
    // Status normalization for display
    val normalizedStatusIndex = when(status) {
        "Processing" -> 0
        "Origin Verified" -> 1
        "Shipped" -> 3
        "Delivered" -> 4
        else -> 0
    }

    Column {
        for ((index, step) in steps.withIndex()) {
            val isCompleted = index < normalizedStatusIndex
            val isActive = index == normalizedStatusIndex
            val isLast = index == steps.size - 1
            
            TrackingStep(
                title = step,
                subtitle = when(index) {
                    0 -> "Successfully authorized & processed"
                    1 -> if (isCompleted) "Tribal Co-op, Wayanad" else "Preparing for pickup"
                    2 -> if (isCompleted) "Eco-facility Hub, Wayanad" else "Checking tribal origin stamp"
                    3 -> if (isCompleted) "Completed transit" else "Leaving Sorting Center, Mysore"
                    4 -> if (isCompleted) "Arrived safely!" else "Pending"
                    else -> ""
                },
                isCompleted = isCompleted,
                isActive = isActive,
                isLast = isLast,
                showLiveGps = isActive && index == 3
            )
        }
    }
}

@Composable
fun TrackingStep(
    title: String,
    subtitle: String,
    isCompleted: Boolean,
    isActive: Boolean,
    isLast: Boolean,
    showLiveGps: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(32.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(
                        if (isCompleted) MaterialTheme.colorScheme.secondary 
                        else if (isActive) Color.White 
                        else MaterialTheme.colorScheme.surfaceVariant,
                        CircleShape
                    )
                    .border(
                        2.dp, 
                        if (isCompleted || isActive) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f), 
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isCompleted) {
                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(12.dp), tint = Color.White)
                } else if (isActive) {
                    Box(modifier = Modifier.size(10.dp).background(MaterialTheme.colorScheme.secondary, CircleShape))
                }
            }
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(2.dp)
                        .background(
                            if (isCompleted) MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f) 
                            else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                        )
                )
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.padding(bottom = 24.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = if (isActive) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (showLiveGps) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
                    shape = CircleShape,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Sensors, contentDescription = null, size = 16.dp, tint = MaterialTheme.colorScheme.secondary)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            "Live GPS Active",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.secondary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SupportChatModal(
    orderId: String,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
                .clickable { onDismiss() },
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(600.dp)
                    .clickable(enabled = false) {},
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.SupportAgent, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Support Agent", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Text("Online • Replies in 2m", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, contentDescription = "Close")
                        }
                    }
                    
                    // Chat Body
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .background(Color(0xFFF8FAFC))
                            .padding(24.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        ChatBubble(
                            message = "Hello! How can I help you with your order #$orderId today?",
                            isSupport = true,
                            time = "14:02"
                        )
                        ChatBubble(
                            message = "I want to know the exact delivery time.",
                            isSupport = false,
                            time = "14:05"
                        )
                        ChatBubble(
                            message = "Sure! Your package is currently at the Bangalore distribution center. It is expected to arrive tomorrow by 4:30 PM.",
                            isSupport = true,
                            time = "Just now"
                        )
                    }
                    
                    // Input Area
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TextField(
                            value = "",
                            onValueChange = {},
                            placeholder = { Text("Type a message...", fontSize = 14.sp) },
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp),
                            shape = CircleShape,
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFFF1F5F9),
                                unfocusedContainerColor = Color(0xFFF1F5F9),
                                disabledContainerColor = Color(0xFFF1F5F9),
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            )
                        )
                        IconButton(
                            onClick = {},
                            modifier = Modifier
                                .size(48.dp)
                                .background(MaterialTheme.colorScheme.secondary, CircleShape)
                        ) {
                            Icon(Icons.Default.Send, contentDescription = "Send", tint = Color.White, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ChatBubble(message: String, isSupport: Boolean, time: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isSupport) Alignment.Start else Alignment.End
    ) {
        Surface(
            color = if (isSupport) Color.White else MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
            shape = RoundedCornerShape(
                topStart = if (isSupport) 0.dp else 20.dp,
                topEnd = if (isSupport) 20.dp else 0.dp,
                bottomEnd = 20.dp,
                bottomStart = 20.dp
            ),
            border = BorderStroke(1.dp, if (isSupport) Color(0xFFF1F5F9) else MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)),
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Text(
                text = message,
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Text(
            text = time,
            style = MaterialTheme.typography.labelSmall,
            color = Color.LightGray,
            modifier = Modifier.padding(top = 4.dp, start = if (isSupport) 4.dp else 0.dp, end = if (!isSupport) 4.dp else 0.dp)
        )
    }
}

@Composable
private fun Icon(imageVector: androidx.compose.ui.graphics.vector.ImageVector, contentDescription: String?, size: androidx.compose.ui.unit.Dp, tint: Color) {
    Icon(
        imageVector = imageVector,
        contentDescription = contentDescription,
        modifier = Modifier.size(size),
        tint = tint
    )
}
@Composable
fun OrderItemSelector(isActive: Boolean, orderId: String, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        color = if (isActive) MaterialTheme.colorScheme.secondary else Color.White.copy(alpha = 0.5f),
        shape = CircleShape,
        border = BorderStroke(1.dp, if (isActive) MaterialTheme.colorScheme.secondary else Color.White.copy(alpha = 0.4f)),
        modifier = Modifier.animateContentSize()
    ) {
        Text(
            text = "#$orderId",
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = if (isActive) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
