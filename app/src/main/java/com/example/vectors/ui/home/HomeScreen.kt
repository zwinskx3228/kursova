package com.example.vectors.ui.screens

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vectors.domain.model.Vector2D
import com.example.vectors.viewmodel.VectorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val viewModel: VectorViewModel = hiltViewModel()
    val vectors by viewModel.vectors.collectAsState()
    val context = LocalContext.current
    val contentResolver = context.contentResolver

    // Поля для створення нового вектора
    var name by remember { mutableStateOf("") }
    var xStr by remember { mutableStateOf("") }
    var yStr by remember { mutableStateOf("") }
    var showForm by remember { mutableStateOf(false) }

    // Стан для редагування
    var vectorToEdit by remember { mutableStateOf<Vector2D?>(null) }
    var editName by remember { mutableStateOf("") }
    var editX by remember { mutableStateOf("") }
    var editY by remember { mutableStateOf("") }

    // Помилки та діалоги
    var inputError by remember { mutableStateOf(false) }
    var editError by remember { mutableStateOf(false) }
    var showClearAllDialog by remember { mutableStateOf(false) }
    var vectorToDelete by remember { mutableStateOf<Vector2D?>(null) }

    // Лаунчери для файлів
    val createFileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("text/plain")
    ) { uri -> uri?.let { viewModel.exportToFile(it, contentResolver) } }

    val openFileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri -> uri?.let { viewModel.importFromFile(it, contentResolver) } }

    // --- ДІАЛОГ РЕДАГУВАННЯ ---
    vectorToEdit?.let { vector ->
        AlertDialog(
            onDismissRequest = { vectorToEdit = null },
            title = { Text("Редагувати вектор") },
            text = {
                Column {
                    OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it; editError = false },
                        label = { Text("Назва") },
                        isError = editError && editName.isBlank()
                    )
                    Spacer(Modifier.height(8.dp))
                    Row {
                        OutlinedTextField(
                            value = editX,
                            onValueChange = { editX = it; editError = false },
                            label = { Text("X") },
                            modifier = Modifier.weight(1f),
                            isError = editError && editX.toFloatOrNull() == null
                        )
                        Spacer(Modifier.width(8.dp))
                        OutlinedTextField(
                            value = editY,
                            onValueChange = { editY = it; editError = false },
                            label = { Text("Y") },
                            modifier = Modifier.weight(1f),
                            isError = editError && editY.toFloatOrNull() == null
                        )
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    val vx = editX.toFloatOrNull()
                    val vy = editY.toFloatOrNull()
                    if (editName.isBlank() || vx == null || vy == null) {
                        editError = true
                    } else {
                        viewModel.addVector(vector.copy(name = editName, x = vx, y = vy))
                        vectorToEdit = null
                        Toast.makeText(context, "Оновлено", Toast.LENGTH_SHORT).show()
                    }
                }) { Text("Зберегти") }
            },
            dismissButton = {
                TextButton(onClick = { vectorToEdit = null }) { Text("Скасувати") }
            }
        )
    }

    // --- ДІАЛОГ ОЧИЩЕННЯ ВСІЄЇ БАЗИ ---
    if (showClearAllDialog) {
        AlertDialog(
            onDismissRequest = { showClearAllDialog = false },
            title = { Text("Очистити базу?") },
            text = { Text("Ви впевнені, що хочете видалити всі вектори?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.clearAll()
                    showClearAllDialog = false
                    Toast.makeText(context, "Базу очищено", Toast.LENGTH_SHORT).show()
                }) { Text("Видалити всі", color = Color.Red) }
            },
            dismissButton = {
                TextButton(onClick = { showClearAllDialog = false }) { Text("Скасувати") }
            }
        )
    }

    // --- ДІАЛОГ ВИДАЛЕННЯ ОДНОГО ВЕКТОРА ---
    vectorToDelete?.let { vector ->
        AlertDialog(
            onDismissRequest = { vectorToDelete = null },
            title = { Text("Видалення") },
            text = { Text("Видалити «${vector.name}»?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteVector(vector.id)
                    vectorToDelete = null
                }) { Text("Видалити", color = Color.Red) }
            },
            dismissButton = {
                TextButton(onClick = { vectorToDelete = null }) { Text("Скасувати") }
            }
        )
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Vector Lab 2D", fontSize = 32.sp, fontWeight = FontWeight.Black, color = Color(0xFF6B4EFF))

        // Статистика та кнопки
        Card(
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF6B4EFF).copy(alpha = 0.08f)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("📊", fontSize = 24.sp)
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text("База даних", fontWeight = FontWeight.Bold)
                        Text("Векторів: ${vectors.size}", fontSize = 14.sp, color = Color.Gray)
                    }
                }
                Spacer(Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = { createFileLauncher.launch("vectors_backup.txt") }, modifier = Modifier.weight(1f)) {
                        Icon(Icons.Default.Save, null, Modifier.size(18.dp)); Text("Зберегти", fontSize = 12.sp)
                    }
                    OutlinedButton(onClick = { openFileLauncher.launch(arrayOf("text/plain")) }, modifier = Modifier.weight(1f)) {
                        Icon(Icons.Default.FileUpload, null, Modifier.size(18.dp)); Text("Відкрити", fontSize = 12.sp)
                    }
                }
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(onClick = { showForm = !showForm }) {
                Text(if (showForm) "Закрити" else "Додати вектор")
            }
            TextButton(onClick = { showClearAllDialog = true }) {
                Icon(Icons.Default.Delete, null, tint = Color.Red)
                Text("Очистити", color = Color.Red)
            }
        }

        // Форма створення
        AnimatedVisibility(visible = showForm) {
            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = name, onValueChange = { name = it; inputError = false },
                        label = { Text("Назва") }, modifier = Modifier.fillMaxWidth(),
                        isError = inputError && name.isBlank()
                    )
                    Row(Modifier.padding(top = 8.dp)) {
                        OutlinedTextField(value = xStr, onValueChange = { xStr = it; inputError = false }, label = { Text("X") }, modifier = Modifier.weight(1f), isError = inputError && xStr.toFloatOrNull() == null)
                        Spacer(Modifier.width(8.dp))
                        OutlinedTextField(value = yStr, onValueChange = { yStr = it; inputError = false }, label = { Text("Y") }, modifier = Modifier.weight(1f), isError = inputError && yStr.toFloatOrNull() == null)
                    }
                    Button(onClick = {
                        val vx = xStr.toFloatOrNull()
                        val vy = yStr.toFloatOrNull()
                        if (name.isBlank() || vx == null || vy == null) { inputError = true } else {
                            viewModel.addVector(Vector2D(name = name, x = vx, y = vy))
                            name = ""; xStr = ""; yStr = ""; showForm = false
                        }
                    }, modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) { Text("Зберегти") }
                }
            }
        }

        // Список
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(vectors) { vector ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable {
                            // Відкриваємо редагування при натисканні на картку
                            vectorToEdit = vector
                            editName = vector.name
                            editX = vector.x.toString()
                            editY = vector.y.toString()
                        }
                ) {
                    ListItem(
                        headlineContent = { Text(vector.name, fontWeight = FontWeight.Bold) },
                        supportingContent = { Text("X: ${vector.x}, Y: ${vector.y}") },
                        trailingContent = {
                            IconButton(onClick = { vectorToDelete = vector }) {
                                Icon(Icons.Default.Delete, null, tint = Color.LightGray)
                            }
                        }
                    )
                }
            }
        }
    }
}