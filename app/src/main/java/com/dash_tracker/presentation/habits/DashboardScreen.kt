package com.dash_tracker.presentation.habits

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dash_tracker.domain.model.Habito

@OptIn(ExperimentalMaterial3Api::class)


@Composable
fun DashboardScreen(
    onNavigateToCreateHabit: () -> Unit,
    viewModel: HabitViewModel = hiltViewModel() // Conecta con el cerebro
) {
    // Observamos la lista de hábitos
    val habitos by viewModel.habitos.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Mis Hábitos", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.headlineMedium)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToCreateHabit,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nuevo Hábito")
            }
        }
    ) { paddingValues ->

        // ESTADO VACÍO: Si aún no hay hábitos
        if (habitos.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Aún no tienes hábitos.",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Toca el botón + para empezar tu rutina.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            // ESTADO CON DATOS: Mostramos la lista de hábitos
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item { Spacer(modifier = Modifier.height(8.dp)) }

                items(habitos) { habito ->
                    HabitCard(habito = habito, onCheckClick = {
                        // TODO: Aquí implementaremos la lógica de marcar como completado
                    })
                }

                // Espacio al final para que el botón flotante no tape la última tarjeta
                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }
    }
}

// COMPONENTE: La tarjeta individual de cada hábito
@Composable
fun HabitCard(habito: Habito, onCheckClick: () -> Unit) {
    // Intentamos parsear el color hexadecimal guardado (ej. "#FF5722")
    val cardColor = try {
        Color(android.graphics.Color.parseColor(habito.color))
    } catch (e: Exception) {
        MaterialTheme.colorScheme.primary // Color de respaldo por si falla
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Franja de color indicadora del hábito
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .height(48.dp)
                    .clip(RoundedCornerShape(50))
                    .background(cardColor)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Textos del hábito
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = habito.titulo,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Frecuencia: ${habito.frecuencia.name}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Botón de Check (Marcar como completado)
            IconButton(
                onClick = onCheckClick,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Marcar completado",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}
