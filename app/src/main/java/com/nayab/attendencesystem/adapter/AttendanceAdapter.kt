package com.nayab.attendencesystem.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nayab.attendencesystem.data.model.Attendance
import com.nayab.attendencesystem.databinding.ItemAttendanceBinding

class AttendanceAdapter : RecyclerView.Adapter<AttendanceAdapter.ViewHolder>() {
    private var attendanceList = listOf<Attendance>()

    fun submitList(list: List<Attendance>) {
        attendanceList = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemAttendanceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Attendance) {
            binding.userIdTextView.text = "User ID: ${item.userId}"
            binding.dateTextView.text = "Date: ${item.date}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAttendanceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = attendanceList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(attendanceList[position])
    }
}
