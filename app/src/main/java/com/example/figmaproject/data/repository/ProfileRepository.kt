package com.example.figmaproject.data.repository

import com.example.figmaproject.data.local.dao.ProfileDao
import com.example.figmaproject.data.local.entity.Profile
import kotlinx.coroutines.flow.Flow

// Репозиторий для работы с профилем
class ProfileRepository(private val profileDao: ProfileDao) {

    // Все профили (обычно один)
    val allProfiles: Flow<List<Profile>> = profileDao.getAllProfiles()

    // Профиль по ID
    fun getProfileById(profileId: Int): Flow<Profile?> = profileDao.getProfileById(profileId)

    // Добавить/обновить профиль
    suspend fun insertProfile(profile: Profile) {
        profileDao.insertProfile(profile)
    }

    // Обновить профиль
    suspend fun updateProfile(profile: Profile) {
        profileDao.updateProfile(profile)
    }

    // Удалить профиль
    suspend fun deleteProfile(profile: Profile) {
        profileDao.deleteProfile(profile)
    }

    // Удалить все профили
    suspend fun deleteAllProfiles() {
        profileDao.deleteAllProfiles()
    }
}