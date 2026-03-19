package com.proyek.kalender.data.repository

import com.proyek.kalender.data.local.dao.EventDao
import com.proyek.kalender.data.local.entity.EventEntity
import com.proyek.kalender.domain.model.Event
import com.proyek.kalender.domain.model.EventCategory
import com.proyek.kalender.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class EventRepositoryImpl(
    private val dao: EventDao
) : EventRepository {

    override fun getAllEvents(): Flow<List<Event>> {
        return dao.getAllEvents().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override suspend fun getEventById(id: String): Event? {
        return dao.getEventById(id)?.toDomainModel()
    }

    override suspend fun insertEvent(event: Event) {
        dao.insertEvent(event.toEntity())
    }

    override suspend fun markAsCompleted(id: String) {
        dao.markEventAsCompleted(id)
    }

    // Fungsi ekstensi (Extension Functions) untuk konversi data
    private fun EventEntity.toDomainModel(): Event {
        return Event(
            id = id,
            title = title,
            date = date,
            time = time,
            location = location,
            category = EventCategory.valueOf(category),
            isCompleted = isCompleted
            // Tambahkan val isCompleted: Boolean = false pada file domain/model/Event.kt milikmu
        )
    }

    private fun Event.toEntity(): EventEntity {
        return EventEntity(
            id = id,
            title = title,
            date = date,
            time = time,
            location = location,
            category = category.name,
            isCompleted = false
        )
    }
}