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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dash_tracker.presentation.navigation.AppDrawer
import kotlinx.coroutines.launch

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

enum class TipoEstadoDia { COMPLETADO, FALLIDO, PENDIENTE, HOY }

// --- ENTRY POINT REAL DE LA PANTALLA ---
@Composable
fun HabitListRoute(
    onNavigateToDashboard: () -> Unit,
    onNavigateToSettings: () -> Unit, // <-- NUEVO
    onNavigateToCreateHabit: () -> Unit, // <-- NUEVO
    viewModel: HabitViewModel = hiltViewModel()
) {
    val habitosProgress by viewModel.habitosProgress.collectAsState()
    HabitListScreen(
        habitosProgress = habitosProgress,
        onNavigateToDashboard = onNavigateToDashboard,
        onNavigateToSettings = onNavigateToSettings,
        onNavigateToCreateHabit = onNavigateToCreateHabit,
        onHabitClick = { /* TODO */ }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitListScreen(
    habitosProgress: List<HabitProgressUI>,
    onNavigateToDashboard: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToCreateHabit: () -> Unit,
    onHabitClick: (Int) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // 1. ENVOLVEMOS CON EL DRAWER
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                onNavigateToHabits = { scope.launch { drawerState.close() } },
                onNavigateToProfile = { },
                onNavigateToFocus = { },
                onNavigateToSettings = {
                    onNavigateToSettings()
                    scope.launch { drawerState.close() }
                },
                onNavigateToPremium = { },
                closeDrawer = { scope.launch { drawerState.close() } }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Hábitos", fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        // AHORA ESTE BOTÓN ABRE EL MENÚ
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menú")
                        }
                    },
                    actions = {
                        IconButton(onClick = { }) { Icon(Icons.Default.Search, "Buscar") }
                        IconButton(onClick = { }) { Icon(Icons.Default.FilterList, "Filtrar") }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
                )
            },
            // 2. AGREGAMOS EL FAB
            floatingActionButton = {
                FloatingActionButton(
                    onClick = onNavigateToCreateHabit,
                    shape = CircleShape,
                    containerColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(65.dp).offset(y = 50.dp)
                ) {
                    Icon(Icons.Default.Add, "Añadir", tint = Color.White, modifier = Modifier.size(40.dp))
                }
            },
            floatingActionButtonPosition = FabPosition.Center,
            bottomBar = {
                MyBottomNavigationBar(
                    onNavigateToDashboard = onNavigateToDashboard,
                    onNavigateToHabitList = { },
                    currentRoute = "HabitList"
                )
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { padding ->
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp)
            ) {
                items(habitosProgress) { habit ->
                    HabitProgressCard(habit = habit, onClick = { onHabitClick(habit.id) })
                }
            }
        }
    }
}

@Composable
fun HabitProgressCard(habit: HabitProgressUI, onClick: () -> Unit) {
    val habitColor = Color(habit.colorHex)

    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // CABECERA
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = habit.titulo, color = MaterialTheme.colorScheme.onSurface, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = habit.frecuencia,
                        color = habitColor,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .background(habitColor.copy(alpha = 0.1f), RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
                Box(
                    modifier = Modifier.size(40.dp).clip(RoundedCornerShape(12.dp)).background(habitColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Home, null, tint = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // --- AQUÍ ESTÁ EL CALENDARIO QUE FALTABA ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                habit.historialDias.forEach { day ->
                    DayProgressCircle(day = day, habitColor = habitColor)
                }
            }
            // ------------------------------------------

            Spacer(modifier = Modifier.height(20.dp))

            // RACHA Y PORCENTAJE
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocalFireDepartment, "Racha", tint = habitColor, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("${habit.racha}", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold, fontSize = 14.sp)

                Spacer(modifier = Modifier.width(16.dp))

                Icon(Icons.Outlined.CheckCircle, "Porcentaje", tint = habitColor, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("${habit.porcentaje}%", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold, fontSize = 14.sp)

                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { }, modifier = Modifier.size(24.dp)) { Icon(Icons.Default.DateRange, "Calendario", tint = Color.Gray) }
                Spacer(modifier = Modifier.width(16.dp))
                IconButton(onClick = { }, modifier = Modifier.size(24.dp)) { Icon(Icons.Default.BarChart, "Stats", tint = Color.Gray) }
            }
        }
    }
}

// --- COMPONENTE: BOLITA DE UN DÍA ---
@Composable
fun DayProgressCircle(day: DayStatus, habitColor: Color) {
    // Definimos el verde de éxito
    val colorCompletado = Color(0xFF4CAF50)

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = day.nombreDia, color = Color.Gray, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .border(
                    width = 2.dp,
                    color = when (day.estado) {
                        TipoEstadoDia.COMPLETADO -> colorCompletado // <-- AHORA ES VERDE
                        TipoEstadoDia.HOY -> habitColor // <-- AHORA USA EL COLOR DEL HÁBITO
                        TipoEstadoDia.FALLIDO -> Color(0xFFE53935)
                        TipoEstadoDia.PENDIENTE -> Color.Gray.copy(alpha = 0.3f)
                    },
                    shape = CircleShape
                )
                .background(
                    when (day.estado) {
                        TipoEstadoDia.COMPLETADO -> colorCompletado.copy(alpha = 0.2f) // Fondo verdecito
                        TipoEstadoDia.FALLIDO -> Color(0xFFE53935).copy(alpha = 0.1f)
                        else -> Color.Transparent
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = day.numeroDia,
                color = when (day.estado) {
                    TipoEstadoDia.HOY -> habitColor // <-- EL NÚMERO DE HOY TOMA EL COLOR DEL HÁBITO
                    TipoEstadoDia.FALLIDO -> Color(0xFFE53935)
                    else -> MaterialTheme.colorScheme.onSurface
                },
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
        HabitListScreen(
            habitosProgress = mockHabits,
            onNavigateToDashboard = {},
            onNavigateToSettings = {},
            onNavigateToCreateHabit = {},
            onHabitClick = {}
        )
    }
}