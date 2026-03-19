package com.proyek.kalender.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationRail
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AppNavigationRail(
    modifier: Modifier = Modifier,
    currentRoute: String = "schedule",
    onNavigate: (String) -> Unit = {}
) {
    NavigationRail(
        modifier = modifier,
        containerColor = Color(0xFFF6F2FF), // Surface Container Low
        header = {
            Spacer(modifier = Modifier.height(32.dp))
            // Tombol Tambah Acara Utama (Primary CTA)
            IconButton(
                onClick = { onNavigate("add_event") },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color(0xFF4956B4)) // Primary Indigo
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Event",
                    tint = Color.White
                )
            }
        }
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        IconButton(onClick = { onNavigate("schedule") }) {
            Icon(
                imageVector = Icons.Default.List,
                contentDescription = "Schedule",
                tint = if (currentRoute == "schedule") Color(0xFF4956B4) else Color(0xFF302E56).copy(alpha = 0.5f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        IconButton(onClick = { onNavigate("calendar") }) {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = "Calendar",
                tint = if (currentRoute == "calendar") Color(0xFF4956B4) else Color(0xFF302E56).copy(alpha = 0.5f)
            )
        }
    }
}