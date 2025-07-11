package com.nayab.attendencesystem.utils

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.nayab.attendencesystem.data.db.AppDatabase
import com.nayab.attendencesystem.data.model.Attendance
import java.io.File
import java.io.FileWriter

object ExportUtils {

    suspend fun exportToCSV(context: Context, data: List<Attendance>): Uri? {
        return try {
            val db = AppDatabase.getDatabase(context)
            val dir = File(context.getExternalFilesDir(null), "AttendanceExports")
            if (!dir.exists()) dir.mkdirs()

            val file = File(dir, "attendance_${System.currentTimeMillis()}.csv")
            val writer = FileWriter(file)

            writer.append("Username,Role,Date,Time\n")

            for (record in data) {
                val user = db.userDao().getUserByUsername(record.username)
                val username = user?.username ?: record.username
                val role = user?.role ?: "unknown"

                writer.append("$username,$role,${record.date},${record.time}\n")
            }

            writer.flush()
            writer.close()

            FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                file
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
