package com.proyek.kalender.ui.screens.addevent

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.proyek.kalender.domain.model.EventCategory
import com.proyek.kalender.ui.components.EditorialTextField
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun AddEventScreen(
    modifier: Modifier = Modifier,
    viewModel: AddEventViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    // Navigasi otomatis kembali jika penyimpanan berhasil
    LaunchedEffect(uiState.isSavedSuccess) {
        if (uiState.isSavedSuccess) {
            viewModel.resetSaveState()
            onNavigateBack()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "New Event",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF302E56)
        )

        Spacer(modifier = Modifier.height(32.dp)) // spacing-10 / spacing-12 per DESIGN.md

        // Judul Acara
        EditorialTextField(
            value = uiState.title,
            onValueChange = viewModel::onTitleChange,
            label = "Event Title"
        )

        // Error message jika judul kosong
        if (uiState.errorMessage != null) {
            Text(
                text = uiState.errorMessage!!,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp, start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Kategori (Selection Chips)
        Text(text = "Category", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF302E56))
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            EventCategory.entries.forEach { category ->
                val isSelected = uiState.selectedCategory == category
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(
                            if (isSelected) Color(0xFF4956B4) // Primary
                            else Color(0xFFF6F2FF) // surface_container_low
                        )
                        .clickable { viewModel.onCategorySelect(category) }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = category.displayName,
                        fontSize = 14.sp,
                        color = if (isSelected) Color.White else Color(0xFF302E56)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Date & Time
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            EditorialTextField(
                value = uiState.date,
                onValueChange = viewModel::onDateChange,
                label = "Date",
                modifier = Modifier.weight(1f)
            )
            EditorialTextField(
                value = uiState.time,
                onValueChange = viewModel::onTimeChange,
                label = "Time",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Location
        EditorialTextField(
            value = uiState.location,
            onValueChange = viewModel::onLocationChange,
            label = "Location"
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Tombol Simpan (Primary CTA)
        Button(
            onClick = viewModel::saveEvent,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = !uiState.isSaving,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4956B4),
                disabledContainerColor = Color(0xFF4956B4).copy(alpha = 0.5f)
            ),
            shape = CircleShape
        ) {
            if (uiState.isSaving) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text("Save Event", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}