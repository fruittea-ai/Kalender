package com.proyek.kalender.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EventCard(
    category: String,
    title: String,
    time: String,
    location: String,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(backgroundColor)
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(Color.White.copy(alpha = 0.4f))
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(text = category, fontSize = 12.sp, color = Color(0xFF302E56))
            }
            Text(text = time, fontSize = 14.sp, color = Color(0xFF302E56))
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = title, fontWeight = FontWeight.SemiBold, fontSize = 18.sp, color = Color(0xFF302E56))
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = location, fontSize = 14.sp, color = Color(0xFF302E56))
    }
}