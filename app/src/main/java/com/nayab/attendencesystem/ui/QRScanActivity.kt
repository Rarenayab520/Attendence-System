package com.nayab.attendencesystem.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.zxing.integration.android.IntentIntegrator
import com.nayab.attendencesystem.data.db.AppDatabase
import com.nayab.attendencesystem.data.model.Attendance
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class QRScanActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = AppDatabase.getDatabase(this)
        startQRScanner()
    }

    private fun startQRScanner() {
        val integrator = IntentIntegrator(this)
        integrator.setPrompt("Scan QR Code")
        integrator.setOrientationLocked(false)
        integrator.setBeepEnabled(true)
        integrator.setBarcodeImageEnabled(true)
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents != null) {
                val scannedUsername = result.contents.trim()
                val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())

                lifecycleScope.launch {
                    val isMarked = db.attendanceDao().isAttendanceMarked(scannedUsername, currentDate)

                    if (isMarked == 0) {
                        val attendance = Attendance(
                            username = scannedUsername,
                            date = currentDate,
                            time = currentTime
                        )
                        db.attendanceDao().markAttendance(attendance)
                        Toast.makeText(this@QRScanActivity, "✅ Attendance marked for $scannedUsername!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@QRScanActivity, "⚠️ Already marked today for $scannedUsername", Toast.LENGTH_SHORT).show()
                    }

                    // 🔁 Open Attendance List directly and clear back stack
                    val intent = Intent(this@QRScanActivity, AttendanceListActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
            } else {
                Toast.makeText(this, "Scan cancelled", Toast.LENGTH_SHORT).show()
                finish()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
