package com.nayab.attendencesystem.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nayab.attendencesystem.data.model.Attendance
import com.nayab.attendencesystem.databinding.ItemAttendanceBinding

class AttendanceAdapter(private val list: List<Attendance>) :
    RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder>() {

    inner class AttendanceViewHolder(val binding: ItemAttendanceBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceViewHolder {
        val binding = ItemAttendanceBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AttendanceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AttendanceViewHolder, position: Int) {
        val attendance = list[position]
        holder.binding.userIdText.text = attendance.userId
        holder.binding.dateText.text = attendance.date
    }

    override fun getItemCount(): Int = list.size
}
