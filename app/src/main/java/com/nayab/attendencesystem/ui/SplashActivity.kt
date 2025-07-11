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

            when {
                // ✅ If first time and no admin → show sign up
                session.isFirstLaunch() && !adminExists -> {
                    session.setFirstLaunchDone()
                    startActivity(Intent(this@SplashActivity, AdminSignUpActivity::class.java))
                }

                // ✅ After first launch → always go to login screen
                else -> {
                    session.clearSession() // 🔥 clear old session (logout effect)
                    startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                }
            }

            finish()
        }
    }
}

