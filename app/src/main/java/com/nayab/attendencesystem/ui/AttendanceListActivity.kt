package com.nayab.attendencesystem.ui

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.nayab.attendencesystem.R
import com.nayab.attendencesystem.data.db.AppDatabase
import com.nayab.attendencesystem.repository.AttendanceRepository
import com.nayab.attendencesystem.utils.ExportUtils
import com.nayab.attendencesystem.viewmodel.AttendanceViewModel
import com.nayab.attendencesystem.viewmodel.AttendanceViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class AttendanceListActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var selectedDateTextView: TextView
    private lateinit var selectDateButton: Button
    private lateinit var exportButton: Button
    private lateinit var backButton: Button

    private var currentDate: String = ""

    private val viewModel: AttendanceViewModel by viewModels {
        AttendanceViewModelFactory(
            AttendanceRepository(
                AppDatabase.getDatabase(this).userDao(),
                AppDatabase.getDatabase(this).attendanceDao()
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attendance_list)

        // Bind UI elements
        listView = findViewById(R.id.attendanceListView)
        selectedDateTextView = findViewById(R.id.selectedDateTextView)
        selectDateButton = findViewById(R.id.selectDateButton)
        exportButton = findViewById(R.id.exportButton)
        backButton = findViewById(R.id.backButton)

        // Set today's date by default
        currentDate = getTodayDate()
        selectedDateTextView.text = "Selected Date: $currentDate"

        loadAttendance(currentDate)

        selectDateButton.setOnClickListener {
            showDatePicker()
        }

        exportButton.setOnClickListener {
            exportAttendance(currentDate)
        }

        backButton.setOnClickListener {
            finish()
        }
    }

    private fun getTodayDate(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        DatePickerDialog(this,
            { _, year, month, dayOfMonth ->
                currentDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
                selectedDateTextView.text = "Selected Date: $currentDate"
                loadAttendance(currentDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun loadAttendance(date: String) {
        lifecycleScope.launch {
            val attendanceList = viewModel.getAttendanceByDate(date)
            val items = attendanceList.map {
                "Username: ${it.username}\nDate: ${it.date}\nTime: ${it.time}"
            }
            listView.adapter = ArrayAdapter(
                this@AttendanceListActivity,
                android.R.layout.simple_list_item_1,
                items
            )
        }
    }

    private fun exportAttendance(date: String) {
        lifecycleScope.launch {
            val attendanceList = viewModel.getAttendanceByDate(date)

            if (attendanceList.isEmpty()) {
                Toast.makeText(this@AttendanceListActivity, "No data to export.", Toast.LENGTH_SHORT).show()
                return@launch
            }

            val uri = withContext(Dispatchers.IO) {
                ExportUtils.exportToCSV(this@AttendanceListActivity, attendanceList)
            }

            if (uri != null) {
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/csv"
                    putExtra(Intent.EXTRA_STREAM, uri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                startActivity(Intent.createChooser(shareIntent, "Share CSV via"))
            } else {
                Toast.makeText(this@AttendanceListActivity, "Export failed.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
