package com.nayab.attendencesystem.ui

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import com.nayab.attendencesystem.databinding.ActivityAdminBinding


class AdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.generateQRButton.setOnClickListener {
            val userId = binding.qrUserIdEditText.text.toString().trim()
            if (userId.isNotEmpty()) {
                val qrBitmap = generateQRCode(userId)
                binding.qrImageView.setImageBitmap(qrBitmap)
            } else {
                Toast.makeText(this, "Enter user ID", Toast.LENGTH_SHORT).show()
            }
        }
        binding.viewAttendanceButton.setOnClickListener {
            startActivity(Intent(this, AttendanceListActivity::class.java))
        }


        binding.backButton.setOnClickListener {
            finish()
        }
    }


    private fun generateQRCode(data: String): Bitmap {
        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, 512, 512)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bmp.setPixel(x, y, if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
            }
        }
        return bmp
    }

}
