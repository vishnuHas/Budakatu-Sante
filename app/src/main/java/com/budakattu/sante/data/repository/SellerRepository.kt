package com.budakattu.sante.data.repository

import com.budakattu.sante.data.local.OrderDao
import com.budakattu.sante.data.local.ProductDao
import com.budakattu.sante.data.local.VerificationDao
import com.budakattu.sante.data.model.Order
import com.budakattu.sante.data.model.Product
import com.budakattu.sante.data.model.VerificationRequest
import com.budakattu.sante.data.model.sampleOrders
import com.budakattu.sante.data.model.sampleProducts
import com.budakattu.sante.data.model.sampleVerifications
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class SellerRepository(
    private val orderDao: OrderDao,
    private val productDao: ProductDao,
    private val verificationDao: VerificationDao
) {
    val allOrders: Flow<List<Order>> = orderDao.getAllOrders()
    val allProducts: Flow<List<Product>> = productDao.getAllProducts()
    val allVerifications: Flow<List<VerificationRequest>> = verificationDao.getAllVerifications()

    suspend fun initializeDataIfEmpty() {
        val orders = orderDao.getAllOrders().first()
        if (orders.isEmpty()) {
            orderDao.insertAll(sampleOrders)
        }

        val products = productDao.getAllProducts().first()
        if (products.isEmpty()) {
            productDao.insertAll(sampleProducts)
        }

        val verifications = verificationDao.getAllVerifications().first()
        if (verifications.isEmpty()) {
            verificationDao.insertAll(sampleVerifications)
        }
    }

    suspend fun updateOrderStatus(orderId: String, newStatus: String) {
        val order = orderDao.getOrderById(orderId)
        if (order != null) {
            order.status = newStatus
            orderDao.updateOrder(order)
        }
    }

    suspend fun updateProductStock(product: Product, newStock: Int) {
        val updatedProduct = product.copy(stock = newStock)
        productDao.updateProduct(updatedProduct)
    }

    suspend fun insertProduct(product: Product) {
        productDao.insertProduct(product)
    }
}
