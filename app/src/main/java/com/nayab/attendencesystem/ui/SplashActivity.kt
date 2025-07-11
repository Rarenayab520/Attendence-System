package com.nayab.attendencesystem.ui

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.WindowInsetsController
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.nayab.attendencesystem.R
import com.nayab.attendencesystem.data.db.AppDatabase
import com.nayab.attendencesystem.utils.SessionManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Dark status bar
        window.statusBarColor = Color.BLACK
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.setSystemBarsAppearance(
                0,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = 0
        }

        val session = SessionManager(this)
        val db = AppDatabase.getDatabase(this)

        lifecycleScope.launch {
            delay(3000) // 2 sec splash screen delay

            val adminExists = db.userDao().isAdminExists()

            // ✅ First launch & no admin: go to sign up screen
            if (session.isFirstLaunch() && !adminExists) {
                session.setFirstLaunchDone()
                startActivity(Intent(this@SplashActivity, AdminSignUpActivity::class.java))
            } else {
                // ✅ For all future launches → Always go to Login screen
                session.clearSession() // clear any session
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            }

            finish()
        }
    }
}
