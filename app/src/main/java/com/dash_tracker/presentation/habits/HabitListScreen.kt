package com.dash_tracker.presentation.habits

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// --- ESTRUCTURA DE DATOS PARA LA VISTA ---
// Esta clase envuelve el Hábito con las estadísticas calculadas para la tarjeta
data class HabitProgressUI(
    val id: Int,
    val titulo: String,
    val frecuencia: String,
    val colorHex: Long, // Usaremos Long para los colores ej: 0xFFFFA500 (Naranja)
    val racha: Int,
    val porcentaje: Int,
    val historialDias: List<DayStatus>
)

data class DayStatus(
    val nombreDia: String, // Ej: "Lun"
    val numeroDia: String, // Ej: "11"
    val estado: TipoEstadoDia
)

enum class TipoEstadoDia { COMPLETADO, PENDIENTE, HOY }

// --- PANTALLA PRINCIPAL ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitListScreen(
    // Aquí recibiremos la lista desde el ViewModel más adelante
    habitosProgress: List<HabitProgressUI>,
    onHabitClick: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hábitos", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { /* Abrir menú lateral */ }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menú")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Buscar */ }) {
                        Icon(Icons.Default.Search, contentDescription = "Buscar")
                    }
                    IconButton(onClick = { /* Filtrar */ }) {
                        Icon(Icons.Default.FilterList, contentDescription = "Filtrar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF121212), // Fondo oscuro
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        containerColor = Color(0xFF121212) // Fondo global oscuro de la app
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp) // Espacio inferior para el nav
        ) {
            items(habitosProgress) { habit ->
                HabitProgressCard(habit = habit, onClick = { onHabitClick(habit.id) })
            }
        }
    }
}

// --- COMPONENTE: LA TARJETA DEL HÁBITO ---
@Composable
fun HabitProgressCard(habit: HabitProgressUI, onClick: () -> Unit) {
    val habitColor = Color(habit.colorHex)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)) // Gris oscuro como en tu foto
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // CABECERA: Título, Frecuencia e Ícono
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = habit.titulo,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = habit.frecuencia,
                        color = habitColor.copy(alpha = 0.8f),
                        fontSize = 12.sp,
                        modifier = Modifier
                            .background(habitColor.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                // Ícono de la esquina superior derecha (Ej. La casita)
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(habitColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Home, // TODO: Hacer dinámico según la categoría
                        contentDescription = "Ícono del Hábito",
                        tint = Color(0xFF1E1E1E) // Contraste oscuro
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // CALENDARIO SEMANAL: Bolitas de los días
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                habit.historialDias.forEach { day ->
                    DayProgressCircle(day = day, habitColor = habitColor)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // PIE DE TARJETA: Estadísticas y Acciones
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Racha (Cadena)
                Icon(Icons.Default.Link, contentDescription = "Racha", tint = habitColor, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("${habit.racha}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)

                Spacer(modifier = Modifier.width(16.dp))

                // Porcentaje (Chulito)
                Icon(Icons.Outlined.CheckCircle, contentDescription = "Porcentaje", tint = habitColor, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("${habit.porcentaje}%", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)

                Spacer(modifier = Modifier.weight(1f)) // Empuja los siguientes íconos a la derecha

                // Botones de acción
                IconButton(onClick = { }, modifier = Modifier.size(24.dp)) {
                    Icon(Icons.Default.DateRange, "Calendario", tint = Color.Gray)
                }
                Spacer(modifier = Modifier.width(16.dp))
                IconButton(onClick = { }, modifier = Modifier.size(24.dp)) {
                    Icon(Icons.Default.BarChart, "Estadísticas", tint = Color.Gray)
                }
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = { }, modifier = Modifier.size(24.dp)) {
                    Icon(Icons.Default.MoreVert, "Opciones", tint = Color.Gray)
                }
            }
        }
    }
}

// --- COMPONENTE: BOLITA DE UN DÍA ---
@Composable
fun DayProgressCircle(day: DayStatus, habitColor: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = day.nombreDia, color = Color.Gray, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .border(
                    width = 2.dp,
                    // Si es HOY, el borde es verde (como en tu foto), sino, usa el color del hábito o gris
                    color = when (day.estado) {
                        TipoEstadoDia.HOY -> Color(0xFF4CAF50) // Verde
                        TipoEstadoDia.COMPLETADO -> habitColor
                        TipoEstadoDia.PENDIENTE -> Color.Gray.copy(alpha = 0.3f)
                    },
                    shape = CircleShape
                )
                // Fondo: Solo se rellena si está completado
                .background(
                    if (day.estado == TipoEstadoDia.COMPLETADO) habitColor.copy(alpha = 0.2f) else Color.Transparent
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = day.numeroDia,
                color = if (day.estado == TipoEstadoDia.HOY) Color(0xFF4CAF50) else Color.White,
                fontWeight = if (day.estado == TipoEstadoDia.HOY) FontWeight.Bold else FontWeight.Normal,
                fontSize = 14.sp
            )
        }
    }
}

// --- PREVIEW: PARA VERLO EN ANDROID STUDIO AHORA MISMO ---
@Preview(showBackground = true)
@Composable
fun HabitListScreenPreview() {
    // Datos falsos para replicar tu captura de pantalla (Semana del 11 al 17)
    val mockHabits = listOf(
        HabitProgressUI(
            id = 1,
            titulo = "Ir a casa",
            frecuencia = "Todos los días",
            colorHex = 0xFFF59E0B, // Naranja
            racha = 1,
            porcentaje = 14,
            historialDias = listOf(
                DayStatus("Lun", "11", TipoEstadoDia.PENDIENTE),
                DayStatus("Mar", "12", TipoEstadoDia.COMPLETADO),
                DayStatus("Mié", "13", TipoEstadoDia.PENDIENTE),
                DayStatus("Jue", "14", TipoEstadoDia.PENDIENTE),
                DayStatus("Vie", "15", TipoEstadoDia.COMPLETADO),
                DayStatus("Sáb", "16", TipoEstadoDia.PENDIENTE),
                DayStatus("Dom", "17", TipoEstadoDia.HOY) // Verde
            )
        )
    )

    // Le pasamos un tema oscuro para que se vea igual a tu foto
    MaterialTheme(colorScheme = darkColorScheme()) {
        HabitListScreen(habitosProgress = mockHabits, onHabitClick = {})
    }
}