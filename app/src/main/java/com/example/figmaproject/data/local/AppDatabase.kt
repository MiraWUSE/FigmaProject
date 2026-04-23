package com.example.figmaproject.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.figmaproject.data.local.dao.DoctorDao
import com.example.figmaproject.data.local.dao.ProfileDao
import com.example.figmaproject.data.local.dao.ServiceDao
import com.example.figmaproject.data.local.dao.ReviewDao
import com.example.figmaproject.data.local.entity.Appointment
import com.example.figmaproject.data.local.entity.Doctor
import com.example.figmaproject.data.local.entity.Profile
import com.example.figmaproject.data.local.entity.Service
import com.example.figmaproject.data.local.entity.Review

// Настройка базы данных Room
@Database(
    entities = [
        Profile::class,      // Таблица профилей
        Doctor::class,       // Таблица врачей
        Service::class,      // Таблица услуг
        Review::class,       // Таблица отзывов
        Appointment::class   // Таблица записей
    ],
    version = 2,             // Версия БД
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    // Доступ к DAO (таблицам)
    abstract fun profileDao(): ProfileDao
    abstract fun doctorDao(): DoctorDao
    abstract fun serviceDao(): ServiceDao
    abstract fun reviewDao(): ReviewDao

    companion object {
        @Volatile  // Гарантия видимости изменений во всех потоках
        private var INSTANCE: AppDatabase? = null

        // Получить экземпляр базы данных (Singleton)
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "medbook_database"  // Имя файла БД
                )
                    .fallbackToDestructiveMigration()  // Сброс БД при изменении версии
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}