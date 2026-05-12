package com.budakattu.sante.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.shadow
import com.budakattu.sante.data.CartManager

@Composable
fun BottomNavBar(
    currentRoute: String?,
    userRole: String = "buyer",
    onNavigate: (String) -> Unit
) {
    val cartItems by CartManager.cartItems.collectAsState()
    val cartCount = cartItems.sumOf { it.quantity }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp, start = 24.dp, end = 24.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(76.dp)
                .shadow(elevation = 16.dp, shape = CircleShape, clip = false)
                .clip(CircleShape)
                .background(Color.White)
                .border(1.dp, Color(0xFFF1F3F4), CircleShape)
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (userRole == "seller") {
                // Seller Nav
                NavItem(
                    icon = Icons.Default.Explore,
                    label = "Trade",
                    active = currentRoute == Screen.SellerDiscover.route,
                    onClick = { onNavigate(Screen.SellerDiscover.route) }
                )
                NavItem(
                    icon = Icons.Default.Dashboard,
                    label = "Dashboard",
                    active = currentRoute == Screen.SellerDashboard.route,
                    onClick = { onNavigate(Screen.SellerDashboard.route) }
                )
                NavItem(
                    icon = Icons.Default.NaturePeople,
                    label = "Origin",
                    active = currentRoute == Screen.Origin.route,
                    onClick = { onNavigate(Screen.Origin.route) }
                )
                NavItem(
                    icon = Icons.Default.Person,
                    label = "Account",
                    active = currentRoute == Screen.Account.route,
                    onClick = { onNavigate(Screen.Account.route) }
                )
            } else {
                // Buyer Nav (Updated to 4 items matching target design)
                NavItem(
                    icon = Icons.Default.Explore,
                    label = "Discover",
                    active = currentRoute == Screen.Discover.route,
                    onClick = { onNavigate(Screen.Discover.route) }
                )
                NavItem(
                    icon = Icons.Default.FavoriteBorder,
                    label = "Favorites",
                    active = currentRoute == Screen.Wishlist.route,
                    onClick = { onNavigate(Screen.Wishlist.route) }
                )
                NavItem(
                    icon = Icons.Default.NaturePeople,
                    label = "Origin",
                    active = currentRoute == Screen.Origin.route,
                    onClick = { onNavigate(Screen.Origin.route) }
                )
                NavItem(
                    icon = Icons.Default.ShoppingBag,
                    label = "Cart",
                    active = currentRoute == Screen.Cart.route,
                    onClick = { onNavigate(Screen.Cart.route) },
                    badgeCount = cartCount
                )
                NavItem(
                    icon = Icons.Default.Person,
                    label = "Account",
                    active = currentRoute == Screen.Account.route,
                    onClick = { onNavigate(Screen.Account.route) }
                )
            }
        }
    }
}

@Composable
fun NavItem(
    icon: ImageVector,
    label: String,
    active: Boolean,
    onClick: () -> Unit,
    badgeCount: Int? = null
) {
    Column(
        modifier = Modifier
            .defaultMinSize(minWidth = 72.dp)
            .height(60.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(if (active) Color(0xFFE8F5E9) else Color.Transparent)
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(bottom = 2.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (active) Color(0xFF006D36) else Color(0xFF444748).copy(alpha = 0.7f),
                modifier = Modifier.size(24.dp)
            )
            if (badgeCount != null && badgeCount > 0) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 8.dp, y = (-4).dp)
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF006D36))
                        .border(1.5.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = badgeCount.toString(),
                        color = Color.White,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (active) FontWeight.Bold else FontWeight.Medium,
            color = if (active) Color(0xFF006D36) else Color(0xFF444748).copy(alpha = 0.8f)
        )
    }
}
