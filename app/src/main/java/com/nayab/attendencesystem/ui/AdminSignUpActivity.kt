package com.nayab.attendencesystem.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.nayab.attendencesystem.data.model.User
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.nayab.attendencesystem.data.db.AppDatabase
import com.nayab.attendencesystem.databinding.ActivityAdminSignUpBinding
import kotlinx.coroutines.launch

class AdminSignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminSignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminSignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userDao = AppDatabase.getDatabase(this).userDao()

        binding.btnRegister.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()

            if (username.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                userDao.insertUser(User(username = username, password = password, role = "admin"))
                startActivity(Intent(this@AdminSignUpActivity, LoginActivity::class.java))
                finish()
            }
        }
    }
}
