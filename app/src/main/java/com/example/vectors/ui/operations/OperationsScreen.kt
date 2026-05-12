package com.example.vectors.ui.operations

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
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
import androidx.navigation.NavHostController
import com.example.vectors.domain.model.Vector2D
import com.example.vectors.viewmodel.VectorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OperationsScreen(navController: NavHostController) {
    val viewModel: VectorViewModel = hiltViewModel()
    val vectors by viewModel.vectors.collectAsState()

    var v1 by remember { mutableStateOf<Vector2D?>(null) }
    var v2 by remember { mutableStateOf<Vector2D?>(null) }
    var resultText by remember { mutableStateOf("Оберіть вектори та дію") }
    var selectedOp by remember { mutableStateOf("") }

    val orangeAccent = Color(0xFFFF9800)
    val lightOrangeBg = Color(0xFFFFF3E0)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color(0xFFFFFBF0))
    ) {
        BannerSection(
            title = "Операції",
            subtitle = "Базові дії з векторами",
            color = Color(0xFFF57C00)
        )

        // 2. Вибір векторів
        Card(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("Вибір даних", style = MaterialTheme.typography.labelLarge, color = orangeAccent)
                Spacer(Modifier.height(12.dp))

                VectorDropdown(vectors, v1, "Перший вектор", orangeAccent) { v1 = it }
                Spacer(Modifier.height(8.dp))
                VectorDropdown(vectors, v2, "Другий вектор", orangeAccent) { v2 = it }
            }
        }

        // 3. Сітка операцій
        Card(
            modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            val ops = listOf("Додати", "Відняти", "Скалярний добуток", "Кут", "Довжина", "Очистити")
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.height(240.dp).padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(ops) { op ->
                    val isSelected = selectedOp == op
                    Card(
                        modifier = Modifier
                            .aspectRatio(1.1f)
                            .clickable {
                                selectedOp = op
                                // ТУТ ТІЛЬКИ ПІДГОТОВКА (БЕЗ ЗБЕРЕЖЕННЯ В БД)
                                if (op == "Очистити") {
                                    v1 = null; v2 = null; resultText = "Оберіть вектори"; selectedOp = ""
                                }
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) lightOrangeBg else Color.White
                        ),
                        border = BorderStroke(1.dp, if (isSelected) orangeAccent else Color(0xFFEEEEEE))
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = when(op) {
                                    "Додати" -> Icons.Default.Add
                                    "Відняти" -> Icons.Default.Remove
                                    "Скалярний добуток" -> Icons.Default.Grain
                                    "Кут" -> Icons.Default.Architecture
                                    "Довжина" -> Icons.Default.Straighten
                                    else -> Icons.Default.DeleteSweep
                                },
                                contentDescription = null,
                                tint = if (isSelected) orangeAccent else Color.Gray
                            )
                            Text(op, style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }
        }

        // 4. Кнопка "Виконати" (КЛЮЧОВИЙ МОМЕНТ ДЛЯ АРХІВУ)
        Button(
            onClick = {
                if (v1 != null && selectedOp.isNotEmpty()) {
                    val res = when(selectedOp) {
                        "Додати" -> if(v2!=null) "(${v1!!.x + v2!!.x}, ${v1!!.y + v2!!.y})" else null
                        "Відняти" -> if(v2!=null) "(${v1!!.x - v2!!.x}, ${v1!!.y - v2!!.y})" else null
                        "Скалярний добуток" -> if(v2!=null) "${v1!!.x * v2!!.x + v1!!.y * v2!!.y}" else null
                        "Кут" -> if(v2!=null) {
                            val dot = v1!!.x * v2!!.x + v1!!.y * v2!!.y
                            val mag1 = Math.sqrt(v1!!.x.toDouble()*v1!!.x + v1!!.y*v1!!.y)
                            val mag2 = Math.sqrt(v2!!.x.toDouble()*v2!!.x + v2!!.y*v2!!.y)
                            val angle = Math.toDegrees(Math.acos(dot / (mag1 * mag2)))
                            "${String.format("%.2f", angle)}°"
                        } else null
                        "Довжина" -> String.format("%.2f", Math.sqrt(v1!!.x.toDouble()*v1!!.x + v1!!.y*v1!!.y))
                        else -> null
                    }

                    if (res != null) {
                        resultText = res
                        // ЗАПИСУЄМО В АРХІВ ТІЛЬКИ ПРИ НАТИСКАННІ
                        viewModel.saveOperationToHistory(
                            opName = selectedOp,
                            input = "v1: ${v1?.name ?: "-"}, v2: ${v2?.name ?: "-"}",
                            result = res
                        )
                    }
                }
            },
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = orangeAccent),
            shape = RoundedCornerShape(12.dp),
            enabled = selectedOp.isNotEmpty() && selectedOp != "Очистити"
        ) {
            Icon(Icons.Default.FlashOn, null)
            Spacer(Modifier.width(8.dp))
            Text("Виконати")
        }

        // 5. Картка результату
        ResultCard(resultText, orangeAccent, selectedOp)

        Spacer(Modifier.height(24.dp))
    }
}

// --- УНІФІКОВАНІ КОМПОНЕНТИ ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VectorDropdown(vectors: List<Vector2D>, selected: Vector2D?, label: String, accent: Color, onSelect: (Vector2D) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selected?.let { "${it.name} (${it.x}, ${it.y})" } ?: label,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier.menuAnchor().fillMaxWidth(),
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color(0xFFFCFCFC),
                focusedBorderColor = accent
            )
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

@Composable
fun ResultCard(text: String, color: Color, opName: String) {
    Card(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.08f)),
        border = BorderStroke(1.dp, color.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier.padding(24.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Calculate,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(40.dp)
            )
            Text("Результат обчислення", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
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
            Text(subtitle, color = Color.White.copy(0.9f), style = MaterialTheme.typography.bodyMedium)
        }
    }
}