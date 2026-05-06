package com.example.vectors.data.local

import android.R
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vectors")
data class VectorEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val x: Float,
    val y: Float,
    val color: Long = 0xFF6B4EFF
)