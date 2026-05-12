package com.budakattu.sante.ui.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.budakattu.sante.data.local.AppDatabase
import com.budakattu.sante.data.model.Order
import com.budakattu.sante.data.model.Product
import com.budakattu.sante.data.model.VerificationRequest
import com.budakattu.sante.data.repository.SellerRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SellerViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: SellerRepository

    val orders: StateFlow<List<Order>>
    val products: StateFlow<List<Product>>
    val verifications: StateFlow<List<VerificationRequest>>

    init {
        val database = AppDatabase.getDatabase(application)
        repository = SellerRepository(
            database.orderDao(),
            database.productDao(),
            database.verificationDao()
        )

        orders = repository.allOrders.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        products = repository.allProducts.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        verifications = repository.allVerifications.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        viewModelScope.launch {
            repository.initializeDataIfEmpty()
        }
    }

    fun advanceOrderStatus(orderId: String, currentStatus: String) {
        val newStatus = when (currentStatus) {
            "Processing" -> "Shipped"
            "Shipped" -> "Delivered"
            else -> "Processing"
        }
        viewModelScope.launch {
            repository.updateOrderStatus(orderId, newStatus)
        }
    }

    fun adjustProductStock(product: Product, delta: Int) {
        val newStock = (product.stock + delta).coerceAtLeast(0)
        viewModelScope.launch {
            repository.updateProductStock(product, newStock)
        }
    }

    fun addProduct(product: Product) {
        viewModelScope.launch {
            repository.insertProduct(product)
        }
    }
}
