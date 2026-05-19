package com.dash_tracker.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dash_tracker.presentation.auth.LoginScreen
import com.dash_tracker.presentation.auth.RegisterScreen
import com.dash_tracker.presentation.habits.CreateHabitRoute
import com.dash_tracker.presentation.habits.DashboardRoute
import com.dash_tracker.presentation.habits.HabitListRoute
import com.dash_tracker.presentation.habits.HabitListScreen
import com.dash_tracker.presentation.settings.SettingsScreen

@Composable
fun AppNavigation(
    // 1. Agregamos estos dos parámetros
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Login.route) {

        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                onLoginSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onNavigateBackToLogin = { navController.popBackStack() },
                onRegisterSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Dashboard.route) {
            DashboardRoute(
                onNavigateToCreateHabit = { navController.navigate(Screen.CreateHabit.route) },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) },
                onNavigateToHabitList = { navController.navigate(Screen.HabitList.route) }
            )
        }

        composable(Screen.HabitList.route) {
            HabitListRoute(
                onNavigateToDashboard = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Dashboard.route) { inclusive = true }
                    }
                },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) },
                onNavigateToCreateHabit = { navController.navigate(Screen.CreateHabit.route) }
            )
        }

        composable(Screen.CreateHabit.route) {
            CreateHabitRoute(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() },
                onLogoutSuccess = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Dashboard.route) { inclusive = true }
                    }
                },
                // 2. Le pasamos los valores a la pantalla
                isDarkTheme = isDarkTheme,
                onThemeChange = onThemeChange
            )
        }
    }
}
