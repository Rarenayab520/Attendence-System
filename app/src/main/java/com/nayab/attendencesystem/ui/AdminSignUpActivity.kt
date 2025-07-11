package com.nayab.attendencesystem.ui

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.WindowInsetsController
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.nayab.attendencesystem.data.db.AppDatabase
import com.nayab.attendencesystem.data.model.User
import com.nayab.attendencesystem.databinding.ActivityAdminSignUpBinding
import com.nayab.attendencesystem.repository.AttendanceRepository
import com.nayab.attendencesystem.utils.SessionManager
import com.nayab.attendencesystem.viewmodel.AttendanceViewModel
import com.nayab.attendencesystem.viewmodel.AttendanceViewModelFactory
import kotlinx.coroutines.launch

class AdminSignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminSignUpBinding
    private lateinit var session: SessionManager

    // ✅ Initialize Room + Repository + ViewModel
    private val viewModel: AttendanceViewModel by viewModels {
        AttendanceViewModelFactory(
            AttendanceRepository(
                AppDatabase.getDatabase(this).userDao(),
                AppDatabase.getDatabase(this).attendanceDao()
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminSignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = Color.BLACK

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.setSystemBarsAppearance(
                0,  // 👈 No LIGHT_STATUS_BAR means white icons
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = 0  // 👈 Remove light status bar flags
        }

        session = SessionManager(this)

        binding.btnRegister.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                lifecycleScope.launch {
                    val newAdmin = User(username = username, password = password, role = "admin")
                    viewModel.insertUser(newAdmin)

                    // Save login session
                    session.saveUser(username, "admin")

                    // Redirect to Admin Dashboard
                    startActivity(Intent(this@AdminSignUpActivity, AdminActivity::class.java))
                    finish()
                }
            } else {
                Toast.makeText(this, "Please fill in both fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
