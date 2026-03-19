package com.proyek.kalender.ui.screens.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.proyek.kalender.domain.model.Event
import com.proyek.kalender.domain.model.EventCategory
import com.proyek.kalender.ui.components.EventCard
import java.util.UUID

@Composable
fun CalendarScreen(
    modifier: Modifier = Modifier
) {
    // State to hold the currently selected date
    var selectedDate by remember { mutableIntStateOf(14) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // Month Header
        Text(
            text = "September 2026",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF302E56) // on_surface text
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Days of Week Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val daysOfWeek = listOf("S", "M", "T", "W", "T", "F", "S")
            daysOfWeek.forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                    color = Color(0xFF302E56).copy(alpha = 0.5f),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Calendar Grid
        val daysInMonth = (1..30).toList()
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.fillMaxWidth(),
            userScrollEnabled = false // Disable scroll to keep it as a static header component
        ) {
            // Empty slots for offset (assuming month starts on a Tuesday)
            items(2) {
                Box(modifier = Modifier.aspectRatio(1f))
            }

            items(daysInMonth) { date ->
                val isSelected = date == selectedDate
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .padding(4.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) Color(0xFFE9E5FF) else Color.Transparent) // surface-container-high for active
                        .clickable { selectedDate = date }
                ) {
                    Text(
                        text = date.toString(),
                        fontSize = 16.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) Color(0xFF4956B4) else Color(0xFF302E56)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Selected Date Events
        Text(
            text = "Events on Sept $selectedDate",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF302E56)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Dummy event mapped to the selected date
        EventCard(
            category = EventCategory.WORK.displayName,
            title = "Design System Sync",
            time = "10:00 — 11:30",
            location = "Meet Room A",
            backgroundColor = Color(EventCategory.WORK.colorHex)
        )
    }
}