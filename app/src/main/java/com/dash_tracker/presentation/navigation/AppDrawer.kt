package com.dash_tracker.presentation.navigation

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AppDrawer(
    onNavigateToHabits: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToFocus: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToPremium: () -> Unit,
    closeDrawer: () -> Unit
) {
    val calendar = Calendar.getInstance()
    val dayName = SimpleDateFormat("EEEE", Locale("es", "ES")).format(calendar.time)
        .replaceFirstChar { it.uppercase() }
    val fullDate = SimpleDateFormat("dd 'de' MMMM, yyyy", Locale("es", "ES")).format(calendar.time)

    ModalDrawerSheet(
        drawerContainerColor = Color(0xFFE0E0E0), // Color gris claro del fondo
        drawerShape = RoundedCornerShape(topEnd = 24.dp, bottomEnd = 24.dp),
        modifier = Modifier.fillMaxHeight().width(300.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            // --- HEADER: FECHA ---
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = dayName,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = fullDate,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(30.dp))

            // --- GRUPO SUPERIOR DE MENÚ ---
            DrawerItem(
                label = "Hábitos y Tareas",
                icon = Icons.Outlined.CheckBox,
                onClick = { onNavigateToHabits(); closeDrawer() }
            )
            DrawerItem(
                label = "Perfil",
                icon = Icons.Outlined.PersonSearch,
                onClick = { onNavigateToProfile(); closeDrawer() }
            )
            DrawerItem(
                label = "Focus",
                icon = Icons.Outlined.Timer,
                onClick = { onNavigateToFocus(); closeDrawer() }
            )

            // Empuja lo siguiente hacia abajo
            Spacer(modifier = Modifier.weight(1f))

            // --- GRUPO INFERIOR ---
            DrawerItem(
                label = "Configuración",
                icon = Icons.Outlined.Settings,
                onClick = { onNavigateToSettings(); closeDrawer() }
            )

            // Item Premium (Más alto y especial)
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                color = Color.Transparent,
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.Gray),
                onClick = { onNavigateToPremium(); closeDrawer() }
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Outlined.WorkspacePremium, contentDescription = null, modifier = Modifier.size(32.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("Premium", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun DrawerItem(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .height(56.dp),
        shape = RoundedCornerShape(16.dp),
        color = Color.Transparent,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.Gray),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(28.dp),
                tint = Color.Black
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = label,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
        }
    }
}
