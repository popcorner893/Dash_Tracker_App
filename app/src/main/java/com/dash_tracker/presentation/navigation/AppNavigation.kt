package com.dash_tracker.presentation.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dash_tracker.presentation.auth.LoginScreen
import com.dash_tracker.presentation.auth.RegisterScreen
import com.dash_tracker.presentation.theme.Dash_TrackerTheme
import com.dash_tracker.presentation.habits.DashboardScreen
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Login.route) {

        // PANTALLA DE LOGIN
        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                onLoginSuccess = {
                    // Navegamos al Dashboard y borramos el Login del historial para que el botón "Atrás" no vuelva aquí
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // PANTALLA DE REGISTRO
        composable(Screen.Register.route) {
            RegisterScreen(
                onNavigateBackToLogin = {
                    navController.popBackStack() // Vuelve a la pantalla anterior (Login)
                },
                onRegisterSuccess = {
                    // Si se registra bien, lo mandamos directo al Dashboard y borramos el historial
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // PANTALLA PRINCIPAL (DASHBOARD)
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onNavigateToCreateHabit = {
                    navController.navigate(Screen.CreateHabit.route) // Va a la pantalla de crear
                }
            )
        }
        composable(Screen.CreateHabit.route) {
            // Esto lo haremos en el siguiente paso
            Text("Pantalla para Crear Hábito en construcción...")
        }

    }
}
