package com.proyek.kalender.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.proyek.kalender.ui.navigation.Screen

@Composable
fun EditorialTopBar(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Outlined.DateRange,
                contentDescription = "Logo",
                tint = Color(0xFF4956B4),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "The Editorial Chronology",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF302E56)
            )
        }

        // Placeholder untuk Avatar (Ganti dengan Image asli jika ada)
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(Color(0xFFFFA184).copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Person, contentDescription = "Profile", tint = Color(0xFF302E56), modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
fun EditorialBottomNav(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier.clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        val items = listOf(
            Triple(Screen.Calendar.route, Icons.Default.DateRange, "Calendar"),
            Triple(Screen.Schedule.route, Icons.Default.DateRange, "Schedule"), // Gunakan icon list/schedule jika ada
            Triple("settings", Icons.Default.Settings, "Settings")
        )

        items.forEach { (route, icon, label) ->
            val isSelected = currentRoute == route
            NavigationBarItem(
                selected = isSelected,
                onClick = { if (route != "settings") onNavigate(route) },
                icon = { Icon(icon, contentDescription = label) },
                label = { Text(label, fontSize = 10.sp, fontWeight = FontWeight.SemiBold) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF4956B4),
                    selectedTextColor = Color(0xFF4956B4),
                    indicatorColor = Color(0xFFE9E5FF), // Pill shape background
                    unselectedIconColor = Color(0xFF302E56).copy(alpha = 0.5f),
                    unselectedTextColor = Color(0xFF302E56).copy(alpha = 0.5f)
                )
            )
        }
    }
}