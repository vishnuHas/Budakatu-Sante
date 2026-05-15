package com.budakattu.sante.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.budakattu.sante.ui.components.BottomNavBar
import com.budakattu.sante.ui.components.GlassCard
import com.budakattu.sante.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    userRole: String = "buyer", // "buyer" or "seller"
    userFullName: String? = null,
    userLocation: String? = null,
    userEmail: String? = null,
    onBack: () -> Unit,
    onNavigate: (String) -> Unit,
    @Suppress("UNUSED_PARAMETER") onOpenDrawer: () -> Unit = {}
) {
    var showEditModal by remember { mutableStateOf(false) }
    var showWithdrawModal by remember { mutableStateOf(false) }
    var showSuccessModal by remember { mutableStateOf(false) }
    var withdrawAmount by remember { mutableStateOf("45250") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Budakattu-Sante",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1F2937)
                        )
                        if (userRole == "seller") {
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                color = Color(0xFFE6F4EA),
                                shape = CircleShape
                            ) {
                                Text(
                                    text = "SELLER ACCOUNT",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    color = Color(0xFF006D36),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color(0xFF1F2937))
                    }
                },
                actions = {
                    IconButton(onClick = { /* Notifications */ }) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = Color(0xFF006D36),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        bottomBar = {
            BottomNavBar(currentRoute = Screen.Account.route, userRole = userRole, onNavigate = onNavigate)
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            // Ambient Background
            AmbientBackground()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(padding)
                    .padding(horizontal = 24.dp)
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                // Profile Header
                ProfileHeader(
                    userRole = userRole,
                    userFullName = userFullName,
                    userLocation = userLocation,
                    userEmail = userEmail,
                    onEdit = { showEditModal = true }
                )

                Spacer(modifier = Modifier.height(32.dp))

                if (userRole == "seller") {
                    // Seller Financial Section
                    SellerBalanceCard(
                        onWithdraw = { showWithdrawModal = true }
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Seller Stats
                    SellerStatsGrid()

                    Spacer(modifier = Modifier.height(32.dp))

                    // Settings List
                    Text("Account Settings", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(16.dp))

                    SettingsList(
                        userRole = userRole,
                        userFullName = userFullName,
                        userLocation = userLocation
                    )

                    Spacer(modifier = Modifier.height(32.dp))
                    
                    // Logout Card
                    LogoutCard(onLogout = { onNavigate("login") })
                } else {
                    // Buyer Action Stack
                    BuyerActionCards(onNavigate = onNavigate)
                }

                Spacer(modifier = Modifier.height(120.dp))
            }
        }
    }

    if (showEditModal) {
        EditProfileModal(onClose = { showEditModal = false })
    }

    if (showWithdrawModal) {
        WithdrawModal(
            amount = withdrawAmount,
            onAmountChange = { withdrawAmount = it },
            onClose = { showWithdrawModal = false },
            onConfirm = {
                showWithdrawModal = false
                showSuccessModal = true
            }
        )
    }

    if (showSuccessModal) {
        WithdrawSuccessModal(
            amount = withdrawAmount,
            onClose = { showSuccessModal = false }
        )
    }
}

@Composable
fun AmbientBackground() {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.1f), Color.Transparent),
                        center = androidx.compose.ui.geometry.Offset(Float.POSITIVE_INFINITY, 0f)
                    )
                )
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(MaterialTheme.colorScheme.tertiary.copy(alpha = 0.05f), Color.Transparent),
                        center = androidx.compose.ui.geometry.Offset(0f, Float.POSITIVE_INFINITY)
                    )
                )
        )
    }
}

