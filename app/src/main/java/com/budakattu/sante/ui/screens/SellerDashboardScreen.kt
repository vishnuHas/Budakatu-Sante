package com.budakattu.sante.ui.screens

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date
import kotlinx.coroutines.launch
import android.net.Uri
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.budakattu.sante.data.model.Order
import com.budakattu.sante.data.model.VerificationRequest
import com.budakattu.sante.data.model.sampleVerifications
import com.budakattu.sante.ui.navigation.Screen
import com.budakattu.sante.ui.components.BottomNavBar
import com.budakattu.sante.ui.components.GlassCard

@Composable
fun SellerDashboardScreen(
    viewModel: SellerViewModel,
    onNavigate: (String) -> Unit
) {
    val orders by viewModel.orders.collectAsState()
    
    val activeOrdersCount = orders.count { it.status != "Delivered" }
    
    var showAddProductModal by remember { mutableStateOf(false) }

    if (showAddProductModal) {
        AddProductDialog(onDismiss = { showAddProductModal = false })
    }

    Scaffold(
        bottomBar = {
            BottomNavBar(currentRoute = Screen.SellerDashboard.route, userRole = "seller", onNavigate = onNavigate)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8FAF9)) // Aesthetic off-white background
                .padding(padding)
        ) {
            // Top App Bar
            SellerTopAppBar(onNavigate = onNavigate)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp)
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                // Dashboard Header
                Text(
                    text = "Market Overview",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Real-time performance and sustainability metrics.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { showAddProductModal = true },
                        modifier = Modifier.weight(1.2f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006D36)),
                        shape = RoundedCornerShape(12.dp), // Slightly less rounded, tighter
                        contentPadding = PaddingValues(vertical = 14.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Add New Product", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    OutlinedButton(
                        onClick = { /* Export */ },
                        modifier = Modifier.weight(1f),
                        border = BorderStroke(1.dp, Color(0xFF006D36).copy(alpha = 0.3f)),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF006D36)),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(vertical = 14.dp)
                    ) {
                        Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Export", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Metrics Bento Grid (Stacked for Mobile as per image)
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Revenue Card
                    MetricCard(
                        title = "TOTAL TRADING VOLUME",
                        value = "$2.4M", // Screenshot uses $2.4M
                        trend = "+14.5% this quarter",
                        icon = Icons.Default.ShowChart,
                        modifier = Modifier.fillMaxWidth(),
                        showBars = true // Custom addition to show graphical data
                    )
                    // Impact Card
                    MetricCard(
                        title = "FOREST AREA PROTECTED",
                        value = "14,200",
                        subText = "hectares",
                        progress = 0.75f,
                        icon = Icons.Default.Public,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Pending Verifications
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Pending Origin Verification",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    TextButton(onClick = { /* View All */ }) {
                        Text("View All", color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
                
                sampleVerifications.forEach { request ->
                    VerificationItem(request)
                    Spacer(modifier = Modifier.height(12.dp))
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Live Orders
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocalShipping, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Live Customer Orders",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Surface(
                        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
                        shape = CircleShape
                    ) {
                        Text(
                            text = "$activeOrdersCount Active",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            color = MaterialTheme.colorScheme.secondary,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Show ALL orders now to show delivered states too, matching screenshot
                orders.forEach { order ->
                    OrderItem(
                        order = order,
                        onAdvanceStatus = { viewModel.advanceOrderStatus(order.id, order.status) }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

@Composable
fun SellerTopAppBar(onNavigate: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(Color.White.copy(alpha = 0.6f))
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Budakattu-Sante",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(8.dp))
            Surface(
                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
                shape = CircleShape
            ) {
                Text(
                    text = "SELLER",
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp
                )
            }
        }

        IconButton(
            onClick = { onNavigate(Screen.Notifications.route) },
            modifier = Modifier
                .size(40.dp)
                .background(Color.White.copy(alpha = 0.4f), CircleShape)
                .border(1.dp, Color.White.copy(alpha = 0.4f), CircleShape)
        ) {
            Icon(Icons.Default.Notifications, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
        }
    }
}

@Composable
fun MetricCard(
    title: String,
    value: String,
    subText: String? = null,
    trend: String? = null,
    progress: Float? = null,
    showBars: Boolean = false,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    GlassCard(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        containerAlpha = 0.6f,
        borderAlpha = 0.1f
    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(if(showBars) 220.dp else 180.dp)
        ) {
            // Background gradient / subtle glow
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.secondary.copy(alpha = 0.03f), 
                                Color.White.copy(alpha = 0.2f)
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            letterSpacing = 1.sp,
                            fontSize = 10.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = value,
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 36.sp
                            )
                            if (subText != null) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = subText,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(bottom = 6.dp)
                                )
                            }
                        }
                        if (trend != null) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.TrendingUp, 
                                    contentDescription = null, 
                                    modifier = Modifier.size(14.dp), 
                                    tint = MaterialTheme.colorScheme.secondary
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    trend, 
                                    style = MaterialTheme.typography.labelSmall, 
                                    color = MaterialTheme.colorScheme.secondary, 
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                    
                    // Icon Circle
                    Surface(
                        modifier = Modifier.size(40.dp),
                        shape = CircleShape,
                        color = Color.White,
                        shadowElevation = 1.dp,
                        border = BorderStroke(1.dp, Color(0xFFF5F5F5))
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = icon, 
                                contentDescription = null, 
                                modifier = Modifier.size(20.dp), 
                                tint = Color(0xFF006D36)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (progress != null) {
                    Column {
                        LinearProgressIndicator(
                            progress = progress,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(CircleShape),
                            color = Color(0xFF006D36),
                            trackColor = Color(0xFFE0E0E0)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "${(progress * 100).toInt()}% of Annual Goal",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.align(Alignment.End),
                            fontSize = 11.sp
                        )
                    }
                }

                if (showBars) {
                    // Graphical Bar Visualization from Image
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        val heights = listOf(0.3f, 0.5f, 0.4f, 0.7f, 0.65f, 1.0f)
                        heights.forEachIndexed { index, h ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight(h)
                                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                                    .background(
                                        if (index == heights.lastIndex) Color(0xFF006D36) // Primary dark green for current peak
                                        else Color(0xFF006D36).copy(alpha = 0.3f + (index * 0.05f)) // varying transparency
                                    )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun VerificationItem(request: VerificationRequest) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        containerAlpha = 0.65f
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                AsyncImage(
                    model = request.image,
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color.White.copy(alpha = 0.5f), CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(request.name, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                    Text(request.location, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                IconButton(
                    onClick = { /* Reject */ },
                    modifier = Modifier
                        .size(40.dp)
                        .background(MaterialTheme.colorScheme.error.copy(alpha = 0.1f), CircleShape)
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Reject", tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(20.dp))
                }
                IconButton(
                    onClick = { /* Approve */ },
                    modifier = Modifier
                        .size(40.dp)
                        .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f), CircleShape)
                ) {
                    Icon(Icons.Default.Check, contentDescription = "Approve", tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}

@Composable
fun OrderItem(order: Order, onAdvanceStatus: () -> Unit) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        containerAlpha = 1.0f,
        borderAlpha = 0.05f
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("#${order.id}", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(order.date),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Surface(
                    color = when(order.status) {
                        "Processing" -> Color(0xFFFFECB3)
                        "Shipped" -> Color(0xFFE1BEE7)
                        else -> Color(0xFFC8E6C9)
                    },
                    shape = CircleShape,
                    border = BorderStroke(1.dp, Color.Black.copy(alpha = 0.05f))
                ) {
                    Text(
                        text = order.status,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = when(order.status) {
                            "Processing" -> Color(0xFF5D4037)
                            "Shipped" -> Color(0xFF4A148C)
                            else -> Color(0xFF1B5E20)
                        }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        text = order.items.joinToString { it.title },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text("₹${order.total}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                }
                
                if (order.status == "Delivered") {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF006D36), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Delivered Safely", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF006D36))
                    }
                } else {
                    Button(
                        onClick = onAdvanceStatus,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006D36)),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = when(order.status) {
                                "Processing" -> "Verify Origin"
                                else -> "Deliver Order"
                            },
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductDialog(onDismiss: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var qualityBadge by remember { mutableStateOf("Certified Organic") }
    var description by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var story by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    
    // Media Launchers
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imageUrl = it.toString() // In production we would upload this
        }
    }
    
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        bitmap?.let {
            imageUrl = "Captured Local Bitmap" // Placeholder for demo
        }
    }
    
    var isEnhancing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    // Simulated AI enhancement logic
    fun enhanceContent() {
        if (name.isEmpty() && description.isEmpty()) return
        isEnhancing = true
        // Run simple delay to simulate LLM inference
        scope.launch {
            kotlinx.coroutines.delay(1500)
            if (name.isNotEmpty()) {
                val adjectives = listOf("Wild-Harvested", "Authentic", "Pristine Forest", "Heritage", "Hand-Gathered")
                if (!name.contains(adjectives[0])) name = "${adjectives.random()} $name"
            }
            if (description.isNotEmpty()) {
                description = "Experience the pure essence of our forests. $description \n\nThis product is sustainably sourced by local tribal artisans, maintaining ecological harmony and preserving ancient harvesting traditions. Every purchase directly supports regenerative forest conservation."
            }
            isEnhancing = false
        }
    }

    androidx.compose.ui.window.Dialog(
        onDismissRequest = onDismiss,
        properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFFF8FAF9)
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Add New Product", fontWeight = FontWeight.Bold) },
                        navigationIcon = {
                            IconButton(onClick = onDismiss) {
                                Icon(Icons.Default.Close, contentDescription = "Close")
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                    )
                },
                bottomBar = {
                    Surface(
                        color = Color.White,
                        shadowElevation = 8.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = onDismiss, // Simulate Success
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006D36))
                        ) {
                            Text("List Product", modifier = Modifier.padding(8.dp), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState())
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // AI Banner
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                        border = BorderStroke(1.dp, Color(0xFF006D36).copy(alpha = 0.2f))
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color(0xFF006D36))
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text("AI Enhancer Enabled", fontWeight = FontWeight.Bold, color = Color(0xFF1B5E20))
                                Text("Automatically generate rich, story-driven descriptions.", fontSize = 12.sp, color = Color(0xFF2E7D32))
                            }
                            if (isEnhancing) {
                                Text("Enhancing...", color = Color(0xFF006D36), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            } else {
                                Button(
                                    onClick = { enhanceContent() },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006D36)),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                                ) {
                                    Text("Enhance", fontSize = 11.sp)
                                }
                            }
                        }
                    }

                    Text("Product Details", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Product Name") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        trailingIcon = {
                            IconButton(onClick = { /* Trigger Voice */ }) {
                                Icon(Icons.Default.Mic, contentDescription = "Speak", tint = Color.Gray)
                            }
                        }
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OutlinedTextField(
                            value = price,
                            onValueChange = { price = it },
                            label = { Text("Price (₹)") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        OutlinedTextField(
                            value = stock,
                            onValueChange = { stock = it },
                            label = { Text("Stock Level") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    OutlinedTextField(
                        value = category,
                        onValueChange = { category = it },
                        label = { Text("Category (e.g. Forest Honey, Crafts)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = qualityBadge,
                        onValueChange = { qualityBadge = it },
                        label = { Text("Quality Badge") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Product Description") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        shape = RoundedCornerShape(12.dp),
                        maxLines = 5,
                        trailingIcon = {
                            Column {
                                IconButton(onClick = { /* Trigger Voice */ }) {
                                    Icon(Icons.Default.Mic, contentDescription = "Speak", tint = Color.Gray)
                                }
                            }
                        }
                    )

                    Text("Origin & Traceability", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)

                    OutlinedTextField(
                        value = location,
                        onValueChange = { location = it },
                        label = { Text("Source Address / Location") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color(0xFF006D36)) },
                        trailingIcon = {
                             IconButton(onClick = { /* Locate */ }) {
                                Icon(Icons.Default.MyLocation, contentDescription = "Detect location", tint = Color.Gray)
                            }
                        }
                    )

                    OutlinedTextField(
                        value = story,
                        onValueChange = { story = it },
                        label = { Text("Sourcing Story") },
                        placeholder = { Text("Share the tradition behind this harvest...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        shape = RoundedCornerShape(12.dp),
                        maxLines = 4
                    )

                    Text("Visuals", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)

                    OutlinedTextField(
                        value = imageUrl,
                        onValueChange = { imageUrl = it },
                        label = { Text("Product Image URL") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = { galleryLauncher.launch("image/*") },
                            modifier = Modifier
                                .weight(1f)
                                .height(80.dp),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, Color.Gray.copy(alpha = 0.5f))
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.PhotoLibrary, contentDescription = null)
                                Text("From Gallery", fontSize = 12.sp)
                            }
                        }
                        OutlinedButton(
                            onClick = { cameraLauncher.launch(null) },
                            modifier = Modifier
                                .weight(1f)
                                .height(80.dp),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, Color.Gray.copy(alpha = 0.5f))
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.PhotoCamera, contentDescription = null)
                                Text("Capture New", fontSize = 12.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}
