package com.nayab.attendencesystem.ui

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.WindowInsetsController
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.nayab.attendencesystem.data.db.AppDatabase
import com.nayab.attendencesystem.databinding.ActivityLoginBinding
import com.nayab.attendencesystem.utils.SessionManager
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
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


        val userDao = AppDatabase.getDatabase(this).userDao()
        session = SessionManager(this)

        // 🟡 Setup Role Spinner (admin/user)
        val roles = listOf("admin", "user")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, roles)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.roleSpinner.adapter = adapter

        // 🔁 First time: If no admin, redirect to AdminSignUpActivity
        lifecycleScope.launch {
            val adminExists = userDao.isAdminExists()
            if (!adminExists) {
                startActivity(Intent(this@LoginActivity, AdminSignUpActivity::class.java))
                finish()
            }
        }

        // 🔓 Login button action
        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val selectedRole = binding.roleSpinner.selectedItem.toString()

            if (username.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Enter credentials", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val user = userDao.getUser(username, password)
                if (user != null && user.role == selectedRole) {
                    // ✅ Save session
                    session.saveUser(user.username, user.role)

                    // ✅ Go to appropriate dashboard
                    val intent = if (user.role == "admin") {
                        Intent(this@LoginActivity, AdminActivity::class.java)
                    } else {
                        Intent(this@LoginActivity, QRScanActivity::class.java)
                    }
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        "Invalid credentials or role",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}
