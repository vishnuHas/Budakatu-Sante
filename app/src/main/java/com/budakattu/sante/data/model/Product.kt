package com.budakattu.sante.data.model

data class Review(
    val reviewer: String,
    val rating: Int,
    val date: String,
    val comment: String,
    val image: String? = null
)

@androidx.room.Entity(tableName = "products")
data class Product(
    @androidx.room.PrimaryKey
    val id: String,
    var name: String,
    var category: String,
    var price: Int,
    var image: String,
    var badge: String,
    var rating: Float,
    var reviews: Int,
    var description: String,
    var origin: String,
    var stock: Int,
    var artisanName: String,
    var artisanBio: String,
    var artisanAvatar: String,
    var location: String,
    var map: String
) {
    @androidx.room.Ignore
    var reviewsList: List<Review> = emptyList()
}

val sampleReviews = listOf(
    Review("Meera Hegde", 5, "2 days ago", "Absolutely authentic and rich taste! This is the real deal."),
    Review("Suresh G.", 5, "3 days ago", "Deep natural flavor. Unbelievable quality and pure essence."),
    Review("Srinivas Rao", 4, "1 week ago", "Very secure and beautiful packaging. Tastes very fresh.")
)

val sampleProducts = listOf(
    // HONEY
    Product(
        id = "h1", name = "Raw Forest Honey", category = "Honey", price = 450,
        image = "https://images.unsplash.com/photo-1587049352846-4a222e784d38?auto=format&fit=crop&q=80&w=600",
        badge = "Fair Price", rating = 4.9f, reviews = 142,
        description = "Precious multi-floral honey harvested sustainably by Soliga tribal honey-hunters.",
        origin = "Soliga Community, BR Hills", stock = 15, artisanName = "Kala & Family",
        artisanBio = "Master Harvesters", artisanAvatar = "https://images.unsplash.com/photo-1544005313-94ddf0286df2?auto=format&fit=crop&q=80&w=300",
        location = "Western Ghats", map = ""
    ).apply { reviewsList = sampleReviews },
    Product(
        id = "h2", name = "Stingless Bee Honey", category = "Honey", price = 850,
        image = "https://images.unsplash.com/photo-1558642452-9d2a7deb7f62?auto=format&fit=crop&q=80&w=600",
        badge = "Rare Find", rating = 5.0f, reviews = 45,
        description = "Medicinal Putanathene honey collected from small hollows in the forest.",
        origin = "Jenu Kuruba Tribe", stock = 5, artisanName = "Bomman",
        artisanBio = "Traditional Healer", artisanAvatar = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&q=80&w=300",
        location = "Nagarahole", map = ""
    ),
    Product(
        id = "h3", name = "Wild Rock Honey", category = "Honey", price = 520,
        image = "https://images.unsplash.com/photo-1471943311424-646960669fbc?auto=format&fit=crop&q=80&w=600",
        badge = "Pure", rating = 4.7f, reviews = 88,
        description = "Harvested from high cliff rock faces deep in the jungle.",
        origin = "Irula Community", stock = 12, artisanName = "Mani",
        artisanBio = "Cliff Climber", artisanAvatar = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&q=80&w=300",
        location = "Nilgiris", map = ""
    ),

    // BAMBOO
    Product(
        id = "b4", name = "Bamboo Serving Tray", category = "Bamboo", price = 650,
        image = "https://images.unsplash.com/photo-1513519245088-0e12902e5a38?auto=format&fit=crop&q=80&w=600",
        badge = "Eco-Friendly", rating = 4.6f, reviews = 42,
        description = "Minimalist serving tray for tea and snacks.",
        origin = "Paniya Tribe", stock = 15, artisanName = "Balan",
        artisanBio = "Woodwork Artist", artisanAvatar = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&q=80&w=300",
        location = "Wayanad", map = ""
    ),
    Product(
        id = "b5", name = "Bamboo Flute", category = "Bamboo", price = 300,
        image = "https://images.unsplash.com/photo-1601314167099-232775b3d6fd?auto=format&fit=crop&q=80&w=600",
        badge = "Instrument", rating = 4.9f, reviews = 95,
        description = "Professionally tuned bamboo flute for beginners.",
        origin = "Soliga Musicians", stock = 30, artisanName = "Kencha",
        artisanBio = "Flutist & Maker", artisanAvatar = "https://images.unsplash.com/photo-1599566150163-29194dcaad36?auto=format&fit=crop&q=80&w=300",
        location = "Biligirirangana", map = ""
    ),

    // HERBAL
    Product(
        id = "e3", name = "Forest Turmeric", category = "Herbal", price = 150,
        image = "https://images.unsplash.com/photo-1615485290382-441e4d049cb5?auto=format&fit=crop&q=80&w=600",
        badge = "High Curcumin", rating = 4.7f, reviews = 200,
        description = "Deep orange turmeric with very high medicinal value.",
        origin = "Soliga Community", stock = 200, artisanName = "Lakshmi",
        artisanBio = "Processing Head", artisanAvatar = "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?auto=format&fit=crop&q=80&w=300",
        location = "BR Hills", map = ""
    ),
    Product(
        id = "nb1", name = "Nano Elaichi Banana", category = "Herbal", price = 180,
        image = "file:///android_asset/nano_banana_ultra.png",
        badge = "Rare Forest", rating = 4.9f, reviews = 75,
        description = "Extremely sweet, nutrient-dense miniature forest bananas harvested from organic wild groves in the Western Ghats canopy.",
        origin = "Malaikudi Collective", stock = 40, artisanName = "Malaikudi Team",
        artisanBio = "Hill Cultivators", artisanAvatar = "https://images.unsplash.com/photo-1517841905240-472988babdf9?auto=format&fit=crop&q=80&w=300",
        location = "Western Ghats", map = ""
    ),

    // HANDMADE
    Product(
        id = "m3", name = "Bead Necklace", category = "Handmade", price = 450,
        image = "https://images.unsplash.com/photo-1515562141207-7a88fb7ce338?auto=format&fit=crop&q=80&w=600",
        badge = "Traditional", rating = 4.7f, reviews = 65,
        description = "Hand-threaded glass and wood beads in traditional patterns.",
        origin = "Hakkipikki Women", stock = 30, artisanName = "Lakshmi",
        artisanBio = "Jewelry Artist", artisanAvatar = "https://images.unsplash.com/photo-1544005313-94ddf0286df2?auto=format&fit=crop&q=80&w=300",
        location = "Bannerghatta", map = ""
    ),
    Product(
        id = "m4", name = "Wooden Carved Totem", category = "Handmade", price = 1800,
        image = "https://images.unsplash.com/photo-1582555172866-f73bb12a2ab3?auto=format&fit=crop&q=80&w=600",
        badge = "Handcarved", rating = 4.8f, reviews = 15,
        description = "Detailed wood carving representing forest deities.",
        origin = "Soliga Woodworkers", stock = 3, artisanName = "Kariyan",
        artisanBio = "Sculptor", artisanAvatar = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&q=80&w=300",
        location = "Biligiri", map = ""
    )
)
