package com.dash_tracker.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dash_tracker.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// Hilt le inyecta automáticamente el AuthRepository sin que hagamos "new"
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    // Estados para la UI (Manejan si está cargando o si hay un error)
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // Este estado avisará a la pantalla cuando el login sea exitoso para cambiar de ruta
    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess.asStateFlow()

    fun login(email: String, pass: String) {
        if (email.isBlank() || pass.isBlank()) {
            _error.value = "Por favor, llena todos los campos"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val result = authRepository.login(email, pass)

            result.onSuccess {
                _isSuccess.value = true
            }.onFailure { exception ->
                _error.value = exception.message ?: "Error al iniciar sesión"
            }

            _isLoading.value = false
        }
    }

    fun register(nombre: String, email: String, pass: String) {
        if (nombre.isBlank() || email.isBlank() || pass.isBlank()) {
            _error.value = "Todos los campos son obligatorios"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val result = authRepository.registrar(nombre, email, pass)

            result.onSuccess {
                _isSuccess.value = true
            }.onFailure { exception ->
                _error.value = exception.message ?: "Error al registrarse"
            }

            _isLoading.value = false
        }
    }

    // Limpia los errores al cambiar de pantalla
    fun clearError() {
        _error.value = null
    }
}