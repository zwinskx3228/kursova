package com.example.vectors.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vectors.domain.model.Vector2D
import com.example.vectors.viewmodel.VectorViewModel

@Composable
fun HomeScreen() {
    val viewModel: VectorViewModel = hiltViewModel()
    val vectors by viewModel.vectors.collectAsState()

    var name by remember { mutableStateOf("") }
    var xStr by remember { mutableStateOf("") }
    var yStr by remember { mutableStateOf("") }
    var showForm by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Vector Lab 2D", fontSize = 32.sp, fontWeight = FontWeight.Black, color = Color(0xFF6B4EFF))

        Card(
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF6B4EFF).copy(alpha = 0.08f)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("📊", fontSize = 24.sp)
                Spacer(Modifier.width(12.dp))
                Column {
                    Text("База даних", fontWeight = FontWeight.Bold)
                    Text("Векторів у списку: ${vectors.size}", fontSize = 14.sp, color = Color.Gray)
                }
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(onClick = { showForm = !showForm }) {
                Text(if (showForm) "Закрити" else "Додати")
            }
            TextButton(onClick = { viewModel.clearAll() }) {
                Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red)
                Text("Очистити", color = Color.Red)
            }
        }

        AnimatedVisibility(visible = showForm) {
            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Назва") }, modifier = Modifier.fillMaxWidth())
                    Row {
                        OutlinedTextField(value = xStr, onValueChange = { xStr = it }, label = { Text("X") }, modifier = Modifier.weight(1f))
                        Spacer(Modifier.width(8.dp))
                        OutlinedTextField(value = yStr, onValueChange = { yStr = it }, label = { Text("Y") }, modifier = Modifier.weight(1f))
                    }
                    Button(onClick = {
                        val vx = xStr.toFloatOrNull() ?: 0f
                        val vy = yStr.toFloatOrNull() ?: 0f
                        if (name.isNotBlank()) {
                            viewModel.addVector(Vector2D(name = name, x = vx, y = vy))
                            name = ""; xStr = ""; yStr = ""
                        }
                    }, modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) { Text("Зберегти") }
                }
            }
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(vectors) { vector ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    ListItem(
                        headlineContent = { Text(vector.name, fontWeight = FontWeight.Bold) },
                        supportingContent = { Text("X: ${vector.x}, Y: ${vector.y}") },
                        trailingContent = {
                            IconButton(onClick = { viewModel.deleteVector(vector.id) }) {
                                Icon(Icons.Default.Delete, contentDescription = null, tint = Color.LightGray)
                            }
                        }
                    )
                }
            }
        }
    }
}