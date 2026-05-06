package com.example.vectors

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Scaffold
import androidx.navigation.compose.rememberNavController
import com.example.vectors.ui.navigation.BottomNavBar
import com.example.vectors.ui.navigation.NavGraph
import com.example.vectors.ui.theme.VectorTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VectorTheme {
                val navController = rememberNavController()

                // Це серце навігації: Scaffold об'єднує бар і контент
                Scaffold(
                    bottomBar = { BottomNavBar(navController) }
                ) { innerPadding ->
                    // Передаємо innerPadding, щоб контент не залазив під кнопки
                    NavGraph(navController, innerPadding)
                }
            }
        }
    }
}