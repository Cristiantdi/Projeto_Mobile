package com.example.alarmapp

import androidx.lifecycle.LiveData

class AlarmRepository(private val alarmDao: AlarmDao) {

    fun getAllAlarms(): LiveData<List<Alarm>> {
        return alarmDao.getAll()
    }

    fun getAlarmById(id: Int): LiveData<Alarm> {
        return alarmDao.getAlarmById(id)
    }

    suspend fun insert(alarm: Alarm) {
        alarmDao.insert(alarm)
    }

    suspend fun update(alarm: Alarm) {
        alarmDao.update(alarm)
    }

    suspend fun delete(alarm: Alarm) {
        alarmDao.delete(alarm)
    }
}