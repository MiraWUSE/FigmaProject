package com.example.figmaproject.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.figmaproject.R
import com.example.figmaproject.data.local.entity.Doctor
import com.example.figmaproject.ui.theme.*
import com.example.figmaproject.ui.viewmodel.MedicalViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorDetailScreen(
    doctorId: Int,
    onBack: () -> Unit,
    viewModel: MedicalViewModel = viewModel()
) {
    // Получаем врача из БД
    val doctor by viewModel.getDoctorById(doctorId).collectAsState(initial = null)
    var isFavorite by remember { mutableStateOf(false) }

    // Загрузка
    if (doctor == null) {
        Box(modifier = Modifier.fillMaxSize().background(BackgroundGray), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = PrimaryBlue)
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Doctor", color = TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад", tint = TextPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundWhite)
            )
        },
        bottomBar = { BottomNavigationBar() }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().background(BackgroundWhite).padding(padding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Фото врача + кнопка избранного
            item {
                Box {
                    Image(
                        painter = painterResource(id = doctor!!.photoResId),
                        contentDescription = doctor!!.name,
                        modifier = Modifier.fillMaxWidth().height(300.dp).clip(RoundedCornerShape(24.dp)),
                        contentScale = ContentScale.Crop
                    )
                    IconButton(
                        onClick = { isFavorite = !isFavorite },
                        modifier = Modifier.align(Alignment.TopEnd).padding(16.dp).size(44.dp).background(Color.White, CircleShape)
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = "Избранное",
                            tint = if (isFavorite) Color.Red else PrimaryBlue,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            // Имя, специальность, рейтинг
            item {
                Column {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text(text = doctor!!.name, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(painter = painterResource(id = R.drawable.ic_star), contentDescription = "Рейтинг", tint = StarYellow, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "${doctor!!.rating} (96 reviews)", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = doctor!!.specialty, fontSize = 16.sp, color = TextSecondary)
                }
            }

            // Статистика (4 колонки)
            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    StatItem(icon = Icons.Default.Person, value = "116+", label = "Patients")
                    StatItem(icon = Icons.Default.CalendarToday, value = "3+", label = "Years")
                    StatItem(icon = Icons.Default.Star, value = "${doctor!!.rating}", label = "Rating")
                    StatItem(icon = Icons.Default.Reviews, value = "90+", label = "Reviews")
                }
            }

            // Описание врача
            item {
                Column {
                    Text(text = "About Me", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Spacer(modifier = Modifier.height(12.dp))
                    val annotatedText = buildAnnotatedString {
                        append("Dr. ${doctor!!.name} is the top most ${doctor!!.specialty.lowercase()} specialist in ${doctor!!.hospital}. He achieved several awards for her wonderful contribution ")
                        withStyle(style = SpanStyle(color = PrimaryBlue, fontWeight = FontWeight.SemiBold)) {
                            append("Read More...")
                        }
                    }
                    Text(text = annotatedText, fontSize = 14.sp, color = TextSecondary, lineHeight = 22.sp)
                }
            }

            // Кнопка записи
            item {
                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth().height(56.dp)
                ) {
                    Text(text = "Book Appointment", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

// Элемент статистики (иконка + значение + подпись)
@Composable
fun StatItem(icon: androidx.compose.ui.graphics.vector.ImageVector, value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(80.dp).padding(8.dp)) {
        Box(modifier = Modifier.size(56.dp).background(PrimaryBlue.copy(alpha = 0.1f), CircleShape), contentAlignment = Alignment.Center) {
            Icon(imageVector = icon, contentDescription = label, tint = PrimaryBlue, modifier = Modifier.size(28.dp))
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, fontSize = 14.sp, color = TextSecondary)
    }
}

// Нижняя панель навигации
@Composable
fun BottomNavigationBar() {
    NavigationBar(containerColor = BackgroundWhite, modifier = Modifier.fillMaxWidth()) {
        NavigationBarItem(icon = { Icon(Icons.Default.Home, contentDescription = "Home", tint = PrimaryBlue) }, label = { Text("", color = PrimaryBlue) }, selected = true, onClick = { }, colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent))
        NavigationBarItem(icon = { Icon(Icons.Default.CalendarMonth, contentDescription = "Calendar", tint = TextLight) }, label = { Text("", color = TextLight) }, selected = false, onClick = { }, colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent))
        NavigationBarItem(icon = { Icon(Icons.Default.FavoriteBorder, contentDescription = "Favorites", tint = TextLight) }, label = { Text("", color = TextLight) }, selected = false, onClick = { }, colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent))
        NavigationBarItem(icon = { Icon(Icons.Default.Person, contentDescription = "Profile", tint = TextLight) }, label = { Text("", color = TextLight) }, selected = false, onClick = { }, colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent))
    }
}