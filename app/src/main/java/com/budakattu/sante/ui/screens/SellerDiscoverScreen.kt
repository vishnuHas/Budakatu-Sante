package com.budakattu.sante.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.border
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.budakattu.sante.data.model.Product
import com.budakattu.sante.data.model.sampleProducts
import com.budakattu.sante.ui.components.BottomNavBar
import com.budakattu.sante.ui.components.GlassCard
import com.budakattu.sante.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellerDiscoverScreen(
    viewModel: SellerViewModel,
    onNavigate: (String) -> Unit
) {
    val products by viewModel.products.collectAsState()
    // Load all items into a mutable local list for visual deletion/management
    val localProductList = remember { mutableStateListOf<Product>().apply { addAll(sampleProducts) } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
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
                                text = "DISCOVER",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                color = MaterialTheme.colorScheme.secondary,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = { onNavigate(Screen.Notifications.route) }) {
                        Icon(Icons.Filled.Notifications, contentDescription = "Notifications", tint = MaterialTheme.colorScheme.secondary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White.copy(alpha = 0.9f)
                )
            )
        },
        bottomBar = {
            BottomNavBar(currentRoute = Screen.SellerDiscover.route, userRole = "seller", onNavigate = onNavigate)
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp)
        ) {
            item {
                Text(
                    text = "Seller Trade Portal",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
            
            item {
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    placeholder = { Text("Discover resources, trends, or crafts...") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White.copy(alpha = 0.6f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.6f),
                        focusedBorderColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                        unfocusedBorderColor = Color.White.copy(alpha = 0.5f)
                    )
                )
                Spacer(modifier = Modifier.height(32.dp))
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Global Product Feed",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Surface(
                        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
                        shape = CircleShape
                    ) {
                        Text(
                            text = "${localProductList.size} Items",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            color = MaterialTheme.colorScheme.secondary,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            items(localProductList, key = { it.id }) { product ->
                ProductItem(
                    product = product,
                    onUpdateStock = { /* Update visually or via viewmodel if available */ },
                    onDeleteClick = { localProductList.remove(product) }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
                MarketTrendsSection()
                Spacer(modifier = Modifier.height(32.dp))
            }
            
            item {
                ArtisanGuidesSection()
                Spacer(modifier = Modifier.height(32.dp))
            }

            item {
                MarketplaceInsightsSection()
                Spacer(modifier = Modifier.height(32.dp))
            }

            item {
                CommunityStorySection()
                Spacer(modifier = Modifier.height(120.dp))
            }
        }
    }
}

@Composable
fun ProductItem(
    product: Product, 
    onUpdateStock: (Int) -> Unit,
    onDeleteClick: () -> Unit
) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        containerAlpha = 0.8f,
        borderAlpha = 0.1f
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = product.image,
                contentDescription = product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Surface(
                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
                    shape = CircleShape,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f))
                ) {
                    Text(
                        text = product.badge,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = product.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "₹${product.price}", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.secondary)
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(onClick = { /* Future price edit */ }, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Filled.Edit, contentDescription = "Edit price", tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f), modifier = Modifier.size(14.dp))
                    }
                }
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            IconButton(onClick = onDeleteClick, modifier = Modifier.align(Alignment.Top)) {
                Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = Color(0xFFBA1A1A))
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("STOCK", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(Color.White.copy(alpha = 0.4f), shape = CircleShape)
                        .padding(4.dp)
                ) {
                    IconButton(onClick = { onUpdateStock(-1) }, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Filled.Remove, contentDescription = "Decrease stock", tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(16.dp))
                    }
                    Text("${product.stock}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp))
                    IconButton(onClick = { onUpdateStock(1) }, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Filled.Add, contentDescription = "Increase stock", tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}


@Composable
fun MarketTrendsSection() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Market Trends",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .clip(RoundedCornerShape(24.dp))
        ) {
            AsyncImage(
                model = "https://images.unsplash.com/photo-1587049352846-4a222e784d38?auto=format&fit=crop&q=80&w=800",
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            // Dark overlay gradient
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        androidx.compose.ui.graphics.Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)),
                            startY = 200f
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Surface(
                    color = Color(0xFFFFD54F), // Vibrant gold for trending label
                    shape = CircleShape
                ) {
                    Text(
                        text = "Trending Now",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        color = Color(0xFF423000),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "High demand for Wild Honey in Bangalore",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun ArtisanGuidesSection() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = "Artisan Guides",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "View All",
                color = Color(0xFF006D36),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            item {
                GuideCard(
                    title = "Organic Certification 101",
                    desc = "A step-by-step guide to verifying your forest products.",
                    icon = Icons.Default.Star,
                    iconBg = Color(0xFFE8F5E9),
                    iconTint = Color(0xFF006D36)
                )
            }
            item {
                GuideCard(
                    title = "Smartphone Photography",
                    desc = "Capture the beauty of your crafts with simple lighting.",
                    icon = Icons.Default.PhotoCamera,
                    iconBg = Color(0xFFFFF8E1),
                    iconTint = Color(0xFFFFA000)
                )
            }
        }
    }
}

