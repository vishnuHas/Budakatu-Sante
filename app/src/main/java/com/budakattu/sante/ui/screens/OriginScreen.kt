package com.budakattu.sante.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.budakattu.sante.data.model.Coop
import com.budakattu.sante.data.model.sampleCoops
import com.budakattu.sante.ui.components.BottomNavBar
import com.budakattu.sante.ui.components.GlassCard
import com.budakattu.sante.ui.navigation.Screen

@Composable
fun OriginScreen(
    userRole: String,
    onBack: () -> Unit,
    onNavigate: (String) -> Unit,
    onProductClick: (String) -> Unit
) {
    var selectedCoopKey by remember { mutableStateOf("soliga") }
    val selectedCoop = sampleCoops[selectedCoopKey]!!

    Scaffold(
        bottomBar = {
            BottomNavBar(currentRoute = Screen.Origin.route, userRole = userRole, onNavigate = onNavigate)
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF4F8F5))) {
            // Slight ambient radial glow at the top for depth
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color(0xFFDCEFE3).copy(alpha = 0.6f), Color.Transparent)
                        )
                    )
            )
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(padding)
            ) {
                // Top Bar
                OriginTopBar(onBack = onBack)

                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Spacer(modifier = Modifier.height(16.dp))

                    // Cooperative Details Card starts immediately
                    AnimatedContent(
                        targetState = selectedCoop,
                        transitionSpec = {
                            fadeIn(animationSpec = tween(400)) + scaleIn(initialScale = 0.98f) togetherWith
                                    fadeOut(animationSpec = tween(300))
                        },
                        label = "CoopDetails"
                    ) { coop ->
                        CoopDetailCard(coop = coop)
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    // Sourcing Principles
                    PrinciplesSection()

                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }
    }
}

@Composable
fun OriginTopBar(onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .size(44.dp)
                    .background(Color.White, CircleShape)
                    .border(1.dp, Color(0xFFE0E0E0), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack, 
                    contentDescription = "Back",
                    tint = Color(0xFF006D36)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column {
                Text(
                    text = "Cooperative Origins",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF161C22),
                    fontSize = 20.sp
                )
                Text(
                    text = "BUDAKATTU SANTE WESTERN GHATS MAP",
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp,
                    color = Color(0xFF444748).copy(alpha = 0.6f)
                )
            }
        }

        Box(
            modifier = Modifier
                .size(44.dp)
                .background(Color.White, CircleShape)
                .border(1.dp, Color(0xFFE0E0E0), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.NaturePeople, 
                contentDescription = null, 
                tint = Color(0xFF006D36)
            )
        }
    }
}

@Composable
fun MapSection(
    selectedCoopKey: String,
    onPinClick: (String) -> Unit
) {
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp)),
        containerAlpha = 0.65f,
        borderAlpha = 0.4f
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "WESTERN GHATS BIOSPERE RESERVE",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary,
                letterSpacing = 2.sp
            )
            Text(
                text = "Interactive Origin Hotspots",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Tap a location pin below to explore the indigenous harvesting communities.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Map Canvas
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.6f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                    .border(1.dp, Color.White.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
            ) {
                // Mock topographical background (using a gradient pattern for now)
                Box(modifier = Modifier.fillMaxSize()) {
                    // This would ideally be a custom canvas drawing or an image
                    AsyncImage(
                        model = "https://lh3.googleusercontent.com/aida-public/AB6AXuAY6FjE6kVVlzCcqey3gEdt4-_VOyMn18qToHy1tUXy8eDKQX8u6i8V7s9YxUf-wtMktI6-oLB3NrwESO4Nbl_72oPhwOOzejF8C4bhld_xbxfrsgY1KPIDJAPB8zEEN1n5zOuBFKwnukRDf9fcSV6lZRwzc3ZnUDZvb8GXsyM8Y90_4lMcVpk5COwwKh9iIUPTNZmx7Cgkk2tfpIBEWBEYA5NqQHhDMZR-RjPi_zra3ax-xUZyqopcdpzl4sCcdpEQ9inK6JSmIWtd",
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize().scale(1.2f).clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop,
                        alpha = 0.25f
                    )
                }
                
                // Hotspot Pins
                MapPin(
                    modifier = Modifier.align(Alignment.TopCenter).padding(top = 40.dp),
                    label = "Soliga Tribe",
                    isSelected = selectedCoopKey == "soliga",
                    onClick = { onPinClick("soliga") }
                )
                
                MapPin(
                    modifier = Modifier.align(Alignment.CenterStart).padding(start = 60.dp),
                    label = "Irula Co-op",
                    isSelected = selectedCoopKey == "irula",
                    onClick = { onPinClick("irula") }
                )
            }
        }
    }
}

