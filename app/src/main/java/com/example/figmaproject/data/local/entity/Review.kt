package com.example.figmaproject.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "reviews",
    foreignKeys = [
        ForeignKey(
            entity = Doctor::class,
            parentColumns = ["id"],
            childColumns = ["doctorId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("doctorId")]
)
data class Review(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val doctorId: Int,
    val userName: String,
    val rating: Float,
    val comment: String,
    val date: String
)