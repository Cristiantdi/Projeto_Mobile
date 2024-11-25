package com.example.alarmapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.alarmapp.databinding.ItemAlarmBinding

class AlarmAdapter(
    private var alarms: List<Alarm>,
    private val onEditClick: (Alarm) -> Unit,
    private val onDeleteClick: (Alarm) -> Unit
) : RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        val binding = ItemAlarmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlarmViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        val alarm = alarms[position]
        holder.bind(alarm)
        holder.binding.btnEdit.setOnClickListener {
            onEditClick(alarm)
        }
        holder.binding.btnDelete.setOnClickListener {
            onDeleteClick(alarm)
        }
    }

    override fun getItemCount(): Int = alarms.size

    fun updateAlarms(newAlarms: List<Alarm>) {
        alarms = newAlarms
        notifyDataSetChanged()
    }

    class AlarmViewHolder(val binding: ItemAlarmBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(alarm: Alarm) {
            binding.textViewTime.text = alarm.time

        }
    }
}