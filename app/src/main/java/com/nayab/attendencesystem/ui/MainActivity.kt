package com.nayab.attendencesystem.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.nayab.attendencesystem.data.model.User
import com.nayab.attendencesystem.data.db.AppDatabase
import com.nayab.attendencesystem.databinding.ActivityMainBinding
import com.nayab.attendencesystem.ui.AdminActivity
import com.nayab.attendencesystem.ui.QRScanActivity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase.getDatabase(this)

        binding.loginButton.setOnClickListener {
            val userId = binding.userIdEditText.text.toString().trim()
            val name = binding.userNameEditText.text.toString().trim()
            val role = if (binding.radioAdmin.isChecked) "admin" else "user"

            if (userId.isEmpty() || name.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val user = User(userId, name, role)

            lifecycleScope.launch {
                db.userDao().insertUser(user)
                val intent = if (role == "admin") {
                    Intent(this@MainActivity, AdminActivity::class.java)
                } else {
                    Intent(this@MainActivity, QRScanActivity::class.java)
                }
                intent.putExtra("userId", userId)
                startActivity(intent)
                finish()
            }
        }
    }
}
