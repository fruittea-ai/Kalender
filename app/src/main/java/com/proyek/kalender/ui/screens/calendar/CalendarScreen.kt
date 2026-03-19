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
import com.proyek.kalender.domain.model.Event
import com.proyek.kalender.ui.components.EditorialTopBar
import com.proyek.kalender.ui.components.EventCard
import com.proyek.kalender.ui.screens.schedule.ScheduleViewModel

@Composable
fun CalendarScreen(
    modifier: Modifier = Modifier,
    viewModel: ScheduleViewModel = hiltViewModel(), // Kita manfaatkan ScheduleViewModel untuk mengambil data
    onEventClick: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var selectedDate by remember { mutableIntStateOf(12) }

    Column(modifier = modifier.fillMaxSize()) {
        EditorialTopBar()

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp),
            contentPadding = PaddingValues(bottom = 80.dp) // Ruang untuk FAB dan BottomNav
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                // Header Bulan
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("March 2026", fontSize = 36.sp, fontWeight = FontWeight.Bold, color = Color(0xFF302E56))
                    Row {
                        IconButton(onClick = { /* Previous */ }, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Prev", tint = Color(0xFF302E56))
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        IconButton(onClick = { /* Next */ }, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Next", tint = Color(0xFF302E56))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text("12th Thursday · Focused Perspective", fontSize = 14.sp, color = Color(0xFF302E56).copy(alpha = 0.7f))
                Spacer(modifier = Modifier.height(32.dp))

                // Hari dalam seminggu
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    listOf("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN").forEach { day ->
                        Text(
                            text = day,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFb1addd)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))

                // Grid Kalender Statis (Simulasi pixel-perfect)
                CalendarGrid(selectedDate = selectedDate, onDateSelected = { selectedDate = it })

                Spacer(modifier = Modifier.height(40.dp))

                // Agenda Title
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Agenda for today", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF302E56))
                    Text("${uiState.data.size} EVENTS", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4956B4), letterSpacing = 1.sp)
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Daftar Event
            if (uiState.isLoading) {
                item { CircularProgressIndicator(modifier = Modifier.fillMaxWidth().wrapContentWidth(Alignment.CenterHorizontally)) }
            } else {
                items(uiState.data, key = { it.id }) { event ->
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
private fun CalendarGrid(selectedDate: Int, onDateSelected: (Int) -> Unit) {
    // Array 2D simulasi penanggalan
    val calendarMatrix = listOf(
        listOf(23, 24, 25, 26, 27, 28, 1),
        listOf(2, 3, 4, 5, 6, 7, 8),
        listOf(9, 10, 11, 12, 13, 14, 15),
        listOf(16, 17, 18, 19, 20, 21, 22)
    )

    // Tanggal dengan simulasi acara (titik di bawah angka)
    val eventDays = listOf(2, 5, 10, 18)

    Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
        calendarMatrix.forEachIndexed { rowIndex, week ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                week.forEach { date ->
                    val isFaded = rowIndex == 0 && date > 20 // abu-abu untuk bulan lalu
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
                                Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                                    Box(modifier = Modifier.size(4.dp).clip(CircleShape).background(if (isSelected) Color.White else Color(0xFF006B64)))
                                    if (date == 10) { // Contoh untuk banyak acara
                                        Box(modifier = Modifier.size(4.dp).clip(CircleShape).background(if (isSelected) Color.White else Color(0xFF4956B4)))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}