@Composable
fun ProfileHeader(
    userRole: String, 
    userFullName: String? = null, 
    userLocation: String? = null, 
    userEmail: String? = null, 
    onEdit: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Avatar with overlapping badge
        Box(contentAlignment = Alignment.BottomEnd) {
            AsyncImage(
                model = if (userRole == "seller")
                    "https://lh3.googleusercontent.com/aida-public/AB6AXuDtX5zmwYdN4LEaGJqwnK_8HOuiPmsL-tgOKANRiNoRJfkX9cqy0IRZ0UFilkUemCaJTtYxPggI-oetU-6S427yYvWJl8T2Gds2EBYxx-VKwkQ6nGBOzW5CVyyCe9xTeSZ8PG4AcSxXf2qA165llyARfkZWOqErIKZAEKiSd9O8fD6gvgBvI7u9U91jcGThMvWyzWERno1D-GRnmH3TTkzuarW8KzrxVEVz3BN-sqwT0h8D60wBFS3qjNoW7tjvcmuo-YPZ3eMESRVQ"
                else "https://lh3.googleusercontent.com/aida-public/AB6AXuCcr7m-LegLZ_4GdgYqfOwWPwKQSUKAUj2X_ULFZnC3cttQpjbWTPlhdNgZqT1pISawg182xyuM295DnZ8a8YG74tsKSk7j5n1bzytb6uWtq8FFQQ9J366XJOf0z7U4UCSZ2hxHHtGypTmMY0EJeLk0IoguW90YgM67mUdvd6CjMl8PM1oe7Js36J7XKKjHg2ESCkPaArCYafhJ3Jt3n95lLBsX9AOuJCnkMClCZOvUa_UvaMGoJRZzKPLvj2HXU9E9NXa8pjChr2lh",
                contentDescription = "Profile Image",
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .border(3.dp, Color.White, CircleShape),
                contentScale = ContentScale.Crop
            )
            if (userRole == "seller") {
                Surface(
                    modifier = Modifier
                        .size(32.dp)
                        .offset(x = (-4).dp, y = (-4).dp),
                    color = Color(0xFFFFD740), // Golden badge
                    shape = CircleShape,
                    shadowElevation = 4.dp,
                    border = BorderStroke(2.dp, Color.White)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.MilitaryTech,
                            contentDescription = "Badge",
                            tint = Color(0xFF423000),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = userFullName ?: (if (userRole == "seller") "Artisan" else "Forest Patron"),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1F2937)
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = if (userRole == "seller") "Harvest Team • ${userLocation ?: "Active Forest"}" 
                   else "${userEmail ?: "supporter@canopy.org"} • ${userLocation ?: "Global"}",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Gray,
            fontWeight = FontWeight.Medium
        )

        if (userRole == "seller") {
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFF006D36),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "4.9 Impact Score",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF4B5563),
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(modifier = Modifier.width(24.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Eco,
                        contentDescription = null,
                        tint = Color(0xFF006D36),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Carbon Neutral",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF4B5563),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        } else {
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onEdit,
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006D36))
            ) {
                Text("Edit Profile", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun OverlappingAvatars() {
    Row(horizontalArrangement = Arrangement.spacedBy((-10).dp)) {
        val imgs = listOf(
            "https://lh3.googleusercontent.com/aida-public/AB6AXuDtX5zmwYdN4LEaGJqwnK_8HOuiPmsL-tgOKANRiNoRJfkX9cqy0IRZ0UFilkUemCaJTtYxPggI-oetU-6S427yYvWJl8T2Gds2EBYxx-VKwkQ6nGBOzW5CVyyCe9xTeSZ8PG4AcSxXf2qA165llyARfkZWOqErIKZAEKiSd9O8fD6gvgBvI7u9U91jcGThMvWyzWERno1D-GRnmH3TTkzuarW8KzrxVEVz3BN-sqwT0h8D60wBFS3qjNoW7tjvcmuo-YPZ3eMESRVQ",
            "https://images.unsplash.com/photo-1544005313-94ddf0286df2?auto=format&fit=crop&q=80&w=100",
            "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&q=80&w=150"
        )
        imgs.forEach {
            AsyncImage(
                model = it,
                contentDescription = null,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.White, CircleShape),
                contentScale = ContentScale.Crop
            )
        }
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(Color(0xFF86EFAC), CircleShape)
                .border(2.dp, Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("+3", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF065F46))
        }
    }
}

