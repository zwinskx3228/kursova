@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.vectors.ui.transformations

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vectors.viewmodel.VectorViewModel
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun TransformationsScreen(navController: NavHostController) {
    val viewModel: VectorViewModel = hiltViewModel()
    var angle by remember { mutableStateOf(45f) }
    var selectedVectorIndex by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Перетворення векторів") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF6B4EFF)
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Оберіть перетворення", fontSize = 20.sp, fontWeight = FontWeight.Bold)

            Spacer(Modifier.height(20.dp))

            // Кнопки перетворень
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = { /* Поворот */ }, modifier = Modifier.fillMaxWidth()) {
                    Text("Повернути на кут")
                }
                Button(onClick = { /* Відображення */ }, modifier = Modifier.fillMaxWidth()) {
                    Text("Віддзеркалення")
                }
                Button(onClick = { /* Масштаб */ }, modifier = Modifier.fillMaxWidth()) {
                    Text("Масштабування")
                }
            }

            Spacer(Modifier.height(32.dp))

            Text("Кут повороту: ${angle.toInt()}°", fontSize = 18.sp)
            Slider(
                value = angle,
                onValueChange = { angle = it },
                valueRange = 0f..360f,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = { /* Застосувати перетворення */ },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFFEC4899))
            ) {
                Text("Застосувати перетворення", fontSize = 18.sp)
            }
        }
    }
}