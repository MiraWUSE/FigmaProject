package com.example.figmaproject.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.figmaproject.ui.theme.*
import com.example.figmaproject.ui.viewmodel.MedicalViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onBack: () -> Unit,
    onDoctorClick: (Int) -> Unit,
    viewModel: MedicalViewModel
) {
    var query by remember { mutableStateOf("") }
    var delayedQuery by remember { mutableStateOf("") }

    // Поиск с задержкой (debounce)
    val searchResults by viewModel.searchDoctors(delayedQuery).collectAsState(initial = emptyList())

    LaunchedEffect(query) {
        kotlinx.coroutines.delay(300)  // Ждём 300мс после ввода
        delayedQuery = query
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Search", color = TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад", tint = TextPrimary) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundWhite)
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().background(BackgroundGray).padding(padding)) {
            // Поле поиска
            Card(colors = CardDefaults.cardColors(containerColor = BackgroundWhite), shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Поиск", tint = TextSecondary)
                    Spacer(modifier = Modifier.width(12.dp))
                    TextField(value = query, onValueChange = { query = it }, placeholder = { Text("Search doctors...", color = TextLight) }, colors = TextFieldDefaults.colors(focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent), modifier = Modifier.weight(1f), singleLine = true)
                    if (query.isNotBlank()) {
                        IconButton(onClick = { query = "" }) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = "Очистить", tint = TextSecondary)
                        }
                    }
                }
            }

            // Количество результатов
            Text(text = if (delayedQuery.isNotBlank()) "Found: ${searchResults.size}" else "Search by name or specialty", fontSize = 14.sp, color = TextSecondary, modifier = Modifier.padding(horizontal = 20.dp))
            Spacer(modifier = Modifier.height(12.dp))

            // Результаты или пустое состояние
            if (searchResults.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Поиск", tint = TextLight, modifier = Modifier.size(64.dp))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = if (query.isBlank()) "Enter doctor name" else "No doctors found", fontSize = 16.sp, color = TextSecondary)
                    }
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(searchResults) { doctor ->
                        DoctorCard(doctor = doctor, onClick = { onDoctorClick(doctor.id) })
                    }
                }
            }
        }
    }
}