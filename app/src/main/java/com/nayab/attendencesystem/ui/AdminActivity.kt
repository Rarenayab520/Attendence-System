package com.nayab.attendencesystem.ui

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.WindowInsetsController
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import com.nayab.attendencesystem.data.db.AppDatabase
import com.nayab.attendencesystem.data.model.User
import com.nayab.attendencesystem.databinding.ActivityAdminBinding
import com.nayab.attendencesystem.utils.SessionManager
import kotlinx.coroutines.launch

class AdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminBinding
    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
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
        val userDao = AppDatabase.getDatabase(this).userDao()

        // ✅ Add User
        binding.btnAddUser.setOnClickListener {
            val username = binding.etNewUsername.text.toString().trim()
            val password = binding.etNewPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both username and password.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                userDao.insertUser(User(username = username, password = password, role = "user"))
                Toast.makeText(this@AdminActivity, "User added successfully.", Toast.LENGTH_SHORT).show()
                binding.etNewUsername.text.clear()
                binding.etNewPassword.text.clear()
            }
        }

        // ✅ Generate QR Code (for username)
        binding.generateQRButton.setOnClickListener {
            val username = binding.qrUsernameEditText.text.toString().trim()
            if (username.isEmpty()) {
                Toast.makeText(this, "Enter username to generate QR", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val qrBitmap = generateQRCode(username)
            qrBitmap?.let {
                binding.qrImageView.setImageBitmap(it)
            } ?: Toast.makeText(this, "QR generation failed", Toast.LENGTH_SHORT).show()
        }

        // ✅ View Attendance
        binding.viewAttendanceButton.setOnClickListener {
            startActivity(Intent(this, AttendanceListActivity::class.java))
        }

        // ✅ Logout
        binding.backButton.setOnClickListener {
            session.clearSession()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

    }

    private fun generateQRCode(data: String): Bitmap? {
        return try {
            val writer = QRCodeWriter()
            val bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, 512, 512)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bmp.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
            bmp
        } catch (e: WriterException) {
            e.printStackTrace()
            null
        }
    }
}
