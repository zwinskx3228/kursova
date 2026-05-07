package com.example.vectors.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vectors")
data class VectorEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val x: Float,
    val y: Float,
    val color: Long = 0xFF6B4EFF
)