package com.nayab.attendencesystem.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "attendance",
    primaryKeys = ["userId", "date"] // Composite primary key
)
data class Attendance(
    val userId: String,
    val date: String
)

