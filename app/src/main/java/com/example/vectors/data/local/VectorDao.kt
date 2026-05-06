package com.example.vectors.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface VectorDao {
    @Query("SELECT * FROM vectors")
    fun getAllVectors(): Flow<List<VectorEntity>>

    @Insert
    suspend fun insertVector(vector: VectorEntity)

    @Query("DELETE FROM vectors WHERE id = :id")
    suspend fun deleteVector(id: Long)
}