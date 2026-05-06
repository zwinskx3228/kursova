package com.example.vectors.domain.usecase

import com.example.vectors.domain.model.Vector2D
import javax.inject.Inject
import kotlin.math.*

class VectorOperations @Inject constructor() {

    // Використовуємо твої методи з моделі
    fun add(v1: Vector2D, v2: Vector2D) = v1 + v2
    fun subtract(v1: Vector2D, v2: Vector2D) = v1 - v2
    fun multiply(v: Vector2D, k: Float) = v.scalarMultiply(k)

    // Додаткові математичні методи
    fun dotProduct(v1: Vector2D, v2: Vector2D): Float = v1.x * v2.x + v1.y * v2.y

    fun angleBetween(v1: Vector2D, v2: Vector2D): Double {
        val dot = dotProduct(v1, v2)
        val mags = v1.magnitude() * v2.magnitude()
        if (mags == 0f) return 0.0
        return acos(dot / mags).toDouble() * (180 / PI)
    }

    fun rotate(v: Vector2D, angleDeg: Double): Vector2D {
        val rad = Math.toRadians(angleDeg)
        val cosA = cos(rad).toFloat()
        val sinA = sin(rad).toFloat()
        return v.copy(
            name = "${v.name} rot",
            x = v.x * cosA - v.y * sinA,
            y = v.x * sinA + v.y * cosA
        )
    }
}