@Composable
fun MapPin(
    modifier: Modifier = Modifier,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition()
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.75f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Column(
        modifier = modifier.clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(contentAlignment = Alignment.Center) {
            // Pulse effect
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .scale(pulseScale)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondary.copy(alpha = pulseAlpha))
            )
            // Inner pin
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) MaterialTheme.colorScheme.secondary else Color.White)
                    .border(2.dp, if (isSelected) Color.White else MaterialTheme.colorScheme.secondary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Verified,
                    contentDescription = null,
                    modifier = Modifier.size(12.dp),
                    tint = if (isSelected) Color.White else MaterialTheme.colorScheme.secondary
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Surface(
            color = Color.White.copy(alpha = 0.9f),
            shape = CircleShape,
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.6f)),
            shadowElevation = 4.dp
        ) {
            Text(
                text = label,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                style = MaterialTheme.typography.labelSmall,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun CoopDetailCard(coop: Coop) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.5.dp, Color(0xFF006D36), RoundedCornerShape(28.dp)),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Main Image with Floating Location Overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(20.dp))
            ) {
                AsyncImage(
                    model = coop.image,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                
                // Forest overlay gradient
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.2f)),
                                startY = 400f
                            )
                        )
                )

                // Location Badge Overlay inside Image
                Surface(
                    color = Color.White.copy(alpha = 0.95f),
                    shape = CircleShape,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Text(
                        text = coop.location,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 10.sp,
                        color = Color(0xFF006D36)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))

            // Textual Content Body
            Column(modifier = Modifier.padding(horizontal = 4.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Verified, 
                        contentDescription = null, 
                        tint = Color(0xFF006D36), 
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = coop.type.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF006D36),
                        letterSpacing = 1.sp
                    )
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = coop.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF161C22),
                    fontSize = 28.sp
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = coop.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF444748).copy(alpha = 0.85f),
                    lineHeight = 22.sp,
                    fontWeight = FontWeight.Normal
                )
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // Grid Stats
                Row(
                    modifier = Modifier.fillMaxWidth(), 
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    InfoTag(
                        icon = Icons.Default.Groups, 
                        label = "Impact", 
                        value = coop.families, 
                        modifier = Modifier.weight(1f)
                    )
                    InfoTag(
                        icon = Icons.Default.Eco, 
                        label = "Sourcing", 
                        value = coop.method, 
                        modifier = Modifier.weight(1f)
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Final Green Action Button
                Button(
                    onClick = { /* Browse */ },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF006D36),
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = coop.btnText, 
                        fontSize = 16.sp, 
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun InfoTag(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        color = Color(0xFFE8F5E9).copy(alpha = 0.7f),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color(0xFFC8E6C9).copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp), 
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon, 
                contentDescription = null, 
                tint = Color(0xFF006D36), 
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = label, 
                    style = MaterialTheme.typography.labelSmall, 
                    fontSize = 10.sp, 
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF444748).copy(alpha = 0.7f)
                )
                Text(
                    text = value, 
                    style = MaterialTheme.typography.bodyMedium, 
                    fontWeight = FontWeight.ExtraBold, 
                    fontSize = 13.sp,
                    color = Color(0xFF006D36)
                )
            }
        }
    }
}

@Composable
fun PrinciplesSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = "ECOSYSTEM SAFEGUARDS",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF006D36),
                letterSpacing = 1.5.sp
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = "Forest Sourcing Guidelines",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 22.sp,
                color = Color(0xFF161C22)
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            val principles = listOf(
                Triple(
                    Icons.Default.CurrencyRupee, 
                    "Direct-to-Coop Sourcing", 
                    "No middlemen involved. 100% of purchase payments go directly to local cooperative bank accounts."
                ),
                Triple(
                    Icons.Default.Eco, 
                    "No-Harm Sourcing", 
                    "Sourcing methods preserve native biosystems, respecting wild bee migrations and botanical health."
                ),
                Triple(
                    Icons.Default.FavoriteBorder, 
                    "Heritage Sourcing Preservation", 
                    "Indigenous harvesting methods passed through generations are preserved and duly celebrated."
                )
            )
            
            principles.forEachIndexed { index, (icon, title, desc) ->
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE8F5E9)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = icon, 
                                contentDescription = null, 
                                modifier = Modifier.size(20.dp), 
                                tint = Color(0xFF006D36)
                            )
                        }
                        
                        Spacer(modifier = Modifier.width(16.dp))
                        
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = title, 
                                style = MaterialTheme.typography.bodyLarge, 
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF161C22)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = desc, 
                                style = MaterialTheme.typography.bodyMedium, 
                                color = Color(0xFF444748).copy(alpha = 0.8f), 
                                lineHeight = 20.sp,
                                fontWeight = FontWeight.Normal
                            )
                        }
                    }
                    
                    if (index < principles.size - 1) {
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }
        }
    }
}
