package com.example.alarmapp

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AlarmDao {
    @Query("SELECT * FROM alarms")
    fun getAll(): LiveData<List<Alarm>>

    @Query("SELECT * FROM alarms WHERE id = :id")
    fun getAlarmById(id: Int): LiveData<Alarm>

    @Insert
    suspend fun insert(alarm: Alarm): Long

    @Update
    suspend fun update(alarm: Alarm): Int

    @Delete
    suspend fun delete(alarm: Alarm): Int
}