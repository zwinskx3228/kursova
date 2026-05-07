package com.example.vectors.domain.usecase

import com.example.vectors.domain.model.Vector2D
import javax.inject.Inject

class VectorOperations @Inject constructor() {
    fun add(v1: Vector2D, v2: Vector2D) = v1.copy(name = "Сума", x = v1.x + v2.x, y = v1.y + v2.y)
    fun subtract(v1: Vector2D, v2: Vector2D) = v1.copy(name = "Різниця", x = v1.x - v2.x, y = v1.y - v2.y)
    fun dotProduct(v1: Vector2D, v2: Vector2D): Float = v1.x * v2.x + v1.y * v2.y

    fun rotate(v: Vector2D, angle: Double): Vector2D {
        val rad = Math.toRadians(angle)
        val newX = (v.x * Math.cos(rad) - v.y * Math.sin(rad)).toFloat()
        val newY = (v.x * Math.sin(rad) + v.y * Math.cos(rad)).toFloat()
        return v.copy(name = "Повернутий", x = newX, y = newY)
    }
}