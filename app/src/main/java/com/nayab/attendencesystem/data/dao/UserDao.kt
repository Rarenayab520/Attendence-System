package com.nayab.attendencesystem.data.dao

import androidx.room.*
import com.nayab.attendencesystem.data.model.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    suspend fun getUser(username: String, password: String): User?

    @Query("SELECT COUNT(*) FROM users WHERE role = 'admin'")
    suspend fun adminExists(): Int
}
