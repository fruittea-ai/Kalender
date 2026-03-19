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
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun CalendarScreen(
    modifier: Modifier = Modifier,
    viewModel: ScheduleViewModel = hiltViewModel(),
    onEventClick: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // State dinamis untuk melacak bulan yang sedang dilihat dan tanggal yang diklik
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    // Formatter untuk menampilkan teks di layar
    val monthHeaderFormatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault())
    val monthHeaderStr = currentMonth.format(monthHeaderFormatter) // Contoh: "March 2026"

    // Formatter untuk mencocokkan format "dd MMM yyyy" yang disimpan di database
    val dbDateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault())
    val selectedDateStr = selectedDate.format(dbDateFormatter)

    // Filter event berdasarkan tanggal yang persis dipilih
    val filteredEvents = uiState.data.filter { it.date == selectedDateStr }

    // Ekstrak hari apa saja di bulan yang sedang DILIHAT yang memiliki acara
    val eventDaysInCurrentMonth = uiState.data.mapNotNull { event ->
        try {
            val eventDate = LocalDate.parse(event.date, dbDateFormatter)
            if (YearMonth.from(eventDate) == currentMonth) eventDate.dayOfMonth else null
        } catch (e: Exception) {
            null
        }
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
                    Text(text = monthHeaderStr, fontSize = 36.sp, fontWeight = FontWeight.Bold, color = Color(0xFF302E56))
                    Row {
                        IconButton(
                            onClick = { currentMonth = currentMonth.minusMonths(1) }, // Mundur 1 bulan
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Prev", tint = Color(0xFF302E56))
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        IconButton(
                            onClick = { currentMonth = currentMonth.plusMonths(1) }, // Maju 1 bulan
                            modifier = Modifier.size(24.dp)
                        ) {
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

                // Render Kalender Dinamis
                DynamicCalendarGrid(
                    currentMonth = currentMonth,
                    selectedDate = selectedDate,
                    eventDays = eventDaysInCurrentMonth,
                    onDateSelected = { date ->
                        selectedDate = date
                        // Opsional: jika mengklik tanggal dari bulan lalu/depan yang pudar, otomatis pindah bulan
                        currentMonth = YearMonth.from(date)
                    }
                )

                Spacer(modifier = Modifier.height(40.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Events for ${selectedDate.dayOfMonth}", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF302E56))
                    Text("${filteredEvents.size} EVENTS", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4956B4), letterSpacing = 1.sp)
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

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
private fun DynamicCalendarGrid(
    currentMonth: YearMonth,
    selectedDate: LocalDate,
    eventDays: Set<Int>,
    onDateSelected: (LocalDate) -> Unit
) {
    // Logika pembuatan matriks kalender
    val daysInMonth = currentMonth.lengthOfMonth()
    val firstDayOfMonth = currentMonth.atDay(1)
    val startDayOfWeek = firstDayOfMonth.dayOfWeek.value // Senin = 1, Minggu = 7

    // Total slot yang dibutuhkan untuk 6 baris (42 slot)
    val calendarDays = mutableListOf<LocalDate>()

    // 1. Masukkan hari-hari pudar dari bulan sebelumnya
    val previousMonth = currentMonth.minusMonths(1)
    val daysFromPrevMonth = startDayOfWeek - 1
    for (i in daysFromPrevMonth downTo 1) {
        calendarDays.add(previousMonth.atEndOfMonth().minusDays((i - 1).toLong()))
    }

    // 2. Masukkan hari-hari bulan ini
    for (i in 1..daysInMonth) {
        calendarDays.add(currentMonth.atDay(i))
    }

    // 3. Masukkan hari-hari pudar dari bulan selanjutnya hingga menggenapi kelipatan 7
    val nextMonth = currentMonth.plusMonths(1)
    var nextMonthDaysAdded = 1
    while (calendarDays.size % 7 != 0) {
        calendarDays.add(nextMonth.atDay(nextMonthDaysAdded++))
    }

    // Bagi menjadi potongan per minggu (7 hari)
    val weeks = calendarDays.chunked(7)

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        weeks.forEach { week ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                week.forEach { date ->
                    val isCurrentMonth = YearMonth.from(date) == currentMonth
                    val isSelected = date == selectedDate
                    val hasEvent = isCurrentMonth && eventDays.contains(date.dayOfMonth)

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .clip(CircleShape)
                            .background(if (isSelected) Color(0xFF4956B4) else Color.Transparent)
                            .clickable { onDateSelected(date) }
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = date.dayOfMonth.toString(),
                                fontSize = 16.sp,
                                color = when {
                                    isSelected -> Color.White
                                    !isCurrentMonth -> Color(0xFF302E56).copy(alpha = 0.2f)
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