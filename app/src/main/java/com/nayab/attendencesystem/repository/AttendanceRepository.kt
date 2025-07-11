package com.nayab.attendencesystem.repository

import com.nayab.attendencesystem.data.dao.AttendanceDao
import com.nayab.attendencesystem.data.dao.UserDao
import com.nayab.attendencesystem.data.model.Attendance
import com.nayab.attendencesystem.data.model.User

class AttendanceRepository(
    private val userDao: UserDao,
    private val attendanceDao: AttendanceDao
) {
    // Insert new user (admin or regular)
    suspend fun insertUser(user: User) = userDao.insertUser(user)

    // Login - fetch user by username + password
    suspend fun getUserByCredentials(username: String, password: String) =
        userDao.getUser(username, password)

    // Check if admin is already registered (used on app launch)
    suspend fun isAdminExists() = userDao.isAdminExists()

    // Mark attendance
    suspend fun markAttendance(attendance: Attendance) =
        attendanceDao.markAttendance(attendance)

    // Check if this user has already marked attendance today
    suspend fun isAttendanceMarked(username: String, date: String) =
        attendanceDao.isAttendanceMarked(username, date)

    // Get attendance list for a specific date
    suspend fun getAttendanceByDate(date: String) =
        attendanceDao.getAttendanceByDateList(date)

    // Get all attendance records
    suspend fun getAllAttendance(): List<Attendance> =
        attendanceDao.getAllAttendance()
}
