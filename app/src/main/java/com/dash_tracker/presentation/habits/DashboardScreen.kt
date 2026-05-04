package com.dash_tracker.presentation.habits

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.dash_tracker.domain.model.CategoriaHabito
import com.dash_tracker.domain.model.Habito
import com.dash_tracker.domain.model.TipoFrecuencia
import com.dash_tracker.presentation.theme.Dash_TrackerTheme
import com.dash_tracker.presentation.navigation.AppDrawer
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import kotlinx.datetime.TimeZone // Asegúrate que sea este
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigateToCreateHabit: () -> Unit,
    habitos: List<Habito> = emptyList()
) {
    // 1. Corregir TimeZone para evitar conflictos con java.util
    val userTimeZone = remember { TimeZone.currentSystemDefault() }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedDate by remember { mutableStateOf(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date) }

    val days = remember(selectedDate) {
        (0..8).map { selectedDate.plus(it, DateTimeUnit.DAY) }
    }
    val listState = rememberLazyListState()
    val currentMonth = selectedDate.month.name.lowercase().replaceFirstChar { it.uppercase() }
// --- ESTADOS DE LA PANTALLA ---
    var showDatePicker by remember { mutableStateOf(false) }

    // --- NUEVOS ESTADOS PARA EL FILTRO ---
    var categoriaSeleccionada by remember { mutableStateOf("Todos") }

    // Generar las opciones del filtro ("Todos" + los valores de tu Enum)
    val opcionesFiltro = remember {
        listOf("Todos") + CategoriaHabito.entries.map {
            it.name.lowercase().replaceFirstChar { char -> char.uppercase() }
        }
    }

    // Crear la lista filtrada de hábitos que le pasaremos a la LazyColumn
    val habitosFiltrados = remember(habitos, categoriaSeleccionada) {
        if (categoriaSeleccionada == "Todos") {
            habitos
        } else {
            habitos.filter { habito ->
                // Comparamos el nombre de la categoría del hábito con el chip seleccionado
                val nombreCategoria = habito.categoria.name.lowercase().replaceFirstChar { c -> c.uppercase() }
                nombreCategoria == categoriaSeleccionada
            }
        }
    }
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                onNavigateToHabits = { scope.launch { drawerState.close() } },
                onNavigateToProfile = { },
                onNavigateToFocus = { },
                onNavigateToSettings = { },
                onNavigateToPremium = { },
                closeDrawer = { scope.launch { drawerState.close() } }
            )
        }
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Dash Tracker", style = MaterialTheme.typography.titleMedium) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, "Menu")
                        }
                    },
                    actions = {
                        IconButton(onClick = { }) { Icon(Icons.Default.Search, "Buscar") }
                        IconButton(onClick = { showDatePicker = true }) { Icon(Icons.Default.CalendarMonth, "Calendario") }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = onNavigateToCreateHabit,
                    shape = CircleShape,
                    containerColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(65.dp)
                        .offset(y = 50.dp) // Reducido un poco para que no desaparezca en el preview
                ) {
                    Icon(Icons.Default.Add, "Añadir", tint = Color.White, modifier = Modifier.size(40.dp))
                }
            },
            floatingActionButtonPosition = FabPosition.Center,
            bottomBar = { MyBottomNavigationBar() }
        ) { padding ->

            // --- DATE PICKER (Encapsulado para evitar crashes en preview) ---
            if (showDatePicker) {
                // Usamos UTC para el picker para evitar desfases de milisegundos
                val datePickerState = rememberDatePickerState(
                    initialSelectedDateMillis = selectedDate.atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()
                )
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        TextButton(onClick = {
                            datePickerState.selectedDateMillis?.let {
                                selectedDate = Instant.fromEpochMilliseconds(it)
                                    .toLocalDateTime(TimeZone.UTC).date
                            }
                            showDatePicker = false
                        }) { Text("OK") }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                Text(
                    text = currentMonth,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp)
                )

                LazyRow(
                    state = listState,
                    modifier = Modifier.padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    items(days) { date ->
                        val dayName = date.dayOfWeek.name.take(3).lowercase().replaceFirstChar { it.uppercase() }
                        DayCard(
                            dayName = dayName,
                            dayOfMonth = date.dayOfMonth.toString(),
                            isSelected = date == selectedDate,
                            onClick = { selectedDate = date }
                        )
                    }
                }

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    items(opcionesFiltro) { cat ->
                        FilterChip(
                            selected = cat == categoriaSeleccionada, // Se pinta si está seleccionado
                            onClick = { categoriaSeleccionada = cat }, // Actualiza el estado al tocarlo
                            label = { Text(cat) },
                            shape = RoundedCornerShape(20.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                    }
                }

                // 4. LISTA DE HÁBITOS FILTRADA
                // Usamos 'habitosFiltrados' en lugar de la original 'habitos'
                if (habitosFiltrados.isEmpty()) {
                    Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(
                            text = if (categoriaSeleccionada == "Todos") "No hay hábitos para este día" else "No tienes hábitos en $categoriaSeleccionada",
                            color = Color.Gray
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(bottom = 100.dp) // Espacio para el FAB
                    ) {
                        items(habitosFiltrados) { habito ->
                            HabitCard(habito)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HabitCard(habito: Habito) {
    // Usamos el toVisual() normal sin @Composable
    val uiConfig = habito.categoria.toVisual()

    val habitCheckColor = remember(habito.color) {
        try { Color(android.graphics.Color.parseColor(habito.color)) }
        catch (e: Exception) { Color.Gray }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(uiConfig.color.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = uiConfig.icono,
                    contentDescription = null,
                    tint = uiConfig.color,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = habito.titulo, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(
                    text = habito.frecuencia.name.lowercase().replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            Box(
                modifier = Modifier.size(32.dp).clip(CircleShape).background(habitCheckColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Check, null, tint = Color.White, modifier = Modifier.size(20.dp))
            }
        }
    }
}
// --- COMPONENTES FALTANTES (Pégalos al final del archivo) ---

// 1. Punto de entrada real que usa el ViewModel y Hilt
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

// 2. Tarjeta individual de los días del calendario
@Composable
fun DayCard(dayName: String, dayOfMonth: String, isSelected: Boolean, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = Modifier
            .width(60.dp)
            .clickable { onClick() }
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

// 3. Barra de navegación inferior
@Composable
fun MyBottomNavigationBar() {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.DateRange, "Hoy") },
            label = { Text("Hoy") },
            selected = true,
            onClick = {}
        )
        NavigationBarItem(
            icon = { Icon(Icons.AutoMirrored.Filled.List, "Hábitos") },
            label = { Text("Hábitos") },
            selected = false,
            onClick = {}
        )

        // Espacio vital para que el FAB central no tape los iconos
        Spacer(Modifier.weight(1f))

        NavigationBarItem(
            icon = { Icon(Icons.Default.AccountBalanceWallet, "Finanzas") },
            label = { Text("Finanzas") },
            selected = false,
            onClick = {}
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.BarChart, "Stats") },
            label = { Text("Stats") },
            selected = false,
            onClick = {}
        )
    }
}

// Previsualización robusta
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DashboardScreenPreview() {
    val sampleHabitos = listOf(
        Habito(1, "Beber Agua", CategoriaHabito.SALUD, TipoFrecuencia.DIARIA, "#4A90E2", true, Date()),
        Habito(2, "Estudiar Kotlin", CategoriaHabito.ESTUDIO, TipoFrecuencia.DIARIA, "#FF8C00", true, Date())
    )
    Dash_TrackerTheme {
        DashboardScreen(onNavigateToCreateHabit = {}, habitos = sampleHabitos)
    }
}