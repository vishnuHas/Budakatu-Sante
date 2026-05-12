package com.budakattu.sante.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.budakattu.sante.data.model.Product
import com.budakattu.sante.data.model.sampleProducts
import com.budakattu.sante.ui.components.BottomNavBar
import com.budakattu.sante.ui.components.GlassCard
import com.budakattu.sante.ui.navigation.Screen
import kotlinx.coroutines.delay

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DiscoverScreen(
    userRole: String = "buyer",
    onProductClick: (String) -> Unit,
    onNavigate: (String) -> Unit,
    onOpenDrawer: () -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }
    var isSearchExpanded by remember { mutableStateOf(false) }
    var currentLang by remember { mutableStateOf("en") }
    val wishlistItems by com.budakattu.sante.data.WishlistManager.wishlistItems.collectAsState()

    Scaffold(
        bottomBar = {
            BottomNavBar(currentRoute = Screen.Discover.route, userRole = userRole, onNavigate = onNavigate)
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(padding)
        ) {
            // Main Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(80.dp)) // Offset for fixed top bar

                // Hero Carousel
                HeroCarousel()

                Spacer(modifier = Modifier.height(32.dp))

                // Categories
                CategorySlider(
                    selectedCategory = selectedCategory,
                    onCategorySelected = { selectedCategory = it }
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Grid Headline
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = if (selectedCategory == "All") "Curated For You" else "Premium $selectedCategory",
                        style = MaterialTheme.typography.displaySmall,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF161C22)
                    )
                    
                    Surface(
                        color = Color.White.copy(alpha = 0.5f),
                        border = BorderStroke(1.dp, Color(0xFFC4C7C8).copy(alpha = 0.4f)),
                        shape = CircleShape
                    ) {
                        val productCount = sampleProducts.count { 
                            (selectedCategory == "All" || it.category.equals(selectedCategory, ignoreCase = true)) &&
                            it.name.contains(searchQuery, ignoreCase = true)
                        }
                        Text(
                            text = "$productCount Products",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF444748)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Products Grid
                val filteredProducts = sampleProducts.filter { 
                    (selectedCategory == "All" || it.category.equals(selectedCategory, ignoreCase = true)) &&
                    it.name.contains(searchQuery, ignoreCase = true)
                }

                if (filteredProducts.isEmpty()) {
                    EmptyState(onReset = { 
                        searchQuery = ""
                        selectedCategory = "All"
                    })
                } else {
                    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                        filteredProducts.chunked(2).forEach { rowProducts ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                rowProducts.forEach { product ->
                                    ProductCard(
                                        product = product,
                                        isFavorite = wishlistItems.any { it.id == product.id },
                                        onToggleFavorite = { com.budakattu.sante.data.WishlistManager.toggleItem(product) },
                                        onClick = { onProductClick(product.id) },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                                if (rowProducts.size == 1) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(100.dp)) // Space for bottom bar
            }

            // Fixed Top App Bar (Sleek Blur)
            DiscoverTopBar(
                isSearchExpanded = isSearchExpanded,
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it },
                onToggleSearch = { isSearchExpanded = !isSearchExpanded },
                currentLang = currentLang,
                onLangToggle = { currentLang = if (currentLang == "en") "kn" else "en" },
                onOpenDrawer = onOpenDrawer
            )
        }
    }
}

