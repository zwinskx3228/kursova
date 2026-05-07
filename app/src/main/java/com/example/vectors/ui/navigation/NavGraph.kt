package com.example.vectors.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.vectors.ui.screens.HomeScreen
import com.example.vectors.ui.visualization.VisualizationScreen
import com.example.vectors.ui.operations.OperationsScreen
import com.example.vectors.ui.extensions.ExtensionsScreen
import com.example.vectors.ui.transformations.TransformationsScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen()
        }
        composable("operations") {
            OperationsScreen(navController)
        }
        composable("transformations") {
            TransformationsScreen()
        }
        composable("visualization") {
            VisualizationScreen()
        }
        composable("extensions") {
            ExtensionsScreen()
        }
    }
}