@Composable
fun BuyerActionCards(onNavigate: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Saved Orders
        ProfileWideCard(
            onClick = { onNavigate("tracking") },
            icon = Icons.Default.Inventory2,
            title = "Saved Orders",
            subtitle = "Click to track your recent forest-to-home deliveries live.",
            badge = "Track live",
            trailingIcon = Icons.Default.NorthEast
        )

        // Impact Level
        ProfileWideCard(
            onClick = { },
            icon = Icons.Default.Eco,
            title = "Impact Level",
            subtitle = "You've helped preserve 12 acres of canopy.",
            containerColor = Color(0xFFE8F5E9),
            borderColor = Color(0xFF4CAF50).copy(alpha = 0.2f),
            iconTint = Color(0xFF2E7D32),
            textColor = Color(0xFF1B5E20)
        )

        // Followed Artisans
        ProfileWideCard(
            onClick = { },
            icon = Icons.Default.Groups,
            title = "Followed Artisans",
            content = { OverlappingAvatars() }
        )

        // Preferences
        ProfileWideCard(
            onClick = { },
            icon = Icons.Default.Tune,
            title = "Preferences",
            subtitle = "Manage notifications and account details."
        )

        // Logout
        ProfileWideCard(
            onClick = { onNavigate("login") },
            icon = Icons.Default.Logout,
            title = "Logout",
            subtitle = "Sign out of your Forest Patron session.",
            containerColor = Color(0xFFFEF2F2),
            borderColor = Color(0xFFEF4444).copy(alpha = 0.1f),
            iconTint = Color(0xFFB91C1C),
            textColor = Color(0xFF991B1B)
        )
    }
}

@Composable
fun ProfileWideCard(
    onClick: () -> Unit,
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    badge: String? = null,
    trailingIcon: ImageVector? = null,
    containerColor: Color = Color.White,
    borderColor: Color = Color.Black.copy(alpha = 0.05f),
    iconTint: Color = MaterialTheme.colorScheme.secondary,
    textColor: Color = Color(0xFF1A1C18),
    content: @Composable (() -> Unit)? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(modifier = Modifier.padding(24.dp)) {
            if (trailingIcon != null) {
                Icon(
                    trailingIcon,
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.TopEnd).size(24.dp),
                    tint = Color.Gray.copy(alpha = 0.6f)
                )
            }
            
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(iconTint.copy(alpha = 0.1f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(22.dp))
                }
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        title, 
                        fontWeight = FontWeight.ExtraBold, 
                        fontSize = 24.sp, 
                        color = textColor,
                        letterSpacing = (-0.5).sp
                    )
                    
                    if (badge != null) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            color = Color(0xFFE6F4EA),
                            shape = CircleShape
                        ) {
                            Text(
                                badge,
                                color = Color(0xFF137333),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
                
                if (content != null) {
                    content()
                }
                
                if (subtitle != null) {
                    Text(
                        subtitle,
                        fontSize = 14.sp,
                        color = if (textColor != Color(0xFF1A1C18)) textColor.copy(alpha = 0.8f) else Color.Gray,
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}

@Composable
fun SellerBalanceCard(onWithdraw: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9).copy(alpha = 0.8f)),
        border = BorderStroke(1.dp, Color.White)
    ) {
        Box(modifier = Modifier.padding(24.dp)) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "CURRENT BALANCE",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF4B5563),
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "₹45,250",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF111827)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Next payout : Oct 15th",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF6B7280)
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Button(
                    onClick = onWithdraw,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006D36))
                ) {
                    Text("Withdraw Funds", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}

@Composable
fun SellerStatsGrid() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        StatItem(modifier = Modifier.weight(1f), value = "128", label = "Orders")
        StatItem(modifier = Modifier.weight(1f), value = "₹1.2L", label = "Sales")
    }
}