@Composable
fun DiscoverTopBar(
    isSearchExpanded: Boolean,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onToggleSearch: () -> Unit,
    currentLang: String,
    onLangToggle: () -> Unit,
    onOpenDrawer: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(Color.White.copy(alpha = 0.6f))
            .drawBehind {
                val strokeWidth = 1.dp.toPx()
                val y = size.height - strokeWidth / 2
                drawLine(
                    color = Color(0xFFC4C7C8).copy(alpha = 0.3f),
                    start = androidx.compose.ui.geometry.Offset(0f, y),
                    end = androidx.compose.ui.geometry.Offset(size.width, y),
                    strokeWidth = strokeWidth
                )
            }
    ) {
        // Blur effect simulation using drawing modifier or just the semi-transparent bg
        // For a true blur, we'd need a backdrop blur modifier (available in newer Compose versions or via AndroidView)
        
        AnimatedContent(
            targetState = isSearchExpanded,
            transitionSpec = {
                fadeIn() togetherWith fadeOut()
            },
            label = "topbar"
        ) { expanded ->
            if (expanded) {
                // Search View
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    IconButton(
                        onClick = onToggleSearch,
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color.White.copy(alpha = 0.4f), CircleShape)
                            .border(1.dp, Color.White.copy(alpha = 0.3f), CircleShape)
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color(0xFF006D36))
                    }
                    
                    TextField(
                        value = searchQuery,
                        onValueChange = onSearchQueryChange,
                        placeholder = { Text("Search crafts, honey, herbs...", fontSize = 14.sp) },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .clip(CircleShape),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White.copy(alpha = 0.7f),
                            unfocusedContainerColor = Color.White.copy(alpha = 0.7f),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedTextColor = Color(0xFF161C22)
                        ),
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(20.dp)) },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { onSearchQueryChange("") }) {
                                    Icon(Icons.Default.Close, contentDescription = "Clear", modifier = Modifier.size(18.dp))
                                }
                            }
                        },
                        singleLine = true
                    )
                }
            } else {
                // Normal View
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = onOpenDrawer,
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color.White.copy(alpha = 0.4f), CircleShape)
                            .border(1.dp, Color.White.copy(alpha = 0.3f), CircleShape)
                    ) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color(0xFF006D36))
                    }

                    Text(
                        text = "Budakattu-Sante",
                        style = MaterialTheme.typography.displaySmall,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF161C22),
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        IconButton(
                            onClick = onToggleSearch,
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color.White.copy(alpha = 0.4f), CircleShape)
                                .border(1.dp, Color.White.copy(alpha = 0.3f), CircleShape)
                        ) {
                            Icon(Icons.Default.Search, contentDescription = "Search", tint = Color(0xFF006D36))
                        }
                        
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.4f))
                                .border(1.dp, Color.White.copy(alpha = 0.3f), CircleShape)
                                .clickable { onLangToggle() },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (currentLang == "en") "EN" else "ಕ",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF006D36)
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HeroCarousel() {
    val pagerState = rememberPagerState(pageCount = { 3 })
    
    LaunchedEffect(Unit) {
        while (true) {
            delay(5000)
            pagerState.animateScrollToPage((pagerState.currentPage + 1) % 3)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(380.dp)
            .padding(horizontal = 24.dp)
            .clip(RoundedCornerShape(32.dp))
            .border(1.dp, Color.White.copy(alpha = 0.4f), RoundedCornerShape(32.dp))
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            Box(modifier = Modifier.fillMaxSize()) {
                AsyncImage(
                    model = when(page) {
                        0 -> "https://lh3.googleusercontent.com/aida-public/AB6AXuBBJTduQZ3kRKkP_DBTe9PKQzbwPkhX0U3JqERVuqrplSZW9nx1xO7cDXMweqskkYEBtpPJe3yw70FeSodFWNlOtnpGI-h15vtJBPEm1tkIxiL5cVIvS9bvwnpGN4d39NU8DFomUs2XA6g-NmztIaC_LIQKLL4fyGLExofx1y7eG1y-qkBIPbJsRsP5cVJ2-8t4FCvPMx6IU0jllNahv-nBB0zkCFwKSrvLmjmEmsgsvaeapiGGQJR2Y5bLj48ZsM9ox_HHJxOk8hhC"
                        1 -> "https://images.unsplash.com/photo-1549488344-1f9b8d2bd1f3?auto=format&fit=crop&w=1200&q=80"
                        else -> "https://images.unsplash.com/photo-1512290923902-8a9f81dc236c?auto=format&fit=crop&w=1200&q=80"
                    },
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.White.copy(alpha = 0.95f)),
                                startY = 400f
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(24.dp)
                ) {
                    Text(
                        text = when(page) {
                            0 -> "Seasonal Harvest"
                            1 -> "Artisanal Heritage"
                            else -> "Traditional Medicine"
                        }.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF705D00),
                        letterSpacing = 2.sp
                    )
                    Text(
                        text = when(page) {
                            0 -> "Wild Forest\nHoney"
                            1 -> "Handcrafted\nBamboo"
                            else -> "Organic Herbal\nRemedies"
                        },
                        style = MaterialTheme.typography.displaySmall,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF161C22),
                        lineHeight = 36.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { /* Shop Now */ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.65f)),
                        shape = CircleShape,
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
                        modifier = Modifier.border(1.dp, Color.White.copy(alpha = 0.35f), CircleShape)
                    ) {
                        Text("Shop Now", color = Color(0xFF006D36), fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color(0xFF006D36))
                    }
                }
            }
        }
        
        // Pager indicators
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(3) { index ->
                val isActive = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .width(if (isActive) 16.dp else 8.dp)
                        .height(8.dp)
                        .clip(CircleShape)
                        .background(if (isActive) Color(0xFF006D36) else Color.White.copy(alpha = 0.4f))
                        .animateContentSize()
                )
            }
        }
    }
}

