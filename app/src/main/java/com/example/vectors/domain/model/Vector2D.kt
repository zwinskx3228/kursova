package com.example.vectors.domain.model

import kotlin.math.*

data class Vector2D(
    val id: Long = 0,
    val name: String = "Вектор",
    val x: Float,
    val y: Float,
    val color: Long = 0xFF6B4EFF

) {
    fun magnitude(): Float = sqrt(x * x + y * y)

    operator fun plus(other: Vector2D) = copy(name = "$name + ${other.name}", x = x + other.x, y = y + other.y)
    operator fun minus(other: Vector2D) = copy(name = "$name - ${other.name}", x = x - other.x, y = y - other.y)
    operator fun times(scalar: Float) = Vector2D(name = "$name * $scalar", x = x * scalar, y = y * scalar)
    operator fun div(scalar: Float) = if (scalar != 0f) Vector2D(name = "$name / $scalar", x = x / scalar, y = y / scalar) else this
    fun dot(other: Vector2D): Float = x * other.x + y * other.y


    fun angleWith(other: Vector2D): Double {
        val mag1 = magnitude()
        val mag2 = other.magnitude()
        if (mag1 == 0f || mag2 == 0f) return 0.0
        val cosTheta = dot(other) / (mag1 * mag2)
        return Math.toDegrees(acos(cosTheta.coerceIn(-1f, 1f).toDouble()))
    }
    fun determinant(other: Vector2D): Float = x * other.y - y * other.x

    fun normalize(): Vector2D {
        val m = magnitude()
        return if (m > 0f) copy(name = "Norm($name)", x = x / m, y = y / m) else this
    }

    fun rotate(angleDeg: Double): Vector2D {
        val rad = Math.toRadians(angleDeg)
        val cosA = cos(rad).toFloat()
        val sinA = sin(rad).toFloat()
        return copy(name = "Rotated $name", x = x * cosA - y * sinA, y = x * sinA + y * cosA)
    }
}