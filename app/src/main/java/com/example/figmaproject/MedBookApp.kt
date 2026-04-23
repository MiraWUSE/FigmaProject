package com.example.figmaproject

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.figmaproject.navigation.AppNavGraph
import com.example.figmaproject.ui.theme.BackgroundGray
import com.example.figmaproject.ui.viewmodel.MedicalViewModel
import com.example.figmaproject.ui.viewmodel.ProfileViewModel

// Главный Composable приложения
@Composable
fun MedBookApp(
    medicalViewModel: MedicalViewModel,
    profileViewModel: ProfileViewModel
) {
    // Контроллер навигации
    val navController = rememberNavController()

    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = BackgroundGray
        ) {
            // Граф навигации между экранами
            AppNavGraph(
                navController = navController,
                medicalViewModel = medicalViewModel,
                profileViewModel = profileViewModel
            )
        }
    }
}