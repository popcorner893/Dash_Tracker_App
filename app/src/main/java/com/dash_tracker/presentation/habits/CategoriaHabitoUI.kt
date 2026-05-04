package com.dash_tracker.presentation.habits


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.dash_tracker.domain.model.CategoriaHabito

// Clase para agrupar los datos visuales
data class CategoriaVisual(
    val nombre: String,
    val icono: ImageVector,
    val color: Color
)

// Función de extensión para convertir el Enum en datos visuales
fun CategoriaHabito.toVisual(): CategoriaVisual {
    return when (this) {
        CategoriaHabito.SALUD -> CategoriaVisual("Salud", Icons.Default.Favorite, Color(0xFFE91E63))
        CategoriaHabito.ESTUDIO -> CategoriaVisual("Estudio", Icons.Default.Book, Color(0xFF2196F3))
        CategoriaHabito.TRABAJO -> CategoriaVisual("Trabajo", Icons.Default.Work, Color(0xFF4CAF50))
        CategoriaHabito.DEPORTE -> CategoriaVisual("Deporte", Icons.Default.FitnessCenter, Color(0xFFFF9800))
        CategoriaHabito.PRODUCTIVIDAD -> CategoriaVisual("Productividad", Icons.Default.Star, Color(0xFF9C27B0))
        CategoriaHabito.OTROS -> CategoriaVisual("Otros", Icons.Default.Category, Color(0xFF607D8B))
    }
}