package com.nayab.attendencesystem.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.nayab.attendencesystem.data.model.Attendance
import com.nayab.attendencesystem.data.model.User
import com.nayab.attendencesystem.repository.AttendanceRepository
import kotlinx.coroutines.Dispatchers

class AttendanceViewModel(private val repo: AttendanceRepository) : ViewModel() {

     suspend fun getAttendanceByDate(date: String) = repo.getAttendanceByDate(date)

    suspend fun getUserById(id: String) = repo.getUserById(id)

    suspend fun isAlreadyMarked(userId: String, date: String) = repo.isAttendanceMarked(userId, date)

    suspend fun insertUser(user: User) = repo.insertUser(user)

    suspend fun markAttendance(attendance: Attendance) = repo.markAttendance(attendance)

    suspend fun getAllAttendance() = repo.getAllAttendance()
}

class AttendanceViewModelFactory(private val repo: AttendanceRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AttendanceViewModel::class.java)) {
            return AttendanceViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

