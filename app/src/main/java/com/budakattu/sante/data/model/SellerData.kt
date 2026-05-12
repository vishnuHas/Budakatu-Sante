package com.budakattu.sante.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "verifications")
data class VerificationRequest(
    @PrimaryKey val id: String,
    val name: String,
    val location: String,
    val category: String,
    val image: String
)

val sampleVerifications = listOf(
    VerificationRequest(
        id = "1",
        name = "Maya Cooperative",
        location = "Guatemala • Wild Cardamom",
        category = "Spices",
        image = "https://lh3.googleusercontent.com/aida-public/AB6AXuBuHI62MDp8jMpkTU4fZxWYYMGpHg2j6oJcj5i7bYATsOWx9nMQq7pHZ0vVWxkQM4n9tWcbM8nY2U4pK9YzANbBryKQDLuM1GdD6zeGzbyyJReU-TArVg5qeX46Jx5hbJy44_GZXnJmaVBCZuW4UN4ik6qRdnYFlpPQ6S8NnHdfyqiyeGiPQesAqwjXsj610irCobmUwbnVCe02RMarxxDeodi_-yYbkQOk5jp-jVAuC7YtFgwNkeeFOYAkR7mg_kjN-jqrZU7uGUvG"
    ),
    VerificationRequest(
        id = "2",
        name = "Kerala Spice Roots",
        location = "India • Black Pepper",
        category = "Spices",
        image = "https://lh3.googleusercontent.com/aida-public/AB6AXuCIc9DKe9McUv7QK6ZuGa_gWQKjmtnwmgXaiUtDgqwMCbgqtXKdRQKLa1nYm0Tm9z7uwGEah8kiI8W1xhzJHh0rTRqOqJcumiFePkYRJeyDPhudS6-JCQJLUvU_v4cR3jtoAztHhNn9aP3u0A_xBnx5sGRUUc6ouEkxgywPQQ11N3qNvPYkFaPO-oNumsQ4-78lnqny-6OCUeAOFcMbQjznJd6jXimSVtZ0D2LwX_v0w0J2AK4Z1DzMZugPh2iqkF_wUx9JZB-y2dUS"
    )
)

// Seller side products will use the central Product model from Product.kt
// Orders will use the central Order model from Order.kt
