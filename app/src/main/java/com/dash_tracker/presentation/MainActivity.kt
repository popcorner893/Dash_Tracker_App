package com.dash_tracker.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.dash_tracker.presentation.navigation.AppNavigation
import com.dash_tracker.presentation.theme.Dash_TrackerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // 1. Preguntamos al celular cómo está configurado (claro u oscuro por defecto)
            val systemTheme = isSystemInDarkTheme()

            // 2. Creamos una variable de estado que Compose vigilará
            // (En la próxima iteración guardaremos esto en la BD para que no se borre al cerrar la app)
            var isDarkTheme by remember { mutableStateOf(systemTheme) }

            // 3. Le pasamos el estado a nuestro Tema
            Dash_TrackerTheme(darkTheme = isDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // 4. Le pasamos el estado a la Navegación (y esta se lo pasará a Settings)
                    AppNavigation(
                        isDarkTheme = isDarkTheme,
                        onThemeChange = { nuevoEstado ->
                            isDarkTheme = nuevoEstado // Al cambiar el switch, esto repinta TODA la app
                        }
                    )
                }
            }
        }
    }
}