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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.budakattu.sante.ui.components.GlassCard

@Composable
fun SignupScreen(
    initialRole: String = "seller",
    onBack: () -> Unit,
    onSignupSuccess: (fullName: String, email: String, location: String) -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var selectedRole by remember { mutableStateOf(initialRole) }
    var forestRegion by remember { mutableStateOf("") }
    var shippingCity by remember { mutableStateOf("") }
    var currentLang by remember { mutableStateOf("en") }
    var agreedToTerms by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAF9))
    ) {
        AuroraBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            // Header Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.5f))
                        .border(1.dp, Color.White.copy(alpha = 0.3f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack, 
                        contentDescription = "Back", 
                        tint = Color(0xFF161C22),
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Language Toggle
                Row(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.5f))
                        .border(1.dp, Color.White.copy(alpha = 0.3f), CircleShape)
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "EN",
                        modifier = Modifier.clickable { currentLang = "en" },
                        color = if (currentLang == "en") Color(0xFF006D36) else Color(0xFF747878),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (currentLang == "en") FontWeight.Bold else FontWeight.Medium,
                        fontSize = 11.sp
                    )
                    Box(modifier = Modifier.padding(horizontal = 8.dp).width(1.dp).height(12.dp).background(Color(0xFFC4C7C8)))
                    Text(
                        text = "ಕನ್ನಡ",
                        modifier = Modifier.clickable { currentLang = "kn" },
                        color = if (currentLang == "kn") Color(0xFF006D36) else Color(0xFF747878),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (currentLang == "kn") FontWeight.Bold else FontWeight.Medium,
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Title
            Text(
                text = if (currentLang == "en") "Create Account" else "ಖಾತೆ ರಚಿಸಿ",
                style = MaterialTheme.typography.displayLarge,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF161C22)
            )
            Text(
                text = if (currentLang == "en") "Join our sustainable marketplace" else "ನಮ್ಮ ಸುಸ್ಥಿರ ಮಾರುಕಟ್ಟೆಗೆ ಸೇರಿ",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF444748),
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Main Glass Card
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(32.dp),
                containerAlpha = 0.5f,
                borderAlpha = 0.4f
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Role Pill Selector
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.3f))
                            .border(1.dp, Color.White.copy(alpha = 0.2f), CircleShape)
                            .padding(4.dp)
                    ) {
                        val isSeller = selectedRole == "seller"
                        
                        // Seller Tab
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clip(CircleShape)
                                .background(if (isSeller) Color.White else Color.Transparent)
                                .clickable { selectedRole = "seller" },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (currentLang == "en") "Tribal Seller" else "ಮಾರಾಟಗಾರ",
                                color = if (isSeller) Color(0xFF006D36) else Color(0xFF747878),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                        
                        // Buyer Tab
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clip(CircleShape)
                                .background(if (!isSeller) Color.White else Color.Transparent)
                                .clickable { selectedRole = "buyer" },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (currentLang == "en") "Buyer" else "ಖರೀದಿದಾರ",
                                color = if (!isSeller) Color(0xFF006D36) else Color(0xFF747878),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Input Fields
                    SignupInputField(
                        label = if (currentLang == "en") "Full Name" else "ಪೂರ್ಣ ಹೆಸರು",
                        value = fullName,
                        onValueChange = { fullName = it },
                        placeholder = "E.g., Madhav Gowda",
                        icon = Icons.Default.Person
                    )

                    SignupInputField(
                        label = if (currentLang == "en") "Email Address" else "ಇಮೇಲ್ ವಿಳಾಸ",
                        value = email,
                        onValueChange = { email = it },
                        placeholder = "example@forest.com",
                        icon = Icons.Default.Email,
                        keyboardType = KeyboardType.Email
                    )

                    SignupInputField(
                        label = if (currentLang == "en") "Password" else "ಗುಪ್ತಪದ",
                        value = password,
                        onValueChange = { password = it },
                        placeholder = "••••••••",
                        icon = Icons.Default.Lock,
                        keyboardType = KeyboardType.Password,
                        isPassword = true,
                        isPasswordVisible = isPasswordVisible,
                        onTogglePassword = { isPasswordVisible = !isPasswordVisible }
                    )

                    if (selectedRole == "seller") {
                        SignupInputField(
                            label = if (currentLang == "en") "Forest Community" else "ಅರಣ್ಯ ಸಮುದಾಯ",
                            value = forestRegion,
                            onValueChange = { forestRegion = it },
                            placeholder = "E.g., BR Hills Community",
                            icon = Icons.Default.Nature
                        )
                    } else {
                        SignupInputField(
                            label = if (currentLang == "en") "Shipping City" else "ಶಿಪ್ಪಿಂಗ್ ನಗರ",
                            value = shippingCity,
                            onValueChange = { shippingCity = it },
                            placeholder = "E.g., Bangalore",
                            icon = Icons.Default.LocationOn
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Terms Checkbox
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top
                    ) {
                        Checkbox(
                            checked = agreedToTerms, 
                            onCheckedChange = { agreedToTerms = it },
                            colors = CheckboxDefaults.colors(checkedColor = Color(0xFF006D36))
                        )
                        Text(
                            text = if (currentLang == "en") 
                                "I agree to support tribal communities and follow platform guidelines." 
                                else "ಬುಡಕಟ್ಟು ಸಮುದಾಯಗಳನ್ನು ಬೆಂಬಲಿಸಲು ಮತ್ತು ಮಾರ್ಗಸೂಚಿಗಳನ್ನು ಪಾಲಿಸಲು ನಾನು ಒಪ್ಪುತ್ತೇನೆ.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF444748),
                            fontSize = 11.sp,
                            modifier = Modifier.padding(top = 12.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Action Button with Gradient
                    Button(
                        onClick = { 
                            if (agreedToTerms) {
                                val locationVal = if (selectedRole == "seller") forestRegion else shippingCity
                                onSignupSuccess(fullName, email, locationVal)
                            } 
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(Color(0xFF006D36), Color(0xFF00A34D))
                                ),
                                shape = RoundedCornerShape(28.dp)
                            ),
                        shape = RoundedCornerShape(28.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        enabled = agreedToTerms
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = if (currentLang == "en") "Create Account" else "ಖಾತೆ ರಚಿಸಿ", 
                                style = MaterialTheme.typography.labelSmall, 
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward, 
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Footer
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 40.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (currentLang == "en") "Already have an account?" else "ಖಾತೆಯನ್ನು ಹೊಂದಿದ್ದೀರಾ?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF444748)
                )
                Text(
                    text = if (currentLang == "en") "Sign In" else "ಸೈನ್ ಇನ್",
                    modifier = Modifier
                        .clickable { onBack() }
                        .padding(start = 6.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF006D36),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun SignupInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    isPasswordVisible: Boolean = false,
    onTogglePassword: () -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = Color(0xFF747878),
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .drawBehind {
                    val strokeWidth = 1.dp.toPx()
                    val y = size.height - strokeWidth / 2
                    drawLine(
                        color = Color(0xFFC4C7C8).copy(alpha = 0.5f),
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = strokeWidth
                    )
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon, 
                contentDescription = null, 
                tint = Color(0xFF006D36), 
                modifier = Modifier.size(20.dp).padding(start = 2.dp)
            )
            TextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = { Text(placeholder, color = Color(0xFFC4C7C8), fontSize = 14.sp) },
                modifier = Modifier.weight(1f),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color(0xFF006D36)
                ),
                textStyle = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp, color = Color(0xFF161C22)),
                visualTransformation = if (isPassword && !isPasswordVisible) PasswordVisualTransformation() else VisualTransformation.None,
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                singleLine = true
            )
            if (isPassword) {
                IconButton(onClick = onTogglePassword) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = null,
                        tint = Color(0xFF747878),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun AuroraBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "aurora")
    
    val xOffset1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 100f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "x1"
    )
    
    val yOffset2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 150f,
        animationSpec = infiniteRepeatable(
            animation = tween(15000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "y2"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        // Soft Green Aurora
        Box(
            modifier = Modifier
                .offset(x = (-50).dp + xOffset1.dp, y = (-100).dp)
                .size(400.dp)
                .blur(80.dp)
                .alpha(0.15f)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color(0xFF83FBA5), Color.Transparent)
                    )
                )
        )
        
        // Soft Yellow Aurora
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = 50.dp, y = 50.dp - yOffset2.dp)
                .size(500.dp)
                .blur(100.dp)
                .alpha(0.1f)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color(0xFFFFD700), Color.Transparent)
                    )
                )
        )
    }
}
