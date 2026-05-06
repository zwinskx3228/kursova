package com.example.vectors.domain.model
import java.util.UUID

data class Vector2D(
    val id: Long = 0,
    val name: String = "Vector",
    val x: Float,
    val y: Float,
    val color: Long = 0xFF6B4EFF
) {
    fun magnitude(): Float = kotlin.math.sqrt(x * x + y * y)

    operator fun plus(other: Vector2D) = copy(
        name = "$name + ${other.name}",
        x = x + other.x,
        y = y + other.y
    )

    operator fun minus(other: Vector2D) = copy(
        name = "$name - ${other.name}",
        x = x - other.x,
        y = y - other.y
    )

    fun scalarMultiply(k: Float) = copy(
        name = "$k × $name",
        x = x * k,
        y = y * k
    )
}