package com.proyek.kalender.ui.screens.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyek.kalender.domain.model.Event
import com.proyek.kalender.domain.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val repository: EventRepository
) : ViewModel() {

    // Langsung menggunakan ScheduleUiState dari file ScheduleUiState.kt
    private val _uiState = MutableStateFlow(ScheduleUiState(isLoading = true))
    val uiState: StateFlow<ScheduleUiState> = _uiState.asStateFlow()

    init {
        loadEventsFromDatabase()
    }

    private fun loadEventsFromDatabase() {
        viewModelScope.launch {
            try {
                repository.getAllEvents().collect { events ->
                    _uiState.value = ScheduleUiState(
                        isLoading = false,
                        data = events
                    )
                }
            } catch (e: Exception) {
                _uiState.value = ScheduleUiState(
                    isLoading = false,
                    errorMessage = "Gagal memuat jadwal: ${e.message}"
                )
            }
        }
    }
}