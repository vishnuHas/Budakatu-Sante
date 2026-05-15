package com.budakattu.sante.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.budakattu.sante.R
import com.budakattu.sante.ui.components.GlassCard
import kotlinx.coroutines.launch

import androidx.compose.foundation.ExperimentalFoundationApi

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(onFinish: () -> Unit) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            userScrollEnabled = true
        ) { page ->
            when (page) {
                0 -> OnboardingPage1()
                1 -> OnboardingPage2()
                2 -> OnboardingPage3()
            }
        }

        // Top Back Button
        if (pagerState.currentPage > 0) {
            IconButton(
                onClick = {
                    scope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) }
                },
                modifier = Modifier
                    .padding(24.dp)
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.6f))
                    .border(1.dp, Color.White.copy(alpha = 0.3f), CircleShape)
                    .align(Alignment.TopStart)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.secondary)
            }
        }

        // Bottom Controls
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Progress Dots
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(3) { index ->
                    val active = pagerState.currentPage == index
                    val width by animateDpAsState(if (active) 32.dp else 8.dp, label = "dot_width")
                    Box(
                        modifier = Modifier
                            .height(8.dp)
                            .width(width)
                            .clip(CircleShape)
                            .background(if (active) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.outlineVariant)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Action Button
            Button(
                onClick = {
                    if (pagerState.currentPage < 2) {
                        scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                    } else {
                        onFinish()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text(
                    text = if (pagerState.currentPage < 2) "Continue" else "Get Started",
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 18.sp
                )
                if (pagerState.currentPage == 2) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
                }
            }
        }
    }
}

@Composable
fun OnboardingPage1() {
    Box(modifier = Modifier.fillMaxSize()) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("https://lh3.googleusercontent.com/aida-public/AB6AXuBf1k8PNp1PJz40lDMCvEy7lp6iLIBCSynxRQypJhIyN4gXBosSYTl3o6vnsxbhn_0LAQ_RUSwS-mcUH-n7kM24-yoeMI263QZx_yxR5OloGSe7cpmnY8FluEbYygJ4wJGYXFHJOJJWNYT1nWRI_tacgsJg6g_AGVD73COPhzqpyTnYCqVK0srSLWa8qL0i9tJu8qgqmenJ4nEw5gIRd6A1U_7OtQe0VEukpVHNHVlEO2GHGA5mMf_H31wxJTLr98kSz6V3hsFNF1oh")
                .crossfade(true)
                .build(),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.7f),
            contentScale = ContentScale.Crop
        )

        // Gradient overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.White.copy(alpha = 0.8f), Color.White),
                        startY = 400f
                    )
                )
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 24.dp, vertical = 140.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GlassCard(
                modifier = Modifier,
                shape = RoundedCornerShape(24.dp),
                containerAlpha = 0.6f,
                borderAlpha = 0.3f
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Support Tribal Communities",
                        style = MaterialTheme.typography.displayLarge,
                        fontSize = 32.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Connect directly with forest artisans to source authentic, sustainably harvested products while ensuring fair trade practices.",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun OnboardingPage2() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFE8EEF6), Color.White)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Buy Authentic Forest Products",
                style = MaterialTheme.typography.displayLarge,
                fontSize = 32.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Discover nature's finest. Sustainably sourced wild honey, artisanal bamboo crafts, and pure herbal oils.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(48.dp))
            
            // Grid simulation
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                OnboardingCard(
                    modifier = Modifier.weight(1f),
                    title = "WILD HONEY",
                    imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuC4LtqudgwMmkZoitDUmPIIpdcjJmhCTkVKnekDtYz26GVp8hrABTlGm0098mXIxwAyPiDnJXr8mi9QYDoruOkwg_ibK_ZkPGcLEkqW0VKubYbqQfIVOrO6PE2SNE4mEas-Ua-l4tyQQr54QaaUsjN9yhAaZ-rxmZSucSAUKOqWwhtjwBqck1bx4lRvETE8EfvkJkKFE3T5Eor7XPrIjuoly-hm2OUgH3muogV-eLjWQDHWgS0juhAdzDXN2Eewz2QS7onCfPNfAIxD"
                )
                OnboardingCard(
                    modifier = Modifier.weight(1f).offset(y = 24.dp),
                    title = "BAMBOO",
                    imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuC1wZXEQ3qOd1ZZD5RxzYanPUhqpDicpTwy2rwWzTgeL2LQsw5kN4p-oqa1hd_3wossf90DHwuioYHtFaftuy-elBwAbVd2ypEvSVX16qOkJbMeX4KIgVNjwAO7ut8Dw9RHiLfGzzU9Q8H4-nrrdctXlJs-5vSgksR1_mMMAvoEnvbbKlzFJpo3HDHNwJ9B9KlpZOtw0jHfwEiCxLlvkI9HYgTbJPnacCTXW_4TaL0ugxja1AqHioeTMkz6YiTID-8vIQu0xzSOKnVu"
                )
            }
        }
    }
}

@Composable
fun OnboardingCard(modifier: Modifier = Modifier, title: String, imageUrl: String) {
    GlassCard(
        modifier = modifier.aspectRatio(1f),
        shape = RoundedCornerShape(16.dp),
        containerAlpha = 0.6f,
        borderAlpha = 0.3f
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}

@Composable
fun OnboardingPage3() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Abstract Background
        Box(
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.1f)
                .background(
                    Brush.radialGradient(
                        colors = listOf(MaterialTheme.colorScheme.secondary, Color.Transparent),
                        center = androidx.compose.ui.geometry.Offset(0f, 0f)
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Central visualization placeholder
            Box(
                modifier = Modifier
                    .size(240.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.6f))
                    .border(1.dp, Color.White.copy(alpha = 0.3f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                // Pulse core and nodes can be added here for full fidelity
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                     Icon(
                         imageVector = Icons.Default.Sync,
                         contentDescription = null,
                         modifier = Modifier.size(64.dp),
                         tint = MaterialTheme.colorScheme.secondary
                     )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "Offline-First\nSmart Marketplace",
                style = MaterialTheme.typography.displayLarge,
                fontSize = 32.sp,
                textAlign = TextAlign.Center,
                lineHeight = 38.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Trade seamlessly deep in the forest. Your data syncs automatically when you return to coverage.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
