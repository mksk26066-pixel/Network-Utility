package com.example.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ConfigProfile::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun configDao(): ConfigDao
}
