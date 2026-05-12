package com.budakattu.sante.data.model

import java.util.*

@androidx.room.Entity(tableName = "orders")
data class Order(
    @androidx.room.PrimaryKey
    var id: String,
    var date: java.util.Date,
    var total: Double,
    var status: String,
    var trackingId: String,
    var deliveryAddress: String = "142 Oakwood Drive, Bangalore, KA"
) {
    @androidx.room.Ignore
    var items: List<OrderItem> = emptyList()
}

data class OrderItem(
    val productId: String,
    val title: String,
    val quantity: Int,
    val price: Double
)

val sampleOrders = listOf(
    Order(
        id = "ORD-942-F",
        date = Date(),
        total = 490.0,
        status = "Shipped",
        trackingId = "TRK-983102-FRST"
    ).apply {
        items = listOf(OrderItem("h1", "Raw Forest Honey", 1, 450.0))
    },
    Order(
        id = "ORD-839-G",
        date = Date(System.currentTimeMillis() - 86400000),
        total = 950.0,
        status = "Processing",
        trackingId = "TRK-983103-BAMB"
    ).apply {
        items = listOf(OrderItem("b2", "Sleek Bamboo Flask", 2, 450.0))
    }
)
