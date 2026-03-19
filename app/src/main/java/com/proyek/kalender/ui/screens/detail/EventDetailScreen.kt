package com.proyek.kalender.ui.screens.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun EventDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: EventDetailViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val event = uiState.event

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // Tombol Kembali melayang (Surface Container Low)
        IconButton(
            onClick = onNavigateBack,
            modifier = Modifier
                .clip(CircleShape)
                .background(Color(0xFFF6F2FF))
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color(0xFF302E56)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        if (event != null) {
            // Label Kategori (Warna dinamis)
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color(event.category.colorHex))
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Text(
                    text = event.category.displayName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF302E56)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Judul Acara (Tipografi Besar)
            Text(
                text = event.title,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF302E56),
                lineHeight = 40.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Baris Informasi Detail
            InfoRow(label = "Time", value = event.time)
            Spacer(modifier = Modifier.height(24.dp)) // Spacing-12
            InfoRow(label = "Location", value = event.location)

            Spacer(modifier = Modifier.weight(1f)) // Mendorong tombol ke paling bawah layar

            // Tombol Aksi Sekunder (Sesuai panduan desain)
            Button(
                onClick = viewModel::markAsComplete,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !uiState.isCompleted,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF006B64), // Warna Secondary
                    disabledContainerColor = Color(0xFF006B64).copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(16.dp) // Action Chips styling
            ) {
                Text(
                    text = if (uiState.isCompleted) "Event Completed" else "Mark as Complete",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Column {
        Text(text = label, fontSize = 14.sp, color = Color(0xFF302E56).copy(alpha = 0.6f))
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF302E56))
    }
}