package com.example.figmaproject.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "appointments",
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
data class Appointment(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val doctorId: Int,
    val patientName: String,
    val date: String,
    val time: String,
    val status: String
)