package com.example.vectors.ui.visualization


import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.vectors.viewmodel.VectorViewModel
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import android.graphics.Paint
import com.example.vectors.ui.extensions.BannerSection
import android.graphics.Color as AndroidColor

@Composable
fun VisualizationScreen() {
    val viewModel: VectorViewModel = hiltViewModel()
    val allVectors by viewModel.vectors.collectAsState()
    var scale by remember { mutableStateOf(60f) }

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F9FF))) {
        Column {
            BannerSection("Візуалізація", "Вектори у просторі", Color(0xFF00BFA5))

            Canvas(modifier = Modifier.fillMaxSize().weight(1f)) {
                val center = Offset(size.width / 2, size.height / 2)

                // Малюємо сітку та осі завжди (без перемикачів)
                val step = scale
                for (i in -10..10) {
                    drawLine(Color.LightGray.copy(0.4f), Offset(center.x + i * step, 0f), Offset(center.x + i * step, size.height), 1f)
                    drawLine(Color.LightGray.copy(0.4f), Offset(0f, center.y + i * step), Offset(size.width, center.y + i * step), 1f)
                }
                drawLine(Color.Black, Offset(0f, center.y), Offset(size.width, center.y), 2f)
                drawLine(Color.Black, Offset(center.x, 0f), Offset(center.x, size.height), 2f)

                // Малюємо вектори зі стрілками
                allVectors.forEach { v ->
                    val color = Color(v.color)
                    val target = Offset(center.x + v.x * scale, center.y - v.y * scale)
                    drawLine(color, center, target, strokeWidth = 8f)

                    // Стрілка
                    val angle = Math.atan2((target.y - center.y).toDouble(), (target.x - center.x).toDouble())
                    val arrowSize = 20f
                    drawLine(color, target, Offset((target.x - arrowSize * Math.cos(angle - Math.PI / 6)).toFloat(), (target.y - arrowSize * Math.sin(angle - Math.PI / 6)).toFloat()), 8f)
                    drawLine(color, target, Offset((target.x - arrowSize * Math.cos(angle + Math.PI / 6)).toFloat(), (target.y - arrowSize * Math.sin(angle + Math.PI / 6)).toFloat()), 8f)
                }
            }
        }

        // Кнопки зуму знизу
        Row(
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 32.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ZoomButton(Icons.Default.ZoomIn, Color(0xFF00C853)) { scale += 10f }
            ZoomButton(Icons.Default.ZoomOut, Color(0xFFFF3D00)) { scale -= 10f }
        }
    }
}

@Composable
fun ZoomButton(icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color, onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = color,
        contentColor = if (color == Color.White) Color.Black else Color.White,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.size(56.dp)
    ) {
        Icon(icon, contentDescription = null)
    }
}