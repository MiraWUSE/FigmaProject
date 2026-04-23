package com.example.figmaproject.data.local.dao

import androidx.room.*
import com.example.figmaproject.data.local.entity.Doctor
import kotlinx.coroutines.flow.Flow

// DAO — доступ к базе данных
@Dao
interface DoctorDao {

    // Получить всех врачей
    @Query("SELECT * FROM doctors")
    fun getAllDoctors(): Flow<List<Doctor>>

    // Получить врача по ID
    @Query("SELECT * FROM doctors WHERE id = :doctorId")
    fun getDoctorById(doctorId: Int): Flow<Doctor?>

    // Поиск врачей по имени или специальности
    @Query("SELECT * FROM doctors WHERE name LIKE '%' || :query || '%' OR specialty LIKE '%' || :query || '%'")
    fun searchDoctors(query: String): Flow<List<Doctor>>

    // Фильтр по специальности
    @Query("SELECT * FROM doctors WHERE specialty = :specialty")
    fun getDoctorsBySpecialty(specialty: String): Flow<List<Doctor>>

    // Добавить одного врача
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDoctor(doctor: Doctor)

    // Добавить несколько врачей
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDoctors(doctors: List<Doctor>)

    // Обновить врача
    @Update
    suspend fun updateDoctor(doctor: Doctor)

    // Удалить врача
    @Delete
    suspend fun deleteDoctor(doctor: Doctor)
}