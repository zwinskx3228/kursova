package com.example.vectors.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [VectorEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun vectorDao(): VectorDao
}