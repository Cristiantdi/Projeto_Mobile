package com.example.alarmapp

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AlarmLogDao {
    @Query("SELECT * FROM alarm_logs WHERE alarmId = :alarmId")
    fun getLogsForAlarm(alarmId: Int): LiveData<List<AlarmLog>>

    @Insert
    fun insert(log: AlarmLog)
}