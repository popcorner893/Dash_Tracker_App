package com.dash_tracker.presentation.habits

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.graphics.toColorInt
import com.dash_tracker.domain.model.CategoriaHabito
import com.dash_tracker.domain.model.Habito
import com.dash_tracker.domain.model.TipoFrecuencia
import kotlinx.datetime.*
import kotlinx.coroutines.flow.StateFlow
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigateToCreateHabit: () -> Unit,
    habitos: List<Habito> = emptyList()
) {
    // Lista de categorías para el ejemplo (REQ-014)
    val categorias = listOf("Todos", "Otros", "Salud", "Estudio", "Trabajo")
    
    // Generar 16 días (2 pasados, Hoy y 13 futuros)
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    val days = (-2..13).map { today.plus(it, DateTimeUnit.DAY) }
    val currentMonth = today.month.name.lowercase().replaceFirstChar { it.uppercase() }

    val listState = rememberLazyListState()

    // Scroll automático al día de hoy (índice 2, ya que hay 2 días antes)
    LaunchedEffect(Unit) {
        listState.scrollToItem(2)
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToCreateHabit,
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                modifier = Modifier
                    .size(70.dp) // Tamaño personalizado exacto
                    .offset(y = 60.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Añadir Hábito", modifier = Modifier.size(40.dp))
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        bottomBar = { MyBottomNavigationBar() }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {

            // 1. TÍTULO DEL MES Y CALENDARIO HORIZONTAL
            Text(
                text = currentMonth,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp)
            )

            LazyRow(
                state = listState,
                modifier = Modifier.padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(days) { date ->
                    val dayName = date.dayOfWeek.name.take(3).lowercase().replaceFirstChar { it.uppercase() }
                    val isToday = date == today
                    DayCard(
                        dayName = dayName,
                        dayOfMonth = date.dayOfMonth.toString(),
                        isSelected = isToday
                    )
                }
            }

            // 2. FILTROS DE CATEGORÍA
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp), contentPadding = PaddingValues(horizontal = 16.dp)) {
                items(categorias) { cat ->
                    FilterChip(selected = cat == "Todos", onClick = {}, label = { Text(cat) })
                }
            }

            // 3. BARRA DE PROGRESO
            Text("Progreso 90%", modifier = Modifier.padding(horizontal = 16.dp))
            LinearProgressIndicator(progress = 0.9f, modifier = Modifier.fillMaxWidth().padding(16.dp))

            // 4. LISTA DE HÁBITOS
            LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
                items(habitos) { habito ->
                    HabitCard(habito)
                }
            }
        }
    }
}

// Punto de entrada real que usa el ViewModel
@Composable
fun DashboardRoute(
    onNavigateToCreateHabit: () -> Unit,
    viewModel: HabitViewModel = hiltViewModel()
) {
    val habitos by viewModel.habitos.collectAsState()
    DashboardScreen(
        onNavigateToCreateHabit = onNavigateToCreateHabit,
        habitos = habitos
    )
}

@Composable
fun DayCard(dayName: String, dayOfMonth: String, isSelected: Boolean) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = Modifier.width(60.dp)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = dayName,
                style = MaterialTheme.typography.bodySmall,
                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = dayOfMonth,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun HabitCard(habito: Habito) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = habito.titulo,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = habito.frecuencia.name.lowercase().replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.bodySmall
                )
            }
            IconButton(
                onClick = { /* Acción de completar */ },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color(habito.color.toColorInt()))
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Completar",
                    tint = Color.White
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    val sampleHabitos = listOf(
        Habito(
            id = 1,
            titulo = "Beber Agua",
            categoria = CategoriaHabito.SALUD,
            frecuencia = TipoFrecuencia.DIARIA,
            color = "#4A90E2",
            activo = true,
            fechaCreacion = Date()
        ),
        Habito(
            id = 2,
            titulo = "Estudiar Kotlin",
            categoria = CategoriaHabito.ESTUDIO,
            frecuencia = TipoFrecuencia.DIARIA,
            color = "#FF8C00",
            activo = true,
            fechaCreacion = Date()
        )
    )
    DashboardScreen(onNavigateToCreateHabit = {}, habitos = sampleHabitos)
}

@Preview(showBackground = true)
@Composable
fun HabitCardPreview() {
    val sampleHabito = Habito(
        id = 1,
        titulo = "Beber Agua",
        categoria = CategoriaHabito.SALUD,
        frecuencia = TipoFrecuencia.DIARIA,
        color = "#4A90E2",
        activo = true,
        fechaCreacion = Date()
    )
    HabitCard(habito = sampleHabito)
}

@Composable
fun MyBottomNavigationBar() {
    NavigationBar {
        NavigationBarItem(icon = { Icon(Icons.Default.DateRange, "Hoy") }, label = { Text("Hoy") }, selected = true, onClick = {})
        NavigationBarItem(icon = { Icon(Icons.AutoMirrored.Filled.List, "Hábitos") }, label = { Text("Hábitos") }, selected = false, onClick = {})
        
        // Espacio para el FAB central
        Spacer(modifier = Modifier.weight(1f))

        NavigationBarItem(icon = { Icon(Icons.Default.AccountBalanceWallet, "Finanzas") }, label = { Text("Finanzas") }, selected = false, onClick = {})
        NavigationBarItem(icon = { Icon(Icons.Default.BarChart, "Stats") }, label = { Text("Stats") }, selected = false, onClick = {})
    }
}
