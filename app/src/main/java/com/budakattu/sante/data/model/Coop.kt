package com.budakattu.sante.data.model

data class Coop(
    val id: String,
    val title: String,
    val type: String,
    val location: String,
    val image: String,
    val description: String,
    val families: String,
    val method: String,
    val iconName: String,
    val category: String,
    val btnText: String
)

val sampleCoops = mapOf(
    "soliga" to Coop(
        id = "soliga",
        title = "Soliga Tribe Cooperative",
        type = "Indigenous Cooperative",
        location = "Biligirirangana Hills, KA",
        image = "https://lh3.googleusercontent.com/aida-public/AB6AXuBwOZZyxJUtzLu7L2AkwI0ZpZJmW0xay47MOrydj8Ob-6zStqrxyp-AfDFhbifCi1e4rOQVKIGW_Tq7ZJAFRzrbAeeNBjx_WDPUJ--WsbbUe1mBAZ0vrvHhOLvEbbTuZtpiC_B1K2s6WCxRzGS_zmvKXLM6Ii5eXzdmO5pMyGmTAxt-s6tkakDaI3Q6nF9JRGeIL5QwoBjwKEs6iRhMR8vSPE03q4kiS5N6KJfjmVlH9if1LoWOFGUToIgzHcxPXg04-Ep_yB-LvSak",
        description = "Living deep within the dense subtropical forests of BR Hills, the Soliga tribe holds a sacred bond with nature. They collect raw honey using traditional techniques, honoring the forest and ensuring bees continue to thrive naturally.",
        families = "120+ Families",
        method = "100% Sustainable",
        iconName = "verified",
        category = "honey",
        btnText = "Browse Honey Products"
    ),
    "irula" to Coop(
        id = "irula",
        title = "Irula Honey Cooperative",
        type = "Nilgiris Rock Sourcing Co-op",
        location = "Nilgiris Biosphere, TN",
        image = "https://images.unsplash.com/photo-1587049352846-4a222e784d38?auto=format&fit=crop&q=80&w=500",
        description = "Living within the high-altitude misty cliffs of the Nilgiris Biosphere, the Irula Cooperative collects premium Wild Rock Honey.",
        families = "85+ Families",
        method = "High-Cliff Harvesting",
        iconName = "forest",
        category = "honey",
        btnText = "Browse honey products"
    )
)
