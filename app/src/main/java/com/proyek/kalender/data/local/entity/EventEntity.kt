package com.proyek.kalender.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val date: String,
    val time: String,
    val location: String,
    val category: String, // Disimpan sebagai String, bukan Enum
    val isCompleted: Boolean
)