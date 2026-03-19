# 🗓️ The Editorial Chronology

A premium, production-ready Android scheduling application designed for content planners, editors, and highly organized individuals. Built entirely from scratch using modern Android development standards.

## ✨ Key Features
* **Dynamic Calendar Grid:** Custom-built, pixel-perfect asymmetric calendar UI with real-time event indicators.
* **Smart Event Management:** Full CRUD capabilities with smooth Jetpack Compose *Swipe-to-Delete* animations.
* **Persistent Storage:** Offline-first architecture using Room Database.
* **Interactive Form:** Integrated Material 3 Date and Time Pickers for seamless data entry.
* **Edge-to-Edge UI:** Transparent system bars and custom typography (Manrope) for a high-end visual experience.

## 🛠️ Tech Stack & Architecture
This project strictly adheres to **Clean Architecture** principles, separating concerns into Data, Domain, and Presentation layers.

* **Language:** Kotlin
* **UI Toolkit:** Jetpack Compose (Material 3)
* **Architecture:** Clean Architecture + MVVM
* **State Management:** UDF (Unidirectional Data Flow) with `UiState` and Kotlin `StateFlow`
* **Dependency Injection:** Dagger Hilt
* **Local Database:** Room Database with Kotlin Coroutines & Flow
* **Navigation:** Jetpack Navigation Compose (Type-safe routes)

## 🏗️ Project Structure
```text
com.proyek.kalender
│
├── data/               # Local DB, DAOs, and Repository Implementation
├── domain/             # Models and Repository Interfaces (Business Logic)
├── ui/
│   ├── components/     # Reusable Compose components (EventCard, TopBar)
│   ├── navigation/     # NavHost and Route definitions
│   ├── screens/        # UI Screens and ViewModels (Calendar, Schedule, AddEvent)
│   └── theme/          # Custom Type, Colors, and Theme setups
└── MainActivity.kt
