package com.dash_tracker.domain.repository

import com.dash_tracker.domain.model.Usuario

interface AuthRepository {
    // Usamos 'Result' de Kotlin para manejar el éxito o el error de forma segura
    suspend fun login(email: String, password: String): Result<Usuario>
    suspend fun registrar(nombre: String, email: String, password: String): Result<Usuario>
    fun getCurrentUser(): Usuario?
    fun logout()
}