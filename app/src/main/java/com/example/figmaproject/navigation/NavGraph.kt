package com.example.figmaproject.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.figmaproject.ui.screens.*
import com.example.figmaproject.ui.viewmodel.MedicalViewModel
import com.example.figmaproject.ui.viewmodel.ProfileViewModel

// Навигация между экранами
@Composable
fun AppNavGraph(
    navController: NavHostController,
    medicalViewModel: MedicalViewModel,
    profileViewModel: ProfileViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,  // Стартовый экран
        modifier = modifier
    ) {
        // === ГЛАВНАЯ ===
        composable(Screen.Home.route) {
            HomeScreen(
                onDoctorClick = { id -> navController.navigate(Screen.DoctorDetail.createRoute(id)) },
                onSearchClick = { navController.navigate(Screen.Search.route) },
                onProfileClick = { navController.navigate(Screen.Profile.route) },
                onAppointmentsClick = { navController.navigate(Screen.Appointments.route) },
                viewModel = medicalViewModel,
                profileViewModel = profileViewModel
            )
        }

        // === ПРОФИЛЬ ВРАЧА ===
        composable(Screen.DoctorDetail.route) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("doctorId")?.toIntOrNull() ?: 1
            DoctorDetailScreen(
                doctorId = id,
                onBack = { navController.popBackStack() },  // Назад
                viewModel = medicalViewModel
            )
        }

        // === ПОИСК ===
        composable(Screen.Search.route) {
            SearchScreen(
                onBack = { navController.popBackStack() },
                onDoctorClick = { id -> navController.navigate(Screen.DoctorDetail.createRoute(id)) },
                viewModel = medicalViewModel
            )
        }

        // === ПРОФИЛЬ ПОЛЬЗОВАТЕЛЯ ===
        composable(Screen.Profile.route) {
            ProfileScreen(
                onBack = { navController.popBackStack() },
                profileViewModel = profileViewModel
            )
        }

        // === ЗАПИСИ ===
        composable(Screen.Appointments.route) {
            AppointmentsScreen(
                onBack = { navController.popBackStack() },
                onDoctorClick = { id -> navController.navigate(Screen.DoctorDetail.createRoute(id)) }
            )
        }
    }
}

// Маршруты экранов
sealed class Screen(val route: String) {
    object Home : Screen("home")

    object DoctorDetail : Screen("doctor_detail/{doctorId}") {
        fun createRoute(doctorId: Int) = "doctor_detail/$doctorId"  // Формирование маршрута с параметром
    }

    object Search : Screen("search")
    object Profile : Screen("profile")
    object Appointments : Screen("appointments")
}