@Composable
fun StatItem(modifier: Modifier = Modifier, value: String, label: String) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFF3F4F6))
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = value,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 32.sp,
                color = Color(0xFF111827)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                fontSize = 12.sp,
                color = Color(0xFF6B7280),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun SettingsList(
    userRole: String,
    userFullName: String? = null,
    userLocation: String? = null
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        SettingsItem(
            icon = Icons.Default.Nature, // Plant-like icon matching screenshot
            title = "Personal Information",
            initialExpanded = true
        ) {
            Column(modifier = Modifier.padding(top = 12.dp)) {
                Divider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFEEEEEE))
                Row(modifier = Modifier.fillMaxWidth()) {
                    SettingsField(modifier = Modifier.weight(1f), label = "FULL NAME", value = userFullName ?: "Not Set")
                    SettingsField(modifier = Modifier.weight(1f), label = "PRIMARY CRAFT", value = if (userRole == "seller") "Forest Collection" else "Sustainable Purchasing")
                }
                Spacer(modifier = Modifier.height(16.dp))
                SettingsField(modifier = Modifier.fillMaxWidth(), label = if (userRole == "seller") "COMMUNITY/TRIBE" else "SHIPPING CITY", value = userLocation ?: "Not Set")
            }
        }

        if (userRole == "seller") {
            SettingsItem(
                icon = Icons.Default.Storefront,
                title = "Business Profile"
            ) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    Divider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFEEEEEE))
                    Row(modifier = Modifier.fillMaxWidth()) {
                        SettingsField(modifier = Modifier.weight(1.5f), label = "COOPERATIVE NAME", value = "${userLocation ?: "Forest"} Co-op")
                        SettingsField(modifier = Modifier.weight(1f), label = "ARTISAN ID", value = "ART-2026-${(100..999).random()}")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth()) {
                        SettingsField(modifier = Modifier.weight(1.5f), label = "REGISTERED ADDRESS", value = userLocation ?: "Forest Basecamp")
                        SettingsField(modifier = Modifier.weight(1f), label = "FOREST PERMIT NO.", value = "FHP-${(1000..9999).random()}-26")
                    }
                }
            }

            SettingsItem(
                icon = Icons.Default.AccountBalance,
                title = "Payout Settings"
            ) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    Divider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFEEEEEE))
                    Row(modifier = Modifier.fillMaxWidth()) {
                        SettingsField(modifier = Modifier.weight(1f), label = "BANK ACCOUNT", value = "**** **** 4891")
                        SettingsField(modifier = Modifier.weight(1f), label = "IFSC CODE", value = "SBIN0004512")
                    }
                }
            }

            SettingsItem(
                icon = Icons.Default.Description,
                title = "Tax Documents"
            ) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    Divider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFEEEEEE))
                    Row(modifier = Modifier.fillMaxWidth()) {
                        SettingsField(modifier = Modifier.weight(1.5f), label = "GSTIN STATUS", value = "Exempted (Tribal Hand)")
                        SettingsField(modifier = Modifier.weight(1f), label = "ANNUAL STATEMENT", value = "FY 2024-25 (Ready)")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        SettingsField(modifier = Modifier.weight(1.5f), label = "FORM 16G (EXEMPTION)", value = "Active & Approved")
                        Button(
                            onClick = { },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE8F5E9)),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                            modifier = Modifier.height(36.dp)
                        ) {
                            Icon(Icons.Default.FileUpload, contentDescription = null, tint = Color(0xFF006D36), modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Download All", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF006D36))
                        }
                    }
                }
            }

            SettingsItem(
                icon = Icons.Default.Group,
                title = "Manage Team"
            ) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    Divider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFEEEEEE))
                    TeamMemberRow(
                        imageUrl = "https://images.unsplash.com/photo-1544005313-94ddf0286df2?auto=format&fit=crop&q=80&w=100",
                        name = "Anandi Kempagowda",
                        role = "Cooperative Leader",
                        badge = "Admin"
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    TeamMemberRow(
                        initials = "MS",
                        name = "Mahadeva Swamy",
                        role = "Harvest Inspector",
                        badge = "Manager"
                    )
                }
            }
        }

        SettingsItem(
            icon = Icons.Default.SupportAgent,
            title = "Support"
        ) {
            Column(modifier = Modifier.padding(top = 12.dp)) {
                Divider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFEEEEEE))
                Text(
                    text = "Need help with your business profile, transactions, or listings? Contact your dedicated cooperative support line:",
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    color = Color(0xFF6B7280)
                )
                Spacer(modifier = Modifier.height(16.dp))
                SupportContactRow(icon = Icons.Default.Phone, text = "+91 94832 34587 (Cooperative Lead)")
                Spacer(modifier = Modifier.height(8.dp))
                SupportContactRow(icon = Icons.Default.Email, text = "support@budakattusante.com")
            }
        }
    }
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    title: String,
    initialExpanded: Boolean = false,
    content: @Composable () -> Unit
) {
    var expanded by remember { mutableStateOf(initialExpanded) }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f)),
        border = BorderStroke(1.dp, Color(0xFFF3F4F6))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth().clickable { expanded = !expanded },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(Color(0xFFEEF2F6), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF374151),
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = Color(0xFF9CA3AF)
                )
            }
            
            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                content()
            }
        }
    }
}

