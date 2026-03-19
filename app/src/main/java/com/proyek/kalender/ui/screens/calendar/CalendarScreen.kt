package com.proyek.kalender.ui.screens.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.proyek.kalender.ui.components.EditorialTopBar
import com.proyek.kalender.ui.components.EventCard
import com.proyek.kalender.ui.screens.schedule.ScheduleViewModel

@Composable
fun CalendarScreen(
    modifier: Modifier = Modifier,
    viewModel: ScheduleViewModel = hiltViewModel(),
    onEventClick: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var selectedDate by remember { mutableIntStateOf(19) } // Default hari ini (19)

    // Filter dinamis: Hanya ambil acara yang tanggalnya memuat angka tanggal terpilih + "Mar"
    // Contoh: "19 Mar 2026"
    val paddedDate = selectedDate.toString().padStart(2, '0') // Ubah 5 jadi "05"
    val filteredEvents = uiState.data.filter { event ->
        event.date.startsWith("$paddedDate Mar") || event.date.startsWith("$selectedDate Mar")
    }

    // Ekstrak secara otomatis tanggal berapa saja yang punya acara di bulan ini
    val eventDays = uiState.data.mapNotNull { event ->
        if (event.date.contains("Mar")) {
            event.date.take(2).trim().toIntOrNull() // Mengambil 2 karakter pertama (contoh: "19")
        } else null
    }.toSet()

    Column(modifier = modifier.fillMaxSize()) {
        EditorialTopBar()

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("March 2026", fontSize = 36.sp, fontWeight = FontWeight.Bold, color = Color(0xFF302E56))
                    Row {
                        IconButton(onClick = { /* Implementasi bulan nanti */ }, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Prev", tint = Color(0xFF302E56))
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        IconButton(onClick = { /* Implementasi bulan nanti */ }, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Next", tint = Color(0xFF302E56))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    listOf("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN").forEach { day ->
                        Text(
                            text = day, modifier = Modifier.weight(1f), textAlign = TextAlign.Center,
                            fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFb1addd)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))

                // Render Kalender dengan data titik yang dinamis
                CalendarGrid(
                    selectedDate = selectedDate,
                    eventDays = eventDays,
                    onDateSelected = { selectedDate = it }
                )

                Spacer(modifier = Modifier.height(40.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Events for Mar $selectedDate", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF302E56))
                    Text("${filteredEvents.size} EVENTS", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4956B4), letterSpacing = 1.sp)
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Tampilkan hanya acara yang difilter
            if (uiState.isLoading) {
                item { CircularProgressIndicator(modifier = Modifier.fillMaxWidth().wrapContentWidth(Alignment.CenterHorizontally)) }
            } else if (filteredEvents.isEmpty()) {
                item {
                    Text(
                        text = "No events scheduled for this day.",
                        color = Color(0xFF302E56).copy(alpha = 0.6f),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                items(filteredEvents, key = { it.id }) { event ->
                    EventCard(
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
    }
}

@Composable
private fun CalendarGrid(selectedDate: Int, eventDays: Set<Int>, onDateSelected: (Int) -> Unit) {
    val calendarMatrix = listOf(
        listOf(23, 24, 25, 26, 27, 28, 1),
        listOf(2, 3, 4, 5, 6, 7, 8),
        listOf(9, 10, 11, 12, 13, 14, 15),
        listOf(16, 17, 18, 19, 20, 21, 22),
        listOf(23, 24, 25, 26, 27, 28, 29),
        listOf(30, 31, 1, 2, 3, 4, 5)
    )

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        calendarMatrix.forEachIndexed { rowIndex, week ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                week.forEach { date ->
                    val isFaded = (rowIndex == 0 && date > 20) || (rowIndex >= 4 && date < 15)
                    val isSelected = date == selectedDate && !isFaded
                    val hasEvent = eventDays.contains(date) && !isFaded

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .clip(CircleShape)
                            .background(if (isSelected) Color(0xFF4956B4) else Color.Transparent)
                            .clickable(enabled = !isFaded) { onDateSelected(date) }
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = date.toString(),
                                fontSize = 16.sp,
                                color = when {
                                    isSelected -> Color.White
                                    isFaded -> Color(0xFF302E56).copy(alpha = 0.2f)
                                    else -> Color(0xFF302E56)
                                }
                            )
                            if (hasEvent) {
                                Spacer(modifier = Modifier.height(2.dp))
                                Box(
                                    modifier = Modifier
                                        .size(4.dp)
                                        .clip(CircleShape)
                                        .background(if (isSelected) Color.White else Color(0xFF006B64))
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}