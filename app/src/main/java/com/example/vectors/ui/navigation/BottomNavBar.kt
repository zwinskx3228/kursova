package com.example.vectors.ui.navigation

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState

// 🔹 1. Опис пунктів меню (тепер він точно буде видимим для функції нижче)
sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: String
) {
    object Home : BottomNavItem("home", "Головна", "🏠")
    object Operations : BottomNavItem("operations", "Операції", "🧮")
    object Visualization : BottomNavItem("visualization", "Візуалізація", "📊")
    object Transformations : BottomNavItem("transformations", "Перетворення", "🔄")
    object Extensions : BottomNavItem("extensions", "Розширені", "✨")
}

// 🔹 2. Сама панель навігації
@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Operations,
        BottomNavItem.Visualization,
        BottomNavItem.Transformations,
        BottomNavItem.Extensions
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        items.forEach { item ->
            NavigationBarItem(
                icon = { Text(item.icon, fontSize = 24.sp) },
                label = { Text(item.title) },
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            // Повертаємося до головного екрана, щоб не плодити копії в стеку
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF6B4EFF),     // Колір іконки при натисканні
                    selectedTextColor = Color(0xFF6B4EFF),     // Колір тексту при натисканні
                    indicatorColor = Color(0xFFF0E6FF),        // Колір "овальчика" навколо іконки
                    unselectedIconColor = Color.Gray,          // Колір іконки, коли вона не вибрана
                    unselectedTextColor = Color.Gray           // Колір тексту, коли він не вибраний (замість unselectedLabelColor)
                )
            )
        }
    }
}