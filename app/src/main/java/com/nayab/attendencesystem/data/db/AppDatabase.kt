package com.nayab.attendencesystem.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nayab.attendencesystem.data.dao.AttendanceDao
import com.nayab.attendencesystem.data.dao.UserDao
import com.nayab.attendencesystem.data.model.Attendance
import com.nayab.attendencesystem.data.model.User

@Database(entities = [User::class, Attendance::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun attendanceDao(): AttendanceDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "qr_attendance_db"
                ).build().also { INSTANCE = it }
            }
    }
}
