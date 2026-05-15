package com.budakattu.sante.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.budakattu.sante.data.model.CartItem
import com.budakattu.sante.data.model.sampleProducts
import com.budakattu.sante.ui.components.BottomNavBar
import com.budakattu.sante.ui.components.GlassCard
import com.budakattu.sante.ui.navigation.Screen
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    userRole: String = "buyer",
    onBack: () -> Unit,
    onNavigate: (String) -> Unit
) {
    val cartItems by com.budakattu.sante.data.CartManager.cartItems.collectAsState()

    var showCheckoutModal by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text("My Shopping Cart", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.secondary)
                    }
                },
                actions = {
                    IconButton(onClick = { com.budakattu.sante.data.CartManager.clearCart() }) {
                        Icon(Icons.Default.DeleteSweep, contentDescription = "Clear", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White.copy(alpha = 0.6f))
            )
        },
        bottomBar = {
            BottomNavBar(currentRoute = Screen.Cart.route, userRole = userRole, onNavigate = onNavigate)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(horizontal = 24.dp)
        ) {
            if (cartItems.isEmpty()) {
                EmptyCartView(onDiscover = { onNavigate("discover") })
            } else {
                Spacer(modifier = Modifier.height(24.dp))
                
                // Cart Items
                cartItems.forEach { item ->
                    CartItemCard(
                        item = item,
                        onIncrement = { com.budakattu.sante.data.CartManager.updateQuantity(item.product.id, item.quantity + 1) },
                        onDecrement = { com.budakattu.sante.data.CartManager.updateQuantity(item.product.id, item.quantity - 1) },
                        onRemove = { com.budakattu.sante.data.CartManager.removeItem(item.product.id) }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Summary
                CartSummaryCard(
                    cartItems = cartItems,
                    onCheckout = { showCheckoutModal = true }
                )

                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }

    if (showCheckoutModal) {
        CheckoutModal(
            totalAmount = calculateTotal(cartItems),
            onClose = { showCheckoutModal = false },
            onOrderSuccess = {
                com.budakattu.sante.data.CartManager.clearCart()
                showCheckoutModal = false
            }
        )
    }
}

@Composable
fun CartItemCard(
    item: CartItem,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onRemove: () -> Unit
) {
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp)),
        containerAlpha = 0.6f,
        borderAlpha = 0.3f
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = item.product.image,
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, Color.White.copy(alpha = 0.4f), RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Surface(
                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
                    shape = CircleShape,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f))
                ) {
                    Text(
                        text = item.product.artisanName.uppercase(),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                Text(text = item.product.name, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Text(text = "₹${item.product.price} / unit", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            
            Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.SpaceBetween) {
                IconButton(onClick = onRemove, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Delete, contentDescription = "Remove", tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(18.dp))
                }
                
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f), CircleShape)
                        .border(1.dp, Color.White.copy(alpha = 0.5f), CircleShape)
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    IconButton(onClick = onDecrement, modifier = Modifier.size(24.dp)) {
                        Text("-", fontWeight = FontWeight.Bold)
                    }
                    Text(text = "${item.quantity}", modifier = Modifier.padding(horizontal = 8.dp), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    IconButton(onClick = onIncrement, modifier = Modifier.size(24.dp)) {
                        Text("+", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun CartSummaryCard(cartItems: List<CartItem>, onCheckout: () -> Unit) {
    val subtotal = cartItems.sumOf { it.product.price * it.quantity }
    val premium = (subtotal * 0.1).toInt()
    val shipping = 40
    val total = subtotal + premium + shipping

    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp)),
        containerAlpha = 0.6f,
        borderAlpha = 0.3f
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Sourcing Livelihood Impact", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            SummaryRow("Products Subtotal", "₹$subtotal")
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                    .border(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Diversity3, contentDescription = null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Tribal Artisan Premium (10%)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
                }
                Text("+ ₹$premium", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            SummaryRow("Eco-friendly Shipping", "₹$shipping")
            
            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f), modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Total Fair Price", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("₹$total", fontWeight = FontWeight.Bold, fontSize = 22.sp, color = MaterialTheme.colorScheme.secondary)
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = onCheckout,
                modifier = Modifier.fillMaxWidth(),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                contentPadding = PaddingValues(16.dp)
            ) {
                Icon(Icons.Default.ShoppingCartCheckout, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Proceed to Checkout", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun SummaryRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
        Text(value, fontWeight = FontWeight.Medium, fontSize = 16.sp)
    }
}

@Composable
fun EmptyCartView(onDiscover: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp)
            .background(Color.White.copy(alpha = 0.6f), RoundedCornerShape(32.dp))
            .border(1.dp, Color.White, RoundedCornerShape(32.dp))
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.ShoppingBasket, contentDescription = null, modifier = Modifier.size(44.dp), tint = MaterialTheme.colorScheme.secondary)
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text("Your Cart is Empty", fontWeight = FontWeight.Bold, fontSize = 22.sp)
        Text(
            "Explore authentic tribal forest products to support rural cooperative communities.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onDiscover,
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
        ) {
            Icon(Icons.Default.Explore, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Discover Products")
        }
    }
}

