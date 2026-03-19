package com.proyek.kalender.domain.model

enum class EventCategory(val displayName: String, val colorHex: Long) {
    WORK("Work", 0xFF8EF4E9),
    PERSONAL("Personal", 0xFFFFA184),
    CREATIVE("Creative", 0xFFE9E5FF)
}

data class Event(
    val id: String,
    val title: String,
    val date: String = "", // Tambahkan ini
    val time: String,
    val location: String,
    val category: EventCategory,
    val isCompleted: Boolean = false
)