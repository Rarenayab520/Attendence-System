package com.nayab.attendencesystem.ui

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
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


class AdminActivity : AppCompatActivity()  {

    private lateinit var binding: ActivityAdminBinding
    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userDao = AppDatabase.getDatabase(this).userDao()
        session = SessionManager(this)

        // 🔐 Add User
        binding.btnAddUser.setOnClickListener {
            val username = binding.etNewUsername.text.toString()
            val password = binding.etNewPassword.text.toString()

            if (username.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Fill user fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                userDao.insertUser(User(username = username, password = password, role = "user"))
                Toast.makeText(this@AdminActivity, "User added!", Toast.LENGTH_SHORT).show()
                binding.etNewUsername.text.clear()
                binding.etNewPassword.text.clear()
            }
        }

        // 🔳 Generate QR
        binding.generateQRButton.setOnClickListener {
            val userId = binding.qrUserIdEditText.text.toString()
            if (userId.isBlank()) {
                Toast.makeText(this, "Enter User ID", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val bitmap = generateQRCode(userId)
            if (bitmap != null) {
                binding.qrImageView.setImageBitmap(bitmap)
            } else {
                Toast.makeText(this, "Failed to generate QR", Toast.LENGTH_SHORT).show()
            }
        }

        // 📄 View Attendance List
        binding.viewAttendanceButton.setOnClickListener {
            startActivity(Intent(this, AttendanceListActivity::class.java))
        }

        // 🔙 Logout
        binding.backButton.setOnClickListener {
            session.clearSession()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun generateQRCode(data: String): Bitmap? {
        val writer = QRCodeWriter()
        return try {
            val bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, 512, 512)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bmp.setPixel(x, y, if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
                }
            }
            bmp
        } catch (e: WriterException) {
            e.printStackTrace()
            null
        }
    }
}
