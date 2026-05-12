package com.budakattu.sante.data

import com.budakattu.sante.data.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object WishlistManager {
    private val _wishlistItems = MutableStateFlow<List<Product>>(emptyList())
    val wishlistItems: StateFlow<List<Product>> = _wishlistItems.asStateFlow()

    fun toggleItem(product: Product) {
        _wishlistItems.update { currentItems ->
            if (currentItems.any { it.id == product.id }) {
                currentItems.filter { it.id != product.id }
            } else {
                currentItems + product
            }
        }
    }

    fun removeItem(productId: String) {
        _wishlistItems.update { currentItems ->
            currentItems.filter { it.id != productId }
        }
    }

    fun isFavorite(productId: String): Boolean {
        return _wishlistItems.value.any { it.id == productId }
    }
}
