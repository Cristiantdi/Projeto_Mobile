package com.example.alarmapp

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.alarmapp.databinding.ActivityAddAlarmBinding
import kotlinx.coroutines.launch
import java.util.Calendar

class AddAlarmActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddAlarmBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar o TimePicker para o formato de 24 horas
        binding.timePicker.setIs24HourView(true)

        binding.btnSave.setOnClickListener {
            val hour = binding.timePicker.hour
            val minute = binding.timePicker.minute
            val time = String.format("%02d:%02d", hour, minute)
            val repeat = binding.switchRepeat.isChecked
            val active = true

            // Salvar o horário no banco de dados
            val alarm = Alarm(time = time, repeat = repeat, active = active)
            saveAlarmToDatabase(alarm) {
                // Configurar o alarme após salvar no banco de dados
                setAlarm(hour, minute)
                finish()
            }
        }
    }

    private fun saveAlarmToDatabase(alarm: Alarm, onSaved: () -> Unit) {
        lifecycleScope.launch {

            (application as AlarmApplication).repository.insert(alarm)
            onSaved()
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