@file:OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalLayoutApi::class
)

package com.example.vectors.ui.operations

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vectors.domain.model.Vector2D
import com.example.vectors.viewmodel.VectorViewModel

@Composable
fun OperationsScreen(navController: NavHostController) {
    val viewModel: VectorViewModel = hiltViewModel()

    // 🔹 Отримуємо стан результату з Flow
    val result by viewModel.resultVector.collectAsState()

    var selectedOperation by remember { mutableStateOf("add") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Операції з векторами") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF6B4EFF),
                    titleContentColor = Color.White
                ),
                // Додамо кнопку назад, оскільки ми передали navController
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Text("←", color = Color.White, fontSize = 24.sp)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // 🔹 Вибір операції
            Text(
                text = "Оберіть дію:",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            OperationSelector(selectedOperation) {
                selectedOperation = it
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Вхідні дані",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = "Для тесту: Вектор А(5, 3) та Вектор В(2, 4)",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 🔹 КНОПКА ОБЧИСЛЕННЯ
            Button(
                onClick = {
                    // Створюємо два тестові вектори для перевірки
                    val v1 = Vector2D(name = "A", x = 5f, y = 3f)
                    val v2 = Vector2D(name = "B", x = 2f, y = 4f)

                    // Викликаємо метод додавання (можна розширити під інші операції)
                    viewModel.add(v1, v2)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6B4EFF)
                )
            ) {
                Text("Обчислити результат", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 🔹 РЕЗУЛЬТАТ
            // Завдяки collectAsState, 'result' тут — це просто Vector2D?
            if (result != null) {
                Text(
                    text = "Результат обчислення",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF0E6FF)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = result?.name ?: "Результат",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF6B4EFF)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Координати: x = ${result?.x}, y = ${result?.y}",
                            fontSize = 18.sp
                        )
                        Text(
                            text = "Довжина: ${String.format("%.2f", result?.magnitude() ?: 0f)}",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun OperationSelector(
    selected: String,
    onSelect: (String) -> Unit
) {
    val operations = listOf(
        "add" to "Додавання (+)",
        "subtract" to "Віднімання (-)",
        "scalar" to "Скалярне множення",
        "dot" to "Скалярний добуток",
        "angle" to "Кут між векторами"
    )

    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        operations.forEach { (key, label) ->
            FilterChip(
                selected = selected == key,
                onClick = { onSelect(key) },
                label = { Text(label) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFF6B4EFF),
                    selectedLabelColor = Color.White
                )
            )
        }
    }
}