package com.proyek.kalender.ui.screens.schedule

import com.proyek.kalender.domain.model.Event

data class ScheduleUiState(
    val isLoading: Boolean = false,
    val data: List<Event> = emptyList(),
    val errorMessage: String? = null
)