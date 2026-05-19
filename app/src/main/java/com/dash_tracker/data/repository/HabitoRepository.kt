package com.dash_tracker.data.repository

import com.dash_tracker.domain.model.Habito
import com.dash_tracker.domain.model.RegistroHabito
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface HabitoRepository {
    // Retorna un flujo constante de datos. Si la BD cambia, la pantalla se actualiza sola.
    fun getHabitosActivos(): Flow<List<Habito>>

    fun getAllRegistros(): Flow<List<RegistroHabito>> // <-- Agrega esta línea

    // Funciones suspendidas (Corrutinas) porque son operaciones asíncronas
    suspend fun crearHabito(habito: Habito)
    suspend fun archivarHabito(id: Int)

    // Función que llamaremos luego para forzar la subida de datos locales a Firebase
    suspend fun sincronizarConNube()

    suspend fun hacerCheckIn(habitoId: Int, fecha: Date, completado: Boolean)

    suspend fun eliminarCheckIn(habitoId: Int, fecha: Date)
}