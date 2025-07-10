package com.nayab.attendencesystem.data.dao

import androidx.room.*
import com.nayab.attendencesystem.data.model.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE userId = :id")
    suspend fun getUserById(id: String): User?
}