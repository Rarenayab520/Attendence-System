package com.nayab.attendencesystem.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.nayab.attendencesystem.data.model.User
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.nayab.attendencesystem.data.db.AppDatabase
import com.nayab.attendencesystem.databinding.ActivityAdminSignUpBinding
import com.nayab.attendencesystem.utils.SessionManager
import com.nayab.attendencesystem.viewmodel.AttendanceViewModel
import com.nayab.attendencesystem.viewmodel.AttendanceViewModelFactory
import kotlinx.coroutines.launch

class AdminSignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminSignUpBinding
    private val viewModel: AttendanceViewModel by viewModels {
        AttendanceViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminSignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userDao = AppDatabase.getDatabase(this).userDao()

        binding.btnRegister.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                lifecycleScope.launch {
                    val user = User(username = username, password = password, role = "admin")
                    viewModel.insertUser(user)

                    // Save session
                    val session = SessionManager(this@AdminSignUpActivity)
                    session.saveUser(username, "admin")

                    // Go directly to Dashboard
                    startActivity(Intent(this@AdminSignUpActivity, AdminActivity::class.java))
                    finish()
                }
            } else {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show()
            }
        }


        }
    }

