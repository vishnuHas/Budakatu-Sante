package com.budakattu.sante

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.budakattu.sante.ui.components.SideDrawerContent
import com.budakattu.sante.data.PreferenceManager
import com.budakattu.sante.ui.navigation.Screen
import com.budakattu.sante.ui.screens.LoginScreen
import com.budakattu.sante.ui.screens.OnboardingScreen
import com.budakattu.sante.ui.screens.SignupScreen
import com.budakattu.sante.ui.screens.SplashScreen
import com.budakattu.sante.ui.screens.DiscoverScreen
import com.budakattu.sante.ui.screens.SellerDashboardScreen
import com.budakattu.sante.ui.screens.SellerDiscoverScreen
import com.budakattu.sante.ui.screens.NotificationScreen
import com.budakattu.sante.ui.screens.PreOrderScreen
import com.budakattu.sante.ui.screens.TrackingScreen
import com.budakattu.sante.ui.screens.OriginScreen
import com.budakattu.sante.ui.screens.WishlistScreen
import com.budakattu.sante.ui.screens.CartScreen
import com.budakattu.sante.ui.screens.AccountScreen
import com.budakattu.sante.ui.screens.ProductDetailScreen
import com.budakattu.sante.ui.screens.SellerViewModel
import com.budakattu.sante.ui.theme.BudakattuSanteTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BudakattuSanteTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val context = LocalContext.current
    val preferenceManager = remember { PreferenceManager(context) }
    val isOnboardingComplete by preferenceManager.isOnboardingComplete.collectAsState(initial = false)
    val isUserLoggedIn by preferenceManager.isUserLoggedIn.collectAsState(initial = false)
    val userRole by preferenceManager.userRole.collectAsState(initial = null)
    val userFullName by preferenceManager.userFullName.collectAsState(initial = null)
    val userLocation by preferenceManager.userLocation.collectAsState(initial = null)
    val userEmail by preferenceManager.userEmail.collectAsState(initial = null)
    
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    // Define safe routes that have explicit burger icons or support sidebar
    val drawerSupportedRoutes = listOf(
        Screen.Discover.route,
        Screen.Origin.route,
        Screen.Wishlist.route,
        Screen.Cart.route,
        Screen.Account.route,
        Screen.Notifications.route,
        Screen.Tracking.route
    )
    
    val isDrawerEnabled = userRole == "buyer" && drawerSupportedRoutes.contains(currentRoute)
    
    val sellerViewModel: SellerViewModel = viewModel()
    
    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = isDrawerEnabled,
        drawerContent = {
            if (isDrawerEnabled) {
                SideDrawerContent(
                    currentRoute = currentRoute,
                    userName = userFullName,
                    userLocation = userLocation,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onCloseDrawer = {
                        scope.launch { drawerState.close() }
                    },
                    onLogout = {
                        scope.launch {
                            preferenceManager.setUserLoggedIn(false, null)
                            drawerState.close()
                            navController.navigate(Screen.Login.route) {
                                popUpTo(0)
                            }
                        }
                    }
                )
            }
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route
        ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onTimeout = {
                    val nextScreen = when {
                        isUserLoggedIn -> {
                            if (userRole == "seller") Screen.SellerDashboard.route else Screen.Discover.route
                        }
                        isOnboardingComplete -> Screen.Login.route
                        else -> Screen.Onboarding.route
                    }
                    navController.navigate(nextScreen) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onFinish = {
                    scope.launch {
                        preferenceManager.setOnboardingComplete(true)
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Onboarding.route) { inclusive = true }
                        }
                    }
                }
            )
        }
        
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = { role ->
                    scope.launch {
                        preferenceManager.setUserLoggedIn(true, role)
                        val nextScreen = if (role == "seller") Screen.SellerDashboard.route else Screen.Discover.route
                        navController.navigate(nextScreen) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                },
                onNavigateToSignup = { role ->
                    navController.navigate("${Screen.Signup.route}/$role")
                }
            )
        }
        
        composable(
            route = "${Screen.Signup.route}/{role}",
            arguments = listOf(navArgument("role") { type = NavType.StringType })
        ) { backStackEntry ->
            val role = backStackEntry.arguments?.getString("role") ?: "seller"
            SignupScreen(
                initialRole = role,
                onBack = { navController.popBackStack() },
                onSignupSuccess = { name, email, location ->
                    scope.launch {
                        preferenceManager.saveUserDetails(name, email, location)
                        navController.navigate(Screen.Login.route) {
                            popUpTo("${Screen.Signup.route}/{role}") { inclusive = true }
                        }
                    }
                }
            )
        }
        
        composable(Screen.Discover.route) {
            DiscoverScreen(
                userRole = userRole ?: "buyer",
                onProductClick = { productId ->
                    navController.navigate(Screen.ProductDetail.createRoute(productId))
                },
                onNavigate = { route ->
                    navController.navigate(route)
                },
                onOpenDrawer = { scope.launch { drawerState.open() } }
            )
        }

        composable(Screen.SellerDashboard.route) {
            SellerDashboardScreen(
                viewModel = sellerViewModel,
                onNavigate = { route ->
                    navController.navigate(route)
                }
            )
        }

        composable(Screen.SellerDiscover.route) {
            SellerDiscoverScreen(
                viewModel = sellerViewModel,
                onNavigate = { route ->
                    navController.navigate(route)
                }
            )
        }

        composable(Screen.Origin.route) {
            OriginScreen(
                userRole = userRole ?: "buyer",
                onBack = { navController.popBackStack() },
                onNavigate = { route -> navController.navigate(route) },
                onProductClick = { productId ->
                    navController.navigate(Screen.ProductDetail.createRoute(productId))
                }
            )
        }

        composable(Screen.Wishlist.route) {
            WishlistScreen(
                userRole = userRole ?: "buyer",
                onBack = { navController.popBackStack() },
                onNavigate = { route -> navController.navigate(route) },
                onProductClick = { productId ->
                    navController.navigate(Screen.ProductDetail.createRoute(productId))
                },
                onOpenDrawer = { scope.launch { drawerState.open() } }
            )
        }

        composable(Screen.Cart.route) {
            CartScreen(
                onBack = { navController.popBackStack() },
                onNavigate = { route -> navController.navigate(route) }
            )
        }

        composable(Screen.Notifications.route) {
            NotificationScreen(
                userRole = userRole ?: "buyer",
                onBack = { navController.popBackStack() },
                onNavigate = { route -> navController.navigate(route) }
            )
        }

        composable(
            Screen.PreOrder.route,
            arguments = listOf(
                navArgument("productId") { type = NavType.StringType },
                navArgument("quantity") { type = NavType.IntType; defaultValue = 1 }
            )
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            val quantity = backStackEntry.arguments?.getInt("quantity") ?: 1
            PreOrderScreen(
                productId = productId,
                initialQuantity = quantity,
                onBack = { navController.popBackStack() },
                onNavigate = { route -> navController.navigate(route) }
            )
        }

        composable(Screen.Tracking.route) {
            TrackingScreen(
                onBack = { navController.popBackStack() },
                onNavigate = { route -> navController.navigate(route) }
            )
        }

        composable(Screen.Account.route) {
            AccountScreen(
                userRole = userRole ?: "buyer",
                userFullName = userFullName,
                userLocation = userLocation,
                userEmail = userEmail,
                onBack = { navController.popBackStack() },
                onNavigate = { route -> navController.navigate(route) },
                onOpenDrawer = { scope.launch { drawerState.open() } }
            )
        }

        composable(
            route = Screen.ProductDetail.route,
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")
            ProductDetailScreen(
                productId = productId,
                onBack = { navController.popBackStack() },
                onNavigate = { route -> navController.navigate(route) },
                onAddToCart = { product, quantity -> com.budakattu.sante.data.CartManager.addItem(product, quantity) }
            )
        }
        }
    }
}
