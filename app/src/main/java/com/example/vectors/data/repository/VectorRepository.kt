package com.example.vectors.data.repository

import com.example.vectors.data.local.VectorDao
import com.example.vectors.data.local.VectorEntity
import com.example.vectors.domain.model.Vector2D
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class VectorRepository @Inject constructor(
    private val vectorDao: VectorDao
) {

    fun getAllVectors(): Flow<List<Vector2D>> {
        return vectorDao.getAllVectors().map { list ->
            list.map { it.toDomain() }
        }
    }

    suspend fun insertVector(vector: Vector2D) {
        vectorDao.insertVector(vector.toEntity())
    }

    suspend fun deleteVector(id: Long) {
        vectorDao.deleteVector(id)
    }

    private fun VectorEntity.toDomain() = Vector2D(
        id = id,
        name = name,
        x = x,
        y = y,
        color = color
    )

    private fun Vector2D.toEntity() = VectorEntity(
        id = id,
        name = name,
        x = x,
        y = y,
        color = color
    )
}