@Composable
fun CategorySlider(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    val categories = listOf(
        CategoryItem("All", Icons.Default.GridView, ""),
        CategoryItem("Honey", null, "https://lh3.googleusercontent.com/aida-public/AB6AXuBFqNZwmbXUExjBFdXuMClxZJ2pblUP8g7iVsav1ybQiI9NVzs2M0DKhZRv62VXFipZJNlIVwBTzjOGFV_I2SPK9U7Txw2bezFks3sW8m9U75wz4j9SBVK88k8sf8Ps8-oROYTWSngA_zaKqSiRfb7xgnCSylNBcX7oIV4flj40jPApkXyQjTRlwvf2HpEPuECQGhO7XCmJguX-6lxSlnpc-mzeYFUep7bo8DiH0T_dCGLoXchltsYTjmPm7-TBqmu_9ZN9sLf85ylw"),
        CategoryItem("Bamboo", null, "https://lh3.googleusercontent.com/aida-public/AB6AXuC3G3Ifdm0fQxuHKr-yxig1GHg0hmvshHdDxbH2JQteTUd3JFGKZkUbabLlbpYE1oaD1vVRSIziyAsQygQbVKJ0JYr_maJPFq5dQS3h2sj9_Qm9HqeW4Qnvf-W11njnuoZ-x3o5beoE8B2drp1TDjAXuZaEvUHe14p-Z2IQJv6jWImJtlmmVw7qWC5KkmL62ta2bckv6wEQE0XY823l59ofmN35a_ay7txHOcVfa0q1DWu5JK4FM5tVwh-2AbvHmEQKq7tKoV0K8FvB"),
        CategoryItem("Herbal", null, "https://lh3.googleusercontent.com/aida-public/AB6AXuB_iHVztFf5OU30GCN7Xhbj5C9h6fVf8KtWNwuZv07DC5Y2_Z0qekB8o9o86HTbSsla7p0HbQTNTVt_LaOgMJkx2iuvezDLkZAJMivOxjPG_6KjUk0JPosBkD391lxOLnPBochY_58yTTTQesIgjTiY9vWgunxnoMZkAzdo8j8eZl581nQBQJUmZG2m8Bn8GuAu8JzTwF3yO1rnOPww3g3BvrjtZKOxy2toEOjI_9E4QnCsTPJRfVEXmL_iZKfCRQ-YTljouTS2hSFE"),
        CategoryItem("Handmade", null, "https://lh3.googleusercontent.com/aida-public/AB6AXuBq1b4O_O7tb1RAGAoLb8rK-N5uWFlCNaM4YcqQOcCU0SSeUCryhE7y7lp2zUPMFLddl_D0oACtqwbGLJIZTT4afoZnDRBjVTTz9dCdfYtuPi9AA00nxIi6lyFhU5ceRZJBlLUg0AyDbgn26e2H7QNB-VykdRO1Rk4I1syvEQ5_K8j4xHUIzZvvNbvYaAyKItQFCpgy680qo-JgUWZTmrLhL1U_AadzlKA3R3mTx3AGxboTUTdTNGh289fG9uFik3sToDQ7ynv27OtW")
    )
    
    LazyRow(
        contentPadding = PaddingValues(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(categories) { item ->
            val isSelected = selectedCategory == item.name
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable { onCategorySelected(item.name) }
            ) {
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) Color(0xFF006D36).copy(alpha = 0.1f) else Color.White.copy(alpha = 0.4f))
                        .border(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) Color(0xFF006D36) else Color.White.copy(alpha = 0.5f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (item.icon != null) {
                        Icon(
                            item.icon,
                            contentDescription = null,
                            tint = if (isSelected) Color(0xFF006D36) else Color(0xFF444748),
                            modifier = Modifier.size(28.dp)
                        )
                    } else {
                        AsyncImage(
                            model = item.imageUrl,
                            contentDescription = item.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize().clip(CircleShape).padding(2.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 11.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                    color = if (isSelected) Color(0xFF006D36) else Color(0xFF444748)
                )
            }
        }
    }
}

data class CategoryItem(val name: String, val icon: androidx.compose.ui.graphics.vector.ImageVector?, val imageUrl: String)

@Composable
fun ProductCard(
    product: Product,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    GlassCard(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .clickable { onClick() },
        containerAlpha = 0.65f,
        borderAlpha = 0.5f
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFFE8EEF6).copy(alpha = 0.5f))
            ) {
                AsyncImage(
                    model = product.image,
                    contentDescription = product.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                
                // Badge Overlay
                Surface(
                    color = Color(0xFFFFE16D).copy(alpha = 0.9f),
                    shape = CircleShape,
                    modifier = Modifier.padding(8.dp).align(Alignment.TopStart)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Icon(Icons.Default.Verified, contentDescription = null, modifier = Modifier.size(10.dp), tint = Color(0xFF544600))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(product.badge, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFF544600))
                    }
                }
                
                // Favorite Button
                IconButton(
                    onClick = onToggleFavorite,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(32.dp)
                        .align(Alignment.TopEnd)
                        .background(Color.White.copy(alpha = 0.7f), CircleShape)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder, 
                        contentDescription = "Favorite", 
                        tint = if (isFavorite) Color(0xFFBA1A1A) else Color(0xFF444748),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f),
                    color = Color(0xFF161C22)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFF006D36), modifier = Modifier.size(14.dp))
                    Text(product.rating.toString(), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = Color(0xFF006D36))
                }
            }
            
            Text(
                text = "₹${product.price}",
                style = MaterialTheme.typography.displaySmall,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF006D36),
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

@Composable
fun EmptyState(onReset: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White.copy(alpha = 0.3f))
            .border(1.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(24.dp))
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Default.SearchOff, contentDescription = null, modifier = Modifier.size(48.dp), tint = Color(0xFF444748))
        Spacer(modifier = Modifier.height(12.dp))
        Text("No Products Found", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
        Text(
            "We couldn't find any matches for your query. Try another keyword!", 
            style = MaterialTheme.typography.bodySmall, 
            textAlign = TextAlign.Center,
            color = Color(0xFF444748),
            modifier = Modifier.padding(top = 4.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onReset,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006D36)),
            shape = CircleShape
        ) {
            Text("Reset Search")
        }
    }
}
