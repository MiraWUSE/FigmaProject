package com.example.figmaproject.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "services")
data class Service(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val iconResId: Int,
    val description: String
)