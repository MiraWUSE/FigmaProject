package com.example.figmaproject.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.figmaproject.R
import com.example.figmaproject.data.local.entity.Profile
import com.example.figmaproject.ui.theme.*
import com.example.figmaproject.ui.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    profileViewModel: ProfileViewModel = viewModel()
) {
    // Получаем профиль из БД
    val profiles by profileViewModel.allProfiles.collectAsState(initial = emptyList())
    val currentProfile = profiles.firstOrNull()

    // Режим редактирования
    var isEditing by remember { mutableStateOf(false) }


    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    // Диалог подтверждения выхода
    var showLogoutDialog by remember { mutableStateOf(false) }

    // Загружаем данные профиля при первом запуске
    LaunchedEffect(currentProfile) {
        if (currentProfile != null) {
            firstName = currentProfile.firstName
            lastName = currentProfile.lastName
            email = currentProfile.email
        } else {
            // Если профиля нет — создаём дефолтный
            firstName = "User"
            lastName = "User"
            email = "user@example.com"
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Profile",
                        color = TextPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад",
                            tint = TextPrimary
                        )
                    }
                },
                actions = {
                    if (!isEditing) {
                        IconButton(onClick = { isEditing = true }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Редактировать",
                                tint = PrimaryBlue
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundWhite)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundGray)
                .padding(padding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Аватар (статичный)
            item {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(PrimaryBlue.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Аватар",
                        tint = PrimaryBlue,
                        modifier = Modifier.size(80.dp)
                    )
                }
            }

            // Имя и почта
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (isEditing) {
                        // Поля ввода
                        OutlinedTextField(
                            value = firstName,
                            onValueChange = { firstName = it },
                            label = { Text("Имя", color = TextSecondary) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryBlue,
                                unfocusedBorderColor = SurfaceGray,
                                focusedLabelColor = PrimaryBlue,
                                unfocusedLabelColor = TextSecondary
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = lastName,
                            onValueChange = { lastName = it },
                            label = { Text("Фамилия", color = TextSecondary) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryBlue,
                                unfocusedBorderColor = SurfaceGray,
                                focusedLabelColor = PrimaryBlue,
                                unfocusedLabelColor = TextSecondary
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email", color = TextSecondary) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryBlue,
                                unfocusedBorderColor = SurfaceGray,
                                focusedLabelColor = PrimaryBlue,
                                unfocusedLabelColor = TextSecondary
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            singleLine = true
                        )
                    } else {
                        // Отображение данных
                        Text(
                            text = "$firstName $lastName",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = email,
                            fontSize = 16.sp,
                            color = TextSecondary
                        )
                    }
                }
            }

            // Кнопки сохранения/отмены (только в режиме редактирования)
            if (isEditing) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = {
                                // Отмена: сброс к исходным значениям
                                firstName = currentProfile?.firstName ?: "User"
                                lastName = currentProfile?.lastName ?: "User"
                                email = currentProfile?.email ?: "User@example.com"
                                isEditing = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = SurfaceGray),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Отмена", color = TextPrimary)
                        }
                        Button(
                            onClick = {
                                // Сохранение в БД
                                val updatedProfile = currentProfile?.copy(
                                    firstName = firstName,
                                    lastName = lastName,
                                    email = email
                                ) ?: Profile(
                                    firstName = firstName,
                                    lastName = lastName,
                                    email = email
                                )
                                profileViewModel.insertProfile(updatedProfile)
                                isEditing = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Сохранить", color = Color.White)
                        }
                    }
                }
            }

            // Статистика
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ProfileStatCard(value = "12", label = "Appointments")
                    ProfileStatCard(value = "5", label = "Favorites")
                    ProfileStatCard(value = "8", label = "Reviews")
                }
            }

            // Меню настроек
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = BackgroundWhite),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        ProfileMenuItem(icon = Icons.Default.PersonOutline, title = "Personal Information")
                        HorizontalDivider(color = SurfaceGray)
                        ProfileMenuItem(icon = Icons.Default.Security, title = "Privacy & Security")
                        HorizontalDivider(color = SurfaceGray)
                        ProfileMenuItem(icon = Icons.Default.Notifications, title = "Notifications")
                        HorizontalDivider(color = SurfaceGray)
                        ProfileMenuItem(icon = Icons.Default.HelpOutline, title = "Help & Support")
                    }
                }
            }

            // Кнопка выхода
            item {
                Button(
                    onClick = { showLogoutDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = ErrorRed),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Logout,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Logout", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }

    // Диалог подтверждения выхода
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Выйти", color = TextPrimary) },
            text = { Text("Вы точно хотите выйти?", color = TextSecondary) },
            containerColor = BackgroundWhite,
            confirmButton = {
                Button(onClick = {  }) {
                    Text("Да", color = Color.White)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showLogoutDialog = false }) {
                    Text("Нет")
                }
            }
        )
    }
}

@Composable
fun ProfileStatCard(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryBlue
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = TextSecondary
        )
    }
}

@Composable
fun ProfileMenuItem(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = TextSecondary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                fontSize = 16.sp,
                color = TextPrimary
            )
        }
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = TextLight,
            modifier = Modifier.size(24.dp)
        )
    }
}