@Composable
fun CheckoutModal(
    totalAmount: Int,
    onClose: () -> Unit,
    onOrderSuccess: () -> Unit
) {
    var currentStep by remember { mutableStateOf(1) }
    
    Dialog(onDismissRequest = onClose, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
                .clickable { onClose() },
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .clickable(enabled = false) {}, // Prevent closing when clicking card
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    // Modal Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Step $currentStep of 4: ${getStepName(currentStep)}",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.secondary,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = getStepTitle(currentStep),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        IconButton(onClick = onClose) {
                            Icon(Icons.Default.Close, contentDescription = "Close")
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    when (currentStep) {
                        1 -> CheckoutStepDetails(onNext = { currentStep = 2 })
                        2 -> CheckoutStepPayment(onBack = { currentStep = 1 }, onNext = { currentStep = 3 })
                        3 -> CheckoutStepLoading(onFinished = { currentStep = 4 })
                        4 -> CheckoutStepSuccess(totalAmount = totalAmount, onFinish = onOrderSuccess)
                    }
                }
            }
        }
    }
}

private fun getStepName(step: Int) = when(step) {
    1 -> "Customer Details"
    2 -> "Payment Method"
    3 -> "Processing"
    else -> "Confirmed"
}

private fun getStepTitle(step: Int) = when(step) {
    1 -> "Shipping & Delivery"
    2 -> "Secure Payment"
    3 -> "Securing Payment"
    else -> "Order Success"
}

@Composable
fun CheckoutStepDetails(onNext: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Please provide your contact and delivery address.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        
        OutlinedTextField(
            value = "", onValueChange = {}, 
            label = { Text("Full Name", fontSize = 10.sp) },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(18.dp)) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        )
        OutlinedTextField(
            value = "", onValueChange = {}, 
            label = { Text("Email Address", fontSize = 10.sp) },
            leadingIcon = { Icon(Icons.Default.Mail, contentDescription = null, modifier = Modifier.size(18.dp)) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        )
        OutlinedTextField(
            value = "", onValueChange = {}, 
            label = { Text("Phone Number", fontSize = 10.sp) },
            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(18.dp)) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        )
        OutlinedTextField(
            value = "", onValueChange = {}, 
            label = { Text("Delivery Address", fontSize = 10.sp) },
            leadingIcon = { Icon(Icons.Default.Home, contentDescription = null, modifier = Modifier.size(18.dp)) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            minLines = 2
        )
        
        Button(
            onClick = onNext,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
        ) {
            Text("Select Payment Method", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(8.dp))
            Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
        }
    }
}

