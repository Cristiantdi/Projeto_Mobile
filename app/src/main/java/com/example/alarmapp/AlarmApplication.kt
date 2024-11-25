package com.example.alarmapp

import android.app.Application

class AlarmApplication : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { AlarmRepository(database.alarmDao()) }
}
