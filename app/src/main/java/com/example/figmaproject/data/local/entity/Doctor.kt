package com.example.figmaproject.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "doctors")
data class Doctor(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val specialty: String,
    val experience: Int,
    val rating: Float,
    val visitorsCount: Int,
    val description: String,
    val photoResId: Int,
    val fee: Double,
    val availableTime: String,
    val education: String = "",
    val hospital: String = ""
)