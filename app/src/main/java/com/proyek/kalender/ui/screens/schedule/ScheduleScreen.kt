package com.proyek.kalender.ui.screens.schedule

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.proyek.kalender.domain.model.Event
import com.proyek.kalender.ui.components.EditorialTopBar
import com.proyek.kalender.ui.components.EventCard

@Composable
fun ScheduleScreen(
    modifier: Modifier = Modifier,
    viewModel: ScheduleViewModel = hiltViewModel(),
    onEventClick: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = modifier.fillMaxSize()) {
        // Menambahkan header seragam
        EditorialTopBar()

        Box(modifier = Modifier.fillMaxSize().weight(1f)) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Color(0xFF4956B4)
                    )
                }
                uiState.errorMessage != null -> {
                    Text(
                        text = uiState.errorMessage ?: "Terjadi kesalahan",
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.Red
                    )
                }
                uiState.data.isEmpty() -> {
                    Text(
                        text = "Belum ada jadwal acara.",
                        modifier = Modifier.align(Alignment.Center),
                        color = Color(0xFF302E56)
                    )
                }
                else -> {
                    ScheduleListContent(
                        events = uiState.data,
                        onEventClick = onEventClick
                    )
                }
            }
        }
    }
}

@Composable
private fun ScheduleListContent(
    events: List<Event>,
    modifier: Modifier = Modifier,
    onEventClick: (String) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        contentPadding = PaddingValues(bottom = 100.dp) // Padding ekstra agar daftar tidak tertutup Bottom Navigation
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "UPCOMING SCHEDULE",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4956B4)
            )
            Text(
                text = "All Events",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF302E56)
            )
            Spacer(modifier = Modifier.height(32.dp))
        }

        items(
            items = events,
            key = { event -> event.id }
        ) { event ->
            EventCard(
                // Parameter category sudah dihapus di sini
                title = event.title,
                time = event.time,
                location = event.location,
                backgroundColor = Color(event.category.colorHex),
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .clickable { onEventClick(event.id) }
            )
        }
    }
}