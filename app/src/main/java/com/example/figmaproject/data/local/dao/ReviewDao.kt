package com.example.figmaproject.data.local.dao

import androidx.room.*
import com.example.figmaproject.data.local.entity.Review
import kotlinx.coroutines.flow.Flow

@Dao
interface ReviewDao {

    // Получить отзывы врача (сначала новые)
    @Query("SELECT * FROM reviews WHERE doctorId = :doctorId ORDER BY date DESC")
    fun getReviewsByDoctorId(doctorId: Int): Flow<List<Review>>

    // Получить количество отзывов
    @Query("SELECT COUNT(*) FROM reviews WHERE doctorId = :doctorId")
    fun getReviewsCount(doctorId: Int): Flow<Int>

    // Добавить отзыв
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: Review)
}