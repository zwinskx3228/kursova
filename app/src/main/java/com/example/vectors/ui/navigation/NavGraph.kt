package com.example.vectors.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.vectors.ui.home.HomeScreen
import com.example.vectors.ui.operations.OperationsScreen
import com.example.vectors.ui.visualization.VisualizationScreen
import com.example.vectors.ui.transformations.TransformationsScreen
import com.example.vectors.ui.extensions.ExtensionsScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    paddingValues: PaddingValues // Важливо: передаємо відступи від BottomBar
) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Home.route,
        modifier = Modifier.padding(paddingValues) // Навхост не перекриває навігацію
    ) {
        composable(BottomNavItem.Home.route) {
            HomeScreen()
        }
        composable(BottomNavItem.Operations.route) {
            OperationsScreen(navController)
        }
        composable(BottomNavItem.Visualization.route) {
            VisualizationScreen(navController)
        }
        composable(BottomNavItem.Transformations.route) {
            TransformationsScreen(navController)
        }
        composable(BottomNavItem.Extensions.route) {
            ExtensionsScreen(navController)
        }
    }
}