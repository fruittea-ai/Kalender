package com.proyek.kalender.ui.screens.addevent

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.proyek.kalender.domain.model.EventCategory
import com.proyek.kalender.ui.components.EditorialTextField
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEventScreen(
    modifier: Modifier = Modifier,
    viewModel: AddEventViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState()
    val timePickerState = rememberTimePickerState()

    LaunchedEffect(uiState.isSavedSuccess) {
        if (uiState.isSavedSuccess) {
            viewModel.resetSaveState()
            onNavigateBack()
        }
    }

    // --- Dialog Date Picker ---
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        // Format millis menjadi string tanggal yang rapi (contoh: 19 Mar 2026)
                        val formattedDate = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(millis))
                        viewModel.onDateChange(formattedDate)
                    }
                    showDatePicker = false
                }) {
                    Text("OK", color = Color(0xFF4956B4))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel", color = Color(0xFF302E56))
                }
            },
            colors = DatePickerDefaults.colors(containerColor = Color(0xFFFCF8FF))
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // --- Dialog Time Picker ---
    if (showTimePicker) {
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    // Format jam dan menit agar selalu 2 digit (contoh: 09:05)
                    val formattedTime = String.format(Locale.getDefault(), "%02d:%02d", timePickerState.hour, timePickerState.minute)
                    viewModel.onTimeChange(formattedTime)
                    showTimePicker = false
                }) {
                    Text("OK", color = Color(0xFF4956B4))
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) {
                    Text("Cancel", color = Color(0xFF302E56))
                }
            },
            text = {
                TimePicker(state = timePickerState)
            },
            containerColor = Color(0xFFFCF8FF)
        )
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

        Spacer(modifier = Modifier.height(32.dp))

        // Judul Acara
        EditorialTextField(
            value = uiState.title,
            onValueChange = viewModel::onTitleChange,
            label = "Event Title"
        )
        if (uiState.errorMessage != null) {
            Text(
                text = uiState.errorMessage!!,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp, start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Kategori
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
                        .background(if (isSelected) Color(0xFF4956B4) else Color(0xFFF6F2FF))
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

        // Date & Time menggunakan Overlay Transparan untuk memicu Dialog
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(modifier = Modifier.weight(1f)) {
                EditorialTextField(
                    value = uiState.date,
                    onValueChange = {},
                    label = "Date",
                    modifier = Modifier.fillMaxWidth()
                )
                // Overlay transparan menutupi TextField untuk menangkap klik
                Box(modifier = Modifier.matchParentSize().clickable { showDatePicker = true })
            }

            Box(modifier = Modifier.weight(1f)) {
                EditorialTextField(
                    value = uiState.time,
                    onValueChange = {},
                    label = "Time",
                    modifier = Modifier.fillMaxWidth()
                )
                Box(modifier = Modifier.matchParentSize().clickable { showTimePicker = true })
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Location
        EditorialTextField(
            value = uiState.location,
            onValueChange = viewModel::onLocationChange,
            label = "Location"
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Tombol Simpan
        Button(
            onClick = viewModel::saveEvent,
            modifier = Modifier.fillMaxWidth().height(56.dp),
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