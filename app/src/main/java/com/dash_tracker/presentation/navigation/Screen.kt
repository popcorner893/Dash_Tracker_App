package com.dash_tracker.presentation.navigation

// Una clase sellada para tener las rutas ordenadas y evitar errores de tipeo
sealed class Screen(val route: String) {
    object Login : Screen("login_screen")
    object Register : Screen("register_screen")
    object Dashboard : Screen("dashboard_screen") // La pantalla principal de hábitos
    object CreateHabit : Screen("create_habit_screen")
}