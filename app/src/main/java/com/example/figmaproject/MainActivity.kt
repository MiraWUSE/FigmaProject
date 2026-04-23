package com.example.figmaproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.figmaproject.data.local.AppDatabase
import com.example.figmaproject.data.repository.MedicalRepository
import com.example.figmaproject.data.repository.ProfileRepository
import com.example.figmaproject.ui.theme.MedBookTheme
import com.example.figmaproject.ui.viewmodel.MedicalViewModel
import com.example.figmaproject.ui.viewmodel.ProfileViewModel

class MainActivity : ComponentActivity() {

    // База данных (ленивая инициализация)
    private val database by lazy { AppDatabase.getDatabase(applicationContext) }

    // Репозиторий для врачей/услуг/отзывов
    private val medicalRepository by lazy {
        MedicalRepository(
            doctorDao = database.doctorDao(),
            serviceDao = database.serviceDao(),
            reviewDao = database.reviewDao()
        )
    }

    // Репозиторий для профиля
    private val profileRepository by lazy {
        ProfileRepository(database.profileDao())
    }

    // ViewModel (создаются один раз)
    private val medicalViewModel by lazy { MedicalViewModel(medicalRepository) }
    private val profileViewModel by lazy { ProfileViewModel(profileRepository) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Запуск Compose UI
        setContent {
            MedBookTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MedBookApp(
                        medicalViewModel = medicalViewModel,
                        profileViewModel = profileViewModel
                    )
                }
            }
        }
    }
}