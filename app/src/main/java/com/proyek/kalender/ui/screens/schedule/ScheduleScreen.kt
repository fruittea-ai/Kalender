package com.proyek.kalender.ui.screens.schedule

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.ui.draw.clip
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
                        viewModel = viewModel,
                        onEventClick = onEventClick
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScheduleListContent(
    events: List<Event>,
    modifier: Modifier = Modifier,
    viewModel: ScheduleViewModel, // Kita butuh viewModel di sini untuk memanggil deleteEvent
    onEventClick: (String) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        contentPadding = PaddingValues(bottom = 100.dp)
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
            // State untuk mengontrol aksi geser
            val dismissState = rememberSwipeToDismissBoxState(
                confirmValueChange = { dismissValue ->
                    if (dismissValue == SwipeToDismissBoxValue.EndToStart) {
                        // Jika digeser penuh dari kanan ke kiri, hapus event
                        viewModel.deleteEvent(event.id)
                        true
                    } else {
                        false
                    }
                }
            )

            SwipeToDismissBox(
                state = dismissState,
                modifier = Modifier.padding(bottom = 16.dp), // Pindahkan padding ke pembungkus luar
                enableDismissFromStartToEnd = false, // Matikan geser ke kanan
                backgroundContent = {
                    val color by animateColorAsState(
                        targetValue = if (dismissState.targetValue == SwipeToDismissBoxValue.EndToStart) {
                            Color(0xFFFF5252) // Merah error saat digeser
                        } else {
                            Color.Transparent
                        },
                        label = "color_animation"
                    )

                    // Latar belakang yang terlihat saat kartu digeser
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(24.dp))
                            .background(color)
                            .padding(end = 24.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Event",
                            tint = Color.White
                        )
                    }
                }
            ) {
                // Kartu asli yang ada di depan
                EventCard(
                    title = event.title,
                    time = event.time,
                    location = event.location,
                    backgroundColor = Color(event.category.colorHex),
                    modifier = Modifier.clickable { onEventClick(event.id) }
                )
            }
        }
    }
}