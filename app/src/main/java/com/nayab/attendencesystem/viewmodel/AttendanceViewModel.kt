package com.nayab.attendencesystem.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.nayab.attendencesystem.data.db.AppDatabase
import com.nayab.attendencesystem.data.model.Attendance
import com.nayab.attendencesystem.data.model.User
import com.nayab.attendencesystem.repository.AttendanceRepository
import kotlinx.coroutines.launch

class AttendanceViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: AttendanceRepository

    init {
        val db = AppDatabase.getDatabase(application)
        repository = AttendanceRepository(db.userDao(), db.attendanceDao())
    }

    fun insertUser(user: User) {
        viewModelScope.launch {
            repository.insertUser(user)
        }
    }

    fun getUserById(id: String): LiveData<User?> {
        val result = MutableLiveData<User?>()
        viewModelScope.launch {
            result.postValue(repository.getUserById(id))
        }
        return result
    }

    fun markAttendance(attendance: Attendance) {
        viewModelScope.launch {
            repository.markAttendance(attendance)
        }
    }

    fun getAttendanceByDate(date: String) = repository.getAttendanceByDate(date)

    fun getAllAttendanceList() = repository.getAllAttendance()
}