@Composable
fun SettingsField(modifier: Modifier = Modifier, label: String, value: String) {
    Column(modifier = modifier) {
        Text(
            text = label,
            fontSize = 9.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF059669), // Darker emerald green from screenshot
            letterSpacing = 0.5.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1F2937)
        )
    }
}

@Composable
fun TeamMemberRow(imageUrl: String? = null, initials: String? = null, name: String, role: String, badge: String) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        if (imageUrl != null) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(40.dp).clip(CircleShape)
            )
        } else {
            Box(
                modifier = Modifier.size(40.dp).background(Color(0xFFD1FAE5), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = initials ?: "", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF065F46))
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = name, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1F2937))
            Text(text = role, fontSize = 11.sp, color = Color(0xFF6B7280))
        }
        Surface(
            color = Color.Transparent,
            shape = RoundedCornerShape(6.dp),
            border = BorderStroke(0.dp, Color.Transparent)
        ) {
            Text(
                text = badge,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF059669),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}

@Composable
fun SupportContactRow(icon: ImageVector, text: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFF9FAFB),
        border = BorderStroke(1.dp, Color(0xFFF3F4F6))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = Color(0xFF059669), modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = text, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color(0xFF374151))
        }
    }
}

@Composable
fun LogoutCard(onLogout: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onLogout() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.05f)),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.2f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(40.dp).background(MaterialTheme.colorScheme.error.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Logout, contentDescription = null, tint = MaterialTheme.colorScheme.error)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text("Logout", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.error, modifier = Modifier.weight(1f))
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.error)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileModal(onClose: () -> Unit) {
    var name by remember { mutableStateOf("Elenora Vance") }
    var email by remember { mutableStateOf("elenora.vance@canopy.org") }
    var phone by remember { mutableStateOf("+91 98765 43210") }
    var address by remember { mutableStateOf("142 Oakwood Drive, Indiranagar, Bangalore, KA 560038") }
    var upiId by remember { mutableStateOf("elenora@okicici") }
    var cardNumber by remember { mutableStateOf("**** **** **** 4532") }
    var expiry by remember { mutableStateOf("12/28") }
    var cvv by remember { mutableStateOf("***") }

    Dialog(onDismissRequest = onClose, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.85f),
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
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
                    Text("Update Profile", fontWeight = FontWeight.Bold, fontSize = 24.sp)
                    IconButton(
                        onClick = onClose,
                        modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), CircleShape)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Divider(color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))

                // Form Fields
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Avatar Section
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Box(contentAlignment = Alignment.BottomEnd) {
                            AsyncImage(
                                model = "https://lh3.googleusercontent.com/aida-public/AB6AXuCcr7m-LegLZ_4GdgYqfOwWPwKQSUKAUj2X_ULFZnC3cttQpjbWTPlhdNgZqT1pISawg182xyuM295DnZ8a8YG74tsKSk7j5n1bzytb6uWtq8FFQQ9J366XJOf0z7U4UCSZ2hxHHtGypTmMY0EJeLk0IoguW90YgM67mUdvd6CjMl8PM1oe7Js36J7XKKjHg2ESCkPaArCYafhJ3Jt3n95lLBsX9AOuJCnkMClCZOvUa_UvaMGoJRZzKPLvj2HXU9E9NXa8pjChr2lh",
                                contentDescription = "Profile Avatar",
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f), CircleShape),
                                contentScale = ContentScale.Crop
                            )
                            FilledIconButton(
                                onClick = { /* Pick Image */ },
                                modifier = Modifier.size(36.dp),
                                colors = IconButtonDefaults.filledIconButtonColors(containerColor = MaterialTheme.colorScheme.secondary)
                            ) {
                                Icon(Icons.Default.PhotoCamera, contentDescription = "Edit Photo", modifier = Modifier.size(16.dp), tint = Color.White)
                            }
                        }
                    }

                    Text("Personal Information", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary, fontSize = 14.sp)

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Full Name") },
                        leadingIcon = { Icon(Icons.Default.Person, null) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email Address") },
                        leadingIcon = { Icon(Icons.Default.Email, null) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Phone Number") },
                        leadingIcon = { Icon(Icons.Default.Phone, null) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = address,
                        onValueChange = { address = it },
                        label = { Text("Shipping Address") },
                        leadingIcon = { Icon(Icons.Default.Home, null) },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2,
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Divider(color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Payment Methods", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary, fontSize = 14.sp)

                    OutlinedTextField(
                        value = upiId,
                        onValueChange = { upiId = it },
                        label = { Text("UPI ID") },
                        leadingIcon = { Icon(Icons.Default.AccountBalance, null) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.05f), RoundedCornerShape(16.dp))
                            .border(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
                            .padding(16.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CreditCard, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Linked Card", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                            
                            OutlinedTextField(
                                value = cardNumber,
                                onValueChange = { cardNumber = it },
                                label = { Text("Card Number") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White
                                )
                            )
                            
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                OutlinedTextField(
                                    value = expiry,
                                    onValueChange = { expiry = it },
                                    label = { Text("Expiry") },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedContainerColor = Color.White,
                                        unfocusedContainerColor = Color.White
                                    )
                                )
                                OutlinedTextField(
                                    value = cvv,
                                    onValueChange = { cvv = it },
                                    label = { Text("CVV") },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedContainerColor = Color.White,
                                        unfocusedContainerColor = Color.White
                                    )
                                )
                            }
                        }
                    }
                }

                // Footer Action
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    Button(
                        onClick = onClose,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                    ) {
                        Text("Save Changes", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun WithdrawModal(amount: String, onAmountChange: (String) -> Unit, onClose: () -> Unit, onConfirm: () -> Unit) {
    Dialog(onDismissRequest = onClose, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Box(modifier = Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(32.dp),
                containerAlpha = 0.95f,
                borderAlpha = 0.2f
            ) {
                Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Payments, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Withdraw Funds", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = MaterialTheme.colorScheme.secondary)
                        }
                        IconButton(onClick = onClose) {
                            Icon(Icons.Default.Close, contentDescription = "Close")
                        }
                    }
                    
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
                            Text("Available for Withdrawal", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("₹45,250", fontSize = 32.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    
                    OutlinedTextField(
                        value = amount,
                        onValueChange = onAmountChange,
                        label = { Text("Amount to Withdraw") },
                        prefix = { Text("₹") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White.copy(alpha = 0.6f),
                            unfocusedContainerColor = Color.White.copy(alpha = 0.6f)
                        )
                    )
                    
                    Text(
                        text = "* Funds will be settled to your linked SBI account within 24 hours.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.05f), RoundedCornerShape(16.dp))
                            .border(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier.size(40.dp).background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f), RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.AccountBalance, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("State Bank of India", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Text("A/c: •••• 4891", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                    
                    Button(
                        onClick = onConfirm,
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                    ) {
                        Text("Confirm Withdrawal", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun WithdrawSuccessModal(amount: String, onClose: () -> Unit) {
    Dialog(onDismissRequest = onClose, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Box(modifier = Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(32.dp),
                containerAlpha = 0.95f,
                borderAlpha = 0.2f
            ) {
                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier.size(80.dp).background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(48.dp), tint = MaterialTheme.colorScheme.secondary)
                    }
                    Text("Request Sent!", fontWeight = FontWeight.Bold, fontSize = 24.sp)
                    Text(
                        "Your withdrawal request for ₹$amount is being processed. It will reflect in your bank account shortly.",
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Button(
                        onClick = onClose,
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                    ) {
                        Text("Got it", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
