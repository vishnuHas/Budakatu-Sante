package com.budakattu.sante.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.budakattu.sante.R
import com.budakattu.sante.ui.components.GlassCard

@Composable
fun LoginScreen(
    onLoginSuccess: (String) -> Unit,
    onNavigateToSignup: (String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var selectedRole by remember { mutableStateOf("buyer") }
    var currentLang by remember { mutableStateOf("en") }

    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Ambient Background / Aurora Effect
        Box(modifier = Modifier.fillMaxSize()) {
            val infiniteTransition = rememberInfiniteTransition(label = "aurora")
            val aurora1Alpha by infiniteTransition.animateFloat(
                initialValue = 0.4f,
                targetValue = 0.7f,
                animationSpec = infiniteRepeatable(
                    animation = tween(8000, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                ), label = "aurora1"
            )
            val aurora2Alpha by infiniteTransition.animateFloat(
                initialValue = 0.3f,
                targetValue = 0.6f,
                animationSpec = infiniteRepeatable(
                    animation = tween(10000, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                ), label = "aurora2"
            )

            // Top-right aurora (Matching screenshot glow)
            Box(
                modifier = Modifier
                    .size(400.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 100.dp, y = (-50).dp)
                    .alpha(aurora1Alpha * 0.15f)
                    .background(Brush.radialGradient(listOf(Color(0xFF83FBA5), Color.Transparent)))
            )

            // Bottom-left aurora
            Box(
                modifier = Modifier
                    .size(500.dp)
                    .align(Alignment.BottomStart)
                    .offset(x = (-100).dp, y = 100.dp)
                    .alpha(aurora2Alpha * 0.15f)
                    .background(Brush.radialGradient(listOf(Color(0xFFFFE16D).copy(alpha = 0.5f), Color.Transparent)))
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp)
        ) {
            // Language Toggle (Top Right)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Row(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.8f))
                        .border(1.dp, Color.White.copy(alpha = 0.3f), CircleShape)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "EN",
                        modifier = Modifier.clickable { currentLang = "en" },
                        color = if (currentLang == "en") Color(0xFF006D36) else Color(0xFF444748),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (currentLang == "en") FontWeight.Bold else FontWeight.Normal,
                        fontSize = 12.sp
                    )
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .width(1.dp)
                            .height(14.dp)
                            .background(Color(0xFFC4C7C8))
                    )
                    Text(
                        text = "ಕನ್ನಡ",
                        modifier = Modifier.clickable { currentLang = "kn" },
                        color = if (currentLang == "kn") Color(0xFF006D36) else Color(0xFF444748),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (currentLang == "kn") FontWeight.Bold else FontWeight.Normal,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Main Login Content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 48.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Solid White Card exactly like screenshot
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(40.dp), // slightly more rounded
                    color = Color.White,
                    shadowElevation = 4.dp,
                    tonalElevation = 2.dp
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 36.dp, horizontal = 28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Brand Logo
                        AsyncImage(
                            model = R.drawable.app_icon,
                            contentDescription = null,
                            modifier = Modifier
                                .size(90.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .shadow(4.dp, RoundedCornerShape(20.dp))
                                .background(Color(0xFF004D2C)), // Matching green container back
                            contentScale = ContentScale.Fit
                        )
                        
                        Spacer(modifier = Modifier.height(20.dp))
                        
                        Text(
                            text = "Budakattu-Sante",
                            style = MaterialTheme.typography.displayLarge,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF161C22),
                            textAlign = TextAlign.Center
                        )
                        
                        Text(
                            text = if (currentLang == "en") "Connecting the Forest to your Home" else "ಕಾಡಿನ ಉತ್ಪನ್ನಗಳು ನೇರವಾಗಿ ನಿಮ್ಮ ಮನೆಗೆ",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF444748),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(28.dp))

                        // Role Switch Tab
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(CircleShape)
                                .background(Color(0xFFF0F3F4)) // surface-container-low placeholder
                                .padding(4.dp)
                        ) {
                            val isBuyer = selectedRole == "buyer"
                            
                            // Buyer Button
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(CircleShape)
                                    .background(if (isBuyer) Color(0xFF006D36) else Color.Transparent)
                                    .clickable { selectedRole = "buyer" }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (currentLang == "en") "Buyer" else "ಖರೀದಿದಾರ",
                                    color = if (isBuyer) Color.White else Color(0xFF444748),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            }
                            
                            // Seller Button
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(CircleShape)
                                    .background(if (!isBuyer) Color(0xFF006D36) else Color.Transparent)
                                    .clickable { selectedRole = "seller" }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (currentLang == "en") "Tribal Seller" else "ಬುಡಕಟ್ಟು ಮಾರಾಟಗಾರ",
                                    color = if (!isBuyer) Color.White else Color(0xFF444748),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        // Email Input
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = if (currentLang == "en") "Email Address" else "ಇಮೇಲ್ ವಿಳಾಸ",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF444748),
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .drawBehind {
                                        val strokeWidth = 1.dp.toPx()
                                        val y = size.height - strokeWidth / 2
                                        drawLine(
                                            color = Color(0xFFC4C7C8),
                                            start = androidx.compose.ui.geometry.Offset(0f, y),
                                            end = androidx.compose.ui.geometry.Offset(size.width, y),
                                            strokeWidth = strokeWidth
                                        )
                                    },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Mail,
                                    contentDescription = null,
                                    tint = Color(0xFF444748),
                                    modifier = Modifier.size(20.dp)
                                )
                                TextField(
                                    value = email,
                                    onValueChange = { email = it },
                                    placeholder = { Text("example@forest.com", color = Color(0xFF747878)) },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = Color.Transparent,
                                        unfocusedContainerColor = Color.Transparent,
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                        cursorColor = Color(0xFF006D36)
                                    ),
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Password Input
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = if (currentLang == "en") "Password" else "ಗುಪ್ತಪದ",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF444748),
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .drawBehind {
                                        val strokeWidth = 1.dp.toPx()
                                        val y = size.height - strokeWidth / 2
                                        drawLine(
                                            color = Color(0xFFC4C7C8),
                                            start = androidx.compose.ui.geometry.Offset(0f, y),
                                            end = androidx.compose.ui.geometry.Offset(size.width, y),
                                            strokeWidth = strokeWidth
                                        )
                                    },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = null,
                                    tint = Color(0xFF444748),
                                    modifier = Modifier.size(20.dp)
                                )
                                TextField(
                                    value = password,
                                    onValueChange = { password = it },
                                    placeholder = { Text("••••••••", color = Color(0xFF747878)) },
                                    modifier = Modifier.weight(1f),
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = Color.Transparent,
                                        unfocusedContainerColor = Color.Transparent,
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                        cursorColor = Color(0xFF006D36)
                                    ),
                                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                                )
                                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                    Icon(
                                        imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                        contentDescription = null,
                                        tint = Color(0xFF444748),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Remember & Forgot
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(
                                    checked = true, 
                                    onCheckedChange = {},
                                    colors = CheckboxDefaults.colors(checkedColor = Color(0xFF006D36))
                                )
                                Text(
                                    text = if (currentLang == "en") "Remember me" else "ನನ್ನನ್ನು ನೆನಪಿಡು",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF444748)
                                )
                            }
                            Text(
                                text = if (currentLang == "en") "Forgot Password?" else "ಪಾಸ್ವರ್ಡ್ ಮರೆತಿರಾ?",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF006D36),
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.clickable { /* Handle forgot password */ }
                            )
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        // Primary Sign In Button with Gradient
                        Button(
                            onClick = { onLoginSuccess(selectedRole) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                                .clip(RoundedCornerShape(30.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(Color(0xFF006D36), Color(0xFF83FBA5))
                                    )
                                ),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                            contentPadding = PaddingValues()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = if (currentLang == "en") "Sign In" else "ಸೈನ್ ಇನ್",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Icon(
                                    imageVector = Icons.Default.ArrowForward, 
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // OR Divider
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        ) {
                            Box(modifier = Modifier.weight(1f).height(1.dp).background(Color(0xFFC4C7C8)))
                            Text(
                                text = "OR",
                                modifier = Modifier.padding(horizontal = 16.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF747878),
                                fontSize = 12.sp
                            )
                            Box(modifier = Modifier.weight(1f).height(1.dp).background(Color(0xFFC4C7C8)))
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Google Sign In Button
                        OutlinedButton(
                            onClick = { /* Google Login */ },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .shadow(1.dp, RoundedCornerShape(28.dp)),
                            shape = RoundedCornerShape(28.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF0F0F0)),
                            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                AsyncImage(
                                    model = "https://www.gstatic.com/images/branding/product/2x/googleg_48dp.png",
                                    contentDescription = "Google Logo",
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = if (currentLang == "en") "Continue with Google" else "ಗೂಗಲ್ ಮೂಲಕ ಮುಂದುವರಿಯಿರಿ",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFF444748), // matching user screen grey tint
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                // Registration Paths
                Text(
                    text = if (currentLang == "en") "New to the marketplace?" else "ಮಾರುಕಟ್ಟೆಗೆ ಹೊಸಬರೇ?",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF444748)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    SignupPathCard(
                        modifier = Modifier.weight(1f),
                        title = if (currentLang == "en") "Tribal Seller" else "ಬುಡಕಟ್ಟು ಮಾರಾಟಗಾರ",
                        icon = Icons.Default.Eco,
                        onClick = { onNavigateToSignup("seller") }
                    )
                    SignupPathCard(
                        modifier = Modifier.weight(1f),
                        title = if (currentLang == "en") "Buyer" else "ಖರೀದಿದಾರ",
                        icon = Icons.Default.ShoppingBag,
                        onClick = { onNavigateToSignup("buyer") }
                    )
                }
            }
        }
    }
}

@Composable
fun SignupPathCard(
    modifier: Modifier = Modifier, 
    title: String, 
    icon: androidx.compose.ui.graphics.vector.ImageVector, 
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        shadowElevation = 1.dp,
        tonalElevation = 1.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF5F5F5))
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE8F5E9)), // Light Mint circle
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon, 
                    contentDescription = null, 
                    tint = Color(0xFF006D36), // Dark green icon
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                lineHeight = 16.sp,
                color = Color(0xFF444748) // soft dark text
            )
        }
    }
}


