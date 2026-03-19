package com.proyek.kalender.domain.model


enum class EventCategory(val displayName: String, val colorHex: Long) {
    WORK("Work", 0xFF8EF4E9), // WorkMint
    PERSONAL("Personal", 0xFFFFA184), // PersonalOrange
    CREATIVE("Creative", 0xFFE9E5FF) // DeepWorkPurple
}

data class Event(
    val id: String,
    val title: String,
    val time: String,
    val location: String,
    val category: EventCategory,
    val isCompleted: Boolean = false
)