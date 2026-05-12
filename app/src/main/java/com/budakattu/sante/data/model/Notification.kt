package com.budakattu.sante.data.model

import androidx.compose.ui.graphics.vector.ImageVector

data class Notification(
    val id: String,
    val title: String,
    val description: String,
    val time: String,
    val type: NotificationType,
    val isRead: Boolean = false,
    val extraInfo: String? = null,
    val imageUrl: String? = null
)

enum class NotificationType {
    ORDER_UPDATE,
    TRIBAL_STORY,
    SYSTEM_ALERT,
    PROMOTION
}

val sampleNotifications = listOf(
    Notification(
        id = "1",
        title = "Shipment Dispatched",
        description = "Your order of Wild Forest Honey and Bamboo Crafts has left the origin center.",
        time = "2h ago",
        type = NotificationType.ORDER_UPDATE,
        isRead = false,
        extraInfo = "Track ID: TRK-9824-FRST"
    ),
    Notification(
        id = "2",
        title = "Order Delivered",
        description = "Your previous order has been delivered. How was your experience?",
        time = "Yesterday",
        type = NotificationType.ORDER_UPDATE,
        isRead = true
    ),
    Notification(
        id = "3",
        title = "New Artisan Story",
        description = "Read the journey of master weaver Kamali from the Nilgiri slopes.",
        time = "5h ago",
        type = NotificationType.TRIBAL_STORY,
        isRead = false,
        imageUrl = "https://images.unsplash.com/photo-1506744038136-46273834b3fb"
    ),
    Notification(
        id = "4",
        title = "Traceability Synced",
        description = "Your impact ledger and supply chain records have been successfully updated on the blockchain.",
        time = "Oct 12",
        type = NotificationType.SYSTEM_ALERT,
        isRead = true
    )
)
