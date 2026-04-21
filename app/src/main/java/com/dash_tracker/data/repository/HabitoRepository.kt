package com.dash_tracker.domain.repository

import com.dash_tracker.domain.model.Habito
import kotlinx.coroutines.flow.Flow

interface HabitoRepository {
    // Retorna un flujo constante de datos. Si la BD cambia, la pantalla se actualiza sola.
    fun getHabitosActivos(): Flow<List<Habito>>

    // Funciones suspendidas (Corrutinas) porque son operaciones asíncronas
    suspend fun crearHabito(habito: Habito)
    suspend fun archivarHabito(id: Int)

    // Función que llamaremos luego para forzar la subida de datos locales a Firebase
    suspend fun sincronizarConNube()
}