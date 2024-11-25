package com.example.alarmapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alarmapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val alarmViewModel: AlarmViewModel by viewModels {
        AlarmViewModelFactory((application as AlarmApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = AlarmAdapter(emptyList(),
            onEditClick = { alarm ->
                val intent = Intent(this, EditAlarmActivity::class.java)
                intent.putExtra("ALARM_ID", alarm.id)
                startActivity(intent)
            },
            onDeleteClick = { alarm ->
                alarmViewModel.delete(alarm)
            }
        )
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        alarmViewModel.getAllAlarms().observe(this, Observer { alarms ->
            alarms?.let {
                adapter.updateAlarms(it)
            }
        })


        binding.btnAddAlarm.setOnClickListener {

            val intent = Intent(this, AddAlarmActivity::class.java)
            startActivity(intent)
        }

        }
    }