@Composable
fun CheckoutStepPayment(onBack: () -> Unit, onNext: () -> Unit) {
    var selectedMethod by remember { mutableStateOf("upi") }
    
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Select your preferred secure payment method.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        
        PaymentMethodItem(
            id = "upi", title = "UPI (GPay / PhonePe)", desc = "Instant secure settlement", 
            icon = Icons.Default.QrCode2, isSelected = selectedMethod == "upi",
            onSelect = { selectedMethod = "upi" }
        )
        PaymentMethodItem(
            id = "card", title = "Credit or Debit Card", desc = "Visa, Mastercard, RuPay", 
            icon = Icons.Default.CreditCard, isSelected = selectedMethod == "card",
            onSelect = { selectedMethod = "card" }
        )
        PaymentMethodItem(
            id = "cod", title = "Cash on Delivery (COD)", desc = "Pay when harvest arrives", 
            icon = Icons.Default.Payments, isSelected = selectedMethod == "cod",
            onSelect = { selectedMethod = "cod" }
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.weight(1f).height(56.dp),
                shape = CircleShape
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Back")
            }
            Button(
                onClick = onNext,
                modifier = Modifier.weight(2f).height(56.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text("Pay & Confirm Order")
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(16.dp))
            }
        }
    }
}

@Composable
fun PaymentMethodItem(id: String, title: String, desc: String, icon: androidx.compose.ui.graphics.vector.ImageVector, isSelected: Boolean, onSelect: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth().clickable { onSelect() },
        color = if (isSelected) MaterialTheme.colorScheme.secondary.copy(alpha = 0.05f) else Color.Transparent,
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(desc, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            RadioButton(selected = isSelected, onClick = onSelect, colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.secondary))
        }
    }
}

@Composable
fun CheckoutStepLoading(onFinished: () -> Unit) {
    var progress by remember { mutableStateOf(0f) }
    var countdown by remember { mutableStateOf(10) }
    
    LaunchedEffect(Unit) {
        for (i in 1..100) {
            delay(50)
            progress = i / 100f
            if (i % 10 == 0) countdown--
        }
        onFinished()
    }
    
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center, modifier = Modifier.padding(vertical = 24.dp)) {
        Box(contentAlignment = Alignment.Center) {
            CircularProgressIndicator(progress = progress, modifier = Modifier.size(100.dp), strokeWidth = 8.dp, color = MaterialTheme.colorScheme.secondary)
            Icon(Icons.Default.Security, contentDescription = null, modifier = Modifier.size(36.dp).animateContentSize(), tint = MaterialTheme.colorScheme.secondary)
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        Text("Securing Payment Corridor", fontWeight = FontWeight.Bold)
        Text("Initializing encrypted tribal escrow corridor...", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Surface(color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f), shape = CircleShape) {
            Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Timer, contentDescription = null, modifier = Modifier.size(12.dp), tint = MaterialTheme.colorScheme.secondary)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Direct Settlement: ${countdown}s", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
            }
        }
    }
}

@Composable
fun CheckoutStepSuccess(totalAmount: Int, onFinish: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Box(
            modifier = Modifier.size(64.dp).background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.TaskAlt, contentDescription = null, modifier = Modifier.size(36.dp), tint = MaterialTheme.colorScheme.secondary)
        }
        
        Spacer(modifier = Modifier.height(20.dp))
        Text("Order Confirmed Successfully!", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Text("Your direct fair price transaction has settled.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Surface(
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OrderInfoRow("Order ID:", "BST-481940")
                OrderInfoRow("Livelihood Premium:", "100% Sourced", color = MaterialTheme.colorScheme.secondary)
                OrderInfoRow("Paid via:", "UPI")
                Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                OrderInfoRow("Amount Paid:", "₹$totalAmount", color = MaterialTheme.colorScheme.secondary, isBold = true)
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(onClick = { /* Track */ }, modifier = Modifier.fillMaxWidth(), shape = CircleShape) {
            Text("Track Order Live")
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedButton(onClick = onFinish, modifier = Modifier.fillMaxWidth(), shape = CircleShape) {
            Text("Awesome, back to Sante!")
        }
    }
}

@Composable
fun OrderInfoRow(label: String, value: String, color: Color = Color.Unspecified, isBold: Boolean = false) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, fontSize = 11.sp, fontWeight = if (isBold) FontWeight.Bold else FontWeight.Medium, color = color)
    }
}

private fun calculateTotal(items: List<CartItem>): Int {
    val subtotal = items.sumOf { it.product.price * it.quantity }
    return subtotal + (subtotal * 0.1).toInt() + 40
}
