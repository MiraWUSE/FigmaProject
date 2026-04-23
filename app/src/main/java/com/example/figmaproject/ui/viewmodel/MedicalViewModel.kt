package com.example.figmaproject.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.figmaproject.data.local.entity.Doctor
import com.example.figmaproject.data.local.entity.Review
import com.example.figmaproject.data.local.entity.Service
import com.example.figmaproject.data.repository.MedicalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

// ViewModel для врачей, услуг и отзывов
class MedicalViewModel(private val repository: MedicalRepository) : ViewModel() {

    // === ВРАЧИ ===

    // Все врачи (поток из репозитория)
    val allDoctors: Flow<List<Doctor>> = repository.allDoctors

    // Врач по ID
    fun getDoctorById(doctorId: Int): Flow<Doctor?> = repository.getDoctorById(doctorId)

    // Поиск врачей
    fun searchDoctors(query: String): Flow<List<Doctor>> = repository.searchDoctors(query)

    // Добавить врача (асинхронно)
    fun insertDoctor(doctor: Doctor) {
        viewModelScope.launch { repository.insertDoctor(doctor) }
    }

    // Добавить нескольких врачей
    fun insertDoctors(doctors: List<Doctor>) {
        viewModelScope.launch { repository.insertDoctors(doctors) }
    }

    // === УСЛУГИ ===

    // Все услуги
    val allServices: Flow<List<Service>> = repository.allServices

    // Добавить услугу
    fun insertService(service: Service) {
        viewModelScope.launch { repository.insertService(service) }
    }

    // Добавить несколько услуг
    fun insertServices(services: List<Service>) {
        viewModelScope.launch { repository.insertServices(services) }
    }

    // === ОТЗЫВЫ ===

    // Отзывы врача
    fun getReviewsByDoctorId(doctorId: Int): Flow<List<Review>> = repository.getReviewsByDoctorId(doctorId)

    // Количество отзывов
    fun getReviewsCount(doctorId: Int): Flow<Int> = repository.getReviewsCount(doctorId)

    // Добавить отзыв
    fun insertReview(review: Review) {
        viewModelScope.launch { repository.insertReview(review) }
    }
}