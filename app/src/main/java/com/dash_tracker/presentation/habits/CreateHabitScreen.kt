package com.dash_tracker.presentation.habits

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.dash_tracker.domain.model.CategoriaHabito
import com.dash_tracker.domain.model.TipoFrecuencia
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateHabitScreen(
    onNavigateBack: () -> Unit,
    onHabitCreated: (String, CategoriaHabito, TipoFrecuencia, String, Date) -> Unit
) {
    var habitName by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(CategoriaHabito.OTROS) }
    var selectedFrequency by remember { mutableStateOf(TipoFrecuencia.DIARIA) }
    var selectedColor by remember { mutableStateOf("#635B9B") }
    var creationDate by remember { mutableStateOf(Date()) }
    var showDatePicker by remember { mutableStateOf(false) }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = creationDate.time
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        creationDate = Date(it)
                    }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // Definición de categorías con sus respectivos iconos y colores
    val categorias = listOf(
        CategoryItem(CategoriaHabito.SALUD, Icons.Default.Favorite, Color(0xFFE91E63)),
        CategoryItem(CategoriaHabito.ESTUDIO, Icons.Default.Book, Color(0xFF2196F3)),
        CategoryItem(CategoriaHabito.TRABAJO, Icons.Default.Work, Color(0xFF4CAF50)),
        CategoryItem(CategoriaHabito.DEPORTE, Icons.Default.FitnessCenter, Color(0xFFFF9800)),
        CategoryItem(CategoriaHabito.PRODUCTIVIDAD, Icons.Default.Star, Color(0xFF9C27B0)),
        CategoryItem(CategoriaHabito.OTROS, Icons.Default.Category, Color(0xFF607D8B))
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Create habit", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier
                            .padding(8.dp)
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", modifier = Modifier.size(20.dp))
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (habitName.isNotBlank()) {
                                onHabitCreated(habitName, selectedCategory, selectedFrequency, selectedColor, creationDate)
                                onNavigateBack()
                            }
                        },
                        modifier = Modifier
                            .padding(8.dp)
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(Icons.Default.Check, contentDescription = "Save", tint = MaterialTheme.colorScheme.onPrimary)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                "General information",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp),
                color = MaterialTheme.colorScheme.onBackground
            )

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    TextField(
                        value = habitName,
                        onValueChange = { habitName = it },
                        placeholder = { Text("Habit name", fontSize = 14.sp) },
                        textStyle = TextStyle(
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            cursorColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "Date of creation",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp),
                color = MaterialTheme.colorScheme.onBackground
            )
            Card(
                onClick = { showDatePicker = true },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault()).format(creationDate),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "Category",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp),
                color = MaterialTheme.colorScheme.onBackground
            )
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column {
                    categorias.forEachIndexed { index, item ->
                        SelectionItem(
                            label = item.categoria.name.lowercase().replaceFirstChar { it.uppercase() },
                            icon = item.icon,
                            iconColor = item.color,
                            isSelected = selectedCategory == item.categoria,
                            onSelect = { 
                                selectedCategory = item.categoria
                            }
                        )
                        if (index < categorias.size - 1) {
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                thickness = 0.5.dp,
                                color = MaterialTheme.colorScheme.outlineVariant
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "Frequency",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp),
                color = MaterialTheme.colorScheme.onBackground
            )
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column {
                    TipoFrecuencia.entries.forEachIndexed { index, freq ->
                        FrequencyItem(
                            label = freq.name.lowercase().replaceFirstChar { it.uppercase() },
                            isSelected = selectedFrequency == freq,
                            onSelect = { selectedFrequency = freq }
                        )
                        if (index < TipoFrecuencia.entries.size - 1) {
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                thickness = 0.5.dp,
                                color = MaterialTheme.colorScheme.outlineVariant
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun CreateHabitRoute(
    onNavigateBack: () -> Unit,
    viewModel: HabitViewModel = hiltViewModel()
) {
    CreateHabitScreen(
        onNavigateBack = onNavigateBack,
        onHabitCreated = { titulo, cat, freq, color, fecha ->
            viewModel.crearHabito(titulo, cat, freq, color, fecha)
        }
    )
}

data class CategoryItem(val categoria: CategoriaHabito, val icon: ImageVector, val color: Color)

@Composable
fun SelectionItem(
    label: String,
    icon: ImageVector,
    iconColor: Color,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun FrequencyItem(
    label: String,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateHabitScreenPreview() {
    CreateHabitScreen(
        onNavigateBack = {},
        onHabitCreated = { _, _, _, _, _ -> }
    )
}
