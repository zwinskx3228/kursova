package com.example.vectors.data.repository

import com.example.vectors.data.local.VectorDao
import com.example.vectors.data.local.VectorEntity
import com.example.vectors.domain.model.Vector2D
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class VectorRepository @Inject constructor(private val dao: VectorDao) {
    fun getAllVectors() = dao.getAllVectors().map { list ->
        list.map { Vector2D(it.id, it.name, it.x, it.y, it.color) }
    }

    suspend fun insertVector(v: Vector2D) {
        dao.insertVector(
            VectorEntity(
                id = 0L, // Передаємо 0, щоб Room сам згенерував ID
                name = v.name,
                x = v.x,
                y = v.y,
                color = v.color
            )
        )
    }

    suspend fun deleteVector(id: Long) = dao.deleteVector(id)
    suspend fun clearAll() = dao.deleteAll()
}