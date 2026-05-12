package com.budakattu.sante.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.budakattu.sante.ui.navigation.Screen

@Composable
fun SideDrawerContent(
    currentRoute: String?,
    userName: String? = null,
    userLocation: String? = null,
    onNavigate: (String) -> Unit,
    onCloseDrawer: () -> Unit,
    onLogout: () -> Unit
) {
    val primaryGreen = Color(0xFF006D36)
    val lightMint = Color(0xFFF4F8F5)
    
    ModalDrawerSheet(
        drawerContainerColor = Color.White,
        drawerShape = RoundedCornerShape(topEnd = 0.dp, bottomEnd = 0.dp),
        modifier = Modifier.fillMaxHeight().width(300.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(lightMint)
                    .padding(top = 32.dp, bottom = 24.dp, start = 24.dp, end = 24.dp)
            ) {
                Column {
                    Text(
                        text = "Budakattu-Sante",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = primaryGreen
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "FOREST-TO-HOME",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF705D00), // Tertiary/Olive-ish tint
                        letterSpacing = 1.5.sp
                    )
                }
                
                // Close Button
                IconButton(
                    onClick = onCloseDrawer,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(40.dp)
                        .background(Color.White, CircleShape)
                        .border(1.dp, Color.Transparent, CircleShape)
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.Black.copy(alpha = 0.7f))
                }
            }
            
            Divider(color = Color(0xFFEEEEEE), thickness = 1.dp)
            
            Spacer(modifier = Modifier.height(24.dp))

            // Nav Items
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                DrawerItem(
                    icon = Icons.Default.Explore, 
                    label = "Discover Marketplace", 
                    isActive = currentRoute == Screen.Discover.route,
                    onClick = { onNavigate(Screen.Discover.route); onCloseDrawer() }
                )
                
                DrawerItem(
                    icon = Icons.Default.LocalShipping, 
                    label = "Track My Orders", 
                    isActive = currentRoute == Screen.Tracking.route,
                    onClick = { onNavigate(Screen.Tracking.route); onCloseDrawer() }
                )
                
                DrawerItem(
                    icon = Icons.Default.FavoriteBorder, 
                    label = "Favorites", 
                    isActive = currentRoute == Screen.Wishlist.route,
                    onClick = { onNavigate(Screen.Wishlist.route); onCloseDrawer() }
                )
                
                DrawerItem(
                    icon = Icons.Default.ShoppingBag, 
                    label = "Shopping Cart", 
                    isActive = currentRoute == Screen.Cart.route,
                    onClick = { onNavigate(Screen.Cart.route); onCloseDrawer() }
                )
                
                DrawerItem(
                    icon = Icons.Default.Person, 
                    label = "Account Profile", 
                    isActive = currentRoute == Screen.Account.route,
                    onClick = { onNavigate(Screen.Account.route); onCloseDrawer() }
                )
                
                DrawerItem(
                    icon = Icons.Default.Notifications, 
                    label = "Notifications", 
                    isActive = currentRoute == Screen.Notifications.route,
                    onClick = { onNavigate(Screen.Notifications.route); onCloseDrawer() }
                )
            }
            
            // Footer Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(lightMint)
                    .padding(24.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(Color(0xFFC8E6C9).copy(alpha = 0.6f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Spa,
                            contentDescription = null,
                            tint = primaryGreen,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = userName ?: "Forest Patron",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF161C22)
                        )
                        Text(
                            text = userLocation ?: "Sustainable Choice",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF444748),
                            fontSize = 11.sp
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // Logout Button
                Button(
                    onClick = onLogout,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF2E6E6), // Soft pinkish/beige
                        contentColor = Color(0xFFBA1A1A) // Error red
                    ),
                    shape = RoundedCornerShape(12.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Logout, 
                            contentDescription = null, 
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Logout Session", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun DrawerItem(
    icon: ImageVector,
    label: String,
    isActive: Boolean,
    onClick: () -> Unit
) {
    val primaryGreen = Color(0xFF006D36)
    
    Surface(
        onClick = onClick,
        color = if (isActive) Color(0xFFE8F5E9).copy(alpha = 0.7f) else Color.Transparent,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .border(
                width = 1.dp,
                color = if (isActive) Color(0xFFC8E6C9).copy(alpha = 0.5f) else Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isActive) primaryGreen else Color(0xFF006D36), // Same primaryGreen as screenshot
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = if (isActive) FontWeight.ExtraBold else FontWeight.Bold,
                color = if (isActive) primaryGreen else Color(0xFF444748)
            )
        }
    }
}
