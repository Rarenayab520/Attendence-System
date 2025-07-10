package com.nayab.attendencesystem.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.nayab.attendencesystem.data.model.User
import com.nayab.attendencesystem.data.db.AppDatabase
import com.nayab.attendencesystem.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = AppDatabase.getDatabase(this)

        binding.loginButton.setOnClickListener {
            val userId = binding.userIdEditText.text.toString().trim()
            val name = binding.userNameEditText.text.toString().trim()
            val role = if (binding.radioAdmin.isChecked) "admin" else "user"

            if (userId.isEmpty() || name.isEmpty()) {
                Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val user = User(userId, name, role)

            lifecycleScope.launch {
                db.userDao().insertUser(user)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Login successful", Toast.LENGTH_SHORT).show()
                    val intent = if (role == "admin") {
                        Intent(this@MainActivity, AdminActivity::class.java)
                    } else {
                        Intent(this@MainActivity, QRScanActivity::class.java)
                    }
                    intent.putExtra("userId", userId)
                    startActivity(intent)
                }
            }
        }
    }
}
