package com.example.vectors.ui.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vectors.viewmodel.VectorViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ArchiveScreen() {
    val viewModel: VectorViewModel = hiltViewModel()
    val history by viewModel.history.collectAsState()

    // --- Розрахунок статистики ---
    val totalCount = history.size

    val calendar = Calendar.getInstance()
    val todayDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(calendar.time)
    val todayCount = history.count {
        SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date(it.timestamp)) == todayDate
    }

    val typesCount = history.distinctBy { it.operationName }.size

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .padding(16.dp)
    ) {
        Text(
            text = "Архів",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333)
        )

        Spacer(Modifier.height(16.dp))

        // --- ПАНЕЛЬ СТАТИСТИКИ (як на фото) ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatCard(modifier = Modifier.weight(1f), label = "Всього", value = totalCount.toString(), color = Color.Blue)
            StatCard(modifier = Modifier.weight(1f), label = "Сьогодні", value = todayCount.toString(), color = Color(0xFF2E7D32))
            StatCard(modifier = Modifier.weight(1f), label = "Типів", value = typesCount.toString(), color = Color(0xFF673AB7))
        }

        Spacer(Modifier.height(24.dp))

        // Заголовок та кнопка очищення
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Історія обчислень",
                style = MaterialTheme.typography.titleLarge
            )
            if (history.isNotEmpty()) {
                TextButton(onClick = { viewModel.clearHistory() }) {
                    Text("Очистити", color = Color.Red)
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        // Список історії
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            if (history.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Історія порожня", color = Color.Gray)
                    }
                }
            }

            items(history) { record ->
                val date = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date(record.timestamp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(1.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = record.operationName,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFF57C00)
                            )
                            Text(
                                text = date,
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Gray
                            )
                        }

                        Divider(
                            modifier = Modifier.padding(vertical = 8.dp),
                            thickness = 0.5.dp,
                            color = Color(0xFFEEEEEE)
                        )

                        Text(
                            text = "Дані: ${record.inputData}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF555555)
                        )

                        Spacer(Modifier.height(4.dp))

                        Text(
                            text = "Результат: ${record.resultText}",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2E7D32),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}

/**
 * Окремий компонент для карток статистики
 */
@Composable
fun StatCard(modifier: Modifier, label: String, value: String, color: Color) {
    Card(
        modifier = modifier.height(90.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp).fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, fontSize = 12.sp, color = Color.Gray)
            Text(
                text = value,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}