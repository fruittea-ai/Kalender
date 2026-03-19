package com.proyek.kalender.ui.screens.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyek.kalender.domain.model.Event
import com.proyek.kalender.domain.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EventDetailUiState(
    val isLoading: Boolean = true,
    val event: Event? = null,
    val isCompleted: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class EventDetailViewModel @Inject constructor(
    private val repository: EventRepository,
    savedStateHandle: SavedStateHandle // Ini yang menangkap argumen dari navigasi Compose
) : ViewModel() {

    private val _uiState = MutableStateFlow(EventDetailUiState())
    val uiState: StateFlow<EventDetailUiState> = _uiState.asStateFlow()

    // Mengambil eventId yang dilempar dari AppNavigation
    private val eventId: String = checkNotNull(savedStateHandle["eventId"])

    init {
        loadEventDetail()
    }

    private fun loadEventDetail() {
        viewModelScope.launch {
            try {
                // Mengambil data dari Room berdasarkan ID
                val event = repository.getEventById(eventId)
                if (event != null) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            event = event,
                            isCompleted = event.isCompleted // Pastikan domain/model/Event.kt sudah punya val isCompleted: Boolean
                        )
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false, errorMessage = "Acara tidak ditemukan") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }

    fun markAsComplete() {
        viewModelScope.launch {
            try {
                repository.markAsCompleted(eventId)
                _uiState.update { it.copy(isCompleted = true) }
            } catch (e: Exception) {
                // Handle error jika diperlukan
            }
        }
    }
}