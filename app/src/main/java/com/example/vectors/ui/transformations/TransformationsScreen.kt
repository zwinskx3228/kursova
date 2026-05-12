package com.example.vectors.ui.transformations

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
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vectors.domain.model.Vector2D
import com.example.vectors.viewmodel.VectorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransformationsScreen() {
    val viewModel: VectorViewModel = hiltViewModel()
    val vectors by viewModel.vectors.collectAsState()

    var selectedVector by remember { mutableStateOf<Vector2D?>(null) }
    var selectedType by remember { mutableStateOf("Поворот") }
    var resultText by remember { mutableStateOf("(0.00, 0.00)") }

    // Поля для вводу
    var angleInput by remember { mutableStateOf("45") }
    var scaleXInput by remember { mutableStateOf("1.0") }
    var scaleYInput by remember { mutableStateOf("1.0") }
    var moveXInput by remember { mutableStateOf("1.0") }
    var moveYInput by remember { mutableStateOf("1.0") }

    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).background(Color(0xFFF0F8FF))) {
        BannerSection("Перетворення", "Трансформуйте вектори", Color(0xFF2196F3))

        // 1. Вибір типу (Сітка)
        Card(Modifier.padding(16.dp).fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White)) {
            val types = listOf("Поворот", "Дзеркало X", "Дзеркало Y", "Дзеркало O", "Масштаб", "Переміщення")
            LazyVerticalGrid(columns = GridCells.Fixed(3), modifier = Modifier.height(220.dp).padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(types) { type ->
                    Card(
                        modifier = Modifier.aspectRatio(1.1f).clickable { selectedType = type },
                        colors = CardDefaults.cardColors(containerColor = if (selectedType == type) Color(0xFFE3F2FD) else Color.White),
                        border = BorderStroke(1.dp, if (selectedType == type) Color(0xFF2196F3) else Color(0xFFEEEEEE))
                    ) {
                        Column(Modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Refresh, null, tint = Color(0xFF2196F3))
                            Text(type, style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }
        }

        // 2. Динамічне Меню
        Card(Modifier.padding(horizontal = 16.dp).fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White)) {
            Column(Modifier.padding(16.dp)) {
                Text("Вектор", style = MaterialTheme.typography.labelMedium)
                VectorDropdown(vectors, selectedVector) { selectedVector = it }

                Spacer(Modifier.height(16.dp))

                when (selectedType) {
                    "Поворот" -> {
                        OutlinedTextField(value = angleInput, onValueChange = { angleInput = it },
                            label = { Text("Кут (градуси)") }, modifier = Modifier.fillMaxWidth())
                    }
                    "Масштаб" -> {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(value = scaleXInput, onValueChange = { scaleXInput = it },
                                label = { Text("Масштаб X") }, modifier = Modifier.weight(1f))
                            OutlinedTextField(value = scaleYInput, onValueChange = { scaleYInput = it },
                                label = { Text("Масштаб Y") }, modifier = Modifier.weight(1f))
                        }
                    }
                    "Переміщення" -> {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(value = moveXInput, onValueChange = { moveXInput = it },
                                label = { Text("Крок X") }, modifier = Modifier.weight(1f))
                            OutlinedTextField(value = moveYInput, onValueChange = { moveYInput = it },
                                label = { Text("Крок Y") }, modifier = Modifier.weight(1f))
                        }
                    }
                    else -> { Text("Додаткові параметри не потрібні", style = MaterialTheme.typography.bodySmall) }
                }

                Button(
                    onClick = {
                        selectedVector?.let { v ->
                            var inputDetails = "Вектор: ${v.name}"
                            val finalResult = when (selectedType) {
                                "Поворот" -> {
                                    val angle = angleInput.toDoubleOrNull() ?: 0.0
                                    val rad = Math.toRadians(angle)
                                    val nx = v.x * Math.cos(rad) - v.y * Math.sin(rad)
                                    val ny = v.x * Math.sin(rad) + v.y * Math.cos(rad)
                                    inputDetails += ", кут: $angle°"
                                    "(${String.format("%.2f", nx)}, ${String.format("%.2f", ny)})"
                                }
                                "Дзеркало X" -> "(${v.x}, ${-v.y})"
                                "Дзеркало Y" -> "(${-v.x}, ${v.y})"
                                "Дзеркало O" -> "(${-v.x}, ${-v.y})"
                                "Масштаб" -> {
                                    val sx = scaleXInput.toDoubleOrNull() ?: 1.0
                                    val sy = scaleYInput.toDoubleOrNull() ?: 1.0
                                    inputDetails += ", S($sx, $sy)"
                                    "(${String.format("%.2f", v.x * sx)}, ${String.format("%.2f", v.y * sy)})"
                                }
                                "Переміщення" -> {
                                    val dx = moveXInput.toDoubleOrNull() ?: 0.0
                                    val dy = moveYInput.toDoubleOrNull() ?: 0.0
                                    inputDetails += ", D($dx, $dy)"
                                    "(${String.format("%.2f", v.x + dx)}, ${String.format("%.2f", v.y + dy)})"
                                }
                                else -> ""
                            }

                            resultText = finalResult

                            // ЗБЕРЕЖЕННЯ В АРХІВ
                            viewModel.saveOperationToHistory(
                                opName = selectedType,
                                input = inputDetails,
                                result = finalResult
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth().padding(top = 24.dp).height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
                    shape = RoundedCornerShape(16.dp),
                    enabled = selectedVector != null
                ) {
                    Text("Застосувати")
                }
            }
        }

        ResultCard(resultText, Color(0xFF1976D2))
    }
}

// Допоміжна функція банера
@Composable
fun BannerSection(title: String, subtitle: String, color: Color) {
    Card(
        shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        Column(Modifier.padding(24.dp)) {
            Icon(Icons.Default.Star, null, tint = Color.Yellow, modifier = Modifier.size(28.dp))
            Spacer(Modifier.height(8.dp))
            Text(title, color = Color.White, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(subtitle, color = Color.White.copy(0.8f), style = MaterialTheme.typography.bodyMedium)
        }
    }
}

// ВСТАВЛЯЙ ЦЕ ПОЗА МЕЖАМИ TransformationsScreen {}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VectorDropdown(
    vectors: List<Vector2D>,
    selected: Vector2D?,
    label: String = "Виберіть вектор",
    onSelect: (Vector2D) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selected?.name ?: label,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier.menuAnchor().fillMaxWidth(),
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF2196F3)
            )
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            vectors.forEach { vector ->
                DropdownMenuItem(
                    text = { Text("${vector.name} (${vector.x}, ${vector.y})") },
                    onClick = {
                        onSelect(vector) // Передаємо вибраний вектор
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun ResultCard(text: String, color: Color) {
    Card(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier.padding(24.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(Icons.Default.Shuffle, contentDescription = null, tint = color)
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