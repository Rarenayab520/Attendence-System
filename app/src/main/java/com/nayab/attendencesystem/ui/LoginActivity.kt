package com.nayab.attendencesystem.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.nayab.attendencesystem.data.db.AppDatabase
import com.nayab.attendencesystem.databinding.ActivityLoginBinding
import com.nayab.attendencesystem.utils.SessionManager
import com.nayab.attendencesystem.ui.QRScanActivity
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userDao = AppDatabase.getDatabase(this).userDao()
        session = SessionManager(this)

        lifecycleScope.launch {
            if (userDao.adminExists() == 0) {
                startActivity(Intent(this@LoginActivity, AdminSignUpActivity::class.java))
                finish()
            }
        }

        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()

            if (username.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Enter credentials", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val user = userDao.getUser(username, password)
                if (user != null) {
                    session.saveUser(user.username, user.role)
                    if (user.role == "admin") {
                        startActivity(Intent(this@LoginActivity, AdminDashboardActivity::class.java))
                    } else {
                        startActivity(Intent(this@LoginActivity, QRScanActivity::class.java))
                    }
                    finish()
                } else {
                    Toast.makeText(this@LoginActivity, "Invalid credentials", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
