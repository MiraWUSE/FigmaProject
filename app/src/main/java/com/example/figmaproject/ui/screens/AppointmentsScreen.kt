package com.example.figmaproject.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.figmaproject.ui.theme.*

// Модель записи на приём (локальная, не из БД)
data class Appointment(
    val id: Int,
    val doctorId: Int,
    val doctorName: String,
    val specialty: String,
    val hospital: String,
    val date: String,
    val time: String,
    val status: String  // scheduled / completed / cancelled
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentsScreen(
    onBack: () -> Unit,
    onDoctorClick: (Int) -> Unit
) {
    // Тестовые данные
    val appointments = getSampleAppointments()
    var selectedFilter by remember { mutableStateOf("all") }

    // Фильтрация записей по статусу
    val filteredAppointments = when (selectedFilter) {
        "upcoming" -> appointments.filter { it.status == "scheduled" }
        "completed" -> appointments.filter { it.status == "completed" }
        "cancelled" -> appointments.filter { it.status == "cancelled" }
        else -> appointments
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("My Appointments", color = TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Назад", tint = TextPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundWhite)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().background(BackgroundGray).padding(padding)
        ) {
            // Вкладки фильтров
            FilterTabs(selectedFilter = selectedFilter, onFilterChange = { selectedFilter = it })

            // Статистика по статусам
            AppointmentStats(appointments = appointments)

            // Список записей или пустое состояние
            if (filteredAppointments.isEmpty()) {
                EmptyAppointmentsView(filter = selectedFilter)
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredAppointments) { appointment ->
                        AppointmentCard(
                            appointment = appointment,
                            onClick = { onDoctorClick(appointment.doctorId) }
                        )
                    }
                }
            }
        }
    }
}

// Вкладки фильтров: All / Upcoming / Completed / Cancelled
@Composable
fun FilterTabs(selectedFilter: String, onFilterChange: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().background(BackgroundWhite).padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        FilterChip(label = "All", selected = selectedFilter == "all", onClick = { onFilterChange("all") })
        FilterChip(label = "Upcoming", selected = selectedFilter == "upcoming", onClick = { onFilterChange("upcoming") })
        FilterChip(label = "Completed", selected = selectedFilter == "completed", onClick = { onFilterChange("completed") })
        FilterChip(label = "Cancelled", selected = selectedFilter == "cancelled", onClick = { onFilterChange("cancelled") })
    }
}

// Кнопка-фильтр
@Composable
fun FilterChip(label: String, selected: Boolean, onClick: () -> Unit) {
    Surface(
        color = if (selected) PrimaryBlue else SurfaceGray,
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.clickable(onClick = onClick).padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            color = if (selected) Color.White else TextSecondary
        )
    }
}

// Статистика: количество записей по статусам
@Composable
fun AppointmentStats(appointments: List<Appointment>) {
    Row(
        modifier = Modifier.fillMaxWidth().background(BackgroundWhite).padding(20.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StatBadge(value = appointments.count { it.status == "scheduled" }.toString(), label = "Upcoming", color = PrimaryBlue)
        StatBadge(value = appointments.count { it.status == "completed" }.toString(), label = "Completed", color = SuccessGreen)
        StatBadge(value = appointments.count { it.status == "cancelled" }.toString(), label = "Cancelled", color = ErrorRed)
    }
}

// Бейдж со значением и подписью
@Composable
fun StatBadge(value: String, label: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = color)
        Text(text = label, fontSize = 12.sp, color = TextSecondary)
    }
}

// Карточка одной записи
@Composable
fun AppointmentCard(appointment: Appointment, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = when (appointment.status) {
                "scheduled" -> BackgroundWhite
                "completed" -> SurfaceGray.copy(alpha = 0.5f)
                "cancelled" -> ErrorRed.copy(alpha = 0.1f)
                else -> BackgroundWhite
            }
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Врач и статус
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = appointment.doctorName, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text(text = appointment.specialty, fontSize = 14.sp, color = TextSecondary)
                }
                StatusChip(status = appointment.status)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Дата и время
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Icon(imageVector = Icons.Default.CalendarToday, contentDescription = "Дата", tint = PrimaryBlue, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "${appointment.date} • ${appointment.time}", fontSize = 14.sp, color = TextPrimary, fontWeight = FontWeight.Medium)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Больница
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.LocationOn, contentDescription = "Место", tint = TextSecondary, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = appointment.hospital, fontSize = 14.sp, color = TextSecondary, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }

            // Кнопка отмены (только для scheduled)
            if (appointment.status == "scheduled") {
                Spacer(modifier = Modifier.height(12.dp))
                TextButton(onClick = { }, modifier = Modifier.align(Alignment.End)) {
                    Text(text = "Cancel Appointment", color = ErrorRed, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

// Бейдж статуса записи
@Composable
fun StatusChip(status: String) {
    val (text, color) = when (status) {
        "scheduled" -> "Scheduled" to PrimaryBlue
        "completed" -> "Completed" to SuccessGreen
        "cancelled" -> "Cancelled" to ErrorRed
        else -> "Unknown" to TextSecondary
    }

    Surface(
        color = color.copy(alpha = 0.1f),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.padding(start = 8.dp)
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = color,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

// Пустое состояние списка
@Composable
fun EmptyAppointmentsView(filter: String) {
    Box(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(imageVector = Icons.Default.CalendarMonth, contentDescription = "Нет записей", tint = TextLight, modifier = Modifier.size(64.dp))
            Spacer(modifier = Modifier.height(16.dp))

            val message = when (filter) {
                "upcoming" -> "No upcoming appointments"
                "completed" -> "No completed appointments"
                "cancelled" -> "No cancelled appointments"
                else -> "You have no appointments yet"
            }

            Text(text = message, fontSize = 16.sp, color = TextSecondary, textAlign = androidx.compose.ui.text.style.TextAlign.Center)

            if (filter == "all") {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Find a doctor and book your first visit!", fontSize = 14.sp, color = TextLight, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
            }
        }
    }
}

// Тестовые данные
fun getSampleAppointments(): List<Appointment> = listOf(
    Appointment(1, 1, "Dr. Hamza Tariq", "Senior Surgeon", "City Hospital", "2024-06-15", "10:30 AM", "scheduled"),
    Appointment(2, 2, "Dr. Alina Fatima", "Senior Surgeon", "Medical Center", "2024-06-10", "2:00 PM", "completed"),
    Appointment(3, 3, "Dr. John Smith", "Cardiologist", "Heart Institute", "2024-06-20", "11:00 AM", "scheduled"),
    Appointment(4, 4, "Dr. Sarah Johnson", "Neurologist", "Neurology Center", "2024-06-05", "3:30 PM", "cancelled"),
    Appointment(5, 5, "Dr. Michael Brown", "Dentist", "Smile Clinic", "2024-06-25", "9:00 AM", "scheduled")
)