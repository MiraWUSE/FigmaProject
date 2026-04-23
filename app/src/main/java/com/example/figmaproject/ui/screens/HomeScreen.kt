package com.example.figmaproject.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.figmaproject.data.local.entity.Doctor
import com.example.figmaproject.data.local.entity.Service
import com.example.figmaproject.data.local.entity.Profile
import com.example.figmaproject.ui.viewmodel.MedicalViewModel
import com.example.figmaproject.ui.viewmodel.ProfileViewModel
import com.example.figmaproject.R
import com.example.figmaproject.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onDoctorClick: (Int) -> Unit,
    onSearchClick: () -> Unit,
    onProfileClick: () -> Unit,
    onAppointmentsClick: () -> Unit,
    viewModel: MedicalViewModel = viewModel(),
    profileViewModel: ProfileViewModel = viewModel()
) {
    // Данные из БД
    val doctors by viewModel.allDoctors.collectAsState(initial = emptyList())
    val services by viewModel.allServices.collectAsState(initial = emptyList())
    val profiles by profileViewModel.allProfiles.collectAsState(initial = emptyList())
    val currentProfile = profiles.firstOrNull()

    // Имя пользователя для приветствия
    val userName = currentProfile?.firstName?.takeIf { it.isNotBlank() } ?: "Hamza"

    var searchQuery by remember { mutableStateOf("") }

    // Инициализация тестовых данных при первом запуске
    LaunchedEffect(Unit) {
        if (doctors.isEmpty()) viewModel.insertDoctors(getSampleDoctors())
        if (services.isEmpty()) viewModel.insertServices(getSampleServices())
        if (profiles.isEmpty()) {
            profileViewModel.insertProfile(Profile(firstName = "User", lastName = "User", email = "user@example.com"))
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("", color = TextPrimary) }, colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundWhite)) },
        bottomBar = { BottomNavigationBar(onProfileClick = onProfileClick, onAppointmentsClick = onAppointmentsClick) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().background(BackgroundGray).padding(padding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Приветствие
            item {
                Column {
                    Text(text = "Hello $userName !", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text(text = "Find your doctor", fontSize = 14.sp, color = TextSecondary, modifier = Modifier.padding(top = 4.dp))
                }
            }

            // Поиск
            item {
                Card(colors = CardDefaults.cardColors(containerColor = BackgroundWhite), shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth().clickable { onSearchClick() }) {
                    Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Поиск", tint = TextSecondary)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = "Search", fontSize = 14.sp, color = TextLight)
                    }
                }
            }

            // Сервисы
            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Services", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    TextButton(onClick = { }) { Text(text = "see all >", fontSize = 14.sp, color = PrimaryBlue) }
                }
            }

            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(services) { service -> ServiceCard(service = service) }
                }
            }

            // Врачи
            item { Text(text = "Top Doctors", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextPrimary) }
            items(doctors.take(5)) { doctor -> DoctorCard(doctor = doctor, onClick = { onDoctorClick(doctor.id) }) }
        }
    }
}

