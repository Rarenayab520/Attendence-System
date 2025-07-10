package com.nayab.attendencesystem.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import com.nayab.attendencesystem.R
import com.nayab.attendencesystem.data.db.AppDatabase
import com.nayab.attendencesystem.data.model.Attendance
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class QRScanActivity : AppCompatActivity() {

    private lateinit var barcodeView: DecoratedBarcodeView
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrscan)

        db = AppDatabase.getDatabase(this)
        barcodeView = findViewById(R.id.scanner_view)

        barcodeView.barcodeView.decoderFactory = DefaultDecoderFactory()
        barcodeView.decodeContinuous(object : BarcodeCallback {
            override fun barcodeResult(result: BarcodeResult?) {
                result?.text?.let { userId ->
                    barcodeView.pause()
                    markAttendance(userId)
                }
            }

            override fun possibleResultPoints(resultPoints: MutableList<com.google.zxing.ResultPoint>?) {}
        })
    }

    private fun markAttendance(userId: String) {
        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        lifecycleScope.launch {
            val alreadyMarked = db.attendanceDao().isAttendanceMarked(userId, date) > 0
            if (alreadyMarked) {
                Toast.makeText(this@QRScanActivity, "Already marked for today!", Toast.LENGTH_SHORT).show()
            } else {
                db.attendanceDao().markAttendance(Attendance(userId, date))
                Toast.makeText(this@QRScanActivity, "Attendance marked for $userId", Toast.LENGTH_SHORT).show()
            }
            finish()
        }
    }

}
