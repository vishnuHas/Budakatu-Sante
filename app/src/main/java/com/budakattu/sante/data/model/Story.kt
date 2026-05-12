package com.budakattu.sante.data.model

data class Story(
    val id: String,
    val title: String,
    val author: String,
    val authorImage: String,
    val image: String,
    val description: String,
    val impact: String,
    val iconName: String
)

val sampleStories = listOf(
    Story(
        id = "s1",
        title = "The Weavers of Majuli",
        author = "Majuli Collective",
        authorImage = "https://lh3.googleusercontent.com/aida-public/AB6AXuCfk6O4zgikakZqFElSRZvj-JT123A3s23AmVEv55r2TNW3BCsqyt48s6RKKSHl6rR3f8pv4ipkE0HU0nKrDT6y_kNOJLCpKjtZLLQVVXDEtJf3wzPkEk4pP1UQa8S7vZi1o_ebB3gjWEgYzLAimHt8SwYRqCuU2YMERdjOqLXsrnyM_FY_ZBXKvMC20BNfBRn2YzkLWXHMhKWIy_eE428HmQzRAebjdhP4lteTK-OluYjshgzP16-97CUjzVJgQCymnVcNuiXthZ3Z",
        image = "https://lh3.googleusercontent.com/aida-public/AB6AXuAwwW7defKR7nwPVZJDZ5kkaR3fXe-vQdZe5pZ_Zg-qt_p1NG0_9fz6fUZuOhN4aZuHt23dxqjYsk9S165PD1ETrX_xaTzJoAhkca2UgkF0ui832jPuBLsRwdnSpJcYvFxXX8AxZu9THdXQ6pkq6ysXYy3BnxqlOei2d21xztg7C7w01zX58l92YaKVsloa2CZs6nHPFzpleb2TIE4qCFxfJp3vQGX5H0UacJrbbzR_xopXwj0UXWHUeyw5tsFCn1yjEVVTZo2rqNal",
        description = "Discovering the intricate patterns and sustainable practices of river island communities.",
        impact = "High",
        iconName = "eco"
    ),
    Story(
        id = "s2",
        title = "Protecting the Sacred Groves",
        author = "Western Ghats Watch",
        authorImage = "https://lh3.googleusercontent.com/aida-public/AB6AXuAqVFKentYCeB8emQzwkoA9TX3_7MGftGXRoJmqIibB6AbBu0IDdURtfA6zdkU8SytYiZ4vgdkMNaalpAfiSToFBIQ7AaLlAEf1pbPz2C3InaF5le6NndrzzerL1IimMztoEU-L0a86LrFFCE3mHEZdR2F7M76WYkA0SMrDGf2I3Y7D-CNYw4YfMkpOuEXWVUrtqTQ2WjUXW5Pi4s9LhDM_9swebuhJsd9bSeVMB_4hSUZJM0P8uFTSGx-b7_fa-1kybs21bSHMyMGW",
        image = "https://lh3.googleusercontent.com/aida-public/AB6AXuAqVFKentYCeB8emQzwkoA9TX3_7MGftGXRoJmqIibB6AbBu0IDdURtfA6zdkU8SytYiZ4vgdkMNaalpAfiSToFBIQ7AaLlAEf1pbPz2C3InaF5le6NndrzzerL1IimMztoEU-L0a86LrFFCE3mHEZdR2F7M76WYkA0SMrDGf2I3Y7D-CNYw4YfMkpOuEXWVUrtqTQ2WjUXW5Pi4s9LhDM_9swebuhJsd9bSeVMB_4hSUZJM0P8uFTSGx-b7_fa-1kybs21bSHMyMGW",
        description = "How your purchases directly fund conservation efforts in ancient forest ecosystems.",
        impact = "100%",
        iconName = "nature"
    )
)
