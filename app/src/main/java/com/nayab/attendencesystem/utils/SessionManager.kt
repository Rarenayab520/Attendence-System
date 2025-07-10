package com.nayab.attendencesystem.utils

import android.content.Context

class SessionManager(context: Context) {
    private val prefs = context.getSharedPreferences("session", Context.MODE_PRIVATE)

    fun saveUser(username: String, role: String) {
        prefs.edit().apply {
            putString("username", username)
            putString("role", role)
            putBoolean("isLoggedIn", true)
            apply()
        }
    }

    fun isLoggedIn() = prefs.getBoolean("isLoggedIn", false)
    fun getUsername() = prefs.getString("username", "")
    fun getRole() = prefs.getString("role", "")
    fun clearSession() = prefs.edit().clear().apply()
}