// Карточка услуги
@Composable
fun ServiceCard(service: Service) {
    Card(
        colors = CardDefaults.cardColors(containerColor = when (service.name) {
            "Odontology" -> OdontologyBlue
            "Neurology" -> NeurologyPurple
            "Cardiology" -> CardiologyRed
            else -> PrimaryBlue
        }),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.width(120.dp).height(120.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Icon(imageVector = getServiceIcon(service.name), contentDescription = service.name, tint = Color.White, modifier = Modifier.size(40.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = service.name, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color.White)
        }
    }
}

// Иконка для сервиса
fun getServiceIcon(serviceName: String): androidx.compose.ui.graphics.vector.ImageVector {
    return when (serviceName) {
        "Odontology" -> Icons.Default.Face
        "Neurology" -> Icons.Default.Psychology
        "Cardiology" -> Icons.Default.Favorite
        else -> Icons.Default.HealthAndSafety
    }
}

// Карточка врача
@Composable
fun DoctorCard(doctor: Doctor, onClick: () -> Unit) {
    Card(onClick = onClick, colors = CardDefaults.cardColors(containerColor = BackgroundWhite), shape = RoundedCornerShape(20.dp), modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(painter = painterResource(id = doctor.photoResId), contentDescription = doctor.name, modifier = Modifier.size(80.dp).clip(RoundedCornerShape(16.dp)), contentScale = ContentScale.Crop)
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = doctor.name, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Text(text = doctor.specialty, fontSize = 14.sp, color = TextSecondary, modifier = Modifier.padding(top = 4.dp))
                Text(text = doctor.availableTime, fontSize = 12.sp, color = TextLight, modifier = Modifier.padding(top = 4.dp))
                Text(text = "Fee: $${doctor.fee}", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = PrimaryBlue, modifier = Modifier.padding(top = 4.dp))
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Star, contentDescription = "Рейтинг", tint = StarYellow, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = doctor.rating.toString(), fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            }
            Spacer(modifier = Modifier.width(12.dp))
            IconButton(onClick = onClick, modifier = Modifier.size(40.dp).background(PrimaryBlue, RoundedCornerShape(12.dp))) {
                Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "Перейти", tint = Color.White, modifier = Modifier.size(20.dp))
            }
        }
    }
}

// Нижняя навигация
@Composable
fun BottomNavigationBar(onProfileClick: () -> Unit, onAppointmentsClick: () -> Unit) {
    NavigationBar(containerColor = BackgroundWhite, modifier = Modifier.fillMaxWidth()) {
        NavigationBarItem(icon = { Icon(Icons.Default.Home, contentDescription = "Home", tint = PrimaryBlue) }, label = { Text("Home", color = PrimaryBlue) }, selected = true, onClick = { })
        NavigationBarItem(icon = { Icon(Icons.Default.CalendarMonth, contentDescription = "Appointments", tint = TextLight) }, label = { Text("Appointments", color = TextLight) }, selected = false, onClick = onAppointmentsClick)
        NavigationBarItem(icon = { Icon(Icons.Default.FavoriteBorder, contentDescription = "Favorites", tint = TextLight) }, label = { Text("Favorites", color = TextLight) }, selected = false, onClick = { })
        NavigationBarItem(icon = { Icon(Icons.Default.Person, contentDescription = "Profile", tint = TextLight) }, label = { Text("Profile", color = TextLight) }, selected = false, onClick = onProfileClick)
    }
}

// Тестовые данные
fun getSampleDoctors(): List<Doctor> = listOf(
    Doctor(1, "Dr. Hamza Tariq", "Senior Surgeon", 10, 4.9f, 1250, "Experienced surgeon specializing in general surgery", R.drawable.doctor1, 12.0, "10:30 AM-3:30 PM", "Harvard Medical School", "City Hospital"),
    Doctor(2, "Dr. Alina Fatima", "Senior Surgeon", 8, 5.0f, 980, "Expert in minimally invasive procedures", R.drawable.doctor2, 12.0, "10:30 AM-3:30 PM", "Johns Hopkins", "Medical Center"),
    Doctor(3, "Dr. John Smith", "Cardiologist", 15, 4.8f, 2100, "Specialist in heart diseases", R.drawable.doctor3, 15.0, "9:00 AM-5:00 PM", "Stanford Medicine", "Heart Institute"),
    Doctor(4, "Dr. Sarah Johnson", "Neurologist", 12, 4.7f, 1500, "Brain and nervous system expert", R.drawable.doctor4, 18.0, "11:00 AM-6:00 PM", "Yale School of Medicine", "Neurology Center"),
    Doctor(5, "Dr. Michael Brown", "Dentist", 9, 4.9f, 1800, "Cosmetic and general dentistry", R.drawable.doctor5, 10.0, "8:00 AM-4:00 PM", "Dental College", "Smile Clinic")
)

fun getSampleServices(): List<Service> = listOf(
    Service(1, "Odontology", R.drawable.ic_tooth, "Dental care and treatment"),
    Service(2, "Neurology", R.drawable.ic_brain, "Brain and nervous system"),
    Service(3, "Cardiology", R.drawable.ic_heart, "Heart and cardiovascular")
)