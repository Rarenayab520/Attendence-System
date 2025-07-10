package com.nayab.attendencesystem.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.nayab.attendencesystem.adapter.AttendanceAdapter
import com.nayab.attendencesystem.databinding.ActivityAttendanceListBinding
import com.nayab.attendencesystem.viewmodel.AttendanceViewModel
import com.opencsv.CSVWriter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*

class AttendanceListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAttendanceListBinding
    private val viewModel: AttendanceViewModel by viewModels()
    private lateinit var attendanceAdapter: AttendanceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAttendanceListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        // Observe today's attendance
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        fetchAttendanceForDate(today)

        // Export button
        binding.exportBtn.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                val file = File(getExternalFilesDir(null), "attendance.csv")
                val writer = FileWriter(file)
                val csv = CSVWriter(writer)
                csv.writeNext(arrayOf("User ID", "Date"))

                val attendanceList = viewModel.getAllAttendanceList()
                attendanceList.forEach {
                    csv.writeNext(arrayOf(it.userId, it.date))
                }
                csv.close()

                launch(Dispatchers.Main) {
                    Toast.makeText(this@AttendanceListActivity, "Exported to ${file.absolutePath}", Toast.LENGTH_LONG).show()
                }
            }
        }
        binding.selectDateBtn.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(this, { _, y, m, d ->
                val selectedDate = String.format("%04d-%02d-%02d", y, m + 1, d)
                fetchAttendanceForDate(selectedDate)
            }, year, month, day)

            datePicker.show()
        }
    }
    private fun fetchAttendanceForDate(date: String) {
        viewModel.getAttendanceByDate(date).observe(this) { list ->
            attendanceAdapter = AttendanceAdapter(list)
            binding.recyclerView.adapter = attendanceAdapter
        }
    }

}
