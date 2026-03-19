package com.proyek.kalender.domain.repository

import com.proyek.kalender.domain.model.Event
import kotlinx.coroutines.flow.Flow

interface EventRepository {
    fun getAllEvents(): Flow<List<Event>>
    suspend fun getEventById(id: String): Event?
    suspend fun insertEvent(event: Event)
    suspend fun markAsCompleted(id: String)
    suspend fun deleteEvent(id: String)
}