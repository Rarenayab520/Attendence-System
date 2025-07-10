package com.nayab.attendencesystem.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.nayab.attendencesystem.data.model.Attendance

@Dao
interface AttendanceDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun markAttendance(attendance: Attendance)

    @Query("SELECT * FROM attendance WHERE date = :date")
    suspend fun getAttendanceByDateList(date: String): List<Attendance>

    @Query("SELECT * FROM attendance")
    suspend fun getAllAttendance(): List<Attendance>

    @Query("SELECT COUNT(*) FROM attendance WHERE userId = :userId AND date = :date")
    suspend fun isAttendanceMarked(userId: String, date: String): Int
}
