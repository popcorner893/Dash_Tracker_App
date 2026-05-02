package com.dash_tracker.presentation.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dash_tracker.presentation.auth.LoginScreen

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
            // TODO: Crearemos RegisterScreen en el próximo paso
            Text("Pantalla de Registro en construcción...")
        }

        // PANTALLA PRINCIPAL (DASHBOARD)
        composable(Screen.Dashboard.route) {
            Text("¡Bienvenido al Dashboard de tus Hábitos!")
        }
    }
}