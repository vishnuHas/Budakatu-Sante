<div align="center">
  <img src="./logo.png" width="180" alt="Budakattu Sante Logo"/>
  
  # Budakattu Sante 🌿
  **The Sovereign Indigenous Forest Marketplace**

  [![Native Android](https://img.shields.io/badge/Platform-Android%20Native-green?style=for-the-badge&logo=android)](https://developer.android.com)
  [![Kotlin](https://img.shields.io/badge/Language-Kotlin%201.9-blue?style=for-the-badge&logo=kotlin)](https://kotlinlang.org)
  [![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-purple?style=for-the-badge&logo=jetpackcompose)](https://developer.android.com/jetpack/compose)

</div>

---

## 🌲 Introduction

**Budakattu Sante** is a revolutionary native Android application dedicated to bridging the gap between indigenous tribal communities and global consumers. It transforms remote forest economies into thriving digital ecosystems, enabling traditional honey-hunters, master weavers, and organic farmers to showcase premium, rare forest goods directly to conscious patrons without exploitative intermediaries.

Built with modern technologies, the app enforces deep-rooted cultural values into a hyper-premium digital shopping and trading experience.

## ✨ Unique & Groundbreaking Features

### 📡 Dual-Portal Ecosystem
Seamlessly switches between two dedicated user interfaces tailored perfectly for their intended workflows:
- **Artisan (Seller) Dashboard:** Simplified inventory control, trade order pipelines, direct-to-consumer pricing adjustment, and simple product digitization.
- **Patron (Buyer) Marketplace:** Premium discovery views focusing on product traceability, storytelling, and glassmorphic high-engagement visuals.

### 🛡️ Real-World Sourcing & Traceability
Every product is hardcoded with real location metadata connecting it back to the actual tribes in **Western Ghats**, **Nilgiris**, and beyond. Patrons get clear visibility into the specific forest ranges and communities cultivating their goods.

### 🤖 Hyper-Premium Product Enrichment
Integrates natively generated AI photography and narrative enhancers (like our signature **Nano Elaichi Banana** generator) to ensure traditional artisan products look globally competitive alongside legacy premium brands.

### 🚀 Indigenous-First Commerce Design
Incorporates high-contrast, dark-mode-first aesthetics and dynamic micro-animations tailored for immersive storytelling rather than generic grid-based retail.

---

## 📸 Application Interface

| Discover Marketplace | Coop Sourcing Origins | Forest Patron Account |
| :---: | :---: | :---: |
| <img src="./docs/screenshots/screenshot_discover.png" width="250" alt="Discover"/> | <img src="./docs/screenshots/screenshot_origin.png" width="250" alt="Origin"/> | <img src="./docs/screenshots/screenshot_home.png" width="250" alt="Account"/> |

---

## 🛠️ Technology Stack

Budakattu Sante is a pure modern Android application. It relies on zero external dependencies from web prototypes to ensure maximum device performance and ultra-low latency.

- **Language:** 100% Type-Safe Kotlin
- **UI Engine:** Jetpack Compose (Stateless declarative UI logic)
- **Design Language:** Material 3 + Custom Glassmorphism overlay engine
- **Persistence Architecture:** 
  - **Android Jetpack DataStore:** Lightweight, asynchronous preferences engine managing user runtime identities and locations locally.
  - **Room Database:** Structured local relational models for product, cart, and order logic.
- **Concurrency:** Kotlin Coroutines & SharedFlow architecture for state bubbling.
- **Image Loader:** Coil (Coroutines Image Loader) with integrated vector and direct asset caching.

---

## 📦 Application Layout

```text
├── app/src/main/java/com/budakattu/sante/
│   ├── data/               # Persistence logic (PreferenceManager, Cart, LocalDB)
│   ├── ui/
│   │   ├── components/     # Shared Atomic UI (SideDrawer, GlassComponents)
│   │   ├── navigation/     # Typed Jetpack Navigation routers
│   │   ├── screens/        # Pure View layouts for both Buyer & Seller
│   │   └── theme/          # Premium Color palettes, Fonts & Typesets
│   └── MainActivity.kt     # Root navigation entry and state aggregator
```

---

## 🚀 How to Run

1.  Clone the repository to your local workstation.
2.  Ensure you have **Java JDK 17+** and **Android SDK** installed.
3.  Connect an Android device or launch an emulator.
4.  Execute the build wrapper from the root directory:
    ```bash
    ./gradlew installDebug
    ```
5.  The application will auto-launch with optimized native layouts.

---

<div align="center">
  Made for and inspired by the guardians of our forests.
</div>
