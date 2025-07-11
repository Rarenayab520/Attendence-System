package com.nayab.attendencesystem.data.model

import androidx.room.Entity

@Entity(
    tableName = "attendance",
    primaryKeys = ["username", "date"]
)
data class Attendance(
    val username: String,
    val date: String,
    val time: String
)
