package com.example.vectors.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [VectorEntity::class, OperationEntity::class], // Має бути тут
    version = 2, // Переконайся, що версія 2 або вище
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun vectorDao(): VectorDao
    abstract fun operationDao(): OperationDao // Має бути тут
}