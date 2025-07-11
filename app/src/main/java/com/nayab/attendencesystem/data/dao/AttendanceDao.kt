package com.nayab.attendencesystem.data.dao

import androidx.room.*
import com.nayab.attendencesystem.data.model.Attendance

@Dao
interface AttendanceDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun markAttendance(attendance: Attendance)

    // Get attendance list for a specific date
    @Query("SELECT * FROM attendance WHERE date = :date")
    suspend fun getAttendanceByDateList(date: String): List<Attendance>

    // Get all attendance records
    @Query("SELECT * FROM attendance")
    suspend fun getAllAttendance(): List<Attendance>

    // Check if this user has already marked attendance on the given date
    @Query("SELECT COUNT(*) FROM attendance WHERE username = :username AND date = :date")
    suspend fun isAttendanceMarked(username: String, date: String): Int
}
