package com.example.figmaproject.data.local.dao

import androidx.room.*
import com.example.figmaproject.data.local.entity.Service
import kotlinx.coroutines.flow.Flow

@Dao
interface ServiceDao {

    // Получить все услуги
    @Query("SELECT * FROM services")
    fun getAllServices(): Flow<List<Service>>

    // Добавить одну услугу
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertService(service: Service)

    // Добавить несколько услуг
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertServices(services: List<Service>)
}