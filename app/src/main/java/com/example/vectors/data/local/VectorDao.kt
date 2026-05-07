package com.example.vectors.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface VectorDao {
    @Query("SELECT * FROM vectors")
    fun getAllVectors(): Flow<List<VectorEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVector(vector: VectorEntity)

    @Query("DELETE FROM vectors WHERE id = :id")
    suspend fun deleteVector(id: Long)

    @Query("DELETE FROM vectors")
    suspend fun deleteAll()
}