package com.nayab.attendencesystem.utils

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.nayab.attendencesystem.data.model.Attendance
import java.io.File
import java.io.FileWriter

object ExportUtils {

    fun exportToCSV(context: Context, data: List<Attendance>): Uri? {
        return try {
            val dir = File(context.getExternalFilesDir(null), "AttendanceExports")
            if (!dir.exists()) dir.mkdirs()

            val file = File(dir, "attendance_${System.currentTimeMillis()}.csv")
            val writer = FileWriter(file)

            writer.append("User ID,Date,Time\n")
            data.forEach {
                writer.append("${it.userId},${it.date},${it.time}\n")
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
