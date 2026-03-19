package com.proyek.kalender.ui.screens.addevent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyek.kalender.domain.model.Event
import com.proyek.kalender.domain.model.EventCategory
import com.proyek.kalender.domain.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

data class AddEventUiState(
    val title: String = "",
    val date: String = "",
    val time: String = "",
    val location: String = "",
    val selectedCategory: EventCategory = EventCategory.WORK,
    val isSaving: Boolean = false,
    val isSavedSuccess: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class AddEventViewModel @Inject constructor(
    private val repository: EventRepository // Disuntikkan otomatis oleh Hilt
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddEventUiState())
    val uiState: StateFlow<AddEventUiState> = _uiState.asStateFlow()

    fun onTitleChange(newTitle: String) { _uiState.update { it.copy(title = newTitle, errorMessage = null) } }
    fun onDateChange(newDate: String) { _uiState.update { it.copy(date = newDate) } }
    fun onTimeChange(newTime: String) { _uiState.update { it.copy(time = newTime) } }
    fun onLocationChange(newLocation: String) { _uiState.update { it.copy(location = newLocation) } }
    fun onCategorySelect(category: EventCategory) { _uiState.update { it.copy(selectedCategory = category) } }

    fun saveEvent() {
        val currentState = _uiState.value

        if (currentState.title.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Title cannot be empty") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, errorMessage = null) }

            try {
                // Membuat objek Event baru dengan ID unik (UUID)
                val newEvent = Event(
                    id = UUID.randomUUID().toString(),
                    title = currentState.title,
                    time = currentState.time,
                    location = currentState.location,
                    category = currentState.selectedCategory
                )

                // Menyimpan ke Room Database
                repository.insertEvent(newEvent)

                _uiState.update {
                    it.copy(
                        isSaving = false,
                        isSavedSuccess = true,
                        title = "", date = "", time = "", location = ""
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isSaving = false, errorMessage = "Gagal menyimpan: ${e.message}") }
            }
        }
    }

    fun resetSaveState() {
        _uiState.update { it.copy(isSavedSuccess = false) }
    }
}