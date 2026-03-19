package com.proyek.kalender.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.proyek.kalender.data.local.dao.EventDao
import com.proyek.kalender.data.local.entity.EventEntity

@Database(
    entities = [EventEntity::class],
    version = 1,
    exportSchema = false
)
abstract class KalenderDatabase : RoomDatabase() {
    abstract val eventDao: EventDao
}