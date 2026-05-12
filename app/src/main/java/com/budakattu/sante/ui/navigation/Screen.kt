package com.budakattu.sante.ui.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")
    object Login : Screen("login")
    object Signup : Screen("signup")
    object Discover : Screen("discover")
    object SellerDashboard : Screen("seller_dashboard")
    object SellerDiscover : Screen("seller_discover")
    object Origin : Screen("origin")
    object Wishlist : Screen("wishlist")
    object Cart : Screen("cart")
    object Account : Screen("account")
    object Notifications : Screen("notifications")
    object PreOrder : Screen("pre_order/{productId}?quantity={quantity}") {
        fun createRoute(productId: String, quantity: Int) = "pre_order/$productId?quantity=$quantity"
    }
    object Tracking : Screen("tracking")
    object ProductDetail : Screen("detail/{productId}") {
        fun createRoute(productId: String) = "detail/$productId"
    }
}
