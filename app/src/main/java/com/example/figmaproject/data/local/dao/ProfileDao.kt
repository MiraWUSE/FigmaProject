package com.example.figmaproject.data.local.dao

import androidx.room.*
import com.example.figmaproject.data.local.entity.Profile
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao {

    // Получить все профили
    @Query("SELECT * FROM profiles")
    fun getAllProfiles(): Flow<List<Profile>>

    // Получить профиль по ID
    @Query("SELECT * FROM profiles WHERE id = :profileId")
    fun getProfileById(profileId: Int): Flow<Profile?>

    // Добавить/обновить профиль
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: Profile)

    // Обновить профиль
    @Update
    suspend fun updateProfile(profile: Profile)

    // Удалить профиль
    @Delete
    suspend fun deleteProfile(profile: Profile)

    // Удалить профиль по ID
    @Query("DELETE FROM profiles WHERE id = :profileId")
    suspend fun deleteProfileById(profileId: Int)

    // Удалить все профили
    @Query("DELETE FROM profiles")
    suspend fun deleteAllProfiles()
}