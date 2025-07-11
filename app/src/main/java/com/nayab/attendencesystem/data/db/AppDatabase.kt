package com.nayab.attendencesystem.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nayab.attendencesystem.data.dao.AttendanceDao
import com.nayab.attendencesystem.data.dao.UserDao
import com.nayab.attendencesystem.data.model.Attendance
import com.nayab.attendencesystem.data.model.User

@Database(entities = [User::class, Attendance::class], version = 2)
abstract class AppDatabase : RoomDatabase() {

    abstract fun attendanceDao(): AttendanceDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "attendance_db"
                )
                    .fallbackToDestructiveMigration() // Safe for now if testing
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}


