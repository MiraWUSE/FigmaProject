package com.example.figmaproject.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.figmaproject.data.local.entity.Profile
import com.example.figmaproject.data.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

// ViewModel для профиля пользователя
class ProfileViewModel(private val repository: ProfileRepository) : ViewModel() {

    // Все профили (обычно один)
    val allProfiles: Flow<List<Profile>> = repository.allProfiles

    // Добавить/обновить профиль
    fun insertProfile(profile: Profile) {
        viewModelScope.launch { repository.insertProfile(profile) }
    }

    // Обновить профиль
    fun updateProfile(profile: Profile) {
        viewModelScope.launch { repository.updateProfile(profile) }
    }

    // Удалить профиль
    fun deleteProfile(profile: Profile) {
        viewModelScope.launch { repository.deleteProfile(profile) }
    }

    // Профиль по ID
    fun getProfileById(profileId: Int): Flow<Profile?> {
        return repository.getProfileById(profileId)
    }
}