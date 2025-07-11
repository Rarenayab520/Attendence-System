package com.nayab.attendencesystem.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.nayab.attendencesystem.R
import com.nayab.attendencesystem.data.db.AppDatabase
import com.nayab.attendencesystem.utils.SessionManager
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val session = SessionManager(this)
        val db = AppDatabase.getDatabase(this)

        lifecycleScope.launch {
            val adminExists = db.userDao().isAdminExists()

            if (!adminExists) {
                // First launch — no admin registered
                startActivity(Intent(this@SplashActivity, AdminSignUpActivity::class.java))
            } else if (session.isLoggedIn()) {
                // Logged in — go to appropriate dashboard
                val role = session.getRole()
                if (role == "admin") {
                    startActivity(Intent(this@SplashActivity, AdminActivity::class.java))
                } else {
                    startActivity(Intent(this@SplashActivity, QRScanActivity::class.java))
                }
            } else {
                // No session — go to login
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            }

            finish() // Close splash screen
        }
    }
}
