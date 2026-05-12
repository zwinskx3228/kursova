package com.example.vectors.data.repository

import com.example.vectors.data.local.OperationDao
import com.example.vectors.data.local.OperationEntity
import com.example.vectors.data.local.VectorDao
import com.example.vectors.data.local.VectorEntity
import com.example.vectors.domain.model.Vector2D
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class VectorRepository @Inject constructor(
    private val vectorDao: VectorDao,
    private val operationDao: OperationDao
) {
    private val gson = Gson()

    // --- ВЕКТОРИ ---
    fun getAllVectors() = vectorDao.getAllVectors().map { list ->
        list.map { Vector2D(it.id, it.name, it.x, it.y, it.color) }
    }

    suspend fun insertVector(v: Vector2D) {
        vectorDao.insertVector(VectorEntity(0L, v.name, v.x, v.y, v.color))
    }

    suspend fun deleteVector(id: Long) = vectorDao.deleteVector(id)

    suspend fun clearAll() = vectorDao.deleteAll()

    // --- ІСТОРІЯ ---
    fun getHistory() = operationDao.getHistory()

    suspend fun saveOperation(name: String, input: String, result: String) {
        operationDao.insertOperation(OperationEntity(0L, name, input, result))
    }

    suspend fun clearHistory() = operationDao.clearHistory()

    // --- JSON ЕКСПОРТ/ІМПОРТ (ВИПРАВЛЕНО) ---
    fun exportVectorsToJson(vectors: List<Vector2D>): String {
        return gson.toJson(vectors)
    }

    suspend fun importVectorsFromJson(jsonString: String) {
        try {
            val listType = object : TypeToken<List<Vector2D>>() {}.type
            val imported: List<Vector2D> = gson.fromJson(jsonString, listType)

            // Використовуємо твій існуючий метод для вставки кожного вектора
            imported.forEach { vector ->
                insertVector(vector)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}