@Composable
fun GuideCard(
    title: String,
    desc: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconBg: Color,
    iconTint: Color
) {
    GlassCard(
        modifier = Modifier.width(260.dp),
        shape = RoundedCornerShape(24.dp),
        containerAlpha = 0.9f,
        borderAlpha = 0.1f
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Surface(
                modifier = Modifier.size(48.dp),
                shape = RoundedCornerShape(16.dp),
                color = iconBg
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(24.dp))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = desc, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun MarketplaceInsightsSection() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Marketplace Insights",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        // Bento Card 1: Seasonal
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            containerAlpha = 0.9f
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Upcoming Seasonal Harvests", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                    Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f), modifier = Modifier.size(32.dp))
                }
                Spacer(modifier = Modifier.height(16.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFF1F8E9).copy(alpha = 0.5f),
                    border = BorderStroke(1.dp, Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Bamboo Shoots", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = Color(0xFF006D36))
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(8.dp).background(Color(0xFFFFA000), CircleShape))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Starts in 2 weeks", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        // Bento Card 2: Performing Categories
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            containerAlpha = 0.9f
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Top Performing Categories", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(20.dp))
                
                InsightBar("Handwoven Textiles", 0.85f, true)
                Spacer(modifier = Modifier.height(16.dp))
                InsightBar("Forest Spices", 0.60f, false)
            }
        }
    }
}

@Composable
fun InsightBar(label: String, fraction: Float, isPositive: Boolean) {
    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = if(isPositive) Color(0xFF006D36) else MaterialTheme.colorScheme.onSurfaceVariant)
            if (isPositive) {
                Icon(Icons.Default.TrendingUp, contentDescription = null, tint = Color(0xFF006D36), modifier = Modifier.size(16.dp))
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        LinearProgressIndicator(
            progress = fraction,
            modifier = Modifier.fillMaxWidth().height(6.dp).clip(CircleShape),
            color = if(isPositive) Color(0xFF006D36) else Color.LightGray,
            trackColor = Color(0xFFEEEEEE)
        )
    }
}

@Composable
fun CommunityStorySection() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Community Stories",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            containerAlpha = 0.9f
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = "https://images.unsplash.com/photo-1544005313-94ddf0286df2?auto=format&fit=crop&q=80&w=300",
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(96.dp).clip(CircleShape).border(3.dp, Color.White, CircleShape)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFA000), modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Seller Spotlight", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "How Anandi doubled her sales using the new Traceability tool.",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "\"Customers love knowing exactly which part of the forest my baskets come from. Being able to show the origin transparently changed everything.\"",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                Spacer(modifier = Modifier.height(20.dp))
                TextButton(
                    onClick = { /* Open Full Story */ }
                ) {
                    Text("Read Full Story", color = Color(0xFF006D36), fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color(0xFF006D36))
                }
            }
        }
    }
}
