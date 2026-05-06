package com.example.vectors.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vectors.viewmodel.VectorViewModel
import com.example.vectors.domain.model.Vector2D

@Composable
fun HomeScreen() {
    val viewModel: VectorViewModel = hiltViewModel()

    val vectors = viewModel.vectors.collectAsState()

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F7FF))
                .padding(paddingValues)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Vector Lab 2D",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6B4EFF)
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    viewModel.addVector(
                        Vector2D(
                            name = "Тест",
                            x = 5f,
                            y = 12f
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Додати вектор")
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text("Кількість: ${vectors.value.size}")
        }
    }
}