package com.example.alarmapp

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.alarmapp.databinding.ActivityEditAlarmBinding
import kotlinx.coroutines.launch
import java.util.Calendar

class EditAlarmActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditAlarmBinding
    private val alarmViewModel: AlarmViewModel by viewModels {
        AlarmViewModelFactory((application as AlarmApplication).repository)
    }
    private var alarmId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.timePicker.setIs24HourView(true)

        alarmId = intent.getIntExtra("ALARM_ID", 0)
        alarmViewModel.getAlarmById(alarmId).observe(this, Observer { alarm ->
            alarm?.let {
                val timeParts = it.time.split(":")
                binding.timePicker.hour = timeParts[0].toInt()
                binding.timePicker.minute = timeParts[1].toInt()
            }
        })

        binding.btnUpdate.setOnClickListener {
            val hour = binding.timePicker.hour
            val minute = binding.timePicker.minute
            val updatedAlarm = Alarm(id = alarmId, time = "$hour:$minute", repeat = false, active = true)
            updateAlarmInDatabase(updatedAlarm) {
                // Configurar o alarme após atualizar no banco de dados
                setAlarm(hour, minute)
                finish()
            }
        }
    }

    private fun updateAlarmInDatabase(alarm: Alarm, onUpdated: () -> Unit) {
        lifecycleScope.launch {

            (application as AlarmApplication).repository.update(alarm)
            onUpdated()
        }
    }

    private fun setAlarm(hour: Int, minute: Int) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }


        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            throw SecurityException("Cannot schedule exact alarms without permission")
        }

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }
}