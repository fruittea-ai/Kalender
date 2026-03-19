package com.proyek.kalender.ui.screens.schedule

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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.proyek.kalender.domain.model.Event
import com.proyek.kalender.ui.components.EventCard
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.clickable

@Composable
fun ScheduleScreen(
    modifier: Modifier = Modifier,
    viewModel: ScheduleViewModel = hiltViewModel(),
    onEventClick: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        when {
            uiState.isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color(0xFF4956B4)
                )
            }
            uiState.errorMessage != null -> {
                Text(
                    text = uiState.errorMessage ?: "An error occurred",
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.Red
                )
            }
            uiState.data.isEmpty() -> {
                Text(
                    text = "No upcoming events.",
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

@Composable
private fun ScheduleListContent(
    events: List<Event>,
    modifier: Modifier = Modifier,
    onEventClick: (String) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = "UPCOMING SCHEDULE",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4956B4)
            )
            Text(
                text = "September",
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
                category = event.category.displayName,
                title = event.title,
                time = event.time,
                location = event.location,
                backgroundColor = Color(event.category.colorHex),
                modifier = Modifier.padding(bottom = 16.dp)
                    .clickable { onEventClick(event.id) }
            )
        }

        item {
            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}