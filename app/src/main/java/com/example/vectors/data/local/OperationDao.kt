package com.example.vectors.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface OperationDao {
    @Query("SELECT * FROM operations_history ORDER BY timestamp DESC")
    fun getHistory(): Flow<List<OperationEntity>>

    @Insert
    suspend fun insertOperation(operation: OperationEntity)

    @Query("DELETE FROM operations_history")
    suspend fun clearHistory()
}