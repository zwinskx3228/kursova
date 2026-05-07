package com.example.vectors.ui.extensions

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vectors.domain.model.Vector2D
import com.example.vectors.viewmodel.VectorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExtensionsScreen() {
    val viewModel: VectorViewModel = hiltViewModel()
    val vectors by viewModel.vectors.collectAsState()

    var selectedOp by remember { mutableStateOf("Інтерполяція") }
    var v1 by remember { mutableStateOf<Vector2D?>(null) }
    var v2 by remember { mutableStateOf<Vector2D?>(null) }

    // Стан полів вводу згідно з твоїми скріншотами
    var tParam by remember { mutableStateOf("0.5") }
    var maxLength by remember { mutableStateOf("0.5") }
    var radiusR by remember { mutableStateOf("0.5") }
    var angleDeg by remember { mutableStateOf("0") }

    var resultText by remember { mutableStateOf("(0.00, 0.00)") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color(0xFFFDF7FF))
    ) {
        // 1. Банер
        BannerSection("Операції", "Математичний аналіз", Color(0xFFAB47BC))

        // 2. Сітка операцій (як на скріншотах)
        Card(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            val ops = listOf("Інтерполяція", "З полярних", "В полярні", "Обмеження", "Відхилення", "Площа", "З кута")
            Text(
                "Операція",
                modifier = Modifier.padding(start = 16.dp, top = 12.dp),
                style = MaterialTheme.typography.labelLarge
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.height(260.dp).padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(ops) { op ->
                    val isSelected = selectedOp == op
                    Card(
                        modifier = Modifier
                            .aspectRatio(1.1f)
                            .clickable { selectedOp = op },
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) Color(0xFFFCE4EC) else Color.White
                        ),
                        border = BorderStroke(1.dp, if (isSelected) Color(0xFFD81B60) else Color(0xFFEEEEEE))
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = when(op) {
                                    "Інтерполяція" -> Icons.Default.Shuffle
                                    "З полярних" -> Icons.Default.AdsClick
                                    "В полярні" -> Icons.Default.LocationOn
                                    "Обмеження" -> Icons.Default.ContentCut
                                    "Відхилення" -> Icons.Default.NorthEast
                                    "Площа" -> Icons.Default.ChangeHistory
                                    else -> Icons.Default.SquareFoot
                                },
                                contentDescription = null,
                                tint = if (isSelected) Color(0xFFD81B60) else Color.Gray
                            )
                            Text(op, style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }
        }

        // 3. Динамічна панель налаштувань
        Card(
            modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(Modifier.padding(16.dp)) {
                when (selectedOp) {
                    "Інтерполяція" -> {
                        VectorDropdown(vectors, v1, "Перший вектор") { v1 = it }
                        Spacer(Modifier.height(8.dp))
                        VectorDropdown(vectors, v2, "Другий вектор") { v2 = it }
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(
                            value = tParam,
                            onValueChange = { tParam = it },
                            label = { Text("Параметр t (0-1)") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    "З полярних" -> {
                        OutlinedTextField(value = radiusR, onValueChange = { radiusR = it }, label = { Text("Довжина (r)") }, modifier = Modifier.fillMaxWidth())
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(value = angleDeg, onValueChange = { angleDeg = it }, label = { Text("Кут (градуси)") }, modifier = Modifier.fillMaxWidth())
                    }
                    "В полярні" -> {
                        VectorDropdown(vectors, v1, "Перший вектор") { v1 = it }
                    }
                    "Обмеження" -> {
                        VectorDropdown(vectors, v1, "Перший вектор") { v1 = it }
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(value = maxLength, onValueChange = { maxLength = it }, label = { Text("Макс. довжина") }, modifier = Modifier.fillMaxWidth())
                    }
                    "Відхилення", "Площа" -> {
                        VectorDropdown(vectors, v1, "Перший вектор") { v1 = it }
                        Spacer(Modifier.height(8.dp))
                        VectorDropdown(vectors, v2, "Другий вектор") { v2 = it }
                    }
                    "З кута" -> {
                        OutlinedTextField(value = angleDeg, onValueChange = { angleDeg = it }, label = { Text("Кут (градуси)") }, modifier = Modifier.fillMaxWidth())
                    }
                }

                // Кнопка Виконати (як на скріншоті)
                Button(
                    onClick = {
                        val t = tParam.replace(",", ".").toDoubleOrNull() ?: 0.5
                        val r = radiusR.replace(",", ".").toDoubleOrNull() ?: 0.0
                        val angle = angleDeg.replace(",", ".").toDoubleOrNull() ?: 0.0
                        val maxL = maxLength.replace(",", ".").toDoubleOrNull() ?: 0.0

                        when (selectedOp) {
                            "Інтерполяція" -> {
                                if (v1 != null && v2 != null) {
                                    val rx = v1!!.x + (v2!!.x - v1!!.x) * t.toFloat()
                                    val ry = v1!!.y + (v2!!.y - v1!!.y) * t.toFloat()
                                    resultText = "(${String.format("%.2f", rx)}, ${String.format("%.2f", ry)})"
                                }
                            }
                            "З полярних" -> {
                                val rad = Math.toRadians(angle)
                                val rx = r * Math.cos(rad)
                                val ry = r * Math.sin(rad)
                                resultText = "(${String.format("%.2f", rx)}, ${String.format("%.2f", ry)})"
                            }
                            "В полярні" -> {
                                v1?.let {
                                    val mag = Math.sqrt(it.x.toDouble() * it.x + it.y * it.y)
                                    val deg = Math.toDegrees(Math.atan2(it.y.toDouble(), it.x.toDouble()))
                                    resultText = "r=${String.format("%.2f", mag)}, θ=${String.format("%.2f", deg)}°"
                                }
                            }
                            "Обмеження" -> {
                                v1?.let {
                                    val mag = Math.sqrt(it.x.toDouble() * it.x + it.y * it.y)
                                    if (mag > maxL && mag > 0) {
                                        val ratio = maxL / mag
                                        resultText = "(${String.format("%.2f", it.x * ratio)}, ${String.format("%.2f", it.y * ratio)})"
                                    } else {
                                        resultText = "(${it.x}, ${it.y})"
                                    }
                                }
                            }
                            "Площа" -> {
                                if (v1 != null && v2 != null) {
                                    val area = Math.abs(v1!!.x * v2!!.y - v1!!.y * v2!!.x)
                                    resultText = "${String.format("%.2f", area)} кв.од."
                                }
                            }
                            "З кута" -> {
                                val rad = Math.toRadians(angle)
                                resultText = "(${String.format("%.2f", Math.cos(rad))}, ${String.format("%.2f", Math.sin(rad))})"
                            }
                            "Відхилення" -> {
                                if (v1 != null && v2 != null) {
                                    val dot = v1!!.x * v2!!.x + v1!!.y * v2!!.y
                                    val mag1 = Math.sqrt(v1!!.x.toDouble() * v1!!.x + v1!!.y * v1!!.y)
                                    val mag2 = Math.sqrt(v2!!.x.toDouble() * v2!!.x + v2!!.y * v2!!.y)
                                    val angleRes = Math.toDegrees(Math.acos(dot / (mag1 * mag2)))
                                    resultText = "${String.format("%.2f", angleRes)}°"
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().padding(top = 24.dp).height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD81B60)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.ElectricBolt, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Виконати", fontWeight = FontWeight.Bold)
                }
            }
        }

        // 4. Картка результату (як на скріншоті)
        ResultCard(resultText, Color(0xFFD81B60), selectedOp)
    }
}

// --- ДОПОМІЖНІ КОМПОНЕНТИ ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VectorDropdown(vectors: List<Vector2D>, selected: Vector2D?, label: String, onSelect: (Vector2D) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Column {
        Text(label, style = MaterialTheme.typography.labelMedium, modifier = Modifier.padding(bottom = 4.dp))
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
            OutlinedTextField(
                value = selected?.let { "${it.name} (${it.x}, ${it.y})" } ?: "Виберіть...",
                onValueChange = {},
                readOnly = true,
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(containerColor = Color(0xFFF5F5F5))
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                vectors.forEach { v ->
                    DropdownMenuItem(
                        text = { Text("${v.name} (${v.x}, ${v.y})") },
                        onClick = { onSelect(v); expanded = false }
                    )
                }
            }
        }
    }
}

@Composable
fun ResultCard(text: String, color: Color, opName: String) {
    Card(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.05f)),
        border = BorderStroke(1.dp, color.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier.padding(24.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = if (opName == "Інтерполяція") Icons.Default.Shuffle else Icons.Default.CheckCircle,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(40.dp)
            )
            Text("Результат", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
            Text(
                text = text,
                style = MaterialTheme.typography.headlineLarge,
                color = color,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun BannerSection(title: String, subtitle: String, color: Color) {
    Card(
        shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        Column(Modifier.padding(24.dp)) {
            Icon(Icons.Default.Star, null, tint = Color.Yellow)
            Text(title, color = Color.White, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
            Text(subtitle, color = Color.White.copy(0.8f), style = MaterialTheme.typography.bodyMedium)
        }
    }
}