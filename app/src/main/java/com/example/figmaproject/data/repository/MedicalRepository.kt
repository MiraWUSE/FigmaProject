package com.example.figmaproject.data.repository

import com.example.figmaproject.data.local.dao.DoctorDao
import com.example.figmaproject.data.local.dao.ReviewDao
import com.example.figmaproject.data.local.dao.ServiceDao
import com.example.figmaproject.data.local.entity.Appointment
import com.example.figmaproject.data.local.entity.Doctor
import com.example.figmaproject.data.local.entity.Review
import com.example.figmaproject.data.local.entity.Service
import kotlinx.coroutines.flow.Flow

// Репозиторий — посредник между ViewModel и DAO
class MedicalRepository(
    private val doctorDao: DoctorDao,
    private val serviceDao: ServiceDao,
    private val reviewDao: ReviewDao
) {


    // Все врачи (поток)
    val allDoctors: Flow<List<Doctor>> = doctorDao.getAllDoctors()

    // Врач по ID
    fun getDoctorById(doctorId: Int): Flow<Doctor?> = doctorDao.getDoctorById(doctorId)

    // Поиск по имени/специальности
    fun searchDoctors(query: String): Flow<List<Doctor>> = doctorDao.searchDoctors(query)

    // Фильтр по специальности
    fun getDoctorsBySpecialty(specialty: String): Flow<List<Doctor>> = doctorDao.getDoctorsBySpecialty(specialty)

    // Добавить одного врача
    suspend fun insertDoctor(doctor: Doctor) = doctorDao.insertDoctor(doctor)

    // Добавить нескольких врачей
    suspend fun insertDoctors(doctors: List<Doctor>) = doctorDao.insertDoctors(doctors)



    // Все услуги
    val allServices: Flow<List<Service>> = serviceDao.getAllServices()

    // Добавить услугу
    suspend fun insertService(service: Service) = serviceDao.insertService(service)

    // Добавить несколько услуг
    suspend fun insertServices(services: List<Service>) = serviceDao.insertServices(services)



    // Отзывы врача
    fun getReviewsByDoctorId(doctorId: Int): Flow<List<Review>> = reviewDao.getReviewsByDoctorId(doctorId)

    // Количество отзывов
    fun getReviewsCount(doctorId: Int): Flow<Int> = reviewDao.getReviewsCount(doctorId)

    // Добавить отзыв
    suspend fun insertReview(review: Review) = reviewDao.insertReview(review)
}