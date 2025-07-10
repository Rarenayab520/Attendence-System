package com.nayab.attendencesystem.repository

import com.nayab.attendencesystem.data.dao.AttendanceDao
import com.nayab.attendencesystem.data.dao.UserDao
import com.nayab.attendencesystem.data.model.Attendance
import com.nayab.attendencesystem.data.model.User

class AttendanceRepository(
    private val userDao: UserDao,
    private val attendanceDao: AttendanceDao
) {
    suspend fun insertUser(user: User) = userDao.insertUser(user)

    suspend fun getUserById(id: String) = userDao.getUserById(id)

    suspend fun markAttendance(attendance: Attendance) = attendanceDao.markAttendance(attendance)

    fun getAttendanceByDate(date: String) = attendanceDao.getAttendanceByDate(date)

    fun getAllAttendance() = attendanceDao.getAllAttendance()
}
