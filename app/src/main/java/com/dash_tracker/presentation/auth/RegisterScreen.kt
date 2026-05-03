package com.dash_tracker.presentation.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dash_tracker.R
import androidx.compose.ui.tooling.preview.Preview
import com.dash_tracker.presentation.theme.Dash_TrackerTheme

@Composable
fun RegisterScreen(
    onNavigateBackToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val isSuccess by viewModel.isSuccess.collectAsState()

    LaunchedEffect(isSuccess) {
        if (isSuccess) onRegisterSuccess()
    }

    RegisterContent(
        isLoading = isLoading,
        error = error,
        onRegister = viewModel::register, // Referencia directa
        onNavigateBackToLogin = onNavigateBackToLogin,
        onClearError = viewModel::clearError
    )
}

@Composable
fun RegisterContent(
    isLoading: Boolean,
    error: String?,
    onRegister: (String, String, String) -> Unit,
    onNavigateBackToLogin: () -> Unit,
    onClearError: () -> Unit,
    modifier: Modifier = Modifier
) {
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 30.dp), // Espaciado lateral más amplio
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // --- SECCIÓN: HEADER ---
        HeaderRegisterSection()

        Spacer(modifier = Modifier.height(32.dp))

        // --- SECCIÓN: FORMULARIO ---
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre completo") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                capitalization = androidx.compose.ui.text.input.KeyboardCapitalization.Words
            )

        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                keyboardType = androidx.compose.ui.text.input.KeyboardType.Email
            )


        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña (Mín. 6 caracteres)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            shape = RoundedCornerShape(12.dp)
        )

        error?.let {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- SECCIÓN: ACCIONES ---
        Button(
            onClick = { onRegister(nombre, email, password) },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp), // Altura moderna
            enabled = !isLoading,
            shape = RoundedCornerShape(12.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text("Registrarse", style = MaterialTheme.typography.titleMedium)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = {
            onClearError()
            onNavigateBackToLogin()
        }) {
            Text("¿Ya tienes cuenta? Inicia Sesión")
        }
    }
}

@Composable
private fun HeaderRegisterSection() {
    Image(
        painter = painterResource(id = R.drawable.logo_dash),
        contentDescription = null,
        modifier = Modifier
            .size(90.dp) // Un poco más pequeño que en el login para dar espacio al form
            .clip(CircleShape)
    )
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = "Crear Cuenta",
        style = MaterialTheme.typography.headlineLarge,
        fontWeight = FontWeight.Bold
    )
    Text(
        text = "Únete a Dash Tracker hoy",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

