package com.example.vectors.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "operations_history")
data class OperationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val operationName: String,
    val inputData: String,
    val resultText: String,
    val timestamp: Long = System.currentTimeMillis()
)