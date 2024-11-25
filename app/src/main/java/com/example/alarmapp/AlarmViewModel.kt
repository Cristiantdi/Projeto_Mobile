package com.example.alarmapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AlarmViewModel(private val repository: AlarmRepository) : ViewModel() {

    fun getAllAlarms(): LiveData<List<Alarm>> {
        return repository.getAllAlarms()
    }

    fun getAlarmById(id: Int): LiveData<Alarm> {
        return repository.getAlarmById(id)
    }

    fun insert(alarm: Alarm) {
        viewModelScope.launch {
            repository.insert(alarm)
        }
    }

    fun update(alarm: Alarm) {
        viewModelScope.launch {
            repository.update(alarm)
        }
    }

    fun delete(alarm: Alarm) {
        viewModelScope.launch {
            repository.delete(alarm)
        }
    }
}