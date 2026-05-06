@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.vectors.ui.visualization

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vectors.viewmodel.VectorViewModel
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.hilt.navigation.compose.hiltViewModel
@Composable
fun VisualizationScreen(navController: NavHostController) {
    val viewModel: VectorViewModel = hiltViewModel()
    val vectors = viewModel.vectors.value

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Візуалізація векторів") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF6B4EFF),
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(24.dp)
            ) {
                VectorCanvas(vectors = vectors)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Ваші вектори (${vectors.size})",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )

            // Список векторів
            vectors.forEach { vector ->
                Text(
                    text = "${vector.name}: (${vector.x}, ${vector.y}) | Довжина: %.2f".format(vector.magnitude()),
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            Button(
                onClick = { /* Додати вектор для тестування */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Додати тестовий вектор")
            }
        }
    }
}

@Composable
private fun VectorCanvas(vectors: List<com.example.vectors.domain.model.Vector2D>) {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        val centerX = size.width / 2
        val centerY = size.height / 2
        val scale = 40f // масштаб

        // Сітка
        drawGrid(centerX, centerY, scale)

        // Вісь X та Y
        drawLine(Color.Blue, Offset(0f, centerY), Offset(size.width, centerY), strokeWidth = 2f)
        drawLine(Color.Blue, Offset(centerX, 0f), Offset(centerX, size.height), strokeWidth = 2f)

        // Малюємо вектори
        vectors.forEach { vector ->
            val endX = centerX + vector.x * scale
            val endY = centerY - vector.y * scale // Y в Compose йде вниз

            // Векторна лінія
            drawLine(
                color = Color(vector.color),
                start = Offset(centerX, centerY),
                end = Offset(endX, endY),
                strokeWidth = 6f
            )

            // Стрілка на кінці
            drawArrow(Offset(endX, endY), vector, scale)
        }
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawGrid(centerX: Float, centerY: Float, scale: Float) {
    val step = scale
    for (i in -10..10) {
        drawLine(Color.LightGray, Offset(centerX + i * step, 0f), Offset(centerX + i * step, size.height))
        drawLine(Color.LightGray, Offset(0f, centerY + i * step), Offset(size.width, centerY + i * step))
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawArrow(
    end: Offset,
    vector: com.example.vectors.domain.model.Vector2D,
    scale: Float
) {
    // Проста стрілка
    val angle = kotlin.math.atan2(-vector.y.toDouble(), vector.x.toDouble())
    val arrowSize = 12f

    val x1 = end.x - arrowSize * kotlin.math.cos(angle - 0.5)
    val y1 = end.y - arrowSize * kotlin.math.sin(angle - 0.5)
    val x2 = end.x - arrowSize * kotlin.math.cos(angle + 0.5)
    val y2 = end.y - arrowSize * kotlin.math.sin(angle + 0.5)

    drawLine(Color(vector.color), end, Offset(x1.toFloat(), y1.toFloat()), strokeWidth = 5f)
    drawLine(Color(vector.color), end, Offset(x2.toFloat(), y2.toFloat()), strokeWidth